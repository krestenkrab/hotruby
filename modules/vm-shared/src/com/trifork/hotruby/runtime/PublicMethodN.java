package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public abstract class PublicMethodN extends RubyMethod {

	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		return call(receiver, NO_ARGS, block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg,
			RubyBlock block) {
		return call(receiver, new IRubyObject[] { arg }, block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2, RubyBlock block) {
		return call(receiver, new IRubyObject[] { arg1, arg2 }, block);
	}

	@Override
	public int getArity() {
		return -1;
	}

}
