package com.trifork.hotruby.objects;
public interface IRubyRange
	extends IRubyObject
{
	IRubyObject first();
	IRubyObject last();
	IRubyObject includes(IRubyObject other);
}
