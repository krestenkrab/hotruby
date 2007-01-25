package com.trifork.hotruby.ast;

public class RescueClause {

	final int line;
	final SequenceExpression args;
	final Expression body;
	IdentifierExpression var;

	public RescueClause(int line, SequenceExpression args, IdentifierExpression id, Expression body)
	{
		this.line = line;
		this.args = args;
		this.var = id;
		this.body = body;
	}
	
	
}
