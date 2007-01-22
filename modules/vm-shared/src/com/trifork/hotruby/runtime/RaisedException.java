package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;


public class RaisedException extends RuntimeException {
	
	private final IRubyObject ruby_exception;

	public RaisedException(IRubyObject exception) {
		this.ruby_exception = exception;
	}

	public IRubyObject getRubyException() {
		return ruby_exception;
	}
	
	@Override
	public String getMessage() {
		RubyRuntime rr = ruby_exception.getRuntime();
		return ruby_exception.toString();
	}
}