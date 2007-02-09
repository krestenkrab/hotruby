package com.trifork.hotruby.ast;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Instructions;

public class SequenceExpression extends Expression implements AssocHolder {

	List<Expression> args;

	private boolean has_rest_arg;

	public SequenceExpression() {
		args = new ArrayList<Expression>();
	}

	public SequenceExpression(Expression arg) {
		this();
		addExpression(arg);
	}
	
	public SequenceExpression(List<Expression> arg) {
		args = arg;
		if (args.get(args.size() - 1) instanceof RestArgExpression) {
			has_rest_arg = true;
		}
	}


	public String toString() {
		if (args.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(String.valueOf(args.get(i)));
		}
		return sb.toString();
	}

	public void addExpression(Expression stmt) {
		args.add(stmt);
		if (stmt instanceof RestArgExpression) {
			has_rest_arg = true;
		}
	}

	public void addAssoc(Expression key, Expression value) {
		if (value == null) {
			addExpression(key);
		} else {
			HashExpression last = lastArgIfHash();
			if (last == null) {
				args.add(new HashExpression(key, value));
			} else {
				last.addAssoc(key, value);
			}
		}
	}

	private HashExpression lastArgIfHash() {
		if (args.size() == 0) {
			return null;
		}
		Expression val = args.get(args.size() - 1);
		if (val instanceof HashExpression) {
			return (HashExpression) val;
		}
		return null;
	}

	private BlockArgExpression lastArgIfBlock() {
		if (args.size() == 0) {
			return null;
		}
		Expression val = args.get(args.size() - 1);
		if (val instanceof BlockArgExpression) {
			return (BlockArgExpression) val;
		}
		return null;
	}

	// *rest
	public void setRestArg(Expression expr) {
		has_rest_arg = true;
		args.add(new RestArgExpression(expr));
	}

	// &proc
	public void setBlockArg(Expression expr) {
		args.add(new BlockArgExpression(expr));
	}

	public boolean hasBlockArg() {
		return lastArgIfBlock() != null;
	}

	public String last() {
		if (args == null || args.size() == 0) {
			return "<?>";
		} else {
			return String.valueOf(args.get(args.size() - 1));
		}
	}

	public String toStringExceptLast() {
		if (args.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.size() - 1; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(String.valueOf(args.get(i)));
		}
		return sb.toString();
	}

	public int size() {
		return args.size();
	}

	public Expression get(int i) {
		return args.get(i);
	}
	
	@Override
	public int line() {
		if (args.size() != 0) {
			return args.get(0).line();
		}
		return line;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (args == null) {
			// do nothing
			if (push) {
				throw new InternalError("empty sequence");
			}
		} else {
			int stack_depth = ctx.get_stack_depth();

			int last_line = 0;
			
			for (int i = 0; i < args.size(); i++) {
				boolean islast = (i == (args.size() - 1));
				Expression expression = args.get(i);

				if (expression == null) {
					throw new InternalError ("null element in "+this);
				}
				
				if (expression.line != last_line) {
					ctx.emit_trace(Instructions.TRACE_LINE, last_line = expression.line);
				}
				
				if (islast && expression instanceof RestArgExpression) {
					((RestArgExpression) expression).compile(ctx, push
							& islast, args.size() - 1);
				} else {
					assert !(expression instanceof RestArgExpression);
					expression.compile(ctx, push&islast);
				}

				if (!(islast || stack_depth == ctx.get_stack_depth())) {
					throw new InternalError(
							"mismatch stack depth after compiling "
									+ expression + " is:"
									+ ctx.get_stack_depth() + ", expecting:"
									+ stack_depth);
				}
			}
		}
	}

	void compile_to_stack(CompileContext ctx) {
		if (args == null || args.size() == 0) {
			throw new InternalError("empty sequence");

		} else {

			if (args.size() == 1) {
				throw new InternalError("compile to stack: sequence of size 1");
			}

			if (has_rest_arg) {
				throw new InternalError("compile to stack: rhs with restarg");
			}

			for (int i = 0; i < args.size(); i++) {
				Expression expression = args.get(i);
				expression.compile(ctx, true);
			}
		}

	}

