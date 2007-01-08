package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassTrueClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseTrueClass
	extends RubyObject
	implements IRubyTrueClass
{
	public interface SelectTrueClass { RubyMethod get_RubyTrueClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectTrueClass) { return ((SelectTrueClass)sel).get_RubyTrueClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectTrueClass.class); }
	}
	public RubyClass get_class() { return RubyClassTrueClass.instance; }
}
