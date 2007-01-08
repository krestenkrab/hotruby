package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class DefinedExpression extends VariableExpression {

	public static Expression instance = new DefinedExpression();

	private DefinedExpression() {
		super("defined?");
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		// TODO: this should probably cause a syntax error?
		throw new InternalError("not implemented");
	}
}
