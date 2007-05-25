package com.trifork.hotruby.compiler;

import com.trifork.hotruby.interp.MethodISeq;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyMethod;

public abstract class CompiledMethod extends RubyMethod {

	static public String[] EMPTY_STRING_ARRAY = new String[0];
	private MethodISeq method_iseq;
	
	abstract int getDVarCount();

	abstract String[] getDVarNames();

	public abstract MetaModule getLexicalContext();

	public void setMethodISeq(MethodISeq iseq) {
		this.method_iseq = iseq;
	}

	@Override
	public RubyMethod specialize_for(MetaModule module, boolean is_module) {
		return method_iseq.specialize_for(module, is_module);
	}
}
