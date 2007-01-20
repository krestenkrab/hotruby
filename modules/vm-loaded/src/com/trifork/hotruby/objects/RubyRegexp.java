package com.trifork.hotruby.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;

public class RubyRegexp extends RubyBaseRegexp {
	Pattern pattern;

	@Override
	public String asSymbol() {
		return pattern.toString();
	}
	
	public int flags() {
		return pattern.flags();
	}
	
	public RubyRegexp(String string, int flags) {
		pattern = Pattern.compile(string, flags);
	}

	@Override
	public String inspect() {
		return "/" + pattern.toString() + "/";
	}

	public IRubyObject match(IRubyObject expr) {

		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		Matcher match = pattern.matcher(value);

		if (!match.matches()) {
			return LoadedRubyRuntime.NIL;
		}
		return new RubyMatchData().initialize(match, value);
	}
	
	public IRubyObject op_eqmatch(IRubyObject expr) {
		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		Matcher match = pattern.matcher(value);

		if (!match.matches()) {
			return LoadedRubyRuntime.NIL;
		}
		return new RubyFixnum(match.start());
	}
}
