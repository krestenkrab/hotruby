package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class BlockArgExpression extends Expression {

	private final Expression expr;

	public BlockArgExpression(Expression expr) {
		this.expr = expr;
	}

	@Override
	public boolean isBlockArg() {
		return true;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		expr.compile(ctx, true);
		ctx.emit_make_blockarg();
		if (!push) {
			ctx.emit_pop();
		}
	}


}
