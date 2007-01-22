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

		// For quick'n'dirty testing, put "if (true)" here
		if (true)
		{
			IRubyObject obj = rr.load("rb/test_require.rb", rr.getObject());
			System.out.println("=> " + obj.inspect());
			return;
		}
		
		// Can be called from the command line with a file name as argument
		if (args.length == 0)
		{
			System.out.println("No file specified");
		}
		else
		{
			IRubyObject obj = rr.load(args[0], rr.getObject());
			System.out.println("=> " + obj.inspect());
		}
	}
}
