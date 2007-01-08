package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Label;

public class LogicalAndExpression extends Expression {

	private final Expression first;
	private final Expression second;

	public LogicalAndExpression(Expression first, Expression second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {

		int st = ctx.get_stack_depth();
		
		first.compile(ctx, true);
		if (push) {
			ctx.emit_dup();
		}
		Label done = ctx.new_label();
		ctx.emit_branch_unless(done);
		if (push) {
			ctx.emit_pop();
		}
		
		second.compile(ctx, push);
		
		ctx.mark(done);

		assert push ? (st+1==ctx.get_stack_depth()) : (st==ctx.get_stack_depth());
	}

}
