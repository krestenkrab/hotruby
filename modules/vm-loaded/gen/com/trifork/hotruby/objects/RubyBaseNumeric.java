package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
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
