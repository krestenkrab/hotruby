package com.trifork.hotruby.objects;

import java.util.regex.Matcher;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;

public class RubyMatchData extends RubyBaseMatchData {
	private Matcher match;
	private CharSequence source;
	
	public RubyMatchData initialize(Matcher match, CharSequence source) {
		this.match = match;
		this.source = source;
		return this;
	}

	public IRubyObject array_access(IRubyObject[] args) {
		if (args.length == 1 && args[0] instanceof IRubyRange) {
			IRubyRange range = (IRubyRange)args[0];
			int start = RubyInteger.induced_from(range.first()).intValue();
			int end = RubyInteger.induced_from(range.last()).intValue();
			return arrayFromTo(start, end - start + 1);
		}
		if (args.length == 1) {
			int val = RubyInteger.induced_from(args[0]).intValue();
			if (val < 0) {
				val += match.groupCount() + 1;
			}
			return get_match(val);
		}
		
		if (args.length == 2) {
			int start = RubyInteger.induced_from(args[0]).intValue();
			int length = RubyInteger.induced_from(args[1]).intValue();
			if (start < 0) {
				start += match.groupCount() + 1;
			}
			return arrayFromTo(start, length);
		}
		return LoadedRubyRuntime.NIL;
	}
	
	private IRubyObject arrayFromTo(int start, int length) {
		IRubyArray result = LoadedRubyRuntime.instance.newArray();
		for (int i = 0; i < length; i++) {
			result.add(get_match(start + i));
		}
		return result;
	}
	
	public IRubyObject to_a() {
		IRubyArray result = LoadedRubyRuntime.instance.newArray();

		for (int i = 0; i <= match.groupCount(); i++) {
			result.add(get_match(i));
		}
		return result;
	}
	
	private IRubyObject get_match(int val) {
		if (val < 0 || val > match.groupCount())
		{
			return LoadedRubyRuntime.NIL;
		}
		CharSequence result_sequence = match.group(val);
        if (result_sequence == null)
        {
          return LoadedRubyRuntime.NIL;
        }
		return LoadedRubyRuntime.instance.newString(result_sequence);
	}

	public IRubyObject begin(IRubyObject arg) {
		IRubyInteger integer = RubyInteger.induced_from(arg);
		return RubyInteger.newInteger(match.start(integer.intValue()));
	}
	public IRubyObject end(IRubyObject arg) {
		IRubyInteger integer = RubyInteger.induced_from(arg);
		return RubyInteger.newInteger(match.end(integer.intValue()));
	}

	public IRubyObject string() {
		RubyString s = new RubyString(source.toString());
		s.setFrozen(true);
		return s;
	}
}
