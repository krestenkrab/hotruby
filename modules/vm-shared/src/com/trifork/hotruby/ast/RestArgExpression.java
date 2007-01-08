package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class RestArgExpression extends Expression {

	private final Expression rest;

	@Override
	public String toString() {
		return "*" + rest;
	}
	
	public RestArgExpression(Expression rest) {
		this.rest = rest;
	}
	
	public boolean isRestArg() {
		return true;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		throw new RuntimeException("should never be called");
	}

	public void compile(CompileContext ctx, boolean push, int arg_count) {
		rest.compile(ctx, push);
		if (push) ctx.emit_make_restarg(arg_count);
	}
	
	@Override
	public void compile_assignment(CompileContext ctx, boolean push) {
		rest.compile_assignment(ctx, push);
	}


}
