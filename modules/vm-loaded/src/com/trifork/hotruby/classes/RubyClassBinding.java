package com.trifork.hotruby.classes;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;


public class RubyClassBinding extends RubyClass {

	static public RubyClassBinding instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassBinding)this; 
		super.init(meta);
	}
	interface SelectBinding { RubyMethod get_RubyClassBinding(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectBinding) { return ((SelectBinding)sel).get_RubyClassBinding(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectBinding.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw new InternalError("should not happen"); }
}
