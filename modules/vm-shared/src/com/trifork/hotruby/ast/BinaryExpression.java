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
	
	public BinaryExpression(int line, Expression left, String operator, Expression right) {
		super(line);
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	/**
	 * @deprecated Use the version taking a line number instead!
	 */
	public BinaryExpression(Expression left, String operator, Expression right) {
		this(0, left, operator, right);
	}

	public Expression getLeft() {
		return left;
	}
	
	public Expression getRight() {
		return right;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		
		if (operator.equals("||") || operator.equals("or")) {
			new LogicalOrExpression(left, right).compile(ctx, push);
			return;
		}

		if (operator.equals("&&") || operator.equals("and")) {
			new LogicalAndExpression(left, right).compile(ctx, push);
			return;
		}
		
		if (operator.equals("..")) {
			left.compile(ctx, true);
			right.compile(ctx, true);
			ctx.emit_new_range(true);
			if (!push) {
				ctx.emit_pop();
			}
			return;
		}

		if (operator.equals("...")) {
			left.compile(ctx, true);
			right.compile(ctx, true);
			ctx.emit_new_range(false);
			if (!push) {
				ctx.emit_pop();
			}
			return;
		}

		SequenceExpression args = new SequenceExpression();
		args.addExpression(right);
		MethodCallExpression mc = new MethodCallExpression(null, line(), left, operator, args, null, false);
		mc.compile(ctx, push);
	}
	

}
