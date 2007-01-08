package com.trifork.hotruby.objects;


public class RubyNilClass extends RubyBaseNilClass {
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
