package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassRegexp
	extends RubyClass
{
	static public RubyClassRegexp instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassRegexp)this; 
		super.init(meta);
		meta.register_instance_method("match", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject expr, RubyBlock b) {
			return ((RubyRegexp)receiver).match(expr); 
		}
			}
		);
		meta.register_instance_method("=~", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject expr, RubyBlock b) {
			return ((RubyRegexp)receiver).op_eqmatch(expr); 
		}
			}
		);
	}
	public interface SelectRegexp { RubyMethod get_RubyClassRegexp(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectRegexp) { return ((SelectRegexp)sel).get_RubyClassRegexp(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectRegexp.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Regexp cannot be instantiated directly"); }
}
