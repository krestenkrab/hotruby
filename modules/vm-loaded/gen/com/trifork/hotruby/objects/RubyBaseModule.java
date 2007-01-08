package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseModule
	extends RubyObject
	implements IRubyModule
{
	interface SelectModule { RubyMethod get_RubyModule(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectModule) { return ((SelectModule)sel).get_RubyModule(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectModule.class); }
	}
	public RubyClass get_class() { return RubyClassModule.instance; }
}
