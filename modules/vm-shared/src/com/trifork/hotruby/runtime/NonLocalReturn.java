package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

/** used when a block issues a return statement */
public class NonLocalReturn extends RuntimeException {
	public ExposedLocals scope;
	public IRubyObject value;
	
	public NonLocalReturn(ExposedLocals scope, IRubyObject value)
	{
		this.scope = scope;
		this.value = value;
	}
	
	public IRubyObject perform(ExposedLocals caught_in_scope) {
		if (caught_in_scope == this.scope) {
			return value;
		} else if (caught_in_scope == null || this.scope == null) {
			return value;
		} else if (scope.top() == caught_in_scope) {
			return value;
		} else {
			throw this;
		}
	}
}
