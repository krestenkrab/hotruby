package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyFloat;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
public class RubyClassFloat
	extends RubyBaseClassFloat
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);
		
		meta.register_instance_method("to_s", 
			new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyFloat) receiver).to_s();
			}
		}
		);
	}
}
