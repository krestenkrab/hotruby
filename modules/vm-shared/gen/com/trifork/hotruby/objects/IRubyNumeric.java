package com.trifork.hotruby.objects;
import java.math.BigInteger;
public interface IRubyNumeric
	extends IRubyObject
{
	/** helper methods for converting argument types */
	IRubyNumeric inverse_minus(int bignum);
	IRubyNumeric inverse_plus(int fixnum);
	IRubyNumeric inverse_mult(int fixnum);
	IRubyNumeric inverse_div(int fixnum);
	
	IRubyNumeric inverse_mult(double float1);
	IRubyObject inverse_plus(double float1);
	IRubyObject inverse_gt(double float1);

	BigInteger asBigInteger();

}
