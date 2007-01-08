package com.trifork.hotruby.objects;
public class RubyTrueClass
	extends RubyBaseTrueClass
{
	public boolean isFalse() {
		return false;
	}

	public boolean isTrue() {
		return true;
	}
	
	public String inspect() {
		return "true";
	}
}
