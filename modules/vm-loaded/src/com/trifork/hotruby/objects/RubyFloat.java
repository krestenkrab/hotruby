package com.trifork.hotruby.objects;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.trifork.hotruby.classes.RubyClassFloat;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.Selector;

public class RubyFloat extends RubyBaseFloat {
	double value;

	private static final Selector TO_F = LoadedRubyRuntime.instance
			.getSelector(RubyClassFloat.instance.get_meta_module(), "to_f");

	private static final IRubyFixnum FIX_MINUS_1 = new RubyFixnum(-1);

	private static final IRubyFixnum FIX_PLUS_1 = new RubyFixnum(1);

	private static final IRubyFixnum FIX_ZERO = new RubyFixnum(0);

	@Override
	public String inspect() {
		return String.valueOf(value);
	}

	public IRubyString to_s() {
		return new RubyString(String.valueOf(value));
	}

	public RubyFloat(double value) {
		this.value = value;
	}

	public IRubyFloat to_f() {
		return this;
	}

	public IRubyObject op_plus(IRubyObject op) {
		return numeric(op).inverse_plus(value);
	}

	public IRubyObject op_neg() {
		return new RubyFloat(-this.value);
	}

	public IRubyObject im_abs() {
		return new RubyFloat(Math.abs(this.value));
	}

	public IRubyObject op_gt(IRubyObject op) {
		return numeric(op).inverse_gt(value);
	}

	public IRubyObject op_mod(IRubyObject op) {
		RubyFloat other = (RubyFloat) op.fast_to_f(TO_F);
		return new RubyFloat(this.value % other.value);
	}

	public IRubyObject op_minus(IRubyObject op) {
		RubyFloat other = (RubyFloat) op.fast_to_f(TO_F);
		return new RubyFloat(this.value - other.value);
	}

	public IRubyObject op_lt(IRubyObject op) {
		RubyFloat other = (RubyFloat) op.fast_to_f(TO_F);
		return (this.value < other.value) ? LoadedRubyRuntime.TRUE
				: LoadedRubyRuntime.FALSE;
	}

	public IRubyObject op_mult(IRubyObject op) {
		return numeric(op).inverse_mult(value);
	}

	public IRubyObject im_ceil() {
		return new RubyFloat(Math.ceil(this.value));
	}

	public IRubyObject op_ge(IRubyObject op) {
		RubyFloat other = (RubyFloat) op.fast_to_f(TO_F);
		return (this.value >= other.value) ? LoadedRubyRuntime.TRUE
				: LoadedRubyRuntime.FALSE;
	}

	public IRubyObject op_eq(IRubyObject op) {
		RubyFloat other = (RubyFloat) op.fast_to_f(TO_F);
		return (this.value == other.value) ? LoadedRubyRuntime.TRUE
				: LoadedRubyRuntime.FALSE;
	}

	public IRubyObject op_le(IRubyObject op) {
		RubyFloat other = (RubyFloat) op.fast_to_f(TO_F);
		return (this.value <= other.value) ? LoadedRubyRuntime.TRUE
				: LoadedRubyRuntime.FALSE;
	}

	public IRubyObject eql_p(IRubyObject other) {
		if (other instanceof RubyFloat)
			return op_eq(other);
		return LoadedRubyRuntime.FALSE;
	}

	public IRubyObject im_divmod(IRubyObject y_val) {

		RubyFloat x = this;

		double y = ((RubyFloat) y_val.fast_to_f(TO_F)).value;
		double q = Math.floor(x.value / y);
		double r = x.value - q * y;

		RubyArray result = new RubyArray();
		result.add(new RubyFloat(q));
		result.add(new RubyFloat(r));

		return result;
	}

	public double value() {
		return value;
	}

	public IRubyObject inverse_gt(double float1) {
		return bool(float1 > value);
	}

	public IRubyNumeric inverse_minus(BigInteger bignum) {
		return newInteger(bignum.subtract(asBigInteger()));
	}

	public RubyNumeric inverse_minus(int bignum) {
		return new RubyFloat(bignum - value);
	}

	public IRubyNumeric inverse_mult(double float1) {
		return new RubyFloat(float1 * value);
	}

	public IRubyNumeric inverse_mult(int num) {
		return new RubyFloat(num * value);
	}

	public IRubyNumeric inverse_div(int num) {
		return new RubyFloat(num / value);
	}

	public IRubyNumeric inverse_plus(int fixnum) {
		return new RubyFloat(fixnum + value);
	}

	public IRubyNumeric inverse_plus(BigInteger bignum) {
		return newInteger(bignum.add(asBigInteger()));
	}

	public BigInteger asBigInteger() {
		return new BigDecimal(value).toBigInteger();
	}

	public IRubyObject inverse_plus(double float1) {
		return new RubyFloat(float1 + value);
	}

	public static IRubyObject unmarshalFrom(UnmarshalStream stream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRubyFixnum inverse_cmp(int ivalue) {
		if (ivalue < value) { 
			return FIX_MINUS_1;
		} else if (ivalue > value) {
			return FIX_PLUS_1;
		} else {
			return FIX_ZERO;
		}
	}

	public double doubleValue() {
		return value;
	}

}
