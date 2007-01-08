package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class RuntimeConstant extends Constant {

	private final String name;
	private final MetaModule module;
	protected IRubyObject value;

	public RuntimeConstant(MetaModule module, String name) {
		this.module = module;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public MetaModule getOwner() {
		return module;
	}

	@Override
	public void set(IRubyObject value) {
		pre_set(this, value);
		this.value = value;
	}

	@Override
	public IRubyObject get(MetaModule lex_context) {
		pre_get(lex_context, value);
		return value;
	}

	@Override
	protected IRubyObject getDirect() {
		return value;
	}

}
