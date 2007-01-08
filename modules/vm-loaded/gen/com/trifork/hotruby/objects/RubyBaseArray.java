package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassArray;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseArray
	extends RubyObject
	implements IRubyArray
{
	public interface SelectArray { RubyMethod get_RubyArray(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectArray) { return ((SelectArray)sel).get_RubyArray(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectArray.class); }
	}
	public RubyClass get_class() { return RubyClassArray.instance; }
}
