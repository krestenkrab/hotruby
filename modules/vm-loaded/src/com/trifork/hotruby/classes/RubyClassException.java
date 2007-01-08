package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyException;
import com.trifork.hotruby.objects.RubyObject;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyClassException extends RubyClass {

	static Selector exc_new;

	static Selector exc_to_s;

	static Selector exc_to_str;

	static public RubyClassException instance;

	public void init(MetaClass meta) {
		instance = (RubyClassException) this;
		super.init(meta);

		exc_new = LoadedRubyRuntime.instance.getSelector(meta, "to_str");

		meta.register_module_method("new", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				RubyException result = new RubyException();

				MetaClass mc = result.get_meta_class();
				mc.getInstanceIVarAccessor("backtrace", false).set(result,
						getRuntime().caller(1));
				if (args.length > 0) {
					mc.getInstanceIVarAccessor("message", false).set(result,
							args[0].fast_to_str(exc_to_str));
				}

				return result;
			}
		});

		exc_new = LoadedRubyRuntime.instance.getSelector(meta, "new");
		meta.register_instance_method("exception", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				if (arg == receiver) {
					return receiver;
				}
				return receiver.get_class().do_select(exc_new).call(arg);
			}
		});

		exc_to_s = LoadedRubyRuntime.instance.getSelector(meta, "to_s");
		meta.register_instance_method("message", new PublicMethod0() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver.fast_to_s(exc_to_s);
			}
		});

		meta.alias_instance_method("to_str", "message");

		meta.register_instance_method("set_backtrace", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				RubyObject rcv = (RubyObject) receiver;
				rcv.get_meta_class()
						.getInstanceIVarAccessor("backtrace", false).set(
								receiver, arg);
				return arg;
			}
		});

		meta.register_instance_method("backtrace", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				RubyObject rcv = (RubyObject) receiver;
				return rcv.get_meta_class().getInstanceIVarAccessor(
						"backtrace", false).get(receiver);
			}
		});

		meta.register_instance_method("to_s", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				IRubyObject message = receiver.get_ivar("message");
				if (message == LoadedRubyRuntime.NIL) {
					return receiver.get_class().get_name();
				} else {
					return message;
				}
			}
		});

		meta.register_instance_method("inspect", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				IRubyString self_class_name = receiver.get_class().get_name();
					return getRuntime().newString(
							    "#<"
							  + self_class_name.asSymbol() 
							  + ": "
							  + receiver.fast_to_s(exc_to_s).asSymbol()
							  + ">"
					);
			}
		});
	}

	interface SelectException {
		RubyMethod get_RubyClassException();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectException) {
			return ((SelectException) sel).get_RubyClassException();
		} else {
			return LoadedRubyRuntime.resolve_method((RubyClass) this, sel,
					SelectException.class);
		}
	}

	public RubyClass get_class() {
		return RubyClassClass.instance;
	}

	public IRubyObject newInstance() {
		return new RubyException();
	}

}
