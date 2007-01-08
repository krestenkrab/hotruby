package com.trifork.hotruby.ast;



public abstract class VariableExpression extends Expression implements MethodDenominator {

	private final String name;
	
	public String toString() {
		return name;
	}

	public VariableExpression(String name) {
		this.name = name;
	}

	public String getMethodName() {
		return name;
	}

	public String getName() {
		return name;
	}

}
