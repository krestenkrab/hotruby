package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassStruct;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseStruct
	extends RubyObject
	implements IRubyStruct
{
	interface SelectStruct { RubyMethod get_RubyStruct(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectStruct) { return ((SelectStruct)sel).get_RubyStruct(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectStruct.class); }
	}
	public RubyClass get_class() { return RubyClassStruct.instance; }
}
