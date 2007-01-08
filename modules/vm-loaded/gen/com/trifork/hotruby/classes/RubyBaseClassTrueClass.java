package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassTrueClass
	extends RubyClass
{
	static public RubyClassTrueClass instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassTrueClass)this; 
		super.init(meta);
	}
	public interface SelectTrueClass { RubyMethod get_RubyClassTrueClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectTrueClass) { return ((SelectTrueClass)sel).get_RubyClassTrueClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectTrueClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyTrueClass(); }
}
