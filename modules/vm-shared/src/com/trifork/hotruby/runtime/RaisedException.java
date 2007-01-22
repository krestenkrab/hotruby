package com.trifork.hotruby.runtime;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.trifork.hotruby.objects.IRubyArray;
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
		return rr.call_to_str(ruby_exception).asSymbol();
	}
	
	@Override
	public void printStackTrace(PrintStream ps) {
		// TODO Auto-generated method stub
		super.printStackTrace(ps);

		RubyRuntime runtime = ruby_exception.getRuntime();

		ps.println("Ruby-level exception: "+ruby_exception.get_class()+": "+runtime.call_to_str(ruby_exception).asSymbol());
		
		IRubyArray trace = runtime.getBacktrace(ruby_exception);
		for (int i = 0; i < trace.int_size(); i++) {
			ps.println("\tat "+ trace.int_at(i).asSymbol());
		}
	}
	
	@Override
	public void printStackTrace(PrintWriter pw) {
		super.printStackTrace(pw);

		RubyRuntime runtime = ruby_exception.getRuntime();

		pw.println("Ruby-level exception: "+ruby_exception.get_class()+": "+runtime.call_to_str(ruby_exception).asSymbol());
		
		IRubyArray trace = runtime.getBacktrace(ruby_exception);
		for (int i = 0; i < trace.int_size(); i++) {
			pw.println("\tat "+ trace.int_at(i).asSymbol());
		}
	}
}