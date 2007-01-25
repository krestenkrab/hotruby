package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class InstanceVarExpression extends VariableExpression {

	public InstanceVarExpression(int line, String text) {
		super(line, text);
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			ctx.emit_getivar(getName());
		}
		
	}
	
	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {
		if (push) { ctx.emit_dup(); }
		ctx.emit_setivar(getName());
	}
	
}
