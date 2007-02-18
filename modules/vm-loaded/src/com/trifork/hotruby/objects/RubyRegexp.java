package com.trifork.hotruby.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trifork.hotruby.runtime.Global;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.util.regexp.RegularExpressionTranslator;

public class RubyRegexp extends RubyBaseRegexp {
	private static Global GLOBAL_TILDE;          // $~
	private static Global GLOBAL_MATCHED;        // $&
	private static Global GLOBAL_BEFORE_MATCHED; // $`
	private static Global GLOBAL_AFTER_MATCHED;  // $'
	private static List<Global> GLOBAL_GROUPS;
	private static ThreadLocal<Integer> LAST_NUMBER_OF_GROUPS;
	private static RubyRuntime RUBY_RUNTIME;

	private String originalExpression;
	private RegularExpressionTranslator translator;
	Pattern pattern;
	private int flags = 0;

	public static void static_init(RubyRuntime runtime) {
		RUBY_RUNTIME = runtime;
		GLOBAL_TILDE = runtime.getGlobal("$~");
		GLOBAL_MATCHED = runtime.getGlobal("$&");
		GLOBAL_BEFORE_MATCHED = runtime.getGlobal("$`");
		GLOBAL_AFTER_MATCHED = runtime.getGlobal("$'");
		GLOBAL_GROUPS = new ArrayList<Global>();
		LAST_NUMBER_OF_GROUPS = new ThreadLocal<Integer>() {
			@Override
			protected Integer initialValue() {
				return 0;
			}
		};
		
		GLOBAL_TILDE.becomeThreadLocal();
		GLOBAL_MATCHED.becomeThreadLocal();
		GLOBAL_BEFORE_MATCHED.becomeThreadLocal();
		GLOBAL_AFTER_MATCHED.becomeThreadLocal();
	}

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
		doInitialize(string, flags);
	}
	
	public IRubyObject initialize(IRubyObject arg) {
		IRubyString string = RubyString.induce_from(arg);
		String value = string.asSymbol();
		doInitialize(value, 0);
		return LoadedRubyRuntime.NIL;
	}

	public IRubyObject initialize(IRubyObject arg1, IRubyObject arg2) {
		IRubyString string = RubyString.induce_from(arg1);
		String value = string.asSymbol();
		int flags = RubyInteger.induced_from(arg2).intValue();
		doInitialize(value, flags);
		return LoadedRubyRuntime.NIL;
	}
	
	public IRubyObject initialize(IRubyObject[] args) {
		// Ignore the 3rd argument (language)
		return initialize(args[0], args[1]);
	}

	private void doInitialize(String regexp, int flags) {
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
		performMatch(expr);
		return GLOBAL_TILDE.get();
	}

	@Override
	public IRubyObject fast_eqtilde(IRubyObject arg, Selector selector) {
		int startMatch = performMatch(arg);
		if (startMatch >= 0) {
			return new RubyFixnum(startMatch);
		}
		return LoadedRubyRuntime.NIL;
	}
	
	public IRubyObject source() {
		return new RubyString(originalExpression);
	}

	@Override
	public IRubyObject fast_eq3(IRubyObject arg, Selector selector) {
		return fast_eqtilde(arg, selector);
	}
	
    @Override
    public IRubyObject fast_eq2(IRubyObject arg, Selector selector) {
        return bool(arg instanceof RubyRegexp
                && ((RubyRegexp)arg).flags == flags
                && originalExpression.equals(((RubyRegexp)arg).originalExpression));
    }
	
	private int performMatch(IRubyObject expr) {
		IRubyString string = RubyString.induce_from(expr);
		String value = string.asSymbol();

		synchronized (GLOBAL_GROUPS) {
			if (translator.isValid()) {
				Matcher match = pattern.matcher(value);
				if (match.find()) {
					GLOBAL_TILDE.set(new RubyMatchData().initialize(match,
							value));
					GLOBAL_MATCHED.set(new RubyString(match.group()));
					GLOBAL_AFTER_MATCHED.set(new RubyString(value
							.substring(match.end())));
					GLOBAL_BEFORE_MATCHED.set(new RubyString(value.substring(0,
							match.start())));
					setGroups(match);
					return match.start();
				}
			}
			GLOBAL_TILDE.set(LoadedRubyRuntime.NIL);
			GLOBAL_MATCHED.set(LoadedRubyRuntime.NIL);
			GLOBAL_AFTER_MATCHED.set(LoadedRubyRuntime.NIL);
			GLOBAL_BEFORE_MATCHED.set(LoadedRubyRuntime.NIL);
			setGroups(null);
			return -1;
		}
	}
	
	/**
	 * Sets $1, $2, etc., and resets $12, $13, or whatever excess globals have
	 * been set previously.
	 * 
	 * Unfortunately, has to synchronize on GLOBAL_GROUPS.
	 */
	private void setGroups(Matcher match) {
		synchronized (GLOBAL_GROUPS) {
			int i = 0;
			int numberOfGroupsThisTime = 0;

			if (match != null) {
				// Set $1, $2, ...
				numberOfGroupsThisTime = match.groupCount();
				for (; i < numberOfGroupsThisTime; i++) {
					if (GLOBAL_GROUPS.size() == i) {
						Global g = RUBY_RUNTIME.getGlobal("$" + (i + 1));
						g.becomeThreadLocal();
						GLOBAL_GROUPS.add(g);
					}
					String s = match.group(i+1);
					//System.out.println("Sætter til '" + s + "' for " + i);
					GLOBAL_GROUPS.get(i).set(s == null ? LoadedRubyRuntime.NIL : new RubyString(s));
				}
			}

			// Reset the globals from last match that are in excess now
			for (; i < LAST_NUMBER_OF_GROUPS.get(); i++) {
				GLOBAL_GROUPS.get(i).set(LoadedRubyRuntime.NIL);
			}
			LAST_NUMBER_OF_GROUPS.set(numberOfGroupsThisTime);
		}
	}
}
