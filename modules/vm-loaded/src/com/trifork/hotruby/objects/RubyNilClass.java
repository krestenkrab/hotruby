package com.trifork.hotruby.objects;

import com.trifork.hotruby.runtime.Selector;


public class RubyNilClass extends RubyBaseNilClass {

	@Override
	public IRubyObject fast_to_str(Selector selector) {
		return new RubyString("nil");
	}
	
	public boolean isNil() {
		return true;
	}

	public boolean isFalse() {
		return true;
	}

	public boolean isTrue() {
		return false;
	}

	public String inspect() {
		return "nil";
	}
}
