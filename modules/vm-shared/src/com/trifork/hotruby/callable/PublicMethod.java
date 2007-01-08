package com.trifork.hotruby.callable;

import com.trifork.hotruby.runtime.RubyMethod;

public abstract class PublicMethod extends RubyMethod {

	public boolean isPrivate() { return false; }

}
