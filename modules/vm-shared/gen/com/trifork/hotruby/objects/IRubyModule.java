package com.trifork.hotruby.objects;
import com.trifork.hotruby.runtime.MetaModule;
public interface IRubyModule
	extends IRubyObject
{
	MetaModule get_meta_module();

	void init(MetaModule module);
}
