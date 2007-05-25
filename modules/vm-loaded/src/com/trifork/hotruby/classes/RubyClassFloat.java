package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyFloat;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
public class RubyClassFloat
	extends RubyBaseClassFloat
{
	static public RubyClassFloat instance;

	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		instance = (RubyClassFloat)this; 

		meta.register_instance_method("to_s", 
			new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyFloat) receiver).to_s();
			}
		});

		meta.register_instance_method("to_f", 
				new PublicMethod0() {
				@Override
				public IRubyObject call(IRubyObject receiver, RubyBlock block) {
					return ((RubyFloat) receiver).to_f();
				}
			});

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

}
