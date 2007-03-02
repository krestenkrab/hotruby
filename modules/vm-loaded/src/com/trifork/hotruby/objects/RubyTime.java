package com.trifork.hotruby.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.plaf.ListUI;

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

		//if the time zone is not UTC then print the offset (e.g. +0100)
		String formatString = "EEE MMM dd HH:mm:ss " + (isUtcZone ?  "zzz" : "ZZZ") + " yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.US);
		sdf.setTimeZone(tz);
		
		//TODO .. the printed string is platform depended. 
		/*
		 * Time.mktime 2005, 1, 1:
		 *   Mac, Sat Jan 01 00:00:00 +0100 2005
		 *   Windows, Sat Jan 01 00:00:00 Rom, normaltid 2005
		 */
		return sdf.format(new Date(when));
	}

	/**
	 * 
	 * @param rArgs arguments on the form: year [, month, day, hour, min, sec, usec]
	 * @param zone the time zone to use
	 * @returna RubyTime with the specifed time set.
 	 * @throws RaisedException if any of the parameters fall outside their valid range.
	 */
	public static RubyTime instance(IRubyObject[] rArgs, TimeZone zone)
	{
		int numOfArgs = rArgs.length;
		
		int year = RubyFixnum.induced_from_allow_string(rArgs[0]).intValue();
		int month = 1;
		int day = 1;
		int hour = 0;
		int min = 0;
		int sec = 0;
		int secsToAdd = 0;
		
		switch (numOfArgs) {
			case 7:
				long usec = RubyBignum.induced_from_allow_string(rArgs[6]).longValue();
				secsToAdd = (int) (usec / 1000000);
			case 6:
				sec = RubyFixnum.induced_from_allow_string(rArgs[5]).intValue();
			case 5:
				min = RubyFixnum.induced_from_allow_string(rArgs[4]).intValue();
			case 4:
				hour = RubyFixnum.induced_from_allow_string(rArgs[3]).intValue();
			case 3:
				day = RubyFixnum.induced_from_allow_string(rArgs[2]).intValue();
			case 2:
				//month is special case; besides a number it can also be a three letter string like "MAY"
				month = getMonth(rArgs[1]); 
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
		when.set(year, (month-1), day, hour, min, (sec + secsToAdd));
		return new RubyTime(when.getTimeInMillis(), zone);
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
