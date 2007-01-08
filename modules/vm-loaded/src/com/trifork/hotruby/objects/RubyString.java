package com.trifork.hotruby.objects;
import java.io.IOException;

import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.Selector;
public class RubyString 
	extends RubyBaseString
{
	String value;
	
	public RubyString(String value) {
		this.value = value;
		if (value == null) { throw new NullPointerException(); }
	}

	public String asSymbol() {
		return value;
	}
	
	public static IRubyObject unmarshalFrom(UnmarshalStream stream) throws IOException {
		RubyString string = new RubyString(stream.unmarshalString());
		stream.registerLinkTarget(string);
		return string;
	}
	
	public String inspect() {
		StringBuilder sb = new StringBuilder();
		sb.append('"');
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);  
			if (ch < 0x1f || ch > 0x7f) {
				unicode(sb, ch);
			} else if (ch == '\\') {
				sb.append("\\\\");
			} else {
				sb.append(ch);
			}
		}
		sb.append('"');
		return sb.toString();
	}

	static private char[] HEX_DIGIT = new char[] {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};
	
	private static Selector to_str_sel;
	
	public static void init(MetaClass string_class) {
			to_str_sel = LoadedRubyRuntime.instance.getSelector(string_class, "to_str");
	}
	
	private void unicode(StringBuilder sb, int ch) {
		sb.append("\\u");
		sb.append(HEX_DIGIT[(ch>>>12)&0xf]);
		sb.append(HEX_DIGIT[(ch>>>8)&0xf]);
		sb.append(HEX_DIGIT[(ch>>>4)&0xf]);
		sb.append(HEX_DIGIT[(ch)&0xf]);
	}

	public IRubyObject at_x(IRubyObject arg) {
		if (arg instanceof RubyFixnum) {
			int fix = ((RubyFixnum)arg).intValue();
			
			if (fix < 0) {
				fix = value.length() + fix;
			}
			
			if (fix < 0) { return LoadedRubyRuntime.NIL; }
			if (fix >= value.length()) { return LoadedRubyRuntime.NIL; }
			return new RubyFixnum(value.charAt(fix));
		}
		
		throw new InternalError("not implemented");
	}

	@Override
	public IRubyObject fast_to_str(Selector selector) {
		return this;
	}

	public static IRubyString induce_from(IRubyObject arg1) {
		if (arg1 instanceof IRubyString) {
			return (IRubyString) arg1;
		}
		
		IRubyObject o = arg1.fast_to_str(to_str_sel);
		if (o instanceof IRubyString) {
			return (IRubyString) o;
		}
		
		throw LoadedRubyRuntime.instance.newTypeError("object is not string");
	}
}
