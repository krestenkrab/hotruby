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
		IRubyArray result = LoadedRubyRuntime.instance.newArray();

		for (int i = 0; i < args.length; i++) {
			int val = RubyInteger.mm_induced_from(args[i]).intValue();
			result.add(get_match(val));
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
		return LoadedRubyRuntime.instance.newString(result_sequence);
	}
}
