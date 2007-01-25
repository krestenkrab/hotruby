package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ClassVarExpression extends VariableExpression {

	public ClassVarExpression(int line, String text) {
		super(line, text);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		ctx.emit_getclassvar(getName());
		if (!push) {
			ctx.emit_pop();
		}
	}
	
	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {
		if (push) {
			ctx.emit_dup();
		}
		ctx.emit_setclassvar(getName());
	}

}
