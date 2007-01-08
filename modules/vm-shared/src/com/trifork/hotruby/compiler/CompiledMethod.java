package com.trifork.hotruby.compiler;

import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyMethod;

public abstract class CompiledMethod extends RubyMethod {

	static public String[] EMPTY_STRING_ARRAY = new String[0];
	
	abstract int getDVarCount();

	abstract String[] getDVarNames();

	public abstract MetaModule getLexicalContext();

}
