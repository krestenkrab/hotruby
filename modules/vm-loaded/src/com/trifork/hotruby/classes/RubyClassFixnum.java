package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyFixnum;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;

public class RubyClassFixnum
	extends RubyBaseClassFixnum
{
	@Override
	public void init(MetaClass meta) {
		// TODO Auto-generated method stub
		super.init(meta);
		
		meta.register_instance_method("succ", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyFixnum)receiver).op_succ();
			}
		});
	}
}
