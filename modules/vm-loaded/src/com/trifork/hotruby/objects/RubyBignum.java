package com.trifork.hotruby.objects;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.trifork.hotruby.classes.RubyClassBignum;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.Selector;

public class RubyBignum extends RubyBaseBignum {

	java.math.BigInteger value;

	public IRubyFixnum inverse_cmp(int i) {
		// TODO: Kresten
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public long longValue() {
		return value.longValue();
	}
	
	public String inspect() {
		return value.toString();
	}
	
	public RubyBignum(java.math.BigInteger value) {
		this.value = value;
	}
	
	@Override
	public IRubyObject inverse_bit_or(int other) {
		return newInteger(value.or(BigInteger.valueOf(other)));
	}

	@Override
	public IRubyObject inverse_bit_and(int other) {
		return newInteger(value.and(BigInteger.valueOf(other)));
	}
	
	@Override
	public IRubyObject inverse_xor(int fixnum) {
		return newInteger(value.xor(BigInteger.valueOf(fixnum)));
	}

	public int intValue() {
		return value.intValue();
	}
	
	public IRubyFloat to_f() {
		return new RubyFloat(value.doubleValue());
	}
	
	@Override
	public IRubyObject fast_plus(IRubyObject op, Selector selector) {
		return op_plus(op);
	}
	
	@Override
	public IRubyObject fast_minus(IRubyObject op, Selector selector) {
		return op_minus(op);
	}
	
	@Override
	public IRubyObject fast_lt(IRubyObject arg, Selector selector) {
		BigInteger other = RubyInteger.mm_induced_from(arg).asBigInteger();
		return bool (value.compareTo(other) < 0);
	}
	@Override
	public IRubyObject fast_le(IRubyObject arg, Selector selector) {
		BigInteger other = RubyInteger.mm_induced_from(arg).asBigInteger();
		return bool (value.compareTo(other) <= 0);
	}

	@Override
	public IRubyObject fast_eq2(IRubyObject arg, Selector selector) {
		if (arg instanceof RubyBignum) {
			return bool (value.equals(((RubyBignum)arg).value));
		}
		
		return bool(false);
	}
	
	public IRubyObject op_plus(IRubyObject other) {
		return newInteger(value.add(numeric(other).asBigInteger()));
	}

	public IRubyObject op_minus(IRubyObject other) {
		return newInteger(value.subtract(numeric(other).asBigInteger()));
	}

	public BigInteger value() {
		return value;
	}

	public BigInteger asBigInteger() {
		return value;
	}

	public IRubyObject inverse_gt(double float1) {
		int compare = (new BigDecimal(float1).toBigInteger()).compareTo(value);
		return bool(compare > 0);
	}

	public RubyNumeric inverse_minus(int fixnum) {
		return newInteger(BigInteger.valueOf(fixnum).subtract(value));
	}

	public IRubyNumeric inverse_mult(double float1) {
		return new RubyFloat(float1 * value.doubleValue());
	}

	public IRubyNumeric inverse_plus(int fixnum) {
		return newInteger(value.add(BigInteger.valueOf(fixnum)));
	}

	public IRubyObject inverse_plus(double float1) {
		return new RubyFloat(float1 + value.doubleValue());
	}

	public static IRubyObject unmarshalFrom(UnmarshalStream stream) {
		// TODO: implement
		return null;
	}

	@Override
	public IRubyFixnum inverse_cmp(int value) {
		int cmp = BigInteger.valueOf(value).compareTo(this.value);
		return new RubyFixnum(cmp);
	}

}
