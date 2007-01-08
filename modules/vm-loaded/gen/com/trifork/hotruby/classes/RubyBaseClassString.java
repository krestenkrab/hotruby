package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassString
	extends RubyClass
{
	static public RubyClassString instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassString)this; 
		super.init(meta);
	}
	public interface SelectString { RubyMethod get_RubyClassString(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectString) { return ((SelectString)sel).get_RubyClassString(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectString.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class String cannot be instantiated directly"); }
}
