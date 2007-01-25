package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassMethod;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.Selector;

public class RubyMethod extends RubyObject implements IRubyMethod {
	
	private final com.trifork.hotruby.runtime.RubyMethod m;
	private final IRubyObject receiver;
	
	public RubyMethod(com.trifork.hotruby.runtime.RubyMethod m, IRubyObject receiver) {
		this.m = m;
		this.receiver = receiver;
	}
	
	public interface SelectMethod { com.trifork.hotruby.runtime.RubyMethod get_RubyMethod(); }
	public com.trifork.hotruby.runtime.RubyMethod select(Selector sel) {
		if(sel instanceof SelectMethod) { return ((SelectMethod)sel).get_RubyMethod(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectMethod.class); }
	}
	public RubyClass get_class() { return RubyClassMethod.instance; }
	
	public IRubyObject arity() {
		return new RubyFixnum(m.getArity());
	}
	
	public IRubyObject call(IRubyObject[] args, RubyBlock block) {
		return m.call(receiver, args, block);
	}
	
	public IRubyObject to_proc() {
		return new RubyProc(new RubyBlock() {

			@Override
			public IRubyObject call() throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
				return m.call(receiver, (RubyBlock)null);
			}

			@Override
			public IRubyObject call(IRubyObject arg1) throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
				return m.call(receiver, arg1, (RubyBlock)null);
			}

			@Override
			public IRubyObject call(IRubyObject arg1, IRubyObject arg2) throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
				return m.call(receiver, arg1, arg2, (RubyBlock)null);
			}

			@Override
			public IRubyObject call(IRubyObject[] args) throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {
				return m.call(receiver, args, (RubyBlock)null);
			}});
	}

}
