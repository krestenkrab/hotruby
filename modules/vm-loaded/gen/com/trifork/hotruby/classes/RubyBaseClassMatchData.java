package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyMatchData;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public class RubyBaseClassMatchData
	extends RubyClass
{
	static public RubyClassMatchData instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassMatchData)this; 
		super.init(meta);
	}
	interface SelectMatchData { RubyMethod get_RubyClassMatchData(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectMatchData) { return ((SelectMatchData)sel).get_RubyClassMatchData(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectMatchData.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyMatchData(); }
}
