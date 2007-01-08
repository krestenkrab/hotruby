package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class Global {

	private IRubyObject value;
	private final String name;

	public Global(String name, IRubyObject value) {
		if (name.charAt(0) != '$') {
			throw new InternalError("global accessed with non-$ name");
		}
		this.name = name;
		this.value = value;
	}

	public void set(IRubyObject value) {
		this.value = value;
	}
	
	public IRubyObject get() {
		return value;
	}
	
	@Override
	public String toString() {
		return name + "=" + (value==null?"nil":value.inspect());
	}
}
