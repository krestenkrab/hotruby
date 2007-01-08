package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseClassRange
	extends RubyClass
{
	static public RubyClassRange instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassRange)this; 
		super.init(meta);
		meta.register_instance_method("first", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyRange)receiver).first(); 
		}
			}
		);
		meta.register_instance_method("===", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject other, RubyBlock b) {
			return ((RubyRange)receiver).includes(other); 
		}
			}
		);
		meta.register_instance_method("include?", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject other, RubyBlock b) {
			return ((RubyRange)receiver).includes(other); 
		}
			}
		);
		meta.register_instance_method("last", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyRange)receiver).last(); 
		}
			}
		);
		meta.register_instance_method("begin", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyRange)receiver).first(); 
		}
			}
		);
		meta.register_instance_method("member?", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject other, RubyBlock b) {
			return ((RubyRange)receiver).includes(other); 
		}
			}
		);
		meta.register_instance_method("==", 
			new PublicMethod1() {
		public IRubyObject call(IRubyObject receiver, IRubyObject other, RubyBlock b) {
			return ((RubyRange)receiver).eq2(other); 
		}
			}
		);
		meta.register_instance_method("end", 
			new PublicMethod0() {
		public IRubyObject call(IRubyObject receiver, RubyBlock b) {
			return ((RubyRange)receiver).last(); 
		}
			}
		);
	}
	interface SelectRange { RubyMethod get_RubyClassRange(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectRange) { return ((SelectRange)sel).get_RubyClassRange(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectRange.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyRange(); }
}
