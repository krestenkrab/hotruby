package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class Global {

	ThreadLocal<IRubyObject> loc;

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
		if (loc != null) {
			loc.set(value);
		} else {
			this.value = value;
		}
	}

	public IRubyObject get() {
		if (loc != null) {
			return loc.get();
		} 
		
		return value;
	}

	@Override
	public String toString() {
		return name + "=" + (get() == null ? "nil" : get().inspect());
	}

	public synchronized void becomeThreadLocal() {
		if (loc == null) {
			loc = new ThreadLocal<IRubyObject>() {
				@Override
				protected IRubyObject initialValue() {
					return Global.this.value;
				}
			};
		}
	}
}
