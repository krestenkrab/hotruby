package com.trifork.hotruby.objects;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;

public abstract class RubyInteger extends RubyBaseInteger implements IRubyInteger {

	public IRubyInteger to_i() {
		return this;
	}


	public static RubyInteger induced_from(IRubyObject obj) {
		if (obj instanceof RubyInteger) {
			return (RubyInteger) obj;
		} else {
			throw LoadedRubyRuntime.instance.newArgumentError("argument not an integer, got: " + obj.get_class().toString());
		}
	}

	public static RubyInteger induced_from_allow_string(IRubyObject obj) {
		if (obj instanceof RubyString) {
			String intStr = ((RubyString) obj).value;
			return RubyInteger.newInteger(Integer.parseInt(intStr));
			//TODO, perhaps an error-message here instead of numberformatexception
		}
		return induced_from(obj);
	}
	

	public abstract IRubyObject inverse_bit_or(int fixnum);


	public abstract IRubyObject inverse_bit_and(int fixnum);


	public abstract IRubyObject inverse_xor(int fixnum);


}
