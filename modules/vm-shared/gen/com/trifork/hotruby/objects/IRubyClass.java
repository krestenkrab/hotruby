package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public interface IRubyClass
	extends IRubyModule
{

	IRubyObject newInstance(IRubyObject[] objects, CallContext ctx);

	IRubyObject newInstance();

	MetaClass get_meta_class();
	
	void init(MetaClass module);

	IRubyString get_name();

}
