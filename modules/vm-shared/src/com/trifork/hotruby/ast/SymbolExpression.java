package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class SymbolExpression extends Expression {

	private Expression string;

	public SymbolExpression(int line, String string) {
		this (line,  new ConstantStringExpression(string,
				ConstantStringExpression.NONE));
	}

	public SymbolExpression(int line, Expression expr) {
		super(line);
		string = expr;
	}

	public static Expression make(int line, Expression expr) {
		if (expr instanceof SymbolExpression) {
			return (SymbolExpression) expr;
		} else {
			return new SymbolExpression(line, expr);
		}
	}

	public String asString() {
		return ((ConstantStringExpression) string).getText();
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (string instanceof ConstantStringExpression) {
			if (push) {
				ConstantStringExpression cse = (ConstantStringExpression) string;
				int idx = ctx.getConstantPool().add_symbol(cse.getText());
				ctx.emit_push_constant(idx);
			}
			return;
		}
		
		string.compile(ctx, push);
		if (push) {
			ctx.emit_new_symbol();
		}
	}
}
