package com.trifork.hotruby.objects;
import com.trifork.hotruby.classes.RubyClassMatchData;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseMatchData
	extends RubyObject
	implements IRubyMatchData
{
	public interface SelectMatchData { RubyMethod get_RubyMatchData(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectMatchData) { return ((SelectMatchData)sel).get_RubyMatchData(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectMatchData.class); }
	}
	public RubyClass get_class() { return RubyClassMatchData.instance; }
}
