package com.trifork.hotruby.ast;

public class RescueClause {

	private final int line;
	private final SequenceExpression args;
	private final Expression body;
	private String name;

	public RescueClause(int line, SequenceExpression args, String name, Expression body)
	{
		this.line = line;
		this.args = args;
		this.name = name;
		this.body = body;
	}
	
	
}
