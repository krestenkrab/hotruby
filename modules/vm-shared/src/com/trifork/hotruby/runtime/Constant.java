package com.trifork.hotruby.runtime;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;

public abstract class Constant {

	protected static void pre_set(Constant constant, IRubyObject value) {
		if (value instanceof IRubyModule) {
			MetaModule mm = ((IRubyModule)value).get_meta_module();
			mm.set_context(constant.getOwner(), constant.getName());
		}
		
		for (int i = 0; i < constant.accessors.size(); i++) {
			constant.accessors.get(i).set(value);
		}
	}
	
	protected void pre_get(MetaModule lex_context, Object value) {
		if (value == null) {
			getOwner().handle_undefined_const(getName(), lex_context);
		}
	}

	public abstract MetaModule getOwner();
	public abstract String getName();

	public abstract void set(IRubyObject value);
	public abstract IRubyObject get(MetaModule lex_context);
	
	protected abstract  IRubyObject getDirect();
	
	List<ConstantAccessor> accessors = new ArrayList<ConstantAccessor>();

	public void add_accessor(ConstantAccessor accessor) {
		accessors.add(accessor);
		if (getDirect() != null) {
			accessor.set(getDirect());
		}
	}

}
