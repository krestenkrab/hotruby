package com.trifork.hotruby.classes;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyProc;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
public abstract class RubyBaseClassProc
	extends RubyClass
{
	static public RubyClassProc instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassProc)this; 
		super.init(meta);
	}
	public interface SelectProc { RubyMethod get_RubyClassProc(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectProc) { return ((SelectProc)sel).get_RubyClassProc(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectProc.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyProc(); }
}
