package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyTime;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassTime
	extends RubyClass
{
	static public RubyClassTime instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassTime)this; 
		super.init(meta);
	}
	public interface SelectTime { RubyMethod get_RubyClassTime(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectTime) { return ((SelectTime)sel).get_RubyClassTime(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectTime.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyTime(); }
}
