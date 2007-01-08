package com.trifork.hotruby.callable;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;

public abstract class PublicMethod extends RubyMethod {

	public boolean isPrivate() { return false; }

}
