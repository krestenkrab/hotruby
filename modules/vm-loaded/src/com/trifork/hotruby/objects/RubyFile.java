package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassFile;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyFile extends RubyObject implements IRubyObject {
	interface SelectFile { RubyMethod get_RubyFile(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFile) { return ((SelectFile)sel).get_RubyFile(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectFile.class); }
	}
	public RubyClass get_class() { return RubyClassFile.instance; }

}
