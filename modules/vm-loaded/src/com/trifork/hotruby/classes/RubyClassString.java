package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyArray;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.Selector;
public class RubyClassString
	extends RubyBaseClassString
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		meta.register_instance_method("to_s", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver;
			}});
		
		meta.register_instance_method("[]", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).at_x(arg);
			}
			
		});

		meta.register_instance_method("==", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).op_eq2(arg);
			}});
		
	}
}