	void compile_to_array(CompileContext ctx) {
		if (args == null || args.size() == 0) {
			throw new InternalError("empty sequence");
		} else {

			if (args.size() == 1 && !(args.get(0) instanceof RestArgExpression)) {
				args.get(0).compile(ctx, true);
				ctx.emit_internal_to_a();
				return;
			}

			for (int i = 0; i < args.size(); i++) {
				boolean islast = (i == (args.size() - 1));
				Expression expression = args.get(i);

				if (islast && expression instanceof RestArgExpression) {
					((RestArgExpression) expression).compile(ctx, true, args
							.size() - 1);
					ctx.emit_new_array(0, true);
					return;

				} else {
					assert !(expression instanceof RestArgExpression);
					expression.compile(ctx, true);
				}
			}
			ctx.emit_new_array(args.size(), false);
		}
	}

	/**
	 * arr -> arr
	 */
	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {

		for (int i = 0; i < args.size(); i++) {
			Expression expr = args.get(i);
			if (expr instanceof RestArgExpression) {
				ctx.emit_array_rest(i);
			} else {
				ctx.emit_array_at(i);
				
				if (expr instanceof SequenceExpression) {
					ctx.emit_internal_to_a();
				}
				
			}
			expr.compile_assignment(ctx, false);
		}

		if (!push) {
			ctx.emit_pop();
		}
	}

	public boolean has_rest_arg() {
		return has_rest_arg;
	}

	public void compile_assignment_from_stack(CompileContext ctx, int howmany) {

		int pop_at_end = 0;

		for (int i = 0; i < args.size(); i++) {
			Expression expr = args.get(i);

			System.out.println("ass: " + expr + "; st=" + ctx.get_stack_depth()
					+ "; i=" + i + "; howmany=" + howmany);

			if (i >= howmany) {
				ctx.emit_push_nil();
				// will be popped by assignment
			} else if (expr instanceof RestArgExpression) {
				ctx.emit_new_array(howmany - i, false);
				// pops howmany-i (i.e. rest of args)

			} else if (howmany - i != 1) {
				ctx.emit_dup_n(howmany - i);
				pop_at_end += 1;
			} else {

			}

			expr.compile_assignment(ctx, false);
		}

		System.out.println("before adjust:" + ctx.get_stack_depth()
				+ " howmany:" + howmany + "; hasrest:" + has_rest_arg);

		if (pop_at_end != 0) {
			ctx.emit_pop_n(pop_at_end);
		}

		System.out.println("after adjust:" + ctx.get_stack_depth()
				+ " howmany:" + howmany + "; hasrest:" + has_rest_arg);

	}

	// will consume the Object[] on stack, and push ruby-array if "push" is true
	public void compile_assignment_from_vargs(CompileContext ctx, boolean push) {
		// top-of-stack is IRubyObject[]

		if (args == null || args.size() == 0) {
			if (push) {
				ctx.emit_new_array(0, true);
			} else {
				ctx.emit_pop();
			}
			return;
		}

		int nargs = args.size();
		for (int i = 0; i < nargs; i++) {
			boolean is_last = (i + 1) == nargs;
			if (!is_last || push)
				ctx.emit_dup();
			Expression expr = args.get(i);
			if (expr instanceof RestArgExpression) {
				ctx.emit_java_array_rest(i);
			} else {
				ctx.emit_java_array_at(i);
			}
			expr.compile_assignment(ctx, false);
		}

		if (push) {
			ctx.emit_new_array(0, true);
		}
	}

	public void compile_assignment_from_locals(CompileContext ctx,
			int first_local, int count, boolean push) {

		if (args == null || args.size() == 0) {
			if (push) {
				for (int i = 0; i < count; i++) {
					ctx.emit_getlocal(first_local + i);
				}
				ctx.emit_new_array(count, false);
			}
			return;
		}

		int nargs = args.size();

		for (int i = 0; i < nargs; i++) {

			if (i < count) {
				ctx.emit_getlocal(first_local + i);
				if (push) {
					ctx.emit_dup();
				}
			} else {
				ctx.emit_push_nil();
			}

			Expression expr = args.get(i);
			if (expr instanceof RestArgExpression) {
				int howmany = 1;
				i += 1;
				while (i < count) {
					ctx.emit_getlocal(first_local + i);
					i += 1;
					howmany += 1;
				}
				ctx.emit_new_array(howmany, false);
			}

			expr.compile_assignment(ctx, false);
		}

		if (push) {
			ctx.emit_new_array(0, true);
		}

	}

	public void compile_to_locals(CompileContext ctx, int first_local) {
		assert !has_rest_arg;
		
		for (int i = 0; i < args.size(); i++) {
			args.get(i).compile(ctx, true);
			ctx.emit_setlocal(first_local+i);
		}
	}

}
