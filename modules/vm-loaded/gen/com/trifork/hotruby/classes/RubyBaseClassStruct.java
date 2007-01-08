package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassStruct
	extends RubyClass
{
	static public RubyClassStruct instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassStruct)this; 
		super.init(meta);
	}
	interface SelectStruct { RubyMethod get_RubyClassStruct(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectStruct) { return ((SelectStruct)sel).get_RubyClassStruct(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectStruct.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyStruct(); }
}
