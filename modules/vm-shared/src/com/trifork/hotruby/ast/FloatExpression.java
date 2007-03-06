package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class FloatExpression extends Expression {

	private String text;

	/**
	 * @deprecated Use the version taking a line number instead!
	 */
	public FloatExpression(String text) {
		this.text = text;
	}

	public FloatExpression(int line, String text) {
		super(line);
		this.text = text;
	}
	
	public String getText() {
		return text;
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
