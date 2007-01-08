package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class CommandOutputExpression extends Expression {

	private final Expression string;

	public CommandOutputExpression(Expression string) {
		this.string = string;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {

		SequenceExpression args = new SequenceExpression();
		args.addExpression(string);
		MethodCallExpression mc = new MethodCallExpression(null, 0,
				new ConstVarExpression("::Kernel"), "command", args, null, true);

		mc.compile(ctx, push);
	}
}
