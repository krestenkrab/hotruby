package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class ConstantAccessor {

	/** the module in which this contant access appears */
	private final MetaModule lex_module;
	private final String name;
	private Constant constant;

	public ConstantAccessor(MetaModule module, String name) {
		this.lex_module = module;
		this.name = name;
		
		assert name != null;
		assert lex_module != null;
	}

	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	public void set(IRubyObject value) {
		if (constant == null) {
			lex_module.const_set(name, value);
			assert constant != null;
		} else {
			if (constant.getOwner() == lex_module) {
				constant.set(value);
			} else {
				lex_module.const_set(name, value);
			}
		}
	}

	public IRubyObject get() {
		if (constant == null) {
			lex_module.handle_undefined_const(name, lex_module);
			assert constant != null;
		}
		return constant.get(lex_module);
	}

}
