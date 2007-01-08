package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class GlobalVarExpression extends VariableExpression {

	public GlobalVarExpression(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {
		if (push) { ctx.emit_dup(); }
		ctx.emit_setglobal(getName());
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			ctx.emit_getglobal(getName());
		}
	}

}
