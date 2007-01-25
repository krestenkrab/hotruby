package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyRange;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;

public class RubyClassRange
	extends RubyBaseClassRange
{
	@Override
	public void init(MetaClass meta) {
		// TODO Auto-generated method stub
		super.init(meta);
		
		meta.register_instance_method("each", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyRange)receiver).each(block);
			}
		});
		meta.register_instance_method("exclude_end?", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return bool(((RubyRange)receiver).exclude_end_p());
			}
		});
	}
}
