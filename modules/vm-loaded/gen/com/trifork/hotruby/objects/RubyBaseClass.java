package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
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
