package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassProc;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseProc
	extends RubyObject
	implements IRubyProc
{
	public interface SelectProc { RubyMethod get_RubyProc(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectProc) { return ((SelectProc)sel).get_RubyProc(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectProc.class); }
	}
	public RubyClass get_class() { return RubyClassProc.instance; }
}
