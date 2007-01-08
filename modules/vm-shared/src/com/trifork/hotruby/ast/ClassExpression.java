package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ClassExpression extends Expression {

	private final String name;
	private final Expression superExpression;
	private final ClassCode code;

	public ClassExpression(String name, Expression superExpression, ClassCode code) {
		super(code.line());
		this.name = name;
		this.superExpression = superExpression;
		this.code = code;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (superExpression != null) {
			superExpression.compile(ctx, true);
		} 

		ctx.emit_class(name, code, superExpression != null);
		
		if (push) {
			ctx.emit_push_nil();
		}
	}

}
