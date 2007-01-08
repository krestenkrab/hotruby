package com.trifork.hotruby.classes;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyMatchData;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;

public class RubyClassMatchData
	extends RubyBaseClassMatchData
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);
		
		meta.register_instance_method("[]", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				return ((RubyMatchData)receiver).array_access(args);
			}
		});
	}
	
}
