package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class RetryExpression extends VariableExpression {

	public static Expression instance = new RetryExpression();

	private RetryExpression() {
		super("retry");
		// TODO Auto-generated constructor stub
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		ctx.emit_retry();
	}
}
