package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class WArrayExpression extends Expression {

	private final Expression string;

	public WArrayExpression(Expression string) {
		this.string = string;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");		
	}


}
