package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
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
