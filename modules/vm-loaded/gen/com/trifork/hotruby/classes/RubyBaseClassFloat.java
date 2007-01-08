package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyFloat;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassFloat
	extends RubyClass
{
	static public RubyClassFloat instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassFloat)this; 
		super.init(meta);
		meta.register_instance_method("abs", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyFloat)receiver).im_abs(); 
		}
			}
		);
		meta.register_instance_method("-@", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyFloat)receiver).op_neg(); 
		}
			}
		);
		meta.register_instance_method("+", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_plus(op); 
		}
			}
		);
		meta.register_instance_method(">", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_gt(op); 
		}
			}
		);
		meta.register_instance_method("eql?", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject other, RubyBlock b) {
			return ((RubyFloat)receiver).eql_p(other); 
		}
			}
		);
		meta.register_instance_method("%", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_mod(op); 
		}
			}
		);
		meta.register_instance_method("divmod", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject numeric, RubyBlock b) {
			return ((RubyFloat)receiver).im_divmod(numeric); 
		}
			}
		);
		meta.register_instance_method("-", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_minus(op); 
		}
			}
		);
		meta.register_instance_method("<", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_lt(op); 
		}
			}
		);
		meta.register_instance_method("*", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_mult(op); 
		}
			}
		);
		meta.register_instance_method("==", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_eq(op); 
		}
			}
		);
		meta.register_instance_method("ceil", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyFloat)receiver).im_ceil(); 
		}
			}
		);
		meta.register_instance_method("=>", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_ge(op); 
		}
			}
		);
		meta.register_instance_method("<=", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject op, RubyBlock b) {
			return ((RubyFloat)receiver).op_le(op); 
		}
			}
		);
	}
	interface SelectFloat { RubyMethod get_RubyClassFloat(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFloat) { return ((SelectFloat)sel).get_RubyClassFloat(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFloat.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { throw LoadedRubyRuntime.instance.newTypeError("class Float cannot be instantiated directly"); }
}
