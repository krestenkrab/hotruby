package com.trifork.hotruby;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyRuntime;

public class Main {

	public static void main(String[] args) throws Exception
	{
		RubyRuntime rr = RubyRuntime.newRuntime();
		
		IRubyObject obj = rr.load("rb/supertest.rb", rr.getObject());
		
		System.out.println("=> " + obj.inspect());
	}
	
}
