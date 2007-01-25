package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

public abstract class CompiledMethod0 extends CompiledMethod {

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg,
			RubyBlock block) {
		throw wrongArgs(receiver, 1);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2, RubyBlock block) {
		throw wrongArgs(receiver, 2);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block) {
		if (args.length == 0) {
			return call(receiver, block);
		} else {
			throw wrongArgs (receiver, args.length);
		}
	}

}
