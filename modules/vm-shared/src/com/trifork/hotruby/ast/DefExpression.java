package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class DefExpression extends Expression {

	private final String name;
	private final MethodCode code;

	public DefExpression(int line, String name, MethodCode code) {
		super(line);
		this.name = name;
		this.code = code;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		
		Expression singleton = code.singleton;
		
		if (singleton != null) {
			singleton.compile(ctx, true);
		}
		
		ctx.emit_def(name, code, singleton!=null);
		if (push) {
			ctx.emit_push_nil();
		}
		
	}

}
