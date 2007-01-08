package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public interface IRubyModule
	extends IRubyObject
{
	MetaModule get_meta_module();

	void init(MetaModule module);
}
