package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

public abstract class CompiledMethod2 extends CompiledMethod {

	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		throw wrongArgs(receiver, 0);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg,
			RubyBlock block) {
		throw wrongArgs(receiver, 1);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block) {
		if (args.length == 2) {
			return call(receiver, args[0], args[1], block);
		}
		throw wrongArgs(receiver, args.length);
	}


}
