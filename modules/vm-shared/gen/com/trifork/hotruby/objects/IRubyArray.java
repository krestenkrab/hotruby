package com.trifork.hotruby.objects;
public interface IRubyArray
	extends IRubyObject
{
	IRubyArray add(IRubyObject obj);
	
	int int_size();
	void int_at_put(int i, IRubyObject object);
	IRubyObject int_at(int i);
	IRubyArray int_rest(int idx);

}
