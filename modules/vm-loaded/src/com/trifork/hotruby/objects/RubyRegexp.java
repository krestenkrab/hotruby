package com.trifork.hotruby.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.util.regexp.RegularExpressionTranslator;

public class RubyRegexp extends RubyBaseRegexp {
	private String originalExpression;
	private RegularExpressionTranslator translator;
	Pattern pattern;
	private int flags = 0;

	@Override
	public String asSymbol() {
		return originalExpression;
	}

	public int flags() {
		return flags;
	}
	
	public RubyRegexp() {
		// Empty
	}

	public RubyRegexp(String string, int flags) {
		do_initialize(string, flags);
	}
	
	public IRubyObject initialize(IRubyObject arg) {
		IRubyString string = RubyString.induce_from(arg);
		String value = string.asSymbol();
		do_initialize(value, 0);
		return LoadedRubyRuntime.NIL;
	}

	public IRubyObject initialize(IRubyObject arg1, IRubyObject arg2) {
		IRubyString string = RubyString.induce_from(arg1);
		String value = string.asSymbol();
		int flags = RubyInteger.induced_from(arg2).intValue();
		do_initialize(value, flags);
		return LoadedRubyRuntime.NIL;
	}
	
	private void do_initialize(String regexp, int flags)
	{
		this.flags = flags;
		originalExpression = regexp;
		translator = new RegularExpressionTranslator(regexp, flags);
		pattern = translator.getPattern();
	}

	@Override
	public String inspect() {
		return "/" + originalExpression + "/";
	}
	
	public IRubyObject options() {
		return RubyInteger.newInteger(flags);
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
	
	public IRubyObject source() {
		return new RubyString(originalExpression);
	}

	public IRubyObject op_eqmatch(IRubyObject expr) {
		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		if (translator.isValid())
		{
			Matcher match = pattern.matcher(value);

			if (!match.find()) {
				return LoadedRubyRuntime.NIL;
			}
			return new RubyFixnum(match.start());
		}
		return LoadedRubyRuntime.NIL;
	}
    
    @Override
    public IRubyObject fast_eq2(IRubyObject arg, Selector selector) {
        return op_eq2(arg);
    }

    public IRubyObject op_eq2(IRubyObject arg) {
        return bool(arg instanceof RubyRegexp
            && ((RubyRegexp)arg).flags == flags
            && originalExpression.equals(((RubyRegexp)arg).originalExpression));
    }
}
