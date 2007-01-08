package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassHash
	extends RubyClass
{
	static public RubyClassHash instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassHash)this; 
		super.init(meta);
	}
	interface SelectHash { RubyMethod get_RubyClassHash(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectHash) { return ((SelectHash)sel).get_RubyClassHash(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectHash.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyHash(); }
}
