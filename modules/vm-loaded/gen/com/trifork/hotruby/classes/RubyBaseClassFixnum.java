package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassFixnum
	extends RubyClass
{
	static public RubyClassFixnum instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassFixnum)this; 
		super.init(meta);
		meta.register_instance_method("+", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFixnum)receiver).op_plus(op); 
		}
			}
		);
		meta.register_instance_method("to_s", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyFixnum)receiver).to_s(); 
		}
			}
		);
		meta.register_instance_method("-", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFixnum)receiver).op_minus(op); 
		}
			}
		);
		meta.register_instance_method("<", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFixnum)receiver).op_lt(op); 
		}
			}
		);
	}
	public interface SelectFixnum { RubyMethod get_RubyClassFixnum(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFixnum) { return ((SelectFixnum)sel).get_RubyClassFixnum(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFixnum.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Fixnum cannot be instantiated directly"); }
}
