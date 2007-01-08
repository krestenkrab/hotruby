package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyObject;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassObject
	extends RubyClass
{
	static public RubyClassObject instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassObject)this; 
		super.init(meta);
	}
	public interface SelectObject { RubyMethod get_RubyClassObject(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectObject) { return ((SelectObject)sel).get_RubyClassObject(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectObject.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyObject(); }
}
