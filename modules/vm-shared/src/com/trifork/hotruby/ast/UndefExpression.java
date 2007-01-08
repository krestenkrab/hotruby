package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;


public class UndefExpression extends Expression {

	private final SequenceExpression args;

	public UndefExpression(SequenceExpression args) {
		this.args = args;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		throw new RuntimeException("not implemented");
	}

}
