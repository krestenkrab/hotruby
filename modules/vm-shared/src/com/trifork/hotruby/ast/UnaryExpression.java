package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class UnaryExpression extends Expression {

	final String operator;
	final Expression expr;

	public UnaryExpression(int line, String string, Expression expr) {
		super(line);
		this.operator = string;
		this.expr = expr;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public Expression getExpression() {
		return expr;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		
		
		
		if ("-@".equals(operator)) {
			if (expr instanceof IntegerExpression) {
				Expression ie = ((IntegerExpression)expr).const_fold_negate();
				ie.compile(ctx, push);
				return;
			}
		} else if ("+@".equals(operator)) {
			if (expr instanceof IntegerExpression) {
				expr.compile(ctx, push);
				return;
			}
		}
		
		MethodCallExpression mce = new MethodCallExpression(null, line, expr, operator, null, null, false);
		mce.compile(ctx, push);
	}

}
