package com.trifork.hotruby.runtime;


public interface CallContext {
	
	CallContext NULL = new CallContext() {
		public MetaModule getCaller() {
			return null;
		}
	};

	MetaModule getCaller();
}
