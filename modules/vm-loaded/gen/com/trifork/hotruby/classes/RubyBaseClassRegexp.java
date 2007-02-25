package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.callable.PublicMethod2;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyRegexp;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public abstract class RubyBaseClassRegexp extends RubyClass {
	static public RubyClassRegexp instance;

	public void init(MetaClass meta) {
		instance = (RubyClassRegexp) this;
		super.init(meta);
		meta.register_instance_method("match", new PublicMethod1() {
			public IRubyObject call(IRubyObject receiver, IRubyObject expr,
					RubyBlock b) {
				return ((RubyRegexp) receiver).match(expr);
			}
		});
        meta.register_instance_method("==", new PublicMethod1() {
            @Override
            public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
                return ((RubyRegexp)receiver).fast_eq2(arg, null);
            }});
		meta.register_instance_method("=~", new PublicMethod1() {
			public IRubyObject call(IRubyObject receiver, IRubyObject expr,
					RubyBlock b) {
				return ((RubyRegexp) receiver).fast_eqtilde(expr, null);
			}
		});
		meta.register_instance_method("initialize", new PublicMethodN() {
            @Override
			public IRubyObject call(IRubyObject receiver, IRubyObject expr,
					RubyBlock b) {
				return ((RubyRegexp) receiver).initialize(expr);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject expr1,
					IRubyObject expr2, RubyBlock b) {
				return ((RubyRegexp) receiver).initialize(expr1, expr2);
			}

            @Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				return ((RubyRegexp) receiver).initialize(args);
			}

        	@Override
        	public int getArity() {
        		return 3;
        	}
		});
		meta.register_instance_method("options", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyRegexp) receiver).options();
			}
		});
		meta.register_instance_method("source", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyRegexp) receiver).source();
			}
		});
		meta.register_module_method("escape", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return RubyRegexp.escape(arg);
			}
		});
	}

	public interface SelectRegexp {
		RubyMethod get_RubyClassRegexp();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectRegexp) {
			return ((SelectRegexp) sel).get_RubyClassRegexp();
		}
		return LoadedRubyRuntime.resolve_method(this, sel, SelectRegexp.class);
	}

	public RubyClass get_class() {
		return RubyClassClass.instance;
	}

	public IRubyObject newInstance() {
		return new RubyRegexp();
	}
}
