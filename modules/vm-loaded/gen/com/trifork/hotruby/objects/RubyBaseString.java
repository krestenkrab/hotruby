package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseString
	extends RubyObject
	implements IRubyString
{
	public interface SelectString { RubyMethod get_RubyString(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectString) { return ((SelectString)sel).get_RubyString(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectString.class); }
	}
	public RubyClass get_class() { return RubyClassString.instance; }
}
