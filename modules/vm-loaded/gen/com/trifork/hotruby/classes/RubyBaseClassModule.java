package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassModule
	extends RubyClass
{
	static public RubyClassModule instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassModule)this; 
		super.init(meta);
	}
	interface SelectModule { RubyMethod get_RubyClassModule(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectModule) { return ((SelectModule)sel).get_RubyClassModule(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectModule.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Module cannot be instantiated directly"); }
}
