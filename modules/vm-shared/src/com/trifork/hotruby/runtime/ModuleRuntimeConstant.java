package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class ModuleRuntimeConstant extends RuntimeConstant {

	private MetaModule meta_value;

	public ModuleRuntimeConstant(MetaModule module, String name) {
		super(module, name);
	}
	
	@Override
	protected IRubyObject getDirect() {
		if (value == null) {
			if (meta_value != null) {
				this.value = meta_value.get_base_module();
				meta_value = null;
			}
		}
		return value;
	}

	protected void pre_get(MetaModule lex_context, Object value) {
		if (value == null) {
			if (meta_value != null) {
				this.value = meta_value.get_base_module();
				meta_value = null;
			} else {
				getOwner().handle_undefined_const(getName(), lex_context);
			}
		}
	}

	public void set_meta(MetaModule module) {
		this.meta_value = module;
	}


}
