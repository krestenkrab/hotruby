package com.trifork.hotruby.callable;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

// takes no args, and no block
public abstract class PublicMethod0 extends PublicMethod {

	public int getArity() {
		return 0;
	}

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
		if (args.length != getArity()) {
			throw wrongArgs(receiver, args.length);
		}
		return call(receiver, block);
	}
}
