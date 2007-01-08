package com.trifork.hotruby.objects;
import java.math.BigInteger;
public interface IRubyBignum
	extends IRubyInteger
{
	BigInteger value();
}
