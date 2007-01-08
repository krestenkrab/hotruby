package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

/** accessor for ivars store in a hash map */
public class DynamicIVarAccessor extends RubyIvarAccessor {

	public DynamicIVarAccessor(String name) {
		super(name);
	}

	@Override
	public IRubyObject get(IRubyObject self) {
		SingletonState ss = self.get_singleton_state(false);
		IRubyObject result;
		if (ss != null) {
			result = ss.get_dynamic_ivar_or_null(name);
			if (result != null) { return result; }
		}
		return self.getRuntime().getNil();
	}

	@Override
	public void set(IRubyObject self, IRubyObject value) {
		SingletonState ss = self.get_singleton_state(true);
		ss.set_dynamic_ivar(name, value);
	}

}
