package com.trifork.hotruby.objects;

import java.io.IOException;
import java.math.BigInteger;

import com.trifork.hotruby.classes.RubyClassFixnum;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.Selector;

public class RubyFixnum extends RubyBaseFixnum {
	
	private static final IRubyFixnum FIX_MINUS_1 =  new RubyFixnum(-1);

	private static final IRubyFixnum FIX_PLUS_1 = new RubyFixnum(1);

	private static final IRubyFixnum FIX0 = new RubyFixnum(0);

	public String inspect() {
		return Integer.toString(value);
	}
	
	public BigInteger asBigInteger() {
		return BigInteger.valueOf(value);
	}

	public long longValue() {
		return value;
	}
	
	static CallContext META = RubyClassFixnum.instance.get_meta_class();

	int value;

	public RubyFixnum(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public IRubyFloat to_f() {
		return new RubyFloat((double) value);
	}

	@Override
	public
	IRubyObject fast_bit_or(IRubyObject arg, Selector selector) {
		RubyInteger intval = RubyInteger.mm_induced_from(arg);
		return intval.inverse_bit_or(value);
	}
	
	@Override
	public
	IRubyObject fast_bit_and(IRubyObject arg, Selector selector) {
		RubyInteger intval = RubyInteger.mm_induced_from(arg);
		return intval.inverse_bit_and(value);
	}
	
	@Override
	public IRubyObject fast_cmp(IRubyObject arg, Selector selector) {
		return op_cmp(arg);
	}
	
	private IRubyObject op_cmp(IRubyObject arg) {
		return numeric(arg).inverse_cmp(this.value);
	}

	@Override
	public IRubyFixnum inverse_cmp(int intval) {
		if (intval < value) {
			return FIX_MINUS_1;
		} else if (intval > value) {
			return FIX_PLUS_1;
		} else {
			return FIX0;
		}
	}
	
	public IRubyObject fast_eq2(IRubyObject arg, Selector selector) {
		if (arg == this) return LoadedRubyRuntime.TRUE;
		if (arg instanceof RubyFixnum) {
			RubyFixnum fixnum = (RubyFixnum) arg;
			return bool(value == fixnum.value);
		}
		return bool(false);
	}

	@Override
	public IRubyObject inverse_bit_or(int other) {
		return new RubyFixnum(value|other);
	}
	
	@Override
	public IRubyObject inverse_bit_and(int other) {
		return new RubyFixnum(value&other);
	}
	
	@Override
	public IRubyObject fast_bit_not(Selector selector) {
		return new RubyFixnum(~value);
	}

	@Override
	public IRubyObject fast_plus(IRubyObject op, Selector selector) {
		return op_plus(op);
	}
	
	@Override
	public IRubyObject fast_minus(IRubyObject op, Selector selector) {
		return numeric(op).inverse_minus(value);
	}
	
	@Override
	public IRubyObject fast_lt(IRubyObject arg, Selector selector) {
		return op_lt(arg);
	}
	
	@Override
	public IRubyObject fast_le(IRubyObject arg, Selector selector) {
		return op_le(arg);
	}
	
	@Override
	public IRubyObject fast_gt(IRubyObject arg, Selector selector) {
		return op_gt(arg);
	}
	
	public IRubyObject op_plus(IRubyObject op) {
		return (IRubyNumeric)numeric(op).inverse_plus(value);
	}

	public IRubyObject op_minus(IRubyObject op) {
		return numeric(op).inverse_minus(value);
	}
	
	public IRubyObject fast_bit_xor(IRubyObject arg, Selector sel) {
		return RubyInteger.mm_induced_from(arg).inverse_xor(value);
	}
	
	@Override
	public IRubyObject inverse_xor(int fixnum) {
		return new RubyFixnum(fixnum ^ value);
	}
	
	public IRubyObject fast_rshift(IRubyObject arg, Selector sel) {
		int val = RubyInteger.mm_induced_from(arg).intValue();
		return new RubyFixnum(value >> val);
	}
	

	public RubyNumeric inverse_minus(int fixnum) {
		return newInteger((long)fixnum - value);
	}

	public IRubyNumeric inverse_plus(int fixnum) {
		return newInteger((long)fixnum + value);
	}
	
	public int value() {
		return value;
	}

	public IRubyObject inverse_gt(double float1) {
		return bool(float1 > value);
	}

	public IRubyNumeric inverse_mult(double float1) {
		return new RubyFloat(float1 * value);
	}

	public IRubyObject inverse_plus(double float1) {
		return new RubyFloat(float1 + value);
	}

	public static IRubyObject unmarshalFrom(UnmarshalStream stream) throws IOException {
		return new RubyFixnum(stream.unmarshalInt());
	}

	public IRubyObject op_lt(IRubyObject op) {
		IRubyInteger other = RubyInteger.mm_induced_from(op);
		return value < other.longValue() ? LoadedRubyRuntime.TRUE : LoadedRubyRuntime.FALSE;
	}

	public IRubyObject op_le(IRubyObject op) {
		IRubyInteger other = RubyInteger.mm_induced_from(op);
		return value <= other.longValue() ? LoadedRubyRuntime.TRUE : LoadedRubyRuntime.FALSE;
	}

	public IRubyObject op_gt(IRubyObject op) {
		IRubyInteger other = RubyInteger.mm_induced_from(op);
		return value > other.longValue() ? LoadedRubyRuntime.TRUE : LoadedRubyRuntime.FALSE;
	}

	public IRubyObject to_s() {
		return new RubyString(Integer.toString(value));
	}

	public IRubyObject op_succ() {
		if (value < Integer.MAX_VALUE) {
			return new RubyFixnum(value+1);
		} else {
			return newInteger(1L + value);
		}
	}

}
