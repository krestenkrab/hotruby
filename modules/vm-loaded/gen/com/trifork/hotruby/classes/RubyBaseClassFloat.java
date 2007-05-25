package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassFloat
	extends RubyClass
{
	public interface SelectFloat { RubyMethod get_RubyClassFloat(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFloat) { return ((SelectFloat)sel).get_RubyClassFloat(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFloat.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Float cannot be instantiated directly"); }
}
