package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassHash;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseHash
	extends RubyObject
	implements IRubyHash
{
	public interface SelectHash { RubyMethod get_RubyHash(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectHash) { return ((SelectHash)sel).get_RubyHash(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectHash.class); }
	}
	public RubyClass get_class() { return RubyClassHash.instance; }
}
