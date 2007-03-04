package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public abstract class Expression implements Cloneable {

	protected int line;

	/**
	 * @deprecated Use the version taking a line number instead!
	 */
	public Expression() {
		// Nothing to do...
	}
	
	public Expression(int line) {
		this.line = line;
	}
	
	public int line() {
		return line;
	}

	/** compile this expression. 
	 * 
	 * @param push if false, don't leave result of expression on top-of-stack
	 */
	abstract void compile(CompileContext ctx, boolean push);

	public boolean isRestArg() {
		return false;
	}

	public boolean isBlockArg() {
		return false;
	}

	public void compile_assignment(CompileContext ctx, boolean push) {
		throw new InternalError("cannot be assigned to "+ this);
	}

}
