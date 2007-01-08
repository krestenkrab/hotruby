package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Instructions;
import com.trifork.hotruby.interp.Label;

public class IfThenElseExpression extends Expression {

	private final Expression testExpr;

	private final Expression thenExpr;

	private Expression elseExpr;

	@Override
	public String toString() {
		return "if " + testExpr + " then " + thenExpr + " else " + elseExpr;
	}
	
	public IfThenElseExpression(int line, Expression testExpr, Expression thenExpr,
			Expression elseExpr) {
		super(line);
		this.testExpr = testExpr;
		this.thenExpr = thenExpr;
		this.elseExpr = elseExpr;
	}
	
	public void setElseExpression(Expression elseExpr) {
		this.elseExpr = elseExpr;
	}

	void compile(CompileContext ctx, boolean push) {
		
		int p = push ? 1 : 0;
		
		ctx.emit_trace(Instructions.TRACE_LINE, line());
		int st = ctx.get_stack_depth();
		
		testExpr.compile(ctx, true);
		
		assert st+1 == ctx.get_stack_depth();

		Label else_label = ctx.new_label();
		Label end_label = ctx.new_label();
		ctx.emit_branch_unless(else_label);

		assert st == ctx.get_stack_depth();

		if (thenExpr == null) {
			if (push) ctx.emit_push_nil();
		} else {
			ctx.emit_trace(Instructions.TRACE_LINE, thenExpr.line());
			thenExpr.compile(ctx, push);
		}

		assert st+p == ctx.get_stack_depth();

		ctx.emit_goto(end_label);
		ctx.mark(else_label);
		ctx.set_stack_depth(st);

		if (elseExpr == null) {
			if (push) ctx.emit_push_nil();
		} else {
			ctx.emit_trace(Instructions.TRACE_LINE, elseExpr.line());
			elseExpr.compile(ctx, push);
		}

		assert st+p == ctx.get_stack_depth();
		ctx.mark(end_label);

	}

}
