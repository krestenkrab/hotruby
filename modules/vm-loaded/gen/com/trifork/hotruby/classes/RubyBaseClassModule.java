package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassModule
	extends RubyClass
{
	static public RubyClassModule instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassModule)this; 
		super.init(meta);
	}
	public interface SelectModule { RubyMethod get_RubyClassModule(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectModule) { return ((SelectModule)sel).get_RubyClassModule(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectModule.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Module cannot be instantiated directly"); }
}
