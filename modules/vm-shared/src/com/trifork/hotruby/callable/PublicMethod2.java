package com.trifork.hotruby.callable;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

// takes no args, and no block
public abstract class PublicMethod2 extends PublicMethod {

	public int getArity() {
		return 0;
	}
	
	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		throw wrongArgs(0);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg,
			RubyBlock block) {
		throw wrongArgs(1);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block) {
		if (args.length == getArity()) {
			return call(receiver, args[0]);
		} else {
			throw wrongArgs(args.length);
		}
	}




}
