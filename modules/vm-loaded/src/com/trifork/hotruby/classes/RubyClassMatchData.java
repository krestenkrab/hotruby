package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
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
		
		meta.register_instance_method("begin", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyMatchData)receiver).begin(arg);
			}
		});
		meta.register_instance_method("end", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyMatchData)receiver).end(arg);
			}
		});
//		meta.register_instance_method("pre_match", new PublicMethod0() {
//			@Override
//			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
//				return ((RubyMatchData)receiver).pre_match();
//			}
//		});
		meta.register_instance_method("string", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMatchData)receiver).string();
			}
		});
		
		meta.register_instance_method("to_a", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMatchData)receiver).to_a();
			}
		});
	}
	
}
