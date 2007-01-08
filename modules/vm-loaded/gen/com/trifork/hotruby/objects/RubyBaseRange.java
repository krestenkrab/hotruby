package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseRange
	extends RubyObject
	implements IRubyRange
{
	interface SelectRange { RubyMethod get_RubyRange(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectRange) { return ((SelectRange)sel).get_RubyRange(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectRange.class); }
	}
	public RubyClass get_class() { return RubyClassRange.instance; }
}
