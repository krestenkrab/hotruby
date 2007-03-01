package com.trifork.hotruby.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RaisedException;

public class RubyTime extends RubyBaseTime {
	private static final String[] MONTH_VALUES = 
		new String[]
		{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	
	long when;

	TimeZone zone;

	public RubyTime(RubyTime other)
	{
		this(other.when, other.zone);
	}
	
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
	
	//year [, month, day, hour, min, sec, usec]
	public static RubyTime instanceFromRubyArgs(IRubyObject[] rArgs, TimeZone zone)
	{
		return new RubyTime(verifyAndExtractTimeFromArgs(zone, rArgs), zone);
	}
	
	/**
	 * Extracts and verifies the parameters.
	 * 
	 * @param zone the timezone
	 * @param rArgs args on the form: year [, month, day, hour, min, sec, usec]
	 * @return the time in millis from the epoch if the parameters are valid
	 * @throws RaisedException if any of the parameters fall outside their valid range.
	 */
	private static long verifyAndExtractTimeFromArgs(TimeZone zone, IRubyObject[] rArgs) {
		int numOfArgs = rArgs.length;
			
		int year = RubyFixnum.induced_from_allow_string(rArgs[0]).intValue();
		int month = 1;
		int day = 1;
		int hour = 0;
		int min = 0;
		int sec = 0;
		int secToAdd = 0;
		
		switch (numOfArgs) {
			case 7:
				long usec = RubyBignum.induced_from_allow_string(rArgs[6]).longValue();
				secToAdd = (int) (usec / 1000000);
			case 6:
				sec = RubyFixnum.induced_from_allow_string(rArgs[5]).intValue();
			case 5:
				min = RubyFixnum.induced_from_allow_string(rArgs[4]).intValue();
			case 4:
				hour = RubyFixnum.induced_from_allow_string(rArgs[3]).intValue();
			case 3:
				day = RubyFixnum.induced_from_allow_string(rArgs[2]).intValue();
			case 2:
				month = RubyTime.getMonth(rArgs[1]); //specialcase to handle month.. can be e.g. "FEB"
		}
		
		if (isOutsideRange(month, 1, 12) ||
			isOutsideRange(day, 1, 31) ||
			isOutsideRange(hour, 0, 23) ||
			isOutsideRange(min, 0, 59) ||
			isOutsideRange(sec, 0, 60))
		{
			throw LoadedRubyRuntime.instance.newArgumentError("argument out of range");
		}
		
		Calendar when = GregorianCalendar.getInstance(zone);
		when.set(year, (month-1), day, hour, min, (sec + secToAdd));
		return when.getTimeInMillis();
	}
	
	private static boolean isOutsideRange(int val, int from, int to)
	{
		return (val < from && to < val); 
	}
	
	/**
	 * Get the int-month-value based on monthArg.
	 * 
	 * @param monthArg the RubyInteger or RubyString integer-value for a month (1-12) or a three letter month-code (e.g. "MAR")
	 * @return the int-value for the given ruby argument.
	 * @throws RaisedException if monthArg the given argument mappes to an integer outside the range, 1-12.
	 * @throws NumberFormatException if monthArg is a RubyString that contains non-digit chars or is not one of the following string:
	 * "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
	 */
	private static int getMonth(IRubyObject monthArg)
	{
		if (monthArg instanceof RubyString) {
			String monthStr = ((RubyString) monthArg).value;
			for (int i = 0; i < MONTH_VALUES.length; i++) {
				if (MONTH_VALUES[i].equalsIgnoreCase(monthStr))
				{
					return (i+1); //add one (valid "ruby-month" values are 1 to 12)
				}
			}
		}
		return RubyFixnum.induced_from_allow_string(monthArg).intValue();
	}
	
}
