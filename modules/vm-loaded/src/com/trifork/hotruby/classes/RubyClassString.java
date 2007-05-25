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
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.Selector;
public class RubyClassString
	extends RubyBaseClassString
{
	Selector call_cmp;
	
	@Override
	public IRubyObject fast_eq3(IRubyObject arg, Selector selector) {
		return super.fast_eq3(arg, selector);
	}
	
	@Override
	public void init(MetaClass meta) {
		super.init(meta);

		call_cmp = LoadedRubyRuntime.instance.getSelector(meta, "<=>");
		
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
		
		meta.register_instance_method("hex", new PublicMethod0() {

			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				String val = ((RubyString)receiver).asSymbol();

				int idx = 0;
				long mult = 1L;
				if (val.charAt(0) == '-') {
					mult = -1L;
					idx = 1;
				}
				
				if (val.startsWith("0x", idx)) {
					idx += 2;
				}
				
				int end = idx;
				for (int i = idx; i < val.length(); i++) {
					char ch = val.charAt(i);
					if (Character.digit(ch, 16) >= 0) {
						end = i+1;
					} else {
						break;
					}
				}
				
				String hex = val.substring(idx, end);
				try {
					return RubyInteger.newInteger(mult * Integer.parseInt(hex, 16));
				} catch (NumberFormatException ex) {
					return new RubyFixnum(0);
				}
			}
			
		});
		
		meta.register_instance_method("length", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyString)receiver).length();
			}
		});
		
		meta.register_instance_method("downcase", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyString)receiver).downcase();
			}
		});
		
		meta.register_instance_method("downcase!", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return ((RubyString)receiver).downcase_bang();
			}
		});
		
		meta.register_instance_method("to_s", new PublicMethod0() {
			@Override
			public IRubyObject call(IRubyObject receiver, RubyBlock block) {
				return receiver;
			}});
		
		meta.register_instance_method("<=>", new PublicMethod1() {
			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return receiver.fast_cmp(arg, call_cmp);
			}			
		});
		
		meta.register_instance_method("*", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).op_mult(arg);
			}
			
		});
		
		meta.register_instance_method("<<", new PublicMethod1() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject arg, RubyBlock block) {
				return ((RubyString)receiver).op_lshift(arg);
			}
			
		});
		
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
