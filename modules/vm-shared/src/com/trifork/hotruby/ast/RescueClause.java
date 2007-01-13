package com.trifork.hotruby.ast;

public class RescueClause {

	final int line;
	final SequenceExpression args;
	final Expression body;
	String name;

	public RescueClause(int line, SequenceExpression args, String name, Expression body)
	{
		this.line = line;
		this.args = args;
		this.name = name;
		this.body = body;
	}
	
	
}
