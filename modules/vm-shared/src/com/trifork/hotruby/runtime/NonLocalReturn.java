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
}
