package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Instructions;


public class AssignmentExpression extends BinaryExpression {

	public AssignmentExpression(Expression left, String operator,
			Expression returnExp_) {
		super(left, operator, returnExp_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {

		ctx.emit_trace(Instructions.TRACE_LINE, left.line());
		
		if (operator.equals("="))
		{		
			right.compile(ctx, true);
			left.compile_assignment(ctx, push);
			return;
		}
		
		String eval = operator.substring(0, operator.length()-1);
		
		BinaryExpression new_right = new BinaryExpression(left, eval, right);
		
		new_right.compile(ctx, true);
		left.compile_assignment(ctx, push);
		
	//	throw new InternalError("not implemented: "+operator);
	}

	public static Expression build(Expression left, String operator, Expression right) {
		
		return new AssignmentExpression(left, operator, right);
		
/*		
		if (! "=".equals(operator)) {
			operator = operator.substring(0, operator.length()-1);
			right = new BinaryExpression(left.copy(), operator, right);
		}
		
		if (left instanceof MethodCallExpression) {
			MethodCallExpression lm = (MethodCallExpression) left;
			lm.setMethodName(lm.getMethodName() + "=");
			lm.addArgument(right);
			return lm;
		} else {
			
			return new AssignmentExpression(left, operator, right);
		}
	*/	
	}
}
