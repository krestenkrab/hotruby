package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

/**
 * Tjah, vi gætter vist bare på hvad Kresten vil med denne :-)
 * @author ofo
 */
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
