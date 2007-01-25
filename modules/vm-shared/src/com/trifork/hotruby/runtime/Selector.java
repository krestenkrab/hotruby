package com.trifork.hotruby.runtime;

public abstract class Selector implements CallContext {

	String name;
	private MetaModule caller;

	public String getName() {
		if (name == null) {
			name = RubyRuntime.decodeMethodNameFromSelectorClassName(getClass()
					.getName());
		}
		return name;
	}
	
	public MetaModule getCaller() {
		if (caller == null) {
			RubyClassLoader loader = (RubyClassLoader)this.getClass().getClassLoader();
			caller = loader.getRuntime().computeModuleForLoadedSelector(this);
		}
		return caller;
	}

	abstract void set(Selector selector);
	public abstract Selector get();

}
