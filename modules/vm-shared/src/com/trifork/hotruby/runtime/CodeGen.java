package com.trifork.hotruby.runtime;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import com.trifork.hotruby.compiler.CompilerConsts;
import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyModule;

public class CodeGen implements Opcodes, CompilerConsts {

	private final RubyRuntime runtime;

	public CodeGen(RubyRuntime runtime) {
		this.runtime = runtime;
	}

	static GeneratorAdapter begin_method(ClassVisitor cv, int access,
			Method method) {
		MethodVisitor mv = cv.visitMethod(access, method.getName(), method
				.getDescriptor(), null, EMPTY_STRING_ARRAY);
		GeneratorAdapter ga = new GeneratorAdapter(access, method, mv);
		return ga;
	}

	public void make_special_selector(Selector old, Class selectInterface, RubyMethodAccessor acc)
	{
		Class old_class = old.getClass();
		Field instance_field;
		try {
			instance_field = old_class.getField("instance");
		} catch (Exception e1) {
			throw new InternalError();
		}
		Class base_selector = instance_field.getDeclaringClass();
		
		String class_name = "_" + next_counter() + "." + base_selector.getName();
		Type self_type = Type.getType("L" + class_name.replace('.', '/') + ";");
		Type super_type = Type.getType("L" + old_class.getName().replace('.', '/') + ";");
		Type sel_type = Type.getType(selectInterface);
		
		ClassWriter cw = new ClassWriter(true);
		cw.visit(Opcodes.V1_4, ACC_PUBLIC, self_type.getInternalName(), null, super_type.getInternalName(), new String[] { sel_type.getInternalName() } );
		
		String field_name = "acc$"+next_counter();
		cw.visitField(ACC_PUBLIC|ACC_STATIC, field_name, METHODACCESSOR_TYPE.getDescriptor(), null, null);
		
		java.lang.reflect.Method mm = selectInterface.getMethods()[0];
		
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, new Method(mm.getName(), RUBYMETHOD, new Type[0]));
		ga.getStatic(self_type, field_name, METHODACCESSOR_TYPE);
		ga.invokeVirtual(METHODACCESSOR_TYPE, new Method("get", RUBYMETHOD, new Type[0]));
		ga.returnValue();
		ga.endMethod();
		
		add_default_constructor(cw, super_type);
		
		
		cw.visitEnd();

