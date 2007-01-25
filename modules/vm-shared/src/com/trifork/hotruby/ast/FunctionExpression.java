package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class FunctionExpression extends VariableExpression {

	
	
	private final boolean toplevel;

	public FunctionExpression(int line, RubyCode scope, boolean toplevel, String fname) {
		super(line, fname);
		this.toplevel = toplevel;
		if (!toplevel) {
			scope.method_call_here(fname);
		}
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		if (toplevel) {
			new ConstVarExpression(-1, "::" + super.getName()).compile(ctx, push);
		} else {
			new MethodCallExpression(null, 0, null, super.getMethodName(), null, null, false).compile(ctx, push);
		}
	}

}
