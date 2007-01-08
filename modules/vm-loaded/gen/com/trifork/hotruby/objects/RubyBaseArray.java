package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
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
