package com.trifork.hotruby.classes;
import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.callable.PublicMethod2;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyArray;
import com.trifork.hotruby.objects.RubyFixnum;
import com.trifork.hotruby.objects.RubyInteger;
import com.trifork.hotruby.objects.RubyRegexp;
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

		meta.register_instance_method("split_by_string", new PublicMethod2() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
				return ((RubyString)receiver).split_by_string((RubyString)arg1, (RubyFixnum)arg2);
			}});
		
		meta.register_instance_method("gsub", new PublicMethod2() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block) {
				return ((RubyString)receiver).gsub((RubyRegexp)arg1, (RubyString)arg2);
			}
			
		});
		
		meta.register_instance_method("length", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyString)receiver).length();
			}
		});
		
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
			
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject start, IRubyObject len, RubyBlock block) {
				return ((RubyString)receiver).substring(start, len);
			}
			
		});

		meta.register_instance_method("==", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).op_eq2(arg);
			}});

		meta.register_instance_method("<<", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).op_concat(arg);
			}});

		meta.register_instance_method("<=>", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).op_compare(arg);
			}});

		meta.register_instance_method("initialize", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).initialize(arg);
			}});

		meta.alias_instance_method("===", "==");
	}
}
