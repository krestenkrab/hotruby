package com.trifork.hotruby.compiler;

import com.trifork.hotruby.ast.LocalVariable;
import com.trifork.hotruby.ast.LocalVariableAccess;
import com.trifork.hotruby.runtime.EvalContext;
import com.trifork.hotruby.runtime.ExposedLocals;

public abstract class CompiledEvalContext extends ExposedLocals implements EvalContext {

	protected CompiledEvalContext(ExposedLocals parent) {
		super(parent);
	}
	
	protected abstract EvalContext getParent();
	protected abstract String[] getDVarNames();
	
	protected int getDVarCount() {
		return getDVarNames().length;
	}
	
	public LocalVariableAccess access_local(String name, int level) {
		String[] dynamics = getDVarNames();
		for (int i = 0; i < dynamics.length; i++) {
			if (name.equals(dynamics[i])) {
				return new LocalVariableAccess(level, new LocalVariable(name, i, true));
			}
		}

		if (getParent() != null) {
			return getParent().access_local(name, level+1);
		} else {
			return null;
		}
	}

	public LocalVariable get_local(String name, int level, boolean create) {
		
		String[] dynamics = getDVarNames();
		for (int i = 0; i < dynamics.length; i++) {
			if (name.equals(dynamics[i])) {
				return new LocalVariable(name, i, true);
			}
		}

		if (getParent() != null) {
			return getParent().get_local(name, level+1, create);
		} else {
			return null;
		}
	}
	
	public ExposedLocals get_locals() {
		return this;
	}
}
