package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassSymbol;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseSymbol
	extends RubyObject
	implements IRubySymbol
{
	public interface SelectSymbol { RubyMethod get_RubySymbol(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectSymbol) { return ((SelectSymbol)sel).get_RubySymbol(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectSymbol.class); }
	}
	public RubyClass get_class() { return RubyClassSymbol.instance; }
}
