package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassClass
	extends RubyClass
{
	static public RubyClassClass instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassClass)this; 
		super.init(meta);
	}
	public interface SelectClass { RubyMethod get_RubyClassClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectClass) { return ((SelectClass)sel).get_RubyClassClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Class cannot be instantiated directly"); }
}
