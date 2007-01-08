package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassFalseClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseFalseClass
	extends RubyObject
	implements IRubyFalseClass
{
	public interface SelectFalseClass { RubyMethod get_RubyFalseClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFalseClass) { return ((SelectFalseClass)sel).get_RubyFalseClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectFalseClass.class); }
	}
	public RubyClass get_class() { return RubyClassFalseClass.instance; }
}
