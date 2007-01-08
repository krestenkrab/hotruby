package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassInteger
	extends RubyClass
{
	static public RubyClassInteger instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassInteger)this; 
		super.init(meta);
		meta.register_module_method("induced_from", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject obj, RubyBlock b) {
			return ((RubyInteger)receiver).mm_induced_from(obj); 
		}
			}
		);
	}
	interface SelectInteger { RubyMethod get_RubyClassInteger(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectInteger) { return ((SelectInteger)sel).get_RubyClassInteger(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectInteger.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Integer cannot be instantiated directly"); }
}
