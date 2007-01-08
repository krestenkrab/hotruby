package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class FalseExpression extends VariableExpression {

	public static Expression instance = new FalseExpression();

	private FalseExpression() {
		super("false");
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			ctx.emit_push_false();
		}
	}
}
