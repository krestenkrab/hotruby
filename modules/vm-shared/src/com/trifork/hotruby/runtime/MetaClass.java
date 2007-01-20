package com.trifork.hotruby.runtime;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;

public final class MetaClass extends MetaModule {

	private static final String COM_TRIFORK_HOTRUBY_CLASSES_RUBY_CLASS = "com.trifork.hotruby.classes.RubyClass";

	private MetaClass super_meta;

	List<MetaClass> subclasses;

	public boolean isClass() {
		return true;
	}


	public MetaClass(RubyRuntime runtime, MetaModule context_meta, String name,
			MetaClass super_meta) {
		super(runtime, context_meta, name);
		this.super_meta = super_meta;

		if (super_meta != null) {
			super_meta.register_subclass(this);
		}
	}

	protected boolean includes_recursively(MetaModule module) {
		if (super.includes_recursively(module))
			return true;
		
		if (super_meta != null) {
			return super_meta.includes_recursively(module);
		} else {
			return false;
		}
	}

	private void register_subclass(MetaClass sub) {
		if (subclasses == null) {
			subclasses = new ArrayList<MetaClass>();
		}

		subclasses.add(sub);
	}

	protected boolean notify_new_instance_method(String method_name,
			RubyMethod method, boolean is_in_descendant, String[] ivarNames) {

		if (super.notify_new_instance_method(method_name, method,
				is_in_descendant, ivarNames)) {
			return true;
		}

		if (subclasses != null) {
			for (MetaClass cls : subclasses) {
				cls.notify_new_instance_method(method_name, method, true, ivarNames);
			}
		}
		
		// this is a special case of subclass
		if (this == getRuntime().meta_Class()) {
			getRuntime().meta_Object().notify_new_module_method(method_name, method, is_in_descendant, ivarNames);
		} 
		
		return false;
	}

	protected boolean notify_new_module_method(String method_name,
			RubyMethod method, boolean is_in_descendant, String[] ivarNames) {

		if (super.notify_new_module_method(method_name, method,
				is_in_descendant, ivarNames)) {
			return true;
		}

		if (subclasses != null) {
			for (MetaClass cls : subclasses) {
				cls.notify_new_module_method(method_name, method, true, ivarNames);
			}
		}

		return false;
	}

	protected boolean notify_new_constant(String const_name, Constant constant,
			boolean is_in_descendant) {

		if (super.notify_new_constant(const_name, constant, is_in_descendant)) {
			return true;
		}

		if (subclasses != null) {
			for (MetaClass cls : subclasses) {
				cls.notify_new_constant(const_name, constant, true);
			}
		}

		return false;

	}

	@Override
	public String toString() {
		return "meta:"+getName();
	}
	
	@Override
	protected RubyMethod lookup_instance_method(String name, boolean recurse) {
		RubyMethod m = super.lookup_instance_method(name, recurse);
		if (m == null) {
			if (super_meta == null) {
				return null;
			}
			m = super_meta.lookup_instance_method(name, recurse);
		}
		return m;
	}

	@Override
	protected RubyMethod lookup_module_method(String name) {
		RubyMethod m = super.lookup_module_method(name);
		if (m == null) {
			if (super_meta == null) {
				// here we need to lookup an instance method in
				// class class
				m = getRuntime().meta_Class().lookup_instance_method(name, false);
			} else {
				m = super_meta.lookup_module_method(name);
			}
		}
		return m;
	}

	public IRubyClass get_base_class() {
		return (IRubyClass) get_base_module();
	}
	
	public IRubyModule get_base_module() {
		if (base == null) {
			construct_base_class();
		}
		return base;
	}

	private void construct_base_class() {
		base = getRuntime().make_class(this);
	}

	// used for creating anonymous subclasses
	public MetaClass new_subclass(MetaModule context) {
		return new MetaClass(getRuntime(), context, null, this);
	}

	public void set_instance_variable(IRubyObject self, String string,
			IRubyObject value) {
		throw new RuntimeException("not implemented");
	}

	protected Constant find_super_constant(String name, boolean recurse) {
		if (super_meta != null) {
			return super_meta.find_constant(name, recurse);
		}
		return null;
	}

	public void set_base_level_class(Class<? extends IRubyModule> baseClass) {
		try {
			Constructor cons = baseClass.getConstructor(new Class[0]);
			cons.setAccessible(true);
			this.base = (IRubyModule) cons.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("unable to init class object: "
					+ baseClass, e);
		}

		get_base_class().init(this);
	}

	public boolean can_see_protected_method_in(MetaModule target) {
		return super.can_see_protected_method_in(target)
				|| (super_meta != null && super_meta
						.can_see_protected_method_in(target));
	}

	public MetaClass get_super_class() {
		return super_meta;
	}

	public String get_base_class_name() {
		if (this.base == null) {
		String base_name = getBaseName();
		return "com.trifork.hotruby.classes." + getPackageName() 
			+ (base_name.startsWith("#") ? "Class" : ("Class" + base_name));
		} else {
			return this.base.getClass().getName();
		}

	}

	public String get_instance_class_name() {
		String meta = get_base_class_name();
		if (meta.startsWith(COM_TRIFORK_HOTRUBY_CLASSES_RUBY_CLASS)) {
			return "com.trifork.hotruby.objects.Ruby" + meta.substring(COM_TRIFORK_HOTRUBY_CLASSES_RUBY_CLASS.length());
		} 

		String base_name = getBaseName();
		return "com.trifork.hotruby.objects." + getPackageName() 
			+ (base_name.startsWith("#") ? "Instance" : base_name);
	}

	public RubyMethodAccessor getSuperMethodAccessor(String methodName, boolean is_module_method) {
		if (this.super_meta != null) {
			return super_meta.getMethodAccessor(methodName, is_module_method);
		}
		
		return new MissingMethodAccessor(this, methodName, is_module_method);
	}


	public boolean is_subclass_of(MetaClass super_class_candidate) {
		for (MetaClass mc = this; mc != null; mc = mc.get_super_class()) {
			if (mc == super_class_candidate) return true;
		}
		
		return false;
	}


	public boolean is_kind_of(MetaModule candidate_kind) {
		if (candidate_kind.isClass()) {
			return is_subclass_of((MetaClass) candidate_kind);
		}

		MetaModule[] included_modules = get_included_modules(true);
		for (int i = 0; i < included_modules.length; i++) {
			if (included_modules[i] == candidate_kind) {
				return true;
			}
		}
		
		return false;
	}


	protected void add_included_recursively(Set<MetaModule> include_set, boolean b) {
		super.add_included_recursively(include_set, b);
		if (super_meta != null) {
			super_meta.add_included_recursively(include_set, b);
		}
	}

	public MetaClass create_singleton_subclass() {
		MetaClass mc = new MetaClass(getRuntime(), null, null, (MetaClass) this);
		
		return mc;
	}



}
