package com.trifork.hotruby.objects;

import com.trifork.hotruby.ast.LocalVariable;
import com.trifork.hotruby.ast.LocalVariableAccess;
import com.trifork.hotruby.classes.RubyClassBinding;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyBinding extends RubyObject implements IRubyBinding {
	
	interface SelectBinding { RubyMethod get_RubyBinding(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectBinding) { return ((SelectBinding)sel).get_RubyBinding(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectBinding.class); }
	}
	public RubyClass get_class() { return RubyClassBinding.instance; }
	
	
	public RubyBlock get_block() {
		// TODO Auto-generated method stub
		return null;
	}
	public MetaModule get_lexical_context() {
		// TODO Auto-generated method stub
		return null;
	}
	public ExposedLocals get_locals() {
		// TODO Auto-generated method stub
		return null;
	}
	public IRubyObject get_self() {
		// TODO Auto-generated method stub
		return null;
	}
	public LocalVariableAccess access_local(String name, int level) {
		// TODO Auto-generated method stub
		return null;
	}
	public LocalVariable get_local(String name, int level, boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

}
