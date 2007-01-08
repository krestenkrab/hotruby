package com.trifork.hotruby.runtime;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class RubyClassLoader extends URLClassLoader {

	private RubyRuntime runtime;

	public RubyClassLoader() {
		super(new URL[] { loaded() }, RubyClassLoader.class.getClassLoader());
	}
	
	static URL loaded() {
		URL shared_url = RubyClassLoader.class.getProtectionDomain().getCodeSource().getLocation();
		String f = shared_url.getFile();
		try {
			return new URL("file", "", f.replace("shared", "loaded"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		try {
			return super.loadClass(name, resolve);
		} catch (ClassNotFoundException ex) {
			
			MetaClass meta = lazy_instances.remove(name);
			if (meta == null) {
				throw ex;
			}
			
			Class result = runtime.gen.make_instance_class(meta);
			
			meta.set_instance_class(result);
			
			return result;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(loaded());
	}

	public void setRuntime(RubyRuntime runtime) {
		this.runtime = runtime;
	}

	public Class doDefineClass(String name, byte[] bytes) {
		return super.defineClass(name, bytes, 0, bytes.length);
	}

	public RubyRuntime getRuntime() {
		return runtime;
	}

	Map<String,MetaClass> lazy_instances = new HashMap<String, MetaClass>();
	
	public void registerInstanceClass(MetaClass klass) {
		lazy_instances.put(klass.get_instance_class_name(), klass);
	}
	
}
