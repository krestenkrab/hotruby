package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassBignum
	extends RubyClass
{
	static public RubyClassBignum instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassBignum)this; 
		super.init(meta);
		meta.register_instance_method("+", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyBignum)receiver).op_plus(op); 
		}
			}
		);
		meta.register_instance_method("-", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyBignum)receiver).op_minus(op); 
		}
			}
		);
	}
	interface SelectBignum { RubyMethod get_RubyClassBignum(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectBignum) { return ((SelectBignum)sel).get_RubyClassBignum(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectBignum.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Bignum cannot be instantiated directly"); }
}
