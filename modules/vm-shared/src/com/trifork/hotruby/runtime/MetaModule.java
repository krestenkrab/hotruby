package com.trifork.hotruby.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.trifork.hotruby.objects.IRubyMethod;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.runtime.ThreadState.ModuleFrame;

public class MetaModule implements CallContext {

	static int next_anon_name = 0;

	IRubyModule base;

	List<MetaModule> included = new ArrayList<MetaModule>();

	List<MetaModule> descendents = new ArrayList<MetaModule>();

	Map<String, MetaModule> contained = new HashMap();

	Map<String, Constant> constants = new HashMap();

	protected MetaModule context_meta;

	private String name;

	public MetaModule(RubyRuntime runtime, MetaModule context_meta, String name) {
		this.name = name;
		this.runtime = runtime;

		if (context_meta != null && name != null) {
			set_context(context_meta, name);
		}
	}

	public void include(MetaModule module) {

		if (this.includes_recursively(module)) {
			return;
		}

		// TODO: eliminate duplicates
		included.add(module);
		module.descendents.add(this);
	}

	protected boolean includes_recursively(MetaModule module) {
		for (MetaModule mod : included) {
			if (mod == module)
				return true;
			if (mod.includes_recursively(module))
				return true;
		}

		return false;
	}

	public MetaModule getCaller() {
		return this;
	}

	// used when a constant is defined
	public void set_context(MetaModule module, String name) {

		if (this.context_meta == null && name != null) {
			assert module != null;

			this.context_meta = module;
			this.name = name;

			module.contained.put(name, this);
			module.const_set(name, this);
		}
	}

	Map<String, RubyMethod> instance_methods = new HashMap<String, RubyMethod>();

	Map<String, RubyMethod> module_methods = new HashMap<String, RubyMethod>();

	public void register_instance_method(String name, RubyMethod method) {
		instance_methods.put(name, method);
		runtime.register_method(method, name);

		notify_new_instance_method(name, method, false, method.getIVarNames());
	}

	public void register_module_method(String name, RubyMethod method) {
		module_methods.put(name, method);
		runtime.register_method(method, name);
	}

	public MetaModule get_contained(String name, boolean use_const_missing) {
		MetaModule mm = contained.get(name);
		if (mm == null && use_const_missing) {
			handle_undefined_const(name, this);
			mm = contained.get(name);
		}
		return mm;
	}

	public MetaModule get_module(String nested, boolean use_const_missing) {
		int idx = nested.indexOf("::");

		if (idx == 0) {
			return getRuntime().meta_Object().get_module(nested.substring(2),
					use_const_missing);
		} else if (idx == -1) {
			return get_contained(nested, use_const_missing);
		} else {
			String first = nested.substring(0, idx);
			String rest = nested.substring(idx + 2);

			MetaModule first_part = get_contained(first, use_const_missing);
			if (first_part == null) {
				return null;
			} else {
				return first_part.get_module(rest, use_const_missing);
			}
		}
	}

	public MetaClass get_class(String nested, boolean use_const_missing) {
		int idx = nested.indexOf("::");

		if (idx == 0) {
			return getRuntime().meta_Object().get_class(nested.substring(2),
					use_const_missing);
		} else if (idx == -1) {
			MetaModule mm = get_contained(nested, use_const_missing);
			if (mm == null || mm instanceof MetaClass) {
				return (MetaClass) mm;
			} else {
				return null;
			}
		} else {
			String first = nested.substring(0, idx);
			String rest = nested.substring(idx + 2);

			MetaModule first_part = contained.get(first);
			if (first_part == null) {
				return null;
			} else {
				return first_part.get_class(rest, use_const_missing);
			}
		}
	}

	public IRubyModule get_base_module() {
		if (base == null) {
			construct_base_module();
		}
		return base;
	}

	private void construct_base_module() {
		base = getRuntime().make_module(this);
	}

