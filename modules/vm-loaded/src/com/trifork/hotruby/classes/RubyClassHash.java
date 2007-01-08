package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.callable.PublicMethod2;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyHash;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
public class RubyClassHash
	extends RubyBaseClassHash
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);
		
		meta.register_instance_method("initialize", new PublicMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				((RubyHash)receiver).init(LoadedRubyRuntime.NIL, block);
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				((RubyHash)receiver).init(arg, block);
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
				throw super.wrongArgs(2);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				switch(args.length) {
				case 0: return call(receiver, block);
				case 1: return call(receiver, args[0], block);
				default: throw super.wrongArgs(args.length);
				}
			}

			@Override
			public int getArity() {
				return -1;
			}});
		
		meta.register_instance_method("[]=", new PublicMethod2() { 
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
				RubyHash rh = (RubyHash) receiver;
				return rh.put(arg1, arg2);
			}
		});
		
		meta.register_instance_method("[]", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				RubyHash rh = (RubyHash) receiver;
				return rh.get(arg);
			}
		});
		
		meta.register_instance_method("default", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				RubyHash rh = (RubyHash) receiver;
				return rh.get_default(arg);
			}
		});
		
		meta.register_instance_method("default=", new PublicMethod1() {
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				RubyHash rh = (RubyHash) receiver;
				return rh.set_default(arg);
			}
		});
		
		meta.register_instance_method("default_proc", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyHash)receiver).default_proc();
			}
		});
		
		meta.register_instance_method("delete", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyHash)receiver).delete(arg, block);
			}
		});
		
		meta.register_instance_method("each", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyHash)receiver).each(block);
			}
		});
	}
}
