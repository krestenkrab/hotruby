package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public abstract class CallerControlException extends Exception {
	public abstract IRubyObject perform(ExposedLocals locals, RubyBlock block) throws CallerControlException;
}
