package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Label;

public class WhileExpression extends Expression {

	private final Expression test;

	private final Expression body;

	private final boolean isModifier;

	public WhileExpression(int line, Expression test, Expression body, boolean isModifier) {
		super(line);
		this.test = test;
		this.body = body;
		this.isModifier = isModifier;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		Label next_label = ctx.new_label();
		Label break_label = ctx.new_label();
		Label loop_start = ctx.new_label();

		ctx.emit_goto(next_label);
		ctx.mark(loop_start);

		if (body != null) {

			Label redo_label = ctx.new_label();
			ctx.mark(redo_label);

			ctx.push_loop_context(break_label, redo_label, next_label);

			// evaluate body
			body.compile(ctx, false);

			ctx.pop_loop_context();

		} else {
			// do nothing, use the nil we pushed before start
		}

		ctx.mark(next_label);

		// optimize the case where the condition is !(expr), which
		// is emitted by the parser for an until loop
		boolean found_until = false;
		if (test instanceof UnaryExpression) {
			UnaryExpression unex = (UnaryExpression) test;
			if ("!".equals(unex.operator)) {
				unex.expr.compile(ctx, true);
				ctx.emit_branch_unless(loop_start);
				found_until = true;
			}
		}

		if (!found_until) {
			test.compile(ctx, true);
			ctx.emit_branch_if(loop_start);
		}

		ctx.mark(break_label);

		// a while loop always returns nil
		if (push) {
			ctx.emit_push_nil();
		}
	}
}
