package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClass
	extends RubyModule
	implements IRubyClass
{
	interface SelectClass { RubyMethod get_RubyClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectClass) { return ((SelectClass)sel).get_RubyClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
}
