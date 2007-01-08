package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class BinaryExpression extends Expression {

	protected final Expression left;
	protected final String operator;
	protected final Expression right;

	public String toString() {
		try {
			return "(" + String.valueOf(left) + operator + String.valueOf(right) + ")";
		} catch (RuntimeException e) {
			return String.valueOf(operator);
		}
	}
	
	public BinaryExpression(Expression left, String operator, Expression right) {
		
		this.left = left;
		this.operator = operator;
		this.right = right;
		
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		
		if (operator.equals("||")) {
			new LogicalOrExpression(left, right).compile(ctx, push);
			return;
		}

		if (operator.equals("&&")) {
			new LogicalAndExpression(left, right).compile(ctx, push);
			return;
		}

		SequenceExpression args = new SequenceExpression();
		args.addExpression(right);
		MethodCallExpression mc = new MethodCallExpression(null, line(), left, operator, args, null, false);
		mc.compile(ctx, push);
	}
	

}
