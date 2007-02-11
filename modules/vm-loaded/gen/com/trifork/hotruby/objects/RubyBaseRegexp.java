package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassRegexp;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public abstract class RubyBaseRegexp extends RubyObject implements IRubyRegexp {
	public interface SelectRegexp {
		RubyMethod get_RubyRegexp();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectRegexp) {
			return ((SelectRegexp) sel).get_RubyRegexp();
		}
		return LoadedRubyRuntime.resolve_method(this, sel, SelectRegexp.class);
	}

	public RubyClass get_class() {
		return RubyClassRegexp.instance;
	}
}
