package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class MethodCallExpression extends Expression {

	private Expression expr;

	private SequenceExpression args;

	private String method;

	private BlockCode block;

	/** used :: notation to access this */
	private final boolean base_must_be_class_or_module;

	public MethodCallExpression(RubyCode ctx, int line, Expression receiver,
			String method, SequenceExpression args, BlockCode block,
			boolean base_must_be_class_or_module) {
		super(line);
		this.expr = receiver;
		this.method = method;
		this.args = args;
		this.block = block;
		this.base_must_be_class_or_module = base_must_be_class_or_module;
		if (ctx != null) {
			ctx.method_call_here(method);
		}
	}

	public void setBlock(BlockCode block) {
		this.block = block;
	}

	public void setArgs(SequenceExpression args) {
		this.args = args;
	}

	public static MethodCallExpression make(RubyCode scope, Expression expr2) {
		if (expr2 instanceof MethodCallExpression) {
			return (MethodCallExpression) expr2;
		} else if (expr2 instanceof MethodDenominator) {
			return new MethodCallExpression(scope, expr2.line, null,
					((MethodDenominator) expr2).getMethodName(), null, null,
					false);
		} else {
			throw new RuntimeException("cannot convert " + expr2
					+ " to methodCall");
		}
	}

	public String getMethodName() {
		return method;
	}

	public void setMethodName(String string) {
		method = string;
	}

	public void addArgument(Expression right) {
		if (args == null) {
			args = new SequenceExpression();
		}

		args.addExpression(right);
	}

	public String toString() {
		if ("[]".equals(method)) {
			return String.valueOf(expr)
					+ (base_must_be_class_or_module ? "::" : ".") + "["
					+ (args == null ? "" : args.toString()) + "]"
					+ (block == null ? "" : block.toString());
		} else if ("[]=".equals(method)) {
			return String.valueOf(expr)
					+ (base_must_be_class_or_module ? "::" : ".") + "["
					+ (args == null ? "" : args.toStringExceptLast()) + "]="
					+ (block == null ? "" : block.toString())
					+ (args == null ? "?" : args.last());
		} else {
			return String.valueOf(expr)
					+ (base_must_be_class_or_module ? "::" : ".") + method
					+ "(" + (args == null ? "" : args.toString()) + ")"
					+ (block == null ? "" : block.toString());
		}
	}

	void compile(CompileContext ctx, boolean push) {
		
		if ("update".equals(method)) {
			System.out.print("");
		}
		
		Expression receiver = expr;
		boolean is_yield = false;
		boolean is_super = false;

		if (receiver == null) {
			// handle special cases for "command-like" method calls

			// ("return"|"break"|"next"|"retry"|"redo")

			if ("break".equals(method)) {
				if (push) { ctx.emit_push_nil(); }
				ctx.emit_break();
				return;
			} else if ("next".equals(method)) {
				ctx.emit_next();
				if (push) { ctx.emit_push_nil(); }
				return;
			} else if ("retry".equals(method)) {
				ctx.emit_retry();
				if (push) { ctx.emit_push_nil(); }
				return;
			} else if ("redo".equals(method)) {
				ctx.emit_redo();
				if (push) { ctx.emit_push_nil(); }
				return;
			} else if ("yield".equals(method)) {
				is_yield = true;

			} else if ("super".equals(method)) {
				is_super = true;

			} else if ("return".equals(method)) {

				if (args == null || args.size() == 0) {
					ctx.emit_push_nil();
				} else if (args.size() == 1 && !args.get(0).isRestArg()) {
					args.get(0).compile(ctx, true);
				} else {
					ArrayExpression ae = new ArrayExpression(args);
					ae.compile(ctx, true);
					ctx.emit_make_returnvalue();
				}

				ctx.emit_return();
				if (push) {
					ctx.set_stack_depth(ctx.get_stack_depth() + 1);
				}
				return;
			}

			// generic case, send message to self
			receiver = SelfExpression.instance;
		}

		if (base_must_be_class_or_module && isConstantName(method)) {
			SequenceExpression new_arg = new SequenceExpression();
			new_arg.addExpression(new SymbolExpression(line, this.method));
			new MethodCallExpression(null, line, receiver, "const_get",
					new_arg, null, false).compile(ctx, push);
			return;
		}

		int st = ctx.get_stack_depth();

		if (!is_yield && !is_super) {
			receiver.compile(ctx, true);

			if (ctx.get_stack_depth() - 1 != st) {
				throw new InternalError("should only push 1 element, did push "
						+ (ctx.get_stack_depth() - st) + ": " + receiver);
			}
		} else if (!is_yield) {
			// self is stored here, before args
			ctx.emit_push_self();
		}

		boolean has_block_arg = false;
		boolean has_rest_arg = false;
		int arg_count = 0;
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				Expression exp = args.get(i);
				if (exp instanceof RestArgExpression) {
					((RestArgExpression)exp).compile(ctx, true, arg_count);
				} else {
					exp.compile(ctx, true);
				}
				if (exp.isRestArg()) {
					has_rest_arg = true;
				} else if (exp.isBlockArg()) {
					has_block_arg = true;
				} else {
					arg_count += 1;
				}
			}
		}

		boolean is_simple_binary = arg_count == 1 && !has_block_arg
				&& !has_rest_arg && block == null;

		if (is_simple_binary && push) {
			if ("+".equals(method)) {
				ctx.emit_plus();
				return;
			} else if ("-".equals(method)) {
				ctx.emit_minus();
				return;
			} else if ("<".equals(method)) {
				ctx.emit_lt();
				return;
			} else if ("<=".equals(method)) {
				ctx.emit_le();
				return;
			} else if (">=".equals(method)) {
				ctx.emit_ge();
				return;
			} else if (">".equals(method)) {
				ctx.emit_gt();
				return;
			} else if (">>".equals(method)) {
				ctx.emit_rshft();
				return;
			} else if ("^".equals(method)) {
				ctx.emit_bit_xor();
				return;
			} else if ("|".equals(method)) {
				ctx.emit_bit_or();
				return;
			} else if ("&".equals(method)) {
				ctx.emit_bit_and();
				return;
			} else if ("==".equals(method)) {
				ctx.emit_eq2();
				return;
			}
		}

		boolean is_simple_unary = arg_count == 0 && !has_block_arg
				&& !has_rest_arg && block == null;

		if (is_simple_unary && push) {
			if ("~".equals(method)) {
				ctx.emit_bit_not();
				return;
			}
		}
		if (is_yield) {
			ctx.emit_invoke_block(arg_count, has_rest_arg, push);
			//ctx.emit_swap();
			//ctx.emit_pop();

		} else if (is_super) {
			ctx.emit_supersend(arg_count, has_rest_arg, has_block_arg,
					push, block);
			
		} else if (receiver == SelfExpression.instance) {
			ctx.emit_selfsend(method, arg_count, has_rest_arg, has_block_arg,
					push, block);
		} else {
			ctx.emit_send(method, arg_count, has_rest_arg, has_block_arg, push,
					block);
		}
	}

	private boolean isConstantName(String method) {
		return Character.isUpperCase(method.charAt(0));
	}

	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {
		Expression receiver = expr;

		if (base_must_be_class_or_module) {
			if (receiver == null) {
				super.compile_assignment(ctx, push);
			} else {
				super.compile_assignment(ctx, push);
			}
		} else {
			if (receiver == null) {
				throw new InternalError("huh?");
			}

			int loc = ctx.alloc_temp(1);
			ctx.emit_setlocal(loc);

			receiver.compile(ctx, true);

			int arg_count = 0;
			boolean has_rest_arg = false;
			boolean has_block_arg = false;
			if (args != null) {
				for (int i = 0; i < args.size(); i++) {
					Expression exp = args.get(i);
					exp.compile(ctx, true);
					if (exp.isRestArg()) {
						has_rest_arg = true;
					} else if (exp.isBlockArg()) {
						has_block_arg = true;
					} else {
						arg_count += 1;
					}
				}
			}

			arg_count += 1;
			ctx.emit_getlocal(loc);

			ctx.emit_send(method + "=", arg_count, has_rest_arg, has_block_arg,
					push, block);

			ctx.free_temp(loc);
		}
	}

}
