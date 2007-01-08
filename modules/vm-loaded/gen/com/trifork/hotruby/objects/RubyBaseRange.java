package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassRange;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
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
