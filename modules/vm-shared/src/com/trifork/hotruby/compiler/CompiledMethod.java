package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyRuntime;

public abstract class CompiledMethod extends RubyMethod {

	static public String[] EMPTY_STRING_ARRAY = new String[0];
	
	abstract int getDVarCount();

	abstract String[] getDVarNames();

	public abstract MetaModule getLexicalContext();

}
