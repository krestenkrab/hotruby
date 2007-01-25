package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyFile;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyClassFile extends RubyClass {

	static public RubyClassFile instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassFile)this; 
		super.init(meta);
		
		meta.register_module_method("file?", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				IRubyString path = RubyString.induce_from(arg);
				return bool( new java.io.File(path.asSymbol()).isFile() );
			}
		});
	}
	public interface SelectFile { RubyMethod get_RubyClassFile(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectFile) { return ((SelectFile)sel).get_RubyClassFile(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFile.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() { return new RubyFile(); }

}
