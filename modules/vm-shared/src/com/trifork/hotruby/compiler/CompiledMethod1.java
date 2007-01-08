package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

public abstract class CompiledMethod1 extends CompiledMethod {

	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		throw wrongArgs(0);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2, RubyBlock block) {
		throw wrongArgs(2);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block) {
		if (args.length == 1) {
			return call(receiver, args[0], block);
		}
		throw wrongArgs(0);
	}

}
