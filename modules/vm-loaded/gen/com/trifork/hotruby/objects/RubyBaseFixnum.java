package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseFixnum
	extends RubyInteger
	implements IRubyFixnum
{
	public interface SelectFixnum { RubyMethod get_RubyFixnum(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFixnum) { return ((SelectFixnum)sel).get_RubyFixnum(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectFixnum.class); }
	}
	public RubyClass get_class() { return RubyClassFixnum.instance; }
}
