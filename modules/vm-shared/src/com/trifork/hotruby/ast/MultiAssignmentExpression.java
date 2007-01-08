package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;


public class MultiAssignmentExpression extends Expression {

	private final SequenceExpression lhs;
	private final SequenceExpression rhs;

	public MultiAssignmentExpression(SequenceExpression lhs, SequenceExpression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		
		if (rhs.size() > 1 && !push && !rhs.has_rest_arg()) {
			
			int first_local = ctx.alloc_temp(rhs.size());
			
			// optimize this common case
			rhs.compile_to_locals(ctx, first_local);
			lhs.compile_assignment_from_locals(ctx, first_local, rhs.size(), push);
			
			ctx.free_temp(first_local);
			
		} else {
			rhs.compile_to_array(ctx);
			lhs.compile_assignment(ctx, push);
		}
	}

}
