package com.trifork.hotruby.objects;

import java.util.regex.Matcher;

import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;

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
			result.add(get_match(args[i]));
		}

		return result;
	}

	private IRubyObject get_match(IRubyObject object) {

		int val = RubyInteger.mm_induced_from(object).intValue();

		if (val < 0 || val > match.groupCount()) {
			return LoadedRubyRuntime.NIL;
		}

		try {
			int start = match.start(val);
			int end = match.end(val);
			
			CharSequence result_sequence = source.subSequence(start, end);
			
			return LoadedRubyRuntime.instance.newString(result_sequence);

		} catch (IndexOutOfBoundsException ex) {
			return LoadedRubyRuntime.NIL;
		}
	}

}
