package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyFile;
import com.trifork.hotruby.objects.RubyMethod;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.Selector;

public class RubyClassMethod extends RubyClass {

	static public RubyClassMethod instance;
	public void init(MetaClass meta) { 
		instance = (RubyClassMethod)this; 
		super.init(meta);
		
		meta.register_instance_method("arity", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMethod)receiver).arity();
			}
		});

		meta.register_instance_method("call", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				return ((RubyMethod)receiver).call(args, block);
			}
		});
		
		meta.alias_instance_method("[]", "call");
		
		meta.register_instance_method("to_proc", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMethod)receiver).to_proc();
			}
		});
		
		
		
		
	}
	
	public interface SelectFile { com.trifork.hotruby.runtime.RubyMethod get_RubyClassFile(); }
	public com.trifork.hotruby.runtime.RubyMethod select(Selector sel) {
		if(sel instanceof SelectFile) { return ((SelectFile)sel).get_RubyClassFile(); }
		else { return LoadedRubyRuntime.resolve_method((RubyClass)this,sel,SelectFile.class); }
	}
	public RubyClass get_class() { return RubyClassClass.instance; }
	public IRubyObject newInstance() {  throw getRuntime().newRuntimeError("cannot allocate Method"); }

}