		byte[] bytes = cw.toByteArray();

		
		Class result = runtime.loader.doDefineClass(class_name, bytes);
		Selector new_selector;
		try {
			new_selector = (Selector) result.newInstance();
			result.getField(field_name).set(new_selector, acc);
			instance_field.set(null, new_selector);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalError("cannot instantiate");
		}
		
	}
	
	public Class make_selector_class(String class_name) {

		String name = class_name.replace('.', '/');
		Type selfType = Type.getType("L" + name + ";");

		ClassWriter cw = new ClassWriter(true);

		cw.visit(Opcodes.V1_4, ACC_PUBLIC, name, null, SELECTOR_CLASS_NAME,
				EMPTY_STRING_ARRAY);

		FieldVisitor fv = cw.visitField(ACC_STATIC | ACC_PUBLIC, "instance",
				"L" + name + ";", null, null);
		fv.visitEnd();

		add_default_constructor(cw, SELECTOR_TYPE);

		GeneratorAdapter ga = begin_method(cw, ACC_STATIC | ACC_PRIVATE,
				CLASS_INITIALIZER);
		ga.newInstance(selfType);
		ga.dup();
		ga.invokeConstructor(selfType, NOARG_CONSTRUCTOR);
		ga.putStatic(selfType, "instance", selfType);

		ga.returnValue();
		ga.endMethod();

		ga = begin_method(cw, ACC_PUBLIC, SELECTOR_SET);
		ga.loadArg(0);
		ga.checkCast(selfType);
		ga.putStatic(selfType, "instance", selfType);
		ga.returnValue();
		ga.endMethod();
		
		ga = begin_method(cw, ACC_PUBLIC, SELECTOR_GET);
		ga.getStatic(selfType, "instance", selfType);
		ga.returnValue();
		ga.endMethod();;
		
		cw.visitEnd();

		byte[] bytes = cw.toByteArray();

		debug_write_class(selfType, bytes);
		
		Class result = runtime.loader.doDefineClass(class_name, bytes);

		// System.out.println("defined selector "+class_name);

		
		
		return result;
	}

	private void add_default_constructor(ClassWriter cw, Type super_type) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, NOARG_CONSTRUCTOR);
		ga.loadThis();
		ga.invokeConstructor(super_type, NOARG_CONSTRUCTOR);
		ga.returnValue();
		ga.endMethod();
	}

	public IRubyModule make_module(MetaModule module) {

		Type self_type = Type.getType("L" + module.get_base_class_name().replace('.', '/') + ";");

		ClassWriter cw = new ClassWriter(true);
		cw.visit(Opcodes.V1_4, ACC_PUBLIC, self_type.getInternalName(), null,
				RUBYMODULE_TYPE.getInternalName(), new String[0]);

		add_default_constructor(cw, RUBYMODULE_TYPE);

		gen_self_instance(self_type, cw);
		gen_module_init(self_type, cw);

		/*
		 * interface SelectKernel { RubyMethod get_RubyModuleKernel(); } public
		 * RubyMethod select(Selector sel) { if(sel instanceof SelectKernel) {
		 * return ((SelectKernel)sel).get_RubyModuleKernel(); } else { return
		 * LoadedRubyRuntime.resolve_method((RubyModuleKernel)this,sel,SelectKernel.class); } }
		 * 
		 */
		gen_get_class(cw, RUBYCLASSMODULE_TYPE);

		Method select_method = new Method("select_" + next_counter(),
				RUBYMETHOD, new Type[0]);
		Type selector_type = gen_selector_interface(self_type, select_method);

		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, SELECT_METHOD);
		Label slow_version = ga.newLabel();
		ga.loadArg(0);
		ga.instanceOf(selector_type);
		ga.ifZCmp(GeneratorAdapter.EQ, slow_version);

		ga.loadArg(0);
		ga.cast(SELECTOR_TYPE, selector_type);
		ga.invokeInterface(selector_type, select_method);
		ga.returnValue();

		ga.mark(slow_version);
		ga.loadThis();
		ga.loadArg(0);
		ga.push(selector_type.getClassName());
		ga.invokeStatic(LOADED_RUBY_RUNTIME, RESOLVE_METHOD);
		ga.returnValue();

		ga.endMethod();

		cw.visitEnd();

		byte[] data = cw.toByteArray();
		
		debug_write_class(self_type, data);
		
		Class module_class = runtime.loader.doDefineClass(self_type
				.getClassName(), data);
		IRubyModule result;
		try {
			result = (IRubyModule) module_class.newInstance();
		} catch (Exception e) {
			throw new InternalError("unable to create module");
		}
		result.init(module);
		return result;
	}

	private void debug_write_class(Type self_type, byte[] data) {
		try {
			String fileName = self_type.getClassName()+".class";
			FileOutputStream fo = new FileOutputStream(fileName);
			fo.write(data);
			fo.close();
			//System.out.println("wrote " + fileName);
		} catch (Exception ex) {
			
		}
	}

	private Type gen_selector_interface(Type context_type, Method select_method2) {

		String name = context_type.getClassName() + "$Selector";
		Type self_type = Type.getType("L" + name.replace('.', '/') + ";");

		ClassWriter cw = new ClassWriter(false);
		cw.visit(Opcodes.V1_4, ACC_PUBLIC | ACC_INTERFACE, self_type
				.getInternalName(), null, OBJECT_TYPE.getInternalName(),
				new String[0]);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_ABSTRACT,
				select_method2.getName(), select_method2.getDescriptor(), null,
				new String[0]);
		mv.visitEnd();

		cw.visitEnd();

		byte[] data = cw.toByteArray();
		runtime.loader.doDefineClass(self_type.getClassName(), data);

		return self_type;

	}

	static int counter = 0;

	private static synchronized int next_counter() {
		return counter++;
	}

	private void gen_get_class(ClassWriter cw, Type class_type) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, GET_CLASS_METHOD);
		ga.getStatic(class_type, "instance", class_type);
		ga.returnValue();
		ga.endMethod();
	}

	private void gen_self_instance(Type self_type, ClassWriter cw) {
		FieldVisitor fv = cw.visitField(ACC_PUBLIC|ACC_STATIC, "instance", self_type
				.getDescriptor(), null, null);
		fv.visitEnd();
	}

	private void gen_module_init(Type self_type, ClassWriter cw) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, MODULE_INIT_METHOD);

		// assign singleton self
		ga.loadThis();
		ga.putStatic(self_type, "instance", self_type);

		// invoke super
		ga.loadThis();
		ga.loadArg(0);
		ga.invokeConstructor(RUBYMODULE_TYPE, MODULE_INIT_METHOD);

		ga.returnValue();

		ga.visitMaxs(0, 0);
		ga.visitEnd();
	}

	private void gen_class_init(Type self_type, ClassWriter cw) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, CLASS_INIT_METHOD);

		// assign singleton self
		ga.loadThis();
		ga.putStatic(self_type, "instance", self_type);

		// invoke super
		ga.loadThis();
		ga.loadArg(0);
		ga.invokeConstructor(RUBYCLASS_TYPE, CLASS_INIT_METHOD);

		ga.returnValue();

		ga.visitMaxs(0, 0);
		ga.visitEnd();
	}

	public IRubyModule make_class(MetaClass klass) {

		klass.set_instance_class_compiled(false);

		Type self_type = Type.getType("L" + klass.get_base_class_name().replace('.', '/') + ";");

		ClassWriter cw = new ClassWriter(true);
		cw.visit(Opcodes.V1_4, ACC_PUBLIC, self_type.getInternalName(), null,
				RUBYCLASS_TYPE.getInternalName(), new String[0]);

		add_default_constructor(cw, RUBYCLASS_TYPE);

		gen_self_instance(self_type, cw);
		gen_class_init(self_type, cw);

		/*
		 * interface SelectKernel { RubyMethod get_RubyModuleKernel(); } public
		 * RubyMethod select(Selector sel) { if(sel instanceof SelectKernel) {
		 * return ((SelectKernel)sel).get_RubyModuleKernel(); } else { return
		 * LoadedRubyRuntime.resolve_method((RubyModuleKernel)this,sel,SelectKernel.class); } }
		 * 
		 */
		gen_get_class(cw, RUBYCLASSCLASS_TYPE);

		Method select_method = new Method("select_" + next_counter(),
				RUBYMETHOD, new Type[0]);
		Type selector_type = gen_selector_interface(self_type, select_method);
		
//		cw.visitInnerClass("Selector", self_type.getInternalName(), selector_type.getInternalName(), ACC_PUBLIC|ACC_INTERFACE);
		
		gen_select_method(cw, select_method, selector_type);

		gen_new_instance(klass, cw);
		
		cw.visitEnd();

		byte[] data = cw.toByteArray();
		
		debug_write_class(self_type, data);
		
		Class class_class = runtime.loader.doDefineClass(self_type
				.getClassName(), data);
		IRubyClass result;
		try {
			result = (IRubyClass) class_class.newInstance();
		} catch (Exception e) {
			throw new InternalError("unable to create module");
		}
		result.init(klass);
		return result;
	}

	private void gen_new_instance(MetaClass klass, ClassWriter cw) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, NEWINSTANCE_METHOD);
		Type instance_type = Type.getType("L" + klass.get_instance_class_name().replace('.', '/') + ";");
		ga.newInstance(instance_type);
		ga.dup();
		ga.invokeConstructor(instance_type, NOARG_CONSTRUCTOR);
		ga.returnValue();
		ga.endMethod();
		
		runtime.loader.registerInstanceClass(klass);
	}

	private void gen_select_method(ClassWriter cw, Method select_method, Type selector_type) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, SELECT_METHOD);
		Label slow_version = ga.newLabel();
		ga.loadArg(0);
		ga.instanceOf(selector_type);
		ga.ifZCmp(GeneratorAdapter.EQ, slow_version);

		ga.loadArg(0);
		ga.cast(SELECTOR_TYPE, selector_type);
		ga.invokeInterface(selector_type, select_method);
		ga.returnValue();

		ga.mark(slow_version);
		ga.loadThis();
		ga.loadArg(0);
		ga.push(selector_type.getClassName());
		ga.invokeStatic(LOADED_RUBY_RUNTIME, RESOLVE_METHOD);
		ga.returnValue();

		ga.endMethod();
	}

	public Class<?> make_instance_class(MetaClass klass) {

		klass.set_instance_class_compiled(true);
		
		Type self_type = Type.getType("L" + klass.get_instance_class_name().replace('.', '/') + ";");
		
		Type super_class_type = Type.getType("L" + klass.get_super_class().get_instance_class_name().replace('.', '/') + ";");
		
		Type class_type = Type.getType("L" + klass.get_base_class_name().replace('.', '/') + ";");
		
		ClassWriter cw = new ClassWriter(true);
		cw.visit(Opcodes.V1_4, ACC_PUBLIC, self_type.getInternalName(), null,
				super_class_type.getInternalName(), new String[0]);

		add_default_constructor(cw, super_class_type);

		add_fast_override(cw, klass.get_super_class().get_instance_class_name());
		
		/*
		 * interface SelectKernel { RubyMethod get_RubyModuleKernel(); } public
		 * RubyMethod select(Selector sel) { if(sel instanceof SelectKernel) {
		 * return ((SelectKernel)sel).get_RubyModuleKernel(); } else { return
		 * LoadedRubyRuntime.resolve_method((RubyModuleKernel)this,sel,SelectKernel.class); } }
		 * 
		 */
		gen_get_class(cw, class_type);

		Method select_method = new Method("select_" + next_counter(),
				RUBYMETHOD, new Type[0]);
		Type selector_type = gen_selector_interface(self_type, select_method);
		
		gen_select_method(cw, select_method, selector_type);
		
		String[] compiled_ivars = klass.getCompiledIvars();
		for (int i = 0; i < compiled_ivars.length; i++) {
			
			FieldVisitor fv = cw.visitField(ACC_PUBLIC, compiled_ivars[i], IRUBYOBJECT.getDescriptor(), null, null);
			fv.visitEnd();
			
		}

		cw.visitEnd();

		byte[] data = cw.toByteArray();
		
		debug_write_class(self_type, data);
		
		Class instance_class = runtime.loader.doDefineClass(self_type
				.getClassName(), data);

		return instance_class;
	}

	private void add_fast_override(ClassWriter cw, String super_instance_name) {
		Class super_instance;
		try {
			super_instance = runtime.loader.loadClass(super_instance_name, false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InternalError("cannot load super class");
		}
		
		Method[] fast_methods = get_fast_methods(super_instance);
		
		for (int i = 0; i < fast_methods.length; i++) {
			add_fast_override(cw, fast_methods[i]);
		}
	}

	private void add_fast_override(ClassWriter cw, Method method) {
		System.out.println("should generate "+method);
		
		GeneratorAdapter mg = begin_method(cw, ACC_PUBLIC, method);
		
		mg.loadThis();
		mg.loadArgs();
		mg.invokeConstructor(RUBYOBJECT_TYPE, method);
		mg.returnValue();
		
		mg.endMethod();
	}

	private Method[] get_fast_methods(Class super_instance) {
		
		Set<Method> methods = new HashSet<Method>();
		
		for (Class c = super_instance; ! c.getName().equals(RUBYOBJECT.getClassName()); c = c.getSuperclass())
		{
			java.lang.reflect.Method[] m = c.getDeclaredMethods();
			for (int i = 0; i < m.length; i++) {
				java.lang.reflect.Method method = m[i];
				Class<?>[] parms = method.getParameterTypes();
				if (method.getName().startsWith("fast_") && parms.length>0 && parms[parms.length-1] == Selector.class) {

					methods.add(reflect_to_asm(method));
				}
			}
		}
		
		return methods.toArray(new Method[methods.size()]);
	}

	private Method reflect_to_asm(java.lang.reflect.Method method) {
		Type result = Type.getType(method.getReturnType());
		Class[] reflect_parms = method.getParameterTypes();
		Type[] parms = new Type[reflect_parms.length];
		for (int i = 0; i < parms.length; i++) {
			parms[i] = Type.getType(reflect_parms[i]);
		}
		return new Method(method.getName(), result, parms);
	}

}
