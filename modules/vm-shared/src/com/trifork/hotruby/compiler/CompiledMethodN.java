package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

public abstract class CompiledMethodN extends CompiledMethod {

	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		return call(receiver, NO_ARGS, block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
		return call(receiver, new IRubyObject[] { arg }, block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
		return call(receiver, new IRubyObject[] { arg1, arg2 }, block);
	}
}
