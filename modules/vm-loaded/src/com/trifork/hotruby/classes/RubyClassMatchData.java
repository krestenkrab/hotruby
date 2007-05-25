package com.trifork.hotruby.classes;

import com.trifork.hotruby.callable.PublicMethod0;
import com.trifork.hotruby.callable.PublicMethod1;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyFixnum;
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
		
		meta.register_instance_method("begin", new PublicMethodN() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyMatchData)receiver).begin(arg);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMatchData)receiver).begin(new RubyFixnum(0));
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				if (args.length == 0) {
					return ((RubyMatchData)receiver).begin(new RubyFixnum(0));
				}
				return ((RubyMatchData)receiver).begin(args[0]);
			}
		});
		meta.register_instance_method("end", new PublicMethodN() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyMatchData)receiver).end(arg);
			}

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMatchData)receiver).end(new RubyFixnum(0));
			}

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {
				if (args.length == 0) {
					return ((RubyMatchData)receiver).end(new RubyFixnum(0));
				}
				return ((RubyMatchData)receiver).end(args[0]);
			}
		});
		meta.register_instance_method("length", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyMatchData)receiver).length();
			}
		});
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
