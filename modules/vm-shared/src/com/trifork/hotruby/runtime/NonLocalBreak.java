package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class NonLocalBreak extends NonLocalJump {

	IRubyObject result;
	
	public NonLocalBreak() {
		super(NONLOCAL_BREAK);
		result = null;
	}

	public NonLocalBreak(IRubyObject result) {
		super(NONLOCAL_BREAK);
		this.result = result;
	}
}
