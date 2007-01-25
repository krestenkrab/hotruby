package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class YieldExpression extends VariableExpression {

	public static Expression instance = new YieldExpression();

	private YieldExpression() {
		super(-1, "yield");
		// TODO Auto-generated constructor stub
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		ctx.emit_invoke_block(0, false, push);
	}
}
