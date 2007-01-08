package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ModuleExpression extends Expression {

	private final String name;
	private final ModuleCode code;

	public ModuleExpression(String name, ModuleCode code) {
		super(code.line());
		this.name = name;
		this.code = code;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		ctx.emit_module(name, code);
		
		if (push) {
			ctx.emit_push_nil();
		}

	}

}
