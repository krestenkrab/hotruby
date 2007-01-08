package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.CallerControlException;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.Selector;

public final class RubyClassClass extends RubyBaseClassClass {

	static Selector initialize = null;

	static MetaClass meta_class_class = null;

	public void init(final MetaClass meta) {
		super.init(meta);

		meta.register_instance_method("name", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				RubyClass rc = (RubyClass) receiver;
				MetaClass mc = rc.get_meta_class();
				return LoadedRubyRuntime.instance.newString(mc.getName());
			}
		});

		initialize = meta.getRuntime().getSelector(meta, "initialize");
		meta_class_class = meta;

		meta.register_module_method("new", new PublicMethod1() {
			public IRubyObject call(IRubyObject receiver,
					IRubyObject superClass, RubyBlock block) {
				return ((RubyClass) receiver).newClass(superClass);
			}
		});

		meta.register_instance_method("new", new PublicMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				RubyClass self = (RubyClass) receiver;
				IRubyObject value = self.newInstance();
				value.do_select(initialize).call(value, block);
				return value;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				RubyClass self = (RubyClass) receiver;
				IRubyObject value = self.newInstance();
				value.do_select(initialize).call(value, arg, block);
				return value;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				RubyClass self = (RubyClass) receiver;
				IRubyObject value = self.newInstance();
				value.do_select(initialize).call(value, arg1, arg2, block);
				return value;
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				RubyClass self = (RubyClass) receiver;
				IRubyObject value = self.newInstance();
				value.do_select(initialize).call(value, args, block);
				return value;
			}

			@Override
			public int getArity() {
				return -1;
			}

		});

		meta.register_instance_method("to_s", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString(((RubyClass) receiver).get_meta_class()
						.getName());
			}
		});

		meta.register_instance_method("inspect", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString(((RubyClass) receiver).get_meta_class()
						.getName());
			}
		});
	}

}
