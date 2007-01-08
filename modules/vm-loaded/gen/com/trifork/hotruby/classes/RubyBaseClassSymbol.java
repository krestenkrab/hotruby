package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassSymbol
	extends RubyClass
{
	static public RubyClassSymbol instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassSymbol)this; 
		super.init(meta);
	}
	interface SelectSymbol { RubyMethod get_RubyClassSymbol(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectSymbol) { return ((SelectSymbol)sel).get_RubyClassSymbol(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectSymbol.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Symbol cannot be instantiated directly"); }
}
