package com.trifork.hotruby.objects;
public class RubyFalseClass
	extends RubyBaseFalseClass
{
	public boolean isFalse() {
		return true;
	}

	public boolean isTrue() {
		return false;
	}
	
	public String inspect() {
		return "false";
	}

}
