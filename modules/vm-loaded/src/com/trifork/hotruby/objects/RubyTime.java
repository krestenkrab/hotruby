package com.trifork.hotruby.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RaisedException;

public class RubyTime extends RubyBaseTime {
	private static final String[] MONTH_VALUES = 
		new String[]
		{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	
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
	
	@Override
	public String inspect()
	{
		TimeZone tz = getTZ();
		boolean isUtcZone = tz.getID().equalsIgnoreCase("UTC");
		//if the tim zone is not UTC then print the offset (e.g. +0100) as "time zone"
		String formatString = "EEE MMM dd HH:mm:ss " + (isUtcZone ?  "zzz" : "ZZZ") + " yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		sdf.setTimeZone(tz);
		return sdf.format(new Date(when));
	}
	
	/**
	 * Get the int-month-value based on monthArg.
	 * 
	 * @param monthArg the RubyInteger or RubyString integer-value for a month (1-12) or a three letter month-code (e.g. "MAR")
	 * @return the java-integer value for the given month (0-11)
	 * @throws RaisedException if monthArg the given argument mappes to an integer outside the range, 1-12.
	 * @throws NumberFormatException if monthArg is a RubyString that contains non-digit chars or is not one of the following string:
	 * "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
	 */
	public static int getMonth(IRubyObject monthArg)
	{
		if (monthArg instanceof RubyString) {
			String monthStr = ((RubyString) monthArg).value;
			for (int i = 0; i < MONTH_VALUES.length; i++) {
				if (MONTH_VALUES[i].equalsIgnoreCase(monthStr))
				{
					return i;
				}
			}
		}
		return getIntValueInRange(monthArg, 1, 12);
	}
	
	/**
	 * @param dayArg the RubyInteger or RubyString integer-value for a day
	 * @return the int value for the day
	 * @throws RaisedException if the day is not between 1 and 31
	 * @throws NumberFormatException if dayArg is a RubyString that contains non-digit chars.
	 */
	public static int getDay(IRubyObject dayArg)
	{
		return getIntValueInRange(dayArg, 1, 31);
	}
	
	/**
	 * Return the integer-value represented by arg if it is included in the given range (both included).
	 * @param arg
	 * @param from
	 * @param to
	 * @return
	 */
	private static int getIntValueInRange(IRubyObject arg, int from, int to)
	{
		int result = RubyFixnum.induced_from_allow_string(arg).intValue();
		if (from <= result && result <= to) 
		{
			return result;
		}
		throw LoadedRubyRuntime.instance.newArgumentError("argument out of range");
	}
}
