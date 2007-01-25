package com.trifork.hotruby.classes;

import com.trifork.hotruby.ast.LocalVariable;
import com.trifork.hotruby.ast.LocalVariableAccess;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.objects.RubyModule;
import com.trifork.hotruby.runtime.EvalContext;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.ThreadState;

public class RubyClassModule extends RubyBaseClassModule {
	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		meta.register_module_method("name", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return getRuntime().newString(((RubyModule)receiver).get_meta_module().getName());
			}
		} );
		
		meta.register_instance_method("include", new PublicMethodN() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				RubyModule self = (RubyModule) receiver;
				for (int i = 0; i < args.length; i++) {
					if (args[i] instanceof IRubyModule
							&& !(args[i] instanceof IRubyClass)) {
						RubyModule mod = (RubyModule) args[i];
						self.get_meta_module().include(mod.get_meta_module());
					} else {
						throw getRuntime().newArgumentError(
								"non-module passed to include: "
										+ args[i].inspect());
					}
				}
				return self;
			}

		});

		meta.register_instance_method("const_missing", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject name,
					RubyBlock block) {
				throw getRuntime().newRuntimeError(
						"cannot find constant " + name + " in " + receiver);
			}
		});

		meta.register_instance_method("module_eval", new PublicMethod1() {
			@Override
			public IRubyObject call(final IRubyObject receiver,
					IRubyObject arg, RubyBlock block) {

				String text = ((IRubyString) arg).asSymbol();
				EvalContext ctx = new EvalContext() {

					public LocalVariableAccess access_local(String name,
							int level) {
						return null;
					}

					public RubyBlock get_block() {
						return null;
					}

					public MetaModule get_lexical_context() {
						return ((RubyModule) receiver).get_meta_module();
					}

					public LocalVariable get_local(String name, int level,
							boolean create) {
						return null;
					}

					public ExposedLocals get_locals() {
						return null;
					}

					public IRubyObject get_self() {
						return receiver;
					}
				};

				return LoadedRubyRuntime.instance.eval(text, ctx, "script", 0);
			}
		});

		meta.register_module_method("nesting", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ThreadState.get().getModuleNesting(getRuntime());
			}
		});

		meta.register_instance_method("const_get", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				IRubySymbol sym = (IRubySymbol) arg;
				return ((IRubyModule) receiver).get_meta_module()
						.getConstantAccessor(sym.asSymbol(), null).get();
			}
		});

	}
}
