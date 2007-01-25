package com.trifork.hotruby.runtime;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;


public class RaisedException extends RuntimeException {
	
	private final IRubyObject ruby_exception;

	public RaisedException(IRubyObject exception) {
		this.ruby_exception = exception;
		exception.getRuntime().getGlobal("$!").set(exception);
		
	//	System.err.println("--DEBUG:EXCEPTION--");
	//	printStackTrace(System.err);
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

		printRubyStackTrace(ps);
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		super.printStackTrace(pw);
		printRubyStackTrace(pw);
	}


	public void printRubyStackTrace(PrintStream ps) {
		RubyRuntime runtime = ruby_exception.getRuntime();

		IRubyArray trace = runtime.getBacktrace(ruby_exception);
		
		ps.println(trace.int_at(0).asSymbol() + ": " + runtime.call_to_str(ruby_exception).asSymbol() + " (" + ruby_exception.get_class() + ")");
		
		for (int i = 1; i < trace.int_size(); i++) {
			ps.println("\tfrom "+ trace.int_at(i).asSymbol());
		}
	}
	
	public void printRubyStackTrace(PrintWriter pw) {
		RubyRuntime runtime = ruby_exception.getRuntime();

		IRubyArray trace = runtime.getBacktrace(ruby_exception);
		
		pw.println(trace.int_at(0).asSymbol() + ": " + runtime.call_to_str(ruby_exception).asSymbol() + " (" + ruby_exception.get_class() + ")");
		
		for (int i = 1; i < trace.int_size(); i++) {
			pw.println("\tfrom "+ trace.int_at(i).asSymbol());
		}
	}
}