package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
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
