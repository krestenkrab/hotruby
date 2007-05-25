package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassNumeric
	extends RubyClass
{
	static public RubyClassNumeric instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassNumeric)this; 
		super.init(meta);
	}
	public interface SelectNumeric { RubyMethod get_RubyClassNumeric(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectNumeric) { return ((SelectNumeric)sel).get_RubyClassNumeric(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectNumeric.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Numeric cannot be instantiated directly"); }
}
