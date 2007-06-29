package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyArray;
import com.trifork.hotruby.objects.RubyInteger;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.Selector;
public class RubyClassArray
	extends RubyBaseClassArray
{
	private static Selector sel_eq2;

	@Override
	public void init(MetaClass meta) {
		super.init(meta);
		
		sel_eq2 = getRuntime().getSelector(meta, "==");
		
		meta.register_instance_method("==", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyArray)receiver).op_eq2(arg, sel_eq2);
			}});
		
		meta.register_instance_method("delete_at", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyArray)receiver).delete_at(arg);
			}});
		
		meta.register_instance_method("initialize", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyArray)receiver).initialize();
			}
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject size, RubyBlock block) {
				return ((RubyArray)receiver).initialize(RubyInteger.induced_from(size).intValue());
			}
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject size, IRubyObject val, RubyBlock block) {
				return ((RubyArray)receiver).initialize(RubyInteger.induced_from(size).intValue(), val);
			}
		});
		
		meta.register_instance_method("inspect", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return new RubyString(receiver.inspect());
			}
		});
		
		meta.register_instance_method("shift", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyArray)receiver).shift();
			}
		});
		
		meta.register_instance_method("sort!", new PublicMethod0(){

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyArray)receiver).sort_bang(block);
			}});
	}
}
