package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.MetaModule.ConstKey;

public class ConstantAccessor {

	/** the module in which this contant access appears */
	private final MetaModule lex_module;
	private final ConstKey key;
	private Constant constant;

	public ConstantAccessor(MetaModule module, ConstKey key) {
		this.lex_module = module;
		this.key = key;
		
		assert key != null;
		assert lex_module != null;
	}

	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	public void set(IRubyObject value) {
		if (constant == null) {
			lex_module.const_set(key.name, value);
			// assert constant != null;
		} else {
			if (constant.getOwner() == lex_module) {
				constant.set(value);
			} else {
				lex_module.const_set(key.name, value);
			}
		}
	}

	public IRubyObject get() {
		if (constant == null) {
			constant = lex_module.find_constant(key.name, key.scope);
			if (constant == null) {
			lex_module.handle_undefined_const(key.name, lex_module);
			assert constant != null;
			}
		}
		return constant.get(lex_module);
	}

}
