package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassString
	extends RubyClass
{
	static public RubyClassString instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassString)this; 
		super.init(meta);
	}
	public interface SelectString { RubyMethod get_RubyClassString(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectString) { return ((SelectString)sel).get_RubyClassString(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectString.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyString(""); }
}
