package com.trifork.hotruby.util;

import java.util.Map;

public abstract class CompiledMap<T> {

	abstract CompiledMap<T> getInstance();
	abstract void setInstance(CompiledMap<T> instance);
	
	public final T get(CompiledKey<T> key) {
		return key.get_for(getInstance());
	}
	
	abstract CompiledKey<T> getKey(String name);
	
	Map backing;
}
