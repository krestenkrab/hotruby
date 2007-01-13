package com.trifork.hotruby.ast;

import java.util.List;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Label;

public class RescueExpression extends Expression {

	private final Expression body;

	private final List<RescueClause> rescues;

	private final Expression elseStmt;

	private final Expression ensureStmt;

	public RescueExpression(Expression body, List<RescueClause> rescues,
			Expression elseStmt, Expression ensureStmt) {
		this.body = body;
		this.rescues = rescues;
		this.elseStmt = elseStmt;
		this.ensureStmt = ensureStmt;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		int depth_before_stack_save = ctx.get_stack_depth();

		Label rescue_end = ctx.new_label();
		Label ensure_label = (ensureStmt == null && depth_before_stack_save==0) ? null : ctx.new_label();

		int saved = ctx.alloc_temp(depth_before_stack_save);
		// save stack
		for (int i = depth_before_stack_save - 1; i >= 0; i--) {
			ctx.emit_setlocal(saved + i);
		}

		assert ctx.get_stack_depth() == 0;

		boolean body_should_push = push && (elseStmt == null);

		if (ensure_label != null) {
		ctx.push_finally_handler(ensure_label);
		}
		
		Label body_start = ctx.new_label();
		ctx.mark(body_start);

		Label else_label = ctx.new_label();

		int depth_before_body = ctx.get_stack_depth();
		compile_with_finally(ctx, body, ensure_label, body_should_push);
		Label body_end = ctx.new_label();
		ctx.mark(body_end);
		
		ctx.emit_goto(else_label);
		int depth_after_body = ctx.get_stack_depth();

		Label handler_label = ctx.new_label();
		ctx.mark(handler_label);
		
		ctx.add_exception_handler(body_start, body_end, handler_label);

		// the following code assumes that the exception has been pushed
		ctx.set_stack_depth(1);
		
		ctx.emit_unwrap_raise();
		
		int ex = ctx.alloc_temp(1);
		ctx.emit_setlocal(ex);

		if (rescues == null || rescues.size()==0) {
			ctx.emit_push_nil();
		}
		
		for (RescueClause clause : rescues) {

			Label next_clause_test = ctx.new_label();
			Label this_clause = ctx.new_label();

			if (clause.args == null || clause.args.size()==0) {
				// if there are no conditions, then this is an unconditional exception
				ctx.emit_goto(this_clause);
			} else {
				for (Expression test : clause.args.args) {

					int dd = ctx.get_stack_depth();

					ctx.emit_getlocal(ex);
					test.compile(ctx, true);
					ctx.emit_send("instance_of?", 1, false, false, true, null);
					ctx.emit_branch_if(this_clause);

					assert dd == ctx.get_stack_depth();
				}
			}

			int depth_before_next_clause = ctx.get_stack_depth();
			ctx.emit_goto(next_clause_test);

			ctx.mark(this_clause);

			compile_with_finally(ctx, clause.body, ensure_label, push);
			assert ctx.get_stack_depth() == depth_before_next_clause
					+ (push ? 1 : 0);
			ctx.emit_goto(rescue_end);

			ctx.mark(next_clause_test);
			ctx.set_stack_depth(depth_before_next_clause);
		}

		ctx.mark(else_label);
		if (elseStmt != null) {

			compile_with_finally(ctx, elseStmt, ensure_label, push);
			assert ctx.get_stack_depth() == depth_after_body + (push ? 1 : 0);

		}

		ctx.emit_goto(rescue_end);

		if (ensure_label != null) {
			ctx.mark(ensure_label);
			ctx.set_stack_depth(1);
			
			ctx.pop_finally_handler();

			int local = ctx.alloc_temp(1);
			ctx.emit_setlocal(local);
			
			if (ensureStmt != null) {
				ensureStmt.compile(ctx, false);
			}
			
			ctx.emit_local_return(local);
		}
			
		ctx.mark(rescue_end);
		int depth_before_stack_restore = ctx.get_stack_depth();
		ctx.set_stack_depth(depth_before_body + (push ? 1 : 0));

		// restore stack
		restore_stack(ctx, push, depth_before_stack_save, saved);

		ctx.free_temp(saved);

		assert ctx.get_stack_depth() == depth_before_stack_save + (push ? 1 : 0);
	}

	private void restore_stack(CompileContext ctx, boolean push, int depth_before_stack_save, int saved) {
		int rescue_result = push ? ctx.alloc_temp(1) : -1;
		if (push) {
			ctx.emit_setlocal(rescue_result);
		}

		for (int i = 0; i < depth_before_stack_save; i++) {
			ctx.emit_getlocal(saved + i);
		}

		if (push) {
			ctx.emit_getlocal(rescue_result);
		}
	}

	private void compile_with_finally(CompileContext ctx, Expression body,
			Label ensure_label, boolean push) {

		int depth_before = ctx.get_stack_depth();

		body.compile(ctx, push);

		int depth_after_body = ctx.get_stack_depth();
		assert depth_after_body == depth_before + (push ? 1 : 0);

	}

}
