package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class TrueExpression extends VariableExpression {

	public static Expression instance = new TrueExpression();

	private TrueExpression() {
		super("true");
		// TODO Auto-generated constructor stub
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			ctx.emit_push_true();
		}
	}
}
