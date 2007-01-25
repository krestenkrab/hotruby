package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;


public abstract class ExposedLocals implements EvalContext {
	
	public final ExposedLocals parent;
	
	public ExposedLocals(ExposedLocals parent) {
		this.parent = parent;
	}
	
	/** return the top-level scope (used as return target) */
	public ExposedLocals top() {
		ExposedLocals l;
		for (l = this; l.parent != null; l = l.parent);
		return l;
	}
	
	public abstract Object get(int idx);
	public abstract void set(int idx, Object value);
	
	public Object getdynamic(int level, int index) {
		ExposedLocals loc = this;
		for (int i = 0; i < level; i++) {
			loc = loc.parent;
		}
		return loc.get(index);
	}
	
	public void setdynamic(int level, int index, IRubyObject value) {
		ExposedLocals loc = this;
		for (int i = 0; i < level; i++) {
			loc = loc.parent;
		}
		loc.set(index, value);
	}

}
