package com.trifork.hotruby.runtime;

import java.util.HashMap;
import java.util.Map;

import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;

public class SingletonState {

	Map<String,IRubyObject> ivars;
	MetaClass meta;
	public boolean frozen;
	public boolean taint;
	public int object_id;
	
	public RubyMethod select(IRubyObject object, Selector sel) {
		if (meta == null) {
			return object.select(sel);
		} else {
			return meta.resolve_method(object, sel);
		}
	}

	public MetaClass get_meta_class(IRubyObject object, boolean create) {
		
		assert !(object instanceof IRubyModule);
		
		if (meta == null && !create) {
			return (MetaClass) object.get_class().get_meta_module();
		} else if (meta == null) {
			meta = object.get_meta_class().create_singleton_subclass();
		}

		return meta;
	}

	public IRubyObject get_dynamic_ivar_or_null(String name) {
		if (ivars == null) {
			return null;
		} else {
			return ivars.get(name);
		}
	}

	public void set_dynamic_ivar(String name, IRubyObject value) {
		if (frozen) { throw new RuntimeException("target frozen"); }
		taint |= value.isTaint();

		if (ivars == null) {
			ivars = new HashMap<String, IRubyObject>();
		}

		ivars.put(name, value);
	}

}
