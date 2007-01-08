package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
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
