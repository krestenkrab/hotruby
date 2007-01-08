package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassFloat;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseFloat
	extends RubyNumeric
	implements IRubyFloat
{
	public interface SelectFloat { RubyMethod get_RubyFloat(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFloat) { return ((SelectFloat)sel).get_RubyFloat(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectFloat.class); }
	}
	public RubyClass get_class() { return RubyClassFloat.instance; }
}
