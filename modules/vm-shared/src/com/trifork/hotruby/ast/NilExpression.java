package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class NilExpression extends VariableExpression {

	public static Expression instance = new NilExpression();

	private NilExpression() {
		super(-1, "nil");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			ctx.emit_push_nil();
		}
	}

}