	public boolean isClass() {
		return false;
	}

	public String getBaseName() {
		if (name == null) {

			return "";
			/*
			 * if (isClass()) { return "#<Class:0x" +
			 * Integer.toHexString(System.identityHashCode(this)) + ">"; } else {
			 * return "#<Module:0x" +
			 * Integer.toHexString(System.identityHashCode(this)) + ">"; }
			 */
		}

		return name;
	}

	public boolean isObject() {
		return context_meta == this && "Object" == name;
	}

	public String getName() {

		if (name == null || context_meta == null || isObject()) {
			return getBaseName();
		}

		StringBuilder sb = new StringBuilder();

		context_meta.append_name(sb, "::");
		sb.append(name);

		return sb.toString();

	}

	private void append_name(StringBuilder sb, String delim) {
		if (!isObject() && context_meta != null) {
			context_meta.append_name(sb, delim);
			sb.append(name);
			sb.append(delim);
		}
	}

	private String package_name;

	private final RubyRuntime runtime;

	public synchronized String getPackageName() {

		if (package_name != null) {
			return package_name;
		}

		if (isObject()) {
			return package_name = "";
		}

		if (name == null || context_meta == null) {
			return package_name = ("Anon$" + next_anon_name++);
		}

		StringBuilder sb = new StringBuilder();

		context_meta.append_name(sb, ".");
		sb.append(name);
		sb.append('.');

		return package_name = sb.toString();
	}

	Map<ConstKey, ConstantAccessor> lex_access_constant = new WeakHashMap<ConstKey, ConstantAccessor>();

	static class ConstKey {
		String name;
		ModuleFrame scope;
		
		public ConstKey(String name2, ModuleFrame lex_scope) {
			this.name = name2;
			this.scope = lex_scope;
		}

