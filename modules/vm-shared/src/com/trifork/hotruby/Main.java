package com.trifork.hotruby;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyConfig;
import com.trifork.hotruby.runtime.RubyRuntime;

public class Main {

	public static void main(String[] args) throws Exception
	{
		RubyConfig cfg = new RubyConfig();
		
		cfg.setInputStream(System.in);
		cfg.setOutputStream(System.out);
		cfg.setErrorStream(System.err);
		cfg.setArguments(args);
		
		RubyRuntime rr = RubyRuntime.newRuntime(cfg);
		
		IRubyObject obj = rr.load("rb/richards.rb", rr.getObject());
		
		System.out.println("=> " + obj.inspect());
	}
	
}
