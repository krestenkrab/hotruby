package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;
public class RubyClassSymbol
	extends RubyBaseClassSymbol
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);
		
		meta.register_instance_method("to_s", new PublicMethod0() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString(receiver.asSymbol());
			}});
	}
}
