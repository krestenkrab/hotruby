package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseRegexp
	extends RubyObject
	implements IRubyRegexp
{
	public interface SelectRegexp { RubyMethod get_RubyRegexp(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectRegexp) { return ((SelectRegexp)sel).get_RubyRegexp(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectRegexp.class); }
	}
	public RubyClass get_class() { return RubyClassRegexp.instance; }
}
