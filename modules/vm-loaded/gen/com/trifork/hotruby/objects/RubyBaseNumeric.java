package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassNumeric;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseNumeric
	extends RubyObject
	implements IRubyNumeric
{
	interface SelectNumeric { RubyMethod get_RubyNumeric(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectNumeric) { return ((SelectNumeric)sel).get_RubyNumeric(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectNumeric.class); }
	}
	public RubyClass get_class() { return RubyClassNumeric.instance; }
}
