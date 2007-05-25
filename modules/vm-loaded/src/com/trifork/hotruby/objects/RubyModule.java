package com.trifork.hotruby.objects;

import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyModule
	extends RubyBaseModule
{
	private MetaModule meta_module;

	public MetaModule get_singleton_meta_module() {
		return meta_module;
	}

	//
	// put taint, frozen, etc. here.
	//
	
	/** all module-subclasses exhibit singleton behavior */
	@Override
	public RubyMethod do_select(Selector sel) {
		return select(sel);
	}
	
	@Override
	public String inspect() {
		return meta_module.getName();
	}
	
	public MetaModule get_meta_module() {
		return meta_module;
	}
	
	public void init(MetaModule module) {
		this.meta_module = module;
	}

	public static IRubyObject unmarshalFrom(UnmarshalStream stream) {
		throw new RuntimeException("not implemented");
	}
}
