package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassFixnum;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
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
