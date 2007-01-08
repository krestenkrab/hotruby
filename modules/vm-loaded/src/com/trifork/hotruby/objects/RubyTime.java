package com.trifork.hotruby.objects;

import java.util.TimeZone;

public class RubyTime extends RubyBaseTime {
	long when;

	TimeZone zone;

	public RubyTime() {
		this(System.currentTimeMillis(), null);
	}

	public RubyTime(long when, TimeZone zone) {
		this.when = when;
		this.zone = zone;
	}

	TimeZone getTZ() {
		if (zone == null) {
			return TimeZone.getDefault();
		} else {
			return zone;
		}
	}

	public IRubyObject subtract(IRubyObject other) {
		if ((other instanceof RubyTime)) {

			long millis_diff = when - ((RubyTime) other).when;
			return new RubyFloat(millis_diff / 1000.0);

		} else if (other instanceof RubyNumeric) {
			
			RubyInteger me = RubyInteger.newInteger(when);
			RubyInteger newme = (RubyInteger) me.op_minus((RubyNumeric)other);
			
			return new RubyTime(newme.longValue(), zone);
		} else {
			throw getRuntime().newArgumentError("bad argument for Time subtraction");
		}
	}

}
