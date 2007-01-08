package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassInteger;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseInteger
	extends RubyNumeric
	implements IRubyInteger
{
	public interface SelectInteger { RubyMethod get_RubyInteger(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectInteger) { return ((SelectInteger)sel).get_RubyInteger(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectInteger.class); }
	}
	public RubyClass get_class() { return RubyClassInteger.instance; }
}
