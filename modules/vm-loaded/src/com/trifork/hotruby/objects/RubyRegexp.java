package com.trifork.hotruby.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.util.regexp.RegularExpressionTranslator;

public class RubyRegexp extends RubyBaseRegexp {
	String originalExpression;
	RegularExpressionTranslator translator;
	Pattern pattern;

	@Override
	public String asSymbol() {
		return originalExpression;
	}
	
	public int flags() {
		return translator.getPattern().flags();
	}
	
	public RubyRegexp(String string, int flags) {
		originalExpression = string;
		translator = new RegularExpressionTranslator(string);
		pattern = translator.getPattern();
	}

	@Override
	public String inspect() {
		return "/" + originalExpression + "/";
	}

	public IRubyObject match(IRubyObject expr) {

		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		Matcher match = pattern.matcher(value);

		if (!match.find()) {
			return LoadedRubyRuntime.NIL;
		}
		return new RubyMatchData().initialize(match, value);
	}
	
	public IRubyObject op_eqmatch(IRubyObject expr) {
		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		Matcher match = pattern.matcher(value);

		if (!match.find()) {
			return LoadedRubyRuntime.NIL;
		}
		return new RubyFixnum(match.start());
	}
}
