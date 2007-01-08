package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ArrayExpression extends Expression implements MethodDenominator {

	private final SequenceExpression init;

	public ArrayExpression(SequenceExpression initializer) {
		this.init = initializer;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		boolean has_rest_arg = false;
		int arg_count = 0;
		for (int i = 0; init != null && i < init.size(); i++) {
			Expression exp = init.get(i);
			
			//System.out.println("st:"+ctx.get_stack_depth()+" before "+exp);
			
			if (exp.isRestArg()) {
				((RestArgExpression)exp).compile(ctx, push, arg_count);
				has_rest_arg = true;
				break;
			}  else {
				exp.compile(ctx, push);
				arg_count += 1;
			}
		}
		
		//System.out.println("st:"+ctx.get_stack_depth()+" after loop");

		if (push) {
			ctx.emit_new_array(has_rest_arg ? 0 : arg_count, has_rest_arg);
			//System.out.println("st:"+ctx.get_stack_depth()+" after new_array("+arg_count+", "+has_rest_arg+")");
		} 
	}
	
	public String getMethodName() {
		return "[]";
	}

}
