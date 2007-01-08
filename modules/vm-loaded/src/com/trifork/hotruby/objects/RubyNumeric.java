package com.trifork.hotruby.objects;

import java.math.BigInteger;

public abstract class RubyNumeric
	extends RubyBaseNumeric
{
	protected static RubyNumeric numeric(IRubyObject op) {
		return ((RubyNumeric)op);
	}


	public static RubyInteger newInteger(long value) {
		if (((int) value) == value) {
			return new RubyFixnum((int) value);
		} else {
			return new RubyBignum(BigInteger.valueOf(value));
		}
	}

	public static RubyInteger newInteger(BigInteger value) {
		if (value.bitLength() < 32) {
			return new RubyFixnum(value.intValue());
		} else {
			return new RubyBignum(value);
		}
	}
	
	public abstract IRubyObject op_minus(IRubyObject numeric);

	public abstract RubyNumeric inverse_minus(int bignum);
}
