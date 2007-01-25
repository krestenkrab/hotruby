package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class FileExpression extends VariableExpression {

	private String file;

	public FileExpression(String text) {
		super(-1, "__FILE__");
		this.file = text;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			new ConstantStringExpression(file, ConstantStringExpression.NONE).compile(ctx, true);
		}
	}
}
