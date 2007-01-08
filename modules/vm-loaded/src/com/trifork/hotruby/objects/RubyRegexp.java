package com.trifork.hotruby.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trifork.hotruby.classes.RubyClassRegexp;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.Selector;

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

		if (match.matches()) {
			return new RubyMatchData().initialize(match, value);
		} else {
			return LoadedRubyRuntime.NIL;
		}

	}

	
	public IRubyObject op_eqmatch(IRubyObject expr) {
		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		Matcher match = pattern.matcher(value);

		if (match.matches()) {
			return new RubyFixnum(match.start());
		} else {
			return LoadedRubyRuntime.NIL;
		}

	}

}
