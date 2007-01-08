package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ConstVarExpression extends VariableExpression {

	public ConstVarExpression(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		ctx.emit_getconst(getName());
		if (!push) { ctx.emit_pop(); }
	}
	
	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {
		if (push) { ctx.emit_dup(); }
		ctx.emit_setconst(getName());
	}
}
