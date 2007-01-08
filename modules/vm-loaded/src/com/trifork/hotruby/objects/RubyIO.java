package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassIO;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyIO extends RubyObject {

	public interface SelectIO { RubyMethod get_RubyIO(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectIO) { return ((SelectIO)sel).get_RubyIO(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectIO.class); }
	}
	public RubyClass get_class() { return RubyClassIO.instance; }
	
	public IRubyObject initialize(IRubyObject arg1, IRubyObject arg2) {
		
		return this;
	}

}
