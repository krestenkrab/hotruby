package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassFalseClass
	extends RubyClass
{
	static public RubyClassFalseClass instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassFalseClass)this; 
		super.init(meta);
	}
	interface SelectFalseClass { RubyMethod get_RubyClassFalseClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFalseClass) { return ((SelectFalseClass)sel).get_RubyClassFalseClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFalseClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyFalseClass(); }
}
