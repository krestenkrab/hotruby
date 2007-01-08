package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;

public abstract class CompiledBlock1 extends CompiledBlock {

	@Override
	public IRubyObject call() throws NonLocalBreak, NonLocalNext, NonLocalRedo,
			NonLocalReturn {
		return call(nil());
	}

	@Override
	public IRubyObject call(IRubyObject arg1, IRubyObject arg2)
			throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
		return call(arg1);
	}

	@Override
	public IRubyObject call(IRubyObject[] args) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn {
		if (args.length==0) {
			return call(nil());
		} else {
			return call(args[0]);
		}
	}

}
