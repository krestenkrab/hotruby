package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class BeginEndExpression extends Expression {

	private final Expression exp;

	public BeginEndExpression(Expression exp) {
		this.exp = exp;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		exp.compile(ctx, push);
	}

}
