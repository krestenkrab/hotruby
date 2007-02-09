package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class WArrayExpression extends Expression {

	private final Expression string;

	public WArrayExpression(Expression string) {
		this.string = string;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		MethodCallExpression mce = new MethodCallExpression(null, line, string, "split", null , null, false);
		mce.compile(ctx, push);
	}


}
