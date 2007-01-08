package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassNilClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseNilClass
	extends RubyObject
	implements IRubyNilClass
{
	public interface SelectNilClass { RubyMethod get_RubyNilClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectNilClass) { return ((SelectNilClass)sel).get_RubyNilClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectNilClass.class); }
	}
	public RubyClass get_class() { return RubyClassNilClass.instance; }
}
