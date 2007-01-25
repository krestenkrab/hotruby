package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class LineExpression extends VariableExpression {

	private int lineno;

	public LineExpression(int lineno) {
		super(-1, "__LINE__");
		this.lineno = lineno;
	}

	void compile(CompileContext ctx, boolean push) {
		if (push) {
			int idx = ctx.getConstantPool().add_integer(lineno);
			ctx.emit_push_constant(idx);
		}
	}

}
