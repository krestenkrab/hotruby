package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassObject;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseObject
	implements IRubyObject
{
	public interface SelectObject { RubyMethod get_RubyObject(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectObject) { return ((SelectObject)sel).get_RubyObject(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectObject.class); }
	}
	public RubyClass get_class() { return RubyClassObject.instance; }
}
