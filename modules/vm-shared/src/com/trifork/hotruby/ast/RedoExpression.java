package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class RedoExpression extends VariableExpression {

	public static Expression instance = new RedoExpression();

	private RedoExpression() {
		super("redo");
		// TODO Auto-generated constructor stub
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		ctx.emit_redo();
	}
}
