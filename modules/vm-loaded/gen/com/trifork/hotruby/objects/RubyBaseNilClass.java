package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
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
