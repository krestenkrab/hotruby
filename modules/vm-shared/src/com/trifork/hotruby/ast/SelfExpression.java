package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class SelfExpression extends VariableExpression {

	public static Expression instance = new SelfExpression();

	private SelfExpression() {
		super(-1, "self");
	}

	void compile(CompileContext ctx, boolean push) {
		if (push) ctx.emit_push_self();
	}
}
