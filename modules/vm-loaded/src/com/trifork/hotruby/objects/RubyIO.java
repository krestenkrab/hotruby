package com.trifork.hotruby.objects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.trifork.hotruby.classes.RubyClassIO;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public class RubyIO extends RubyObject {

	private static Selector to_s_selector;
	InputStream in;
	OutputStream out;
	
	public interface SelectIO { RubyMethod get_RubyIO(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectIO) { return ((SelectIO)sel).get_RubyIO(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectIO.class); }
	}
	public RubyClass get_class() { return RubyClassIO.instance; }
	
	public IRubyObject initialize(IRubyObject arg1, IRubyObject arg2) {
		
		if (to_s_selector == null) {
			to_s_selector = getRuntime().getSelector(RubyClassIO.instance.get_meta_module(), "to_s");
		}
		
		int i = RubyInteger.induced_from(arg1).intValue();
		
		switch(i) {
		case 0: in = System.in; break;
		case 1: out = System.out; break;
		case 2: out = System.err; break;
		default: throw getRuntime().newArgumentError("bad file descriptor: "+i);
		}
		
		return this;
	}
	
	public void print(IRubyObject[] args) {

		for (int i = 0; i < args.length; i++) {
			
			IRubyObject arg = args[i];
			RubyString val = (RubyString) RubyString.induce_from((arg instanceof IRubyString) ? arg : arg.fast_to_s(to_s_selector));
			
			try {
				out.write(val.value.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		IRubyObject term = getRuntime().getGlobal("$\\").get();
		if (term != null && term != LoadedRubyRuntime.NIL) {
			String val = RubyString.induce_from(term).asSymbol();
			try {
				out.write(val.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
