package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod2;
import com.trifork.hotruby.modules.RubyModuleEnumerable;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyIO;
import com.trifork.hotruby.objects.RubyObject;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyClassIO extends RubyClass {

	public static RubyClass instance;

	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		instance = new RubyClassIO();

		meta.include(RubyModuleEnumerable.instance.get_meta_module());

		meta.register_module_method("new", new PublicMethod2() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				return new RubyIO().initialize(arg1, arg2);
			}
		});

	}

	public interface SelectClassIO {
		RubyMethod get_RubyClassIO();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectClassIO) {
			return ((SelectClassIO) sel).get_RubyClassIO();
		} else {
			return LoadedRubyRuntime.resolve_method((RubyObject) this, sel,
					SelectClassIO.class);
		}
	}

	public RubyClass get_class() {
		return RubyClassIO.instance;
	}

	@Override
	public IRubyObject newInstance() {
		return new RubyIO();
	}

}
