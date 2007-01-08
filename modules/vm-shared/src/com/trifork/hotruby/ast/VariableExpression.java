package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;


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
