package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class NormalReturnException extends CallerControlException {

	private final IRubyObject value;

	public NormalReturnException(IRubyObject value) {
		this.value = value;
	}

	@Override
	public IRubyObject perform(ExposedLocals locals, RubyBlock block) {
		return value;
	}

}
