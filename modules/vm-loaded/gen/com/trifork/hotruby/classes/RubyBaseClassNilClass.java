package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyNilClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassNilClass
	extends RubyClass
{
	static public RubyClassNilClass instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassNilClass)this; 
		super.init(meta);
	}
	public interface SelectNilClass { RubyMethod get_RubyClassNilClass(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectNilClass) { return ((SelectNilClass)sel).get_RubyClassNilClass(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectNilClass.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyNilClass(); }
}
