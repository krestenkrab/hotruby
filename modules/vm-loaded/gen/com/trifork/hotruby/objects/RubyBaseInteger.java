package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseInteger
	extends RubyNumeric
	implements IRubyInteger
{
	public interface SelectInteger { RubyMethod get_RubyInteger(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectInteger) { return ((SelectInteger)sel).get_RubyInteger(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectInteger.class); }
	}
	public RubyClass get_class() { return RubyClassInteger.instance; }
}
