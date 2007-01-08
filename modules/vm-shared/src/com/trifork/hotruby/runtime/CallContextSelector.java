package com.trifork.hotruby.runtime;

public abstract class CallContextSelector extends Selector implements CallContext {
	private MetaModule ctx;

	protected CallContextSelector(MetaModule ctx) {
		this.ctx = ctx;
	}
	
	public MetaModule getCaller() {
		return ctx;
	}
}
