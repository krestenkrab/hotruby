package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyTime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;

public class RubyClassTime
	extends RubyBaseClassTime
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		meta.register_module_method("new", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyTime();
			}			
		});
		
		meta.register_module_method("gm", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				// TODO: http://developer.trifork.com/browse/HR-1
				return new RubyTime();
			}
		});
	
		meta.register_instance_method("-", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyTime)receiver).subtract(arg);
			}
		});
	}
}
