package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseString
	extends RubyObject
	implements IRubyString
{
	public interface SelectString { RubyMethod get_RubyString(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectString) { return ((SelectString)sel).get_RubyString(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectString.class); }
	}
	public RubyClass get_class() { return RubyClassString.instance; }
}
