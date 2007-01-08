package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;

public abstract class CompiledBlock2 extends CompiledBlock {

	@Override
	public IRubyObject call() throws NonLocalBreak, NonLocalNext, NonLocalRedo,
			NonLocalReturn {
		return call(nil(), nil());
	}

	@Override
	public IRubyObject call(IRubyObject arg1) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn {
		return call(arg1, nil());
	}

	@Override
	public IRubyObject call(IRubyObject[] args) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn {
		switch (args.length) {
		case 0:
			return call(nil(), nil());
		case 1:
			return call(args[0], nil());
		default:
			return call(args[0], args[1]);
		}
	}

}
