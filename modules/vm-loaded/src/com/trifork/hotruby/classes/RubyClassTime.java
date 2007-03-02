package com.trifork.hotruby.classes;

import java.util.TimeZone;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.callable.PublicMethod2;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyFixnum;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.objects.RubyTime;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;

public class RubyClassTime
	extends RubyBaseClassTime
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		meta.register_instance_method("new", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyTime();
			}			
		});
		
		meta.register_instance_method("inspect", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString(((RubyTime) receiver).inspect());
			}			
		});
		
		meta.register_module_method("local", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				TimeZone zone = TimeZone.getDefault();
				if (args.length == 10)
				{
					//TODO - implement
				} else if (1 <= args.length && args.length <= 7)
				{
					return RubyTime.instance(args, zone);
				}
				throw LoadedRubyRuntime.instance.newArgumentError("wrong number of arguments");
			}
		});
		
		meta.register_module_method("gm", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				TimeZone utcZone = TimeZone.getTimeZone("UTC");
				if (args.length == 10)
				{
					//TODO - implement
				} else if (1 <= args.length && args.length <= 7)
				{
					return RubyTime.instance(args, utcZone);
				}
				throw LoadedRubyRuntime.instance.newArgumentError("wrong number of arguments");
			}
		});
	
		meta.register_module_method("at", new PublicMethod2() {
            @Override
			public IRubyObject call(IRubyObject receiver, IRubyObject expr,
					RubyBlock b) {
            	if (expr instanceof RubyTime) {
					return new RubyTime((RubyTime) expr);
            	}
				return new RubyTime(RubyFixnum.induced_from(expr).longValue(), null);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject expr1,
					IRubyObject expr2, RubyBlock b) {
				long secs = RubyFixnum.induced_from(expr1).longValue();
				long usecs = RubyFixnum.induced_from(expr2).longValue();
				long msecs = (secs * 1000) + (usecs == 0 ? 0 : usecs / 1000);   
				return new RubyTime(msecs, null);
			}

        	@Override
        	public int getArity() {
        		return 2;
        	}
		});
		
		meta.register_instance_method("-", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyTime)receiver).subtract(arg);
			}
		});
	}
	
}
