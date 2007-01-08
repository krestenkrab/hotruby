package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public abstract class RubyBlock1 extends RubyBlock {

	@Override
	public IRubyObject call() throws NonLocalBreak, NonLocalNext, NonLocalRedo,
			NonLocalReturn {
		return call(LoadedRubyRuntime.NIL);
	}

	@Override
	public abstract IRubyObject call(IRubyObject arg1) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn;

	@Override
	public IRubyObject call(IRubyObject arg1, IRubyObject arg2)
			throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
		return call(arg1);
	}

	@Override
	public IRubyObject call(IRubyObject[] args) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn {
		if (args.length > 0) {
			return call(args[0]);
		} else {
			return call(LoadedRubyRuntime.NIL);
		}
	}

}
