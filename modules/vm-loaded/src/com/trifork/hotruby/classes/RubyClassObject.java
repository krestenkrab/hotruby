package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyModule;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyClassObject extends RubyBaseClassObject {
	@Override
	public void init(final MetaClass meta) {
		super.init(meta);

		final Selector call_to_s = getRuntime().getSelector(meta, "to_s");
		final Selector call_eq2 = getRuntime().getSelector(meta, "==");
		
		meta.register_instance_method("freeze", new PublicMethod0() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				receiver.setFrozen(true);
				return receiver;
			}
			
		});
		
		meta.register_instance_method("__id__", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return meta.getRuntime().get_object_id(receiver);
			}
		});
		
		
		meta.register_instance_method("__send__", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				if (args.length < 1) {
					throw wrongArgs(receiver, args.length);
				}

				IRubyObject sym = args[0];
				IRubyObject[] call_args = new IRubyObject[args.length-1];
				System.arraycopy(args, 1, call_args, 0, call_args.length);
				
				com.trifork.hotruby.objects.RubyMethod m =
					(com.trifork.hotruby.objects.RubyMethod) receiver.get_meta_module().method(receiver, sym.fast_to_s(call_to_s).asSymbol());
				
				return m.call(call_args, block);
			}
			
			@Override
			public int getArity() {
				return -2;
			}
		});
		
		meta.alias_instance_method("send", "__send__");
		meta.register_instance_method("method", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				
				return receiver.get_meta_module().method(receiver, arg.fast_to_s(call_to_s).asSymbol());
			}});
		
		meta.register_instance_method("respond_to?", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				if (args.length < 1 || args.length > 2) { wrongArgs(receiver, args.length); }

				String msg = args[0].fast_to_s(call_to_s).asSymbol();
				boolean includePriv = (args.length == 2 ? args[1].isTrue() : false); 
				
				return bool(receiver.get_meta_module().respond_to_p(msg, includePriv, receiver instanceof IRubyModule));
			}
			
			@Override
			public int getArity() { return -2; }
		});

		meta.register_instance_method("instance_of?", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				if (arg instanceof RubyClass) {
					RubyClass self_type = (RubyClass) receiver.get_class();
					boolean isa = self_type.get_meta_class().is_subclass_of(
							((RubyClass) arg).get_meta_class());
					return bool(isa);
				}

				return bool(false);
			}

		});

		meta.register_instance_method("kind_of?", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				if (arg instanceof RubyModule) {
					RubyClass self_type = (RubyClass) receiver.get_class();
					boolean isa = self_type.get_meta_class().is_kind_of(
							((RubyModule) arg).get_meta_module());
					return bool(isa);
				}

				return bool(false);
			}

		});

		meta.alias_instance_method("is_a?", "kind_of?");

		meta.register_instance_method("==", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				return bool(receiver == arg);
			}
		});

		meta.register_instance_method("===", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return receiver.fast_eq2(arg, call_eq2);
			}
			
		});
		// meta.alias_instance_method("===", "==");

		meta.register_instance_method("class", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver.get_class();
			}
		});

		meta.register_instance_method("p", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				for (int i = 0; i < args.length; i++) {
					System.out.println(args[i] == null ? "NULL" : args[i].inspect());
				}
				return LoadedRubyRuntime.NIL;
			}

		});

		meta.register_instance_method("to_s", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString("#<"
						+ receiver.get_class().get_meta_class().getName()
						+ ":0x"
						+ Integer
								.toHexString(System.identityHashCode(receiver))
						+ ">");
			}
		});

		meta.register_instance_method("inspect", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString("#<"
						+ receiver.get_class().get_meta_class().getName()
						+ ":0x"
						+ Integer
								.toHexString(System.identityHashCode(receiver))
						+ ">");
			}
		});

		meta.register_instance_method("initialize", new RubyMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				return receiver;
			}

			@Override
			public int getArity() {
				return -1;
			}

		});

		meta.register_instance_method("missing_method", new PublicMethod() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject name,
					RubyBlock block) {
				throw getRuntime().newNoMethodError(
						"cannot find method " + name + " in "
								+ receiver.inspect()
								+ " (a " + receiver.get_class() + ")");
			}

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				throw getRuntime().newNoMethodError(
						"cannot find method in " + receiver.inspect()
						+ " (a " + receiver.get_class() + ")");
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject name,
					IRubyObject arg2, RubyBlock block) {
				throw getRuntime().newNoMethodError(
						"cannot find method " + name + " in "
								+ receiver.inspect() 
								+ " (a " + receiver.get_class() + ")");
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				throw getRuntime().newNoMethodError(
						"cannot find method "
								+ (args.length > 0 ? args[0] : "?") + " in "
								+ receiver.inspect()
								+ " (a " + receiver.get_class() + ")");
			}

			@Override
			public int getArity() {
				return -2;
			}
		});
		
		meta.register_instance_method("extend", new PublicMethodN() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				MetaModule mm;
				boolean receiver_is_module;
				if (receiver instanceof IRubyModule) {
					mm = ((IRubyModule)receiver).get_meta_module();
					receiver_is_module = true;
				} else {
					mm = receiver.get_singleton_meta_class();
					receiver_is_module = false;
				}
				
				for (int i = 0; i < args.length; i++) {
					if (args[i] instanceof IRubyModule) {
						
						MetaModule being_added_from = ((IRubyModule)args[i]).get_meta_module();
						
						being_added_from.copy_methods_to(mm, receiver_is_module);
						
					} else {
						throw getRuntime().newArgumentError("argument not a module");
					}
				}
				
				return receiver;
				
			}});

	}
}
