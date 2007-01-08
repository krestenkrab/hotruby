package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseHash
	extends RubyObject
	implements IRubyHash
{
	interface SelectHash { RubyMethod get_RubyHash(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectHash) { return ((SelectHash)sel).get_RubyHash(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectHash.class); }
	}
	public RubyClass get_class() { return RubyClassHash.instance; }
}
