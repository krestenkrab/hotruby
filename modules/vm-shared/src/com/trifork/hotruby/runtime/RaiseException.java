package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public class RaiseException extends RuntimeException {
	private final IRubyObject rubyException;

	public RaiseException(IRubyObject rubyException)
	{
		this.rubyException = rubyException;  
	}

	public IRubyObject getRubyException()
	{
		return rubyException;
	}
}
