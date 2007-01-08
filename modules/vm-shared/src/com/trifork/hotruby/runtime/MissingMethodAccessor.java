package com.trifork.hotruby.runtime;

public class MissingMethodAccessor extends RubyMethodAccessor {

	public MissingMethodAccessor(MetaModule module, String name, boolean is_module) {
		super(module, name, is_module);
	}

	@Override
	public RubyMethod get() {
		return new MissingMethod(module.getRuntime(), module.getRuntime().getSelector(module, name));
	}
}
