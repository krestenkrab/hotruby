package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyProc;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.NewProcException;
import com.trifork.hotruby.runtime.NonLocalJump;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.ThreadState;

public class RubyClassProc extends RubyBaseClassProc {
	@Override
	public IRubyObject newInstance() {
		throw new InternalError("should not happen");
	}

	@Override
	public void init(final MetaClass meta) {
		super.init(meta);

		meta.register_module_method("new", new PublicMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				if (block == null) {
					block = ThreadState.get().getCallersBlock();

					if (block == null) {
						// throw this special exception, which means that the
						// caller should
						// allocate a proc in his context
						throw new NewProcException();
					}
				}

				return new RubyProc(block);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				throw meta.getRuntime().newArgumentError(
						"wrong numer of arguments (1 for 0)");
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				throw meta.getRuntime().newArgumentError(
						"wrong numer of arguments (2 for 0)");
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				throw meta.getRuntime().newArgumentError(
						"wrong numer of arguments (" + args.length + " for 0)");
			}

			@Override
			public int getArity() {
				return 0;
			}

		});

		meta.register_instance_method("call", new PublicMethod() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				try {
					return ((RubyProc) receiver).get_block().call();
				} catch (NonLocalJump e) {
					throw meta.getRuntime().newLocalJumpError("invoking proc",
							e);
				}
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg,
					RubyBlock block) {
				try {
					return ((RubyProc) receiver).get_block().call(arg);
				} catch (NonLocalJump e) {
					throw meta.getRuntime().newLocalJumpError("invoking proc",
							e);
				}
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
					IRubyObject arg2, RubyBlock block) {
				try {
					return ((RubyProc) receiver).get_block().call(arg1, arg2);
				} catch (NonLocalJump e) {
					throw meta.getRuntime().newLocalJumpError("invoking proc",
							e);
				}
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
					RubyBlock block) {
				try {
					return ((RubyProc) receiver).get_block().call(args);
				} catch (NonLocalJump e) {
					throw meta.getRuntime().newLocalJumpError("invoking proc",
							e);
				}
			}

			@Override
			public int getArity() {
				return -1;
			}

		});
		
		meta.alias_instance_method("[]", "call");
	}
}
