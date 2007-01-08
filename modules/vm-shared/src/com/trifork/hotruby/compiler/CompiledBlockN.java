package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;

public abstract class CompiledBlockN extends CompiledBlock {

	static final IRubyObject[] NO_ARGS = new IRubyObject[0];
	
	@Override
	public IRubyObject call() throws NonLocalBreak, NonLocalNext, NonLocalRedo,
			NonLocalReturn {
		return call(NO_ARGS);
	}

	@Override
	public IRubyObject call(IRubyObject arg1) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn {
		return call(new IRubyObject[] { arg1 });
	}

	@Override
	public IRubyObject call(IRubyObject arg1, IRubyObject arg2)
			throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
		return call(new IRubyObject[] { arg1, arg2 });
	}

}
