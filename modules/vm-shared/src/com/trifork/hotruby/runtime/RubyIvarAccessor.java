package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public abstract class RubyIvarAccessor {

	protected final String name;
	public RubyIvarAccessor(String name) {
		this.name = name;
	}

	public abstract void set(IRubyObject self, IRubyObject object);
	public abstract IRubyObject get(IRubyObject self);

	public boolean isCompiled() {
		return false;
	}

	public String getName() {
		return name;
	}

	public String getFieldName() {
		throw new InternalError("call this for my subclass");
	}

}
