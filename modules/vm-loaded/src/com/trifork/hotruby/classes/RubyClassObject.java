package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
public class RubyClassObject
	extends RubyBaseClassObject
{
	@Override
	public void init(final MetaClass meta) {
		super.init(meta);

		meta.register_instance_method("__id__", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return meta.getRuntime().get_object_id(receiver);
			}
		});
		
		meta.register_instance_method("==", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return bool(receiver==arg);
			}});
		
		meta.register_instance_method("class", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver.get_class();
			}
		});
		
		meta.register_instance_method("p", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				System.out.println(arg==null ? "NULL" : arg.inspect());
				return LoadedRubyRuntime.NIL;
				} 
			
		});
		
		meta.register_instance_method("to_s", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString("#<" + receiver.get_class().get_meta_class().getName() 
						+ ":0x" 
						+ Integer.toHexString(System.identityHashCode(receiver)) + ">");
			}
		});

		meta.register_instance_method("inspect", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString("#<" + receiver.get_class().get_meta_class().getName() 
						+ ":0x" 
						+ Integer.toHexString(System.identityHashCode(receiver)) + ">");
			}
		});
		
		meta.register_instance_method("initialize", new RubyMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				return receiver;
			}

			@Override
			public int getArity() {
				return -1;
			}
			
		});
		
		meta.register_instance_method("missing_method", new PublicMethod() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject name, RubyBlock block) {
				System.out.println(meta.getRuntime().caller(0));
				throw new RuntimeException("cannot find method "+name+" in "+receiver.get_class());
			}

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				throw new RuntimeException("cannot find method in "+receiver);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject name, IRubyObject arg2, RubyBlock block) {
				System.out.println(meta.getRuntime().caller(0));
				throw new RuntimeException("cannot find method "+name+" in "+receiver.get_class());
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				System.out.println(meta.getRuntime().caller(0));
				throw new RuntimeException("cannot find method "+(args.length>0?args[0]:"?")+" in "+receiver.get_class());
			}

			@Override
			public int getArity() {
				return -2;
			}
		});
		
}
}
