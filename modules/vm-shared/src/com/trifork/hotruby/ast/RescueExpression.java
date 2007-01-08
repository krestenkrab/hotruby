package com.trifork.hotruby.ast;

import java.util.List;

import com.trifork.hotruby.interp.CompileContext;

public class RescueExpression extends Expression {

	private final Expression body;
	private final List<RescueClause> rescues;
	private final Expression elseStmt;
	private final Expression ensureStmt;

	public RescueExpression(Expression body, List<RescueClause> rescues, Expression elseStmt, Expression ensureStmt) {
		this.body = body;
		this.rescues = rescues;
		this.elseStmt = elseStmt;
		this.ensureStmt = ensureStmt;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		// TODO Auto-generated method stub

	}

}
