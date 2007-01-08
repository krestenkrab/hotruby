package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class SingletonClassExpression extends Expression {

	private final Expression superExpression;
	private final ClassCode code;

	public SingletonClassExpression(Expression superExpression, ClassCode code) {
		this.superExpression = superExpression;
		this.code = code;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (superExpression != null) {
			superExpression.compile(ctx, true);
		} 

		ctx.emit_class(null, code, superExpression != null);
		
		if (push) {
			ctx.emit_push_nil();
		}
	}

}
