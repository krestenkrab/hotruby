package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseObject
	implements IRubyObject
{
	interface SelectObject { RubyMethod get_RubyObject(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectObject) { return ((SelectObject)sel).get_RubyObject(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectObject.class); }
	}
	public RubyClass get_class() { return RubyClassObject.instance; }
}
