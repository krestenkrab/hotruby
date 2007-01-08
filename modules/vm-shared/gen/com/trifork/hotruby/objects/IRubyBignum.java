package com.trifork.hotruby.objects;
import java.math.BigInteger;

import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public interface IRubyBignum
	extends IRubyInteger
{
	BigInteger value();
}
