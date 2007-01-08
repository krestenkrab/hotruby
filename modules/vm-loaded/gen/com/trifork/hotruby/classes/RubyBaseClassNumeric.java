package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassNumeric
	extends RubyClass
{
	static public RubyClassNumeric instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassNumeric)this; 
		super.init(meta);
	}
	interface SelectNumeric { RubyMethod get_RubyClassNumeric(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectNumeric) { return ((SelectNumeric)sel).get_RubyClassNumeric(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectNumeric.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Numeric cannot be instantiated directly"); }
}
