package com.trifork.hotruby.util;

import java.lang.reflect.InvocationTargetException;

public abstract class CompiledKey<ValueType> {

	public abstract String getKeyName();
	
	static final Object[] NO_ARGS = new Object[0];
	static final Class[] NO_PARMS = new Class[0];
	java.lang.reflect.Method getter;
	
	@SuppressWarnings("unchecked")
	ValueType get_for(CompiledMap<ValueType> instance) {
		
		if (getter == null) {
			String getter_name = "__get_" + getKeyName();
			try {
				getter = instance.getClass().getMethod(getter_name, NO_PARMS);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			return (ValueType) getter.invoke(instance, NO_ARGS);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			
		}
		
	}
	
}
