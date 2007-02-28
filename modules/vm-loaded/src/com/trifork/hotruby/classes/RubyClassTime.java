package com.trifork.hotruby.classes;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyBignum;
import com.trifork.hotruby.objects.RubyFixnum;
import com.trifork.hotruby.objects.RubyNumeric;
import com.trifork.hotruby.objects.RubyObject;
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
		
		meta.register_module_method("gm", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				TimeZone utcZone = TimeZone.getTimeZone("UTC");
				if (args.length == 11)
				{
					//TODO - implement
				} else if (1 <= args.length && args.length <= 7)
				{
				//year [, month, day, hour, min, sec, usec]
			    //TODO: why is the first element in args the actual args in a ruby array?
					IRubyArray rArgs = (IRubyArray)args[0];
					int numOfArgs = rArgs.int_size();
						
					int year = RubyFixnum.induced_from_allow_string(rArgs.int_at(0)).intValue();
					int month = 0;
					int day = 0;
					int hour = 0;
					int min = 0;
					int sec = 0;
					
					switch (numOfArgs) {
						case 7:
							long usec = RubyBignum.induced_from_allow_string(rArgs.int_at(6)).longValue();
							sec = (int) (usec / 1000000);
						case 6:
							sec += RubyFixnum.induced_from(rArgs.int_at(5)).intValue();
						case 5:
							min = RubyFixnum.induced_from(rArgs.int_at(4)).intValue();
						case 4:
							hour = RubyFixnum.induced_from(rArgs.int_at(3)).intValue();
						case 3:
							day = RubyTime.getDay(rArgs.int_at(2));
						case 2:
							month = RubyTime.getMonth(rArgs.int_at(1));
					}
					Calendar when = GregorianCalendar.getInstance(utcZone);
					when.set(year, month, day, hour, min, sec);
					
					return new RubyTime(when.getTimeInMillis(), utcZone);
				}
				throw LoadedRubyRuntime.instance.newArgumentError("wrong number of arguments");
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
