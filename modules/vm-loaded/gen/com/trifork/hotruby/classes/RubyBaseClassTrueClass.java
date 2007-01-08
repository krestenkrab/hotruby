package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyTrueClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassTrueClass
	extends RubyClass
{
	static public RubyClassTrueClass instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassTrueClass)this; 
		super.init(meta);
	}
	public interface SelectTrueClass { RubyMethod get_RubyClassTrueClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectTrueClass) { return ((SelectTrueClass)sel).get_RubyClassTrueClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectTrueClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyTrueClass(); }
}
