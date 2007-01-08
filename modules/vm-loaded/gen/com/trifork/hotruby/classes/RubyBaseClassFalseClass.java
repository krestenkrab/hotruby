package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyFalseClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassFalseClass
	extends RubyClass
{
	static public RubyClassFalseClass instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassFalseClass)this; 
		super.init(meta);
	}
	interface SelectFalseClass { RubyMethod get_RubyClassFalseClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFalseClass) { return ((SelectFalseClass)sel).get_RubyClassFalseClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFalseClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyFalseClass(); }
}