		@Override
		public int hashCode() {
			return name.hashCode() + (scope==null?0: scope.hashCode());
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ConstKey) {
				ConstKey key = (ConstKey) obj;
				return name.equals(key.name) && (scope != null ? scope.equals(key.scope) : scope==key.scope);
			}
			return false;
		}
	}
	
	public ConstantAccessor getConstantAccessor(String name, ModuleFrame lex_scope) {
		if (name.startsWith("::")) {
			return getRuntime().meta_Object().getConstantAccessor(
					name.substring(2), lex_scope);
		}

		ConstKey key = new ConstKey(name, lex_scope);
		ConstantAccessor result = lex_access_constant.get(key);
		if (result == null) {
			lex_access_constant.put(key, result = new ConstantAccessor(this,
					key));
		}

		Constant const_var = find_constant(name, lex_scope);
		if (const_var != null) {
			result.setConstant(const_var);
		}

		return result;
	}

	Map<String, RubyMethodAccessor> ctx_ins_access_method = new WeakHashMap<String, RubyMethodAccessor>();

	Map<String, RubyMethodAccessor> ctx_mod_access_method = new WeakHashMap<String, RubyMethodAccessor>();

	public RubyMethodAccessor getMethodAccessor(String name,
			boolean is_module_method) {

		if ("attr_reader".equals(name)) {
			System.out.println("here");
		}

		Map<String, RubyMethodAccessor> map = is_module_method ? ctx_mod_access_method
				: ctx_ins_access_method;
		RubyMethodAccessor result = map.get(name);
		if (result == null) {
			map.put(name, result = new RubyMethodAccessor(this, name,
					is_module_method));
		}

		return result;
	}

	@Override
	public String toString() {
		return "meta:" + getName();
	}

	public void const_set(String name, MetaModule module) {
		Constant constant = constants.get(name);
		if (constant == null) {
			ModuleRuntimeConstant mm_constant = new ModuleRuntimeConstant(this,
					name);
			mm_constant.set_meta(module);
			constants.put(name, mm_constant);
			notify_new_constant(name, mm_constant, false);
		} else if (constant instanceof ModuleRuntimeConstant) {
			((ModuleRuntimeConstant) constant).set_meta(module);
		} else {
			constant.set(module.get_base_module());
		}
	}

	public void const_set(String name, IRubyObject value) {
		Constant constant = constants.get(name);

		if (constant == null) {
			constant = new RuntimeConstant(this, name);
			constant.set(value);
			constants.put(name, constant);

			notify_new_constant(name, constant, false);
		} else {
			constant.set(value);
		}
	}

	protected boolean notify_new_constant(String const_name, Constant constant,
			boolean is_in_descendant) {

		if (is_in_descendant) {
			if (constants.containsKey(const_name)) {
				return true;
			}
		}

		ConstantAccessor acc = lex_access_constant.get(const_name);
		if (acc != null) {
			acc.setConstant(constant);
		}

		for (MetaModule mod : descendents) {
			mod.notify_new_constant(const_name, constant, true);
		}

		return false;
	}

	protected boolean notify_new_instance_method(String method_name,
			RubyMethod method, boolean is_in_descendant, String[] ivarNames) {

		if (is_in_descendant) {
			if (instance_methods.containsKey(method_name)) {
				return true;
			}
			method = null;
		}

		register_instance_ivars(ivarNames);

		RubyMethodAccessor acc = ctx_ins_access_method.get(method_name);
		if (acc != null) {
			acc.set(method);
		}

		for (MetaModule mod : descendents) {
			mod
					.notify_new_instance_method(method_name, method, true,
							ivarNames);
		}

		return false;
	}

	protected boolean notify_new_module_method(String method_name,
			RubyMethod method, boolean is_in_descendant, String[] ivarNames) {

		if (is_in_descendant) {
			if (instance_methods.containsKey(method_name)) {
				return true;
			}
			method = null;
		}

		register_module_ivars(ivarNames);

		RubyMethodAccessor acc = ctx_mod_access_method.get(method_name);
		if (acc != null) {
			acc.set(method);
		}

		for (MetaModule mod : descendents) {
			mod.notify_new_module_method(method_name, method, true, ivarNames);
		}

		return false;
	}

	protected Constant find_constant(String name, ModuleFrame lex_scope) {
		Constant found = find_constant(name, false);
		if (found == null) {
			for (ModuleFrame scope = lex_scope; scope != null; scope = scope.parent) {
				found = scope.module.constants.get(name);
				if (found != null) {
					break;
				}
			}
		}
		return found;
	}

	protected Constant find_constant(String name, boolean recursive_invocation) {
		Constant result = constants.get(name);
		if (result != null) {
			return result;
		}

		for (MetaModule inc : included) {
			result = inc.find_constant(name, recursive_invocation);
			if (result != null) {
				return result;
			}
		}

		return find_super_constant(name, recursive_invocation);
	}

	// overridden in MetaClass
	protected Constant find_super_constant(String name, boolean recursive_invocation) {
		if (recursive_invocation)
			return null;
		MetaClass module = getRuntime().meta_Module();
		return module.find_constant(name, true);
	}

	public void handle_undefined_const(String const_name, MetaModule lex_context) {
		IRubySymbol name = getRuntime().newSymbol(const_name);
		IRubyModule module = this.get_base_module();
		RubyMethod op = module.do_select(getRuntime().getSelector(lex_context,
				"const_missing"));
		IRubyObject value = op.call(module, name);
		const_set(const_name, value);
	}

	public void set_base_level_class(Class<? extends IRubyModule> baseClass) {
		try {
			this.base = baseClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("unable to init class object", e);
		}

		base.init(this);
	}

	public RubyRuntime getRuntime() {
		return runtime;
	}

	public boolean can_see_protected_method_in(CallContext target) {
		if (this == target)
			return true;
		for (MetaModule inc : included) {
			if (inc.can_see_protected_method_in(target.getCaller())) {
				return true;
			}
		}
		return false;
	}

	protected RubyMethod lookup_instance_method(String name, boolean recurse) {
		RubyMethod rm = instance_methods.get(name);
		if (rm != null) {
			return rm;
		}
		for (MetaModule mod : included) {
			rm = mod.lookup_instance_method(name, true);
			if (rm != null) {
				if (isClass() && !recurse) {
					return rm.specialize_for(this, false);
				} else {
					return rm;
				}
			}
		}
		if (!recurse && !this.isClass() && true) {
			rm = getRuntime().meta_Module()
					.lookup_instance_method(name, true);
			if (isClass() && !recurse) {
				return rm.specialize_for(this, false);
			} else {
				return rm;
			}
		} else {
			return null;
		}
	}

	protected RubyMethod lookup_module_method(String name) {
		RubyMethod rm = module_methods.get(name);
		if (rm != null) {
			return rm;
		}
		/* including a module does not expose the module's module methods */
		/* also, we cannot see class Module's static methods */
		
		if (!this.isClass()) {
			return getRuntime().meta_module.lookup_instance_method(name, true);
		}
		
		return null;
	}

	public RubyMethod resolve_method(IRubyObject object, Selector sel,
			Class selectorClass) {

		String name = sel.getName();
		RubyMethod m;

		boolean is_module = false;
		if (object instanceof IRubyModule) {
			is_module = true;
			m = lookup_module_method(name);
			if (m == null) {
				return new MissingMethod(getRuntime(), sel);
			}

		} else {

			m = lookup_instance_method(name, false);
			if (m == null) {
				return new MissingMethod(getRuntime(), sel);
			}
		}

		m = m.specialize_for(this, is_module);

		if (selectorClass != null) {
			
			RubyMethodAccessor methodAccessor = getMethodAccessor(name, is_module);
			methodAccessor.set(m);
			getRuntime().gen.make_special_selector(sel, selectorClass,
					methodAccessor);
		}

		return m;
	}

	public RubyMethod resolve_method(IRubyObject object, Selector sel) {
		return resolve_method(object, sel, null);
	}

	public Selector getSelector(String name) {
		return getRuntime().getSelector(this, name);
	}

	public String get_base_class_name() {
		
		
		if (this.base == null) {
			String base_name = getBaseName();
			return "com.trifork.hotruby.modules." + getPackageName() 
				+ (base_name.startsWith("#") ? "Module" : ("Module" + base_name));
		} else {
			return this.base.getClass().getName();
		}
	}

	Map<String, RubyIvarAccessor> ivar_accessors = new HashMap<String, RubyIvarAccessor>();

	Map<String, RubyIvarAccessor> module_ivar_accessors = new HashMap<String, RubyIvarAccessor>();

	/** a method is added that references ivars in this class */
	private void register_instance_ivars(String[] varNames) {
		for (int i = 0; i < varNames.length; i++) {
			register_instance_ivar(varNames[i]);
		}
	}

	/** a method is added that references ivars in this class */
	private void register_module_ivars(String[] varNames) {
		for (int i = 0; i < varNames.length; i++) {
			register_module_ivar(varNames[i]);
		}
	}

	boolean instance_class_is_compiled = false;

	boolean module_class_is_compiled = true;

	public void set_instance_class_compiled(boolean value) {
		instance_class_is_compiled = value;
	}

	private void register_instance_ivar(String name) {
		if (ivar_accessors.containsKey(name))
			return;

		if (instance_class_is_compiled) {
			ivar_accessors.put(name, new DynamicIVarAccessor(name));
		} else {
			ivar_accessors.put(name, new CompiledIVarAccessor(name));
		}
	}

	private void register_module_ivar(String name) {
		if (module_ivar_accessors.containsKey(name))
			return;

		if (module_class_is_compiled) {
			module_ivar_accessors.put(name, new DynamicIVarAccessor(name));
		} else {
			module_ivar_accessors.put(name, new CompiledIVarAccessor(name));
		}
	}

	public RubyIvarAccessor getInstanceIVarAccessor(String name) {
		return getInstanceIVarAccessor(name, true);
	}

	public RubyIvarAccessor getInstanceIVarAccessor(String name, boolean create) {
		RubyIvarAccessor result = ivar_accessors.get(name);

		if (result == null && create) {
			register_instance_ivar(name);
			result = ivar_accessors.get(name);
		}

		return result;
	}

	public RubyIvarAccessor getModuleIVarAccessor(String name) {
		RubyIvarAccessor result = module_ivar_accessors.get(name);

		if (result == null) {
			register_module_ivar(name);
			result = module_ivar_accessors.get(name);
		}

		return result;
	}

	public void set_instance_class(Class result) {
		for (RubyIvarAccessor acc : ivar_accessors.values()) {
			if (acc instanceof CompiledIVarAccessor) {
				((CompiledIVarAccessor) acc).setBaseClass(result);
			}
		}
	}

	public String[] getCompiledIvars() {
		List<String> result = new ArrayList<String>();
		for (RubyIvarAccessor acc : ivar_accessors.values()) {
			if (acc instanceof CompiledIVarAccessor) {
				result.add(((CompiledIVarAccessor) acc).getFieldName());
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public void alias_instance_method(String new_name, String original_name) {
		RubyMethod m = lookup_instance_method(original_name, true);
		if (m == null) {
			throw getRuntime().newNameError(
					"undefined " + original_name + " in " + this.getName());
		}
		register_instance_method(new_name, m);
	}

	public void alias(IRubySymbol new_sym, IRubySymbol orig_sym,
			boolean self_is_module) {
		String new_name = new_sym.asSymbol();
		String orig_name = orig_sym.asSymbol();

		if (new_name.charAt(0) == '$') {
			getRuntime().alias_global(new_name, orig_name);
		} else {
			// todo: how do we alias module methods?
			alias_instance_method(new_name, orig_name);
		}
	}

	public RubyMethodAccessor getSuperMethodAccessor(String methodName,
			boolean is_module_method) {
		return this.getMethodAccessor(methodName, is_module_method);
	}

	protected MetaModule[] get_included_modules(boolean b) {
		Set<MetaModule> include_set = new HashSet<MetaModule>();

		add_included_recursively(include_set, b);

		return include_set.toArray(new MetaModule[include_set.size()]);
	}

	protected void add_included_recursively(Set<MetaModule> include_set,
			boolean b) {

		for (MetaModule m : included) {
			if (!include_set.contains(m)) {
				include_set.add(m);
				m.add_included_recursively(include_set, b);
			}
		}

	}

	public MetaClass create_singleton_subclass() {
		throw new InternalError("should not happen");
	}

	public boolean respond_to_p(String name, boolean includePriv, boolean is_module) {

		RubyMethod m;
		if (is_module) {
			m = lookup_module_method(name);
		} else {
			m = lookup_instance_method(name, false);
		}
		
		return m != null;
	}
	
	public IRubyMethod method(IRubyObject receiver, String name) {

		RubyMethod m;
		if (receiver instanceof IRubyModule) {
			m = lookup_module_method(name);
		} else {
			m = lookup_instance_method(name, false);
		}
		
		if (m == null) {
			throw getRuntime().newNameError("no such method: `"+name+"' in "+this.getName());
		}
		
		return getRuntime().newMethodObject(m, receiver);
	}

	public void copy_methods_to(MetaModule mm, boolean receiver_is_module) {
		for (Map.Entry<String,RubyMethod> ent : instance_methods.entrySet()) {
			if (receiver_is_module) {
				mm.register_module_method(ent.getKey(), ent.getValue());
			} else {
				mm.register_instance_method(ent.getKey(), ent.getValue());
			}
		}
	}

	public Collection<String> public_instance_methods(boolean include_super) {
		return instance_methods.keySet();
	}

}
