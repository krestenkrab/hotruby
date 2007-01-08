package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassTime;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseTime
	extends RubyObject
	implements IRubyTime
{
	public interface SelectTime { RubyMethod get_RubyTime(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectTime) { return ((SelectTime)sel).get_RubyTime(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectTime.class); }
	}
	public RubyClass get_class() { return RubyClassTime.instance; }
}
