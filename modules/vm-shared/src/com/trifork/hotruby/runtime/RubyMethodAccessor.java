package com.trifork.hotruby.runtime;

public class RubyMethodAccessor {

	RubyMethod value;
	protected final MetaModule module;
	protected final String name;
	private final boolean is_module;
	
	@Override
	public String toString() {
		return "accessor for " + (is_module ? "module" : "instance") + " method " + name + " in " + module.getName();
	}
	
	public RubyMethodAccessor(MetaModule module, String name, boolean is_module) {
		this.module = module;
		this.name = name;
		this.is_module = is_module;
	}

	public RubyMethod get() {
		if (value == null) {
			if (is_module) {
				value = module.lookup_module_method(name);
			} else {
				value = module.lookup_instance_method(name, false);
			}
			if (value == null) {
				return new MissingMethod(module.getRuntime(), module.getRuntime()
						.getSelector(module, name));
			}
		}
		
		return value;
	}
	
	public void set(RubyMethod m) {
		this.value = m;
	}

}
