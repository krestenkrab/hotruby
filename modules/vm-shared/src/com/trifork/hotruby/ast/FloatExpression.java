package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class FloatExpression extends Expression {

	private String text;

	public FloatExpression(String text) {
		this.text = text;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			double val = Double.parseDouble(text);
			int idx = ctx.getConstantPool().add_float(val);
			ctx.emit_push_constant(idx);
		}
	}
}
