package com.trifork.hotruby.objects;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.Selector;

public class RubySymbol
	extends RubyBaseSymbol
{
	static Map symbolMap = new HashMap();
	private String name;
	
	private RubySymbol(String value) {
		this.name = value;
	}
	
	@Override
	public IRubyObject fast_to_str(Selector selector) {
		return new RubyString(name);
	}
	
	@Override
	public String asSymbol() {
		return name;
	}
	
	public static RubySymbol get(String string) {
		synchronized(symbolMap) {
			RubySymbol result = (RubySymbol) symbolMap.get(string);
			if (result == null) {
				result = new RubySymbol(string);
				symbolMap.put(string, result);
			}
			return result;
		}
	}
	
	public static IRubyObject unmarshalFrom(UnmarshalStream stream) throws IOException {
		RubySymbol result = get(stream.unmarshalString());
		stream.registerLinkTarget(result);
		return result;
	}
	
	public String inspect() {
		return ":" + name;
	}
}
