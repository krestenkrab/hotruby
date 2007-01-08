package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassException;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyException extends RubyObject {
	
	interface SelectException { RubyMethod get_RubyException(); }

	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectException) { return ((SelectException)sel).get_RubyException(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectException.class); }
	}

	public RubyClass get_class() { return RubyClassException.instance; }

}
