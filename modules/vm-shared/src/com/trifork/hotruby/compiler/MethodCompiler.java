package com.trifork.hotruby.compiler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import com.trifork.hotruby.interp.BindingContext;
import com.trifork.hotruby.interp.ISeq;
import com.trifork.hotruby.interp.Instructions;
import com.trifork.hotruby.interp.MethodISeq;
import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyFalseClass;
import com.trifork.hotruby.objects.IRubyFixnum;
import com.trifork.hotruby.objects.IRubyFloat;
import com.trifork.hotruby.objects.IRubyNilClass;
import com.trifork.hotruby.objects.IRubyProc;
import com.trifork.hotruby.objects.IRubyRegexp;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.objects.IRubyTrueClass;
import com.trifork.hotruby.runtime.ConstantAccessor;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.Global;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.NonLocalReturn;
import com.trifork.hotruby.runtime.RubyIvarAccessor;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;

public class MethodCompiler implements Opcodes, Instructions, CompilerConsts {

	private static final Type STRING_ARRAY_TYPE = Type.getType(String[].class);

	private static final Type RUBY_IVAR_ACCESSOR_TYPE = Type
			.getType(RubyIvarAccessor.class);

	private static final Method GET_INSTANCE_IVAR_ACCESSOR = new Method(
			"getInstanceIVarAccessor", RUBY_IVAR_ACCESSOR_TYPE,
			new Type[] { STRING_TYPE });

	private static final Method GET_MODULE_IVAR_ACCESSOR = new Method(
			"getModuleIVarAccessor", RUBY_IVAR_ACCESSOR_TYPE,
			new Type[] { STRING_TYPE });

	private static final Type[] NO_TYPES = new Type[0];

	private static final Type[] ONE_IRUBYOBJECT = new Type[] { IRUBYOBJECT };

	private static final Method MATH_MIN_INT = new Method("min", Type.INT_TYPE,
			new Type[] { Type.INT_TYPE, Type.INT_TYPE });

	private static final Method METHOD_CONSTRUCTOR = new Method("<init>",
			Type.VOID_TYPE, new Type[] { METAMODULE_TYPE, METAMODULE_TYPE });

	private static final Type COMPILED_METHOD_DVARS = Type
			.getType(CompiledMethodEvalContext.class);

	private static final Type COMPILED_DVARS = Type
			.getType(CompiledEvalContext.class);

	private static final Method COMPILED_METHOD_DVARS_CONSTRUCTOR = new Method(
			"<init>", Type.VOID_TYPE, new Type[] { IRUBYOBJECT, COMPILEDMETHOD,
					RUBYBLOCK });

	private static final Method WRONG_ARGS_METHOD = new Method("wrongArgs",
			Type.getType(RuntimeException.class), new Type[] { Type.INT_TYPE });

	private static final Method SET_DVAR = new Method("setDVar",
			Type.VOID_TYPE, new Type[] { IRUBYOBJECT, COMPILED_DVARS,
					Type.INT_TYPE });

	private static final Type RUBYPROC = Type
			.getType("Lcom/trifork/hotruby/objects/RubyProc;");

	private static final Method NEW_PROC = new Method("newProc", RUBYPROC,
			new Type[] { RUBYBLOCK });

	private static final Type IRUBYARRAY = Type.getType(IRubyArray.class);

	private static final Type IRUBYPROC = Type.getType(IRubyProc.class);

	private static final Method JAVA_ARRAY_REST_METHOD = new Method(
			"java_array_rest", IRUBYARRAY, new Type[] { IRUBYOBJECT_ARR,
					Type.INT_TYPE, RUBY_RUNTIME_TYPE });

	private static final Type MATH_TYPE = Type.getType(Math.class);

	private static final Type GLOBAL_TYPE = Type.getType(Global.class);

	private static final Method GET_GLOBAL = new Method("getGlobal",
			GLOBAL_TYPE, new Type[] { STRING_TYPE });

	private static final Method SET_TAKING_OBJECT = new Method("set",
			Type.VOID_TYPE, ONE_IRUBYOBJECT);

	private static final Method GET_RETURNING_OBJECT = new Method("get",
			IRUBYOBJECT, NO_TYPES);

	private static final Method GET_DVAR_IDX = new Method("get", IRUBYOBJECT,
			new Type[] { Type.INT_TYPE });

	private static final Method GET_DYNAMIC_IDX = new Method("getdynamic", OBJECT_TYPE,
			new Type[] { Type.INT_TYPE, Type.INT_TYPE });

	private static final Method IS_FALSE = new Method("isFalse",
			Type.BOOLEAN_TYPE, NO_TYPES);

	private static final Method IS_TRUE = new Method("isTrue",
			Type.BOOLEAN_TYPE, NO_TYPES);

	private static final Type STRING_BUILDER = Type
			.getType(StringBuilder.class);

	private static final Method APPEND_STRING = new Method("append",
			STRING_BUILDER, new Type[] { STRING_TYPE });

	private static final Method AS_SYMBOL = new Method("asSymbol", STRING_TYPE,
			NO_TYPES);

	private static final Method TO_STRING = new Method("toString", STRING_TYPE,
			NO_TYPES);

	private static final Method FAST_LT_METHOD = new Method("fast_lt",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method FAST_LE_METHOD = new Method("fast_le",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method FAST_MINUS_METHOD = new Method("fast_minus",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method FAST_PLUS_METHOD = new Method("fast_plus",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Type RUBYMETHOD = Type.getType(RubyMethod.class);

	private static final Method METHODACCESSOR_GET = new Method("get",
			RUBYMETHOD, NO_TYPES);

	private static final Method DO_SELECT_METHOD = new Method("do_select",
			RUBYMETHOD, new Type[] { SELECTOR_TYPE });

	private static final Type CONSTANT_ACCESSOR = Type
			.getType(ConstantAccessor.class);

	private static final Method GET_CONSTANT_ACCESSOR = new Method(
			"getConstantAccessor", CONSTANT_ACCESSOR,
			new Type[] { STRING_TYPE });

	private static final Method IVAR_GET = new Method("get", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT });

	private static final Method IVAR_SET = new Method("set", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT, IRUBYOBJECT });

	private static final Method FAST_EQ2_METHOD = new Method("fast_eq2",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method FAST_GT_METHOD = new Method("fast_gt",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method FAST_BIT_XOR_METHOD = new Method(
			"fast_bit_xor", IRUBYOBJECT, new Type[] { IRUBYOBJECT,
					SELECTOR_TYPE });;

	private static final Method FAST_BIT_AND_METHOD = new Method(
			"fast_bit_and", IRUBYOBJECT, new Type[] { IRUBYOBJECT,
					SELECTOR_TYPE });

	private static final Method FAST_BIT_OR_METHOD = new Method("fast_bit_or",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method FAST_BIT_NOT_METHOD = new Method(
			"fast_bit_not", IRUBYOBJECT, new Type[] { SELECTOR_TYPE });

	private static final Method FAST_RSHIFT_METHOD = new Method("fast_rshift",
			IRUBYOBJECT, new Type[] { IRUBYOBJECT, SELECTOR_TYPE });

	private static final Method CHECK_BLOCK_METHOD = new Method("checkBlock",
			Type.VOID_TYPE, new Type[] { RUBYBLOCK, RUBY_RUNTIME_TYPE });

	private static final Type OBJECT_ARR_TYPE = Type.getType(Object[].class);

	private static final Method BLOCK_CALLN = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT_ARR });

	private static final Method BLOCK_CALL0 = new Method("call", IRUBYOBJECT,
			new Type[] {});

	private static final Method BLOCK_CALL1 = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT });

	private static final Method BLOCK_CALL2 = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT, IRUBYOBJECT });

	private static final Type COMPILEDBLOCK0 = Type
			.getType(CompiledBlock0.class);

	private static final Type COMPILEDBLOCK1 = Type
			.getType(CompiledBlock1.class);

	private static final Type COMPILEDBLOCK2 = Type
			.getType(CompiledBlock2.class);

	private static final Type COMPILEDBLOCKN = Type
			.getType(CompiledBlockN.class);

	private static final Type COMPILEDCODEUTIL = Type
			.getType(CompiledCodeUtil.class);

	private static final Method ASSIGN_INTO_CHECK = new Method("assignInto", Type.VOID_TYPE, new Type[] { IRUBYOBJECT });

	private static final Type NONLOCAL_RETURN_EXCEPTION = Type.getType(NonLocalReturn.class);

	private static final Method NONLOCAL_RETURN_CONSTRUCTOR = new Method("<init>", Type.VOID_TYPE, new Type[] { Type.getType(ExposedLocals.class), IRUBYOBJECT } );

	public static final Method GET_DVAR_NAMES = new Method("getDVarNames", STRING_ARRAY_TYPE, new Type[0]);

	private static final Type EXPOSED_LOCALS_TYPE = Type.getType(ExposedLocals.class);

	private static final Type COMPILED_BLOCK_DVARS = Type.getType(CompiledBlockEvalContext.class);

	private static final Type COMPILEDBLOCK = Type.getType(CompiledBlock.class);

	private static final Method COMPILED_BLOCK_DVARS_CONSTRUCTOR = new Method("<init>", Type.VOID_TYPE, new Type[] { COMPILEDBLOCK, EXPOSED_LOCALS_TYPE} );

	private static final Type IRUBYREGEXP = Type.getType(IRubyRegexp.class);

	private static final Method NEW_REGEXP = new Method("newRegexp", IRUBYREGEXP, new Type[]{ STRING_TYPE, Type.INT_TYPE });

	private static final Method NEW_ARRAY_METHOD = new Method("newArray", IRUBYARRAY, new Type[] { IRUBYOBJECT_ARR } );

	private static final Method IRUBYARRAY_INT_AT = new Method("int_at", IRUBYOBJECT, new Type[] { Type.INT_TYPE } );

	private static final Method PROC_TO_BLOCK = new Method("get_block", RUBYBLOCK, new Type[0]);
	
	private final RubyRuntime runtime;

	private Type self_type;

	private Type super_class;

	private MethodCompiler method_context;

	static GeneratorAdapter begin_method(ClassVisitor cv, int access,
			Method method) {
		MethodVisitor mv = cv.visitMethod(access, method.getName(), method
				.getDescriptor(), null, EMPTY_STRING_ARRAY);
		GeneratorAdapter ga = new GeneratorAdapter(access, method, mv);
		return ga;
	}

	public MethodCompiler(RubyRuntime runtime) {
		this.runtime = runtime;
		this.method_context = this;
	}

	/** constructor for a block compiler */
	public MethodCompiler(RubyRuntime runtime, MethodCompiler compiler) {
		this.runtime = runtime;
		this.method_context = compiler;
	}

	static int counter = 0;

	static synchronized int nextCount() {
		return counter++;
	}

	Type[] cpool_types;

	private BindingContext bound_method;

	public RubyMethod compile(MethodISeq miseq) {
		bound_method = miseq.binding();
		ISeq iseq = bound_method.getISeq();
		// MetaModule dyn_meta = bound.getDynamicContext();
		MetaModule lex_meta = bound_method.getLexicalContext();
		String packageName = lex_meta.getPackageName();
		String className = "Compiled$"
				+ runtime.encodeMethodName(miseq.getName()) + "$" + nextCount();
		String fullName = "com.trifork.hotruby.compiled." + packageName
				+ className;

		this.self_type = Type.getType("L" + fullName.replace('.', '/') + ";");

		ClassWriter cw = new ClassWriter(true);

		Method impl_method;
		int code_args = iseq.code_args;
		switch (code_args) {
		case -1:
			super_class = COMPILEDMETHODN;
			impl_method = CALLN;
			break;
		case 0:
			super_class = COMPILEDMETHOD0;
			impl_method = CALL0;
			break;
		case 1:
			super_class = COMPILEDMETHOD1;
			impl_method = CALL1;
			break;
		case 2:
			super_class = COMPILEDMETHOD2;
			impl_method = CALL2;
			break;
		default:
			throw new InternalError();
		}

		cw.visit(V1_4, ACC_PUBLIC, self_type.getInternalName(), null,
				super_class.getInternalName(), new String[0]);

		cw.visitSource(iseq.getFile(), null);

		gen_constructor(cw, bound_method);
		// gen_clinit(cw, bound);

		GeneratorAdapter arr = begin_method(cw, ACC_PUBLIC, GET_ARITY);
		arr.push(miseq.getArity());
		arr.returnValue();
		arr.endMethod();
		
		gen_get_dvar_names(cw, iseq);

		MethodVisitor call = cw.visitMethod(ACC_PUBLIC, impl_method.getName(),
				impl_method.getDescriptor(), null, new String[0]);

		RubyAdapter ra = new RubyAdapter(call, ACC_PUBLIC, impl_method
				.getName(), impl_method.getDescriptor());
		gen_code(ra, iseq, bound_method, self_type, bound_method.selfIsModule(), false);
		ra.visitMaxs(0, 0);
		ra.visitEnd();

		cw.visitEnd();

		byte[] bytes = cw.toByteArray();

		try {
			String name = fullName + ".rclass";
			FileOutputStream fo = new FileOutputStream(name);
			fo.write(bytes);
			fo.close();
			System.out.println("wrote " + name);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		Class bc = runtime.getClassLoader().doDefineClass(fullName, bytes);
		try {
			Constructor cons = bc.getConstructor(new Class[] {
					MetaModule.class, MetaModule.class });
			return (RubyMethod) cons.newInstance(new Object[] { lex_meta,
					bound_method.getDynamicContext() });
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void gen_get_dvar_names(ClassWriter cw, ISeq iseq) {
		GeneratorAdapter dv = begin_method(cw, ACC_PUBLIC, GET_DVAR_NAMES);

		String[] dvars = iseq.getDVarNames();
		if (dvars.length == 0) {
			dv.getStatic(COMPILEDMETHOD, "EMPTY_STRING_ARRAY", STRING_ARRAY_TYPE);
		} else {
			dv.getStatic(self_type, "DVAR_NAMES", STRING_ARRAY_TYPE);
		}
		
		dv.returnValue();
		
		dv.endMethod();
	}

	String selfSendMethodAccessorName(int idx) {
		return "selfsend_" + idx;
	}

	void emit_load_runtime(GeneratorAdapter ga) {
		ga.getStatic(LOADED_RUBY_RUNTIME, "instance", LOADED_RUBY_RUNTIME);
	}

	private void gen_constructor(ClassWriter cw, BindingContext ctx) {
		GeneratorAdapter ga = begin_method(cw, ACC_PUBLIC, METHOD_CONSTRUCTOR);
		ga.loadThis();
		ga.invokeConstructor(super_class, NOARG_CONSTRUCTOR);

		String[] dvar_names = ctx.getISeq().getDVarNames();
		if (dvar_names.length != 0) {
			ga.push(dvar_names.length);
			ga.newArray(STRING_TYPE);
			
			for (int i = 0; i < dvar_names.length; i++) {
				ga.dup();
				ga.push(i);
				ga.push(dvar_names[i]);
				ga.arrayStore(STRING_TYPE);
			}
			
			cw.visitField(ACC_STATIC|ACC_PROTECTED, "DVAR_NAMES", STRING_ARRAY_TYPE.getDescriptor(), null, null).visitEnd();
			ga.putStatic(self_type, "DVAR_NAMES", STRING_ARRAY_TYPE);
		}
		
		// prepare method accessors for self-methods
		String[] self_methods = ctx.getISeq().getSelfMethods();
		for (int i = 0; i < self_methods.length; i++) {

			String field_name = selfSendMethodAccessorName(i);
			FieldVisitor fv = cw.visitField(ACC_STATIC, field_name,
					METHODACCESSOR_TYPE.getDescriptor(), null, null);
			fv.visitEnd();

			ga.loadArg(1); // dyn ctx
			ga.push(self_methods[i]);
			ga.push(ctx.selfIsModule());
			ga.invokeVirtual(METAMODULE_TYPE, GET_METHOD_ACCESSOR);

			ga.putStatic(self_type, field_name, METHODACCESSOR_TYPE);

		}

		RubyIvarAccessor[] ivars = ctx.getIVarAccessors();
		if (ivars == null) {
			System.out.println("ivars is null!");
		}
		for (int i = 0; i < ivars.length; i++) {
			if (ivars[i].isCompiled()) {
				continue;
			}

			String field_name = ivarAccessorName(i);
			FieldVisitor fv = cw.visitField(ACC_STATIC, field_name,
					RUBY_IVAR_ACCESSOR_TYPE.getDescriptor(), null, null);
			fv.visitEnd();

			ga.loadArg(1);
			ga.push(ivars[i].getName());

			if (ctx.selfIsModule()) {
				ga.invokeVirtual(METAMODULE_TYPE, GET_MODULE_IVAR_ACCESSOR);
			} else {
				ga.invokeVirtual(METAMODULE_TYPE, GET_INSTANCE_IVAR_ACCESSOR);
			}

			ga.putStatic(self_type, field_name, RUBY_IVAR_ACCESSOR_TYPE);
		}

		// prepare the constantpool
		Object[] cpool = ctx.getISeq().getCPool();
		this.cpool_types = new Type[cpool.length];
		for (int i = 0; i < cpool.length; i++) {
			Object val = cpool[i];

			if (val instanceof ISeq) {
				// we handle this elsewhere
				continue;
			}

			Type const_type = construct_cpool_value(ga, val);

			FieldVisitor fv = cw.visitField(ACC_STATIC, getConstPoolName(i),
					const_type.getDescriptor(), null, null);
			fv.visitEnd();

			cpool_types[i] = const_type;
			ga.putStatic(self_type, getConstPoolName(i), const_type);
		}

		String[] consts = ctx.getISeq().getConstants();
		for (int i = 0; i < consts.length; i++) {

			String field_name = getConstName(i);
			FieldVisitor fv = cw.visitField(ACC_STATIC, field_name,
					CONSTANT_ACCESSOR.getDescriptor(), null, null);
			fv.visitEnd();

			ga.loadArg(0); // lex
			ga.push(consts[i]);
			ga.invokeVirtual(METAMODULE_TYPE, GET_CONSTANT_ACCESSOR);

			ga.putStatic(self_type, field_name, CONSTANT_ACCESSOR);
		}

		String[] globals = ctx.getGlobals();
		for (int i = 0; i < globals.length; i++) {

			String field_name = globalName(i);
			FieldVisitor fv = cw.visitField(ACC_STATIC, field_name, GLOBAL_TYPE
					.getDescriptor(), null, null);
			fv.visitEnd();

			emit_load_runtime(ga);
			ga.push(globals[i]);
			ga.invokeVirtual(RUBY_RUNTIME_TYPE, GET_GLOBAL);

			ga.putStatic(self_type, field_name, GLOBAL_TYPE);
		}

		ga.returnValue();
		ga.endMethod();

	}

	private String ivarAccessorName(int i) {
		return "ivar_access_" + i;
	}

	private String getConstName(int i) {
		return "const_" + i;
	}

	private String globalName(int i) {
		return "global_" + i;
	}

	private Type construct_cpool_value(GeneratorAdapter ga, Object val) {
		if (val instanceof IRubyString) {
			String s = ((IRubyString) val).asSymbol();
			emit_load_runtime(ga);
			ga.push(s);
			ga.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_STRING);
			return IRUBYSTRING; // todo: can be made exact

		} else if (val instanceof IRubyFixnum) {
			int ival = ((IRubyFixnum) val).intValue();
			emit_load_runtime(ga);
			ga.push(ival);
			ga.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_FIXNUM);
			ga.checkCast(RUBYFIXNUM);
			return RUBYFIXNUM;

		} else if (val instanceof IRubyFloat) {
			double dval = ((IRubyFloat) val).value();
			emit_load_runtime(ga);
			ga.push(dval);
			ga.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_FLOAT);
			return IRUBYFLOAT;

		} else if (val instanceof IRubySymbol) {
			String sval = ((IRubySymbol) val).asSymbol();
			emit_load_runtime(ga);
			ga.push(sval);
			ga.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_SYMBOL);
			return IRUBYSYMBOL;

		} else if (val instanceof IRubyRegexp) {
			String sval = ((IRubyRegexp) val).asSymbol();
			emit_load_runtime(ga);
			ga.push(sval);
			ga.push(((IRubyRegexp)val).flags());
			ga.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_REGEXP);
			return IRUBYSYMBOL;

		} else if (val instanceof IRubyNilClass) {
			ga.getStatic(LOADED_RUBY_RUNTIME, "NIL", RUBYNILCLASS);
			return RUBYNILCLASS;

		} else if (val instanceof IRubyTrueClass) {
			ga.getStatic(LOADED_RUBY_RUNTIME, "TRUE", RUBYTRUECLASS);
			return RUBYTRUECLASS;

		} else if (val instanceof IRubyFalseClass) {
			ga.getStatic(LOADED_RUBY_RUNTIME, "FALSE", RUBYFALSECLASS);
			return RUBYFALSECLASS;

		} else {
			throw new InternalError("unhandled const pool item: "
					+ val.getClass());
		}
	}

	private String getConstPoolName(int i) {
		return "cpool_" + i;
	}

	private void gen_code(RubyAdapter call, ISeq iseq, BindingContext ctx,
			Type compiled_self_type, boolean self_is_module, boolean context_has_dvars) {

		boolean is_static = false;

		Type type_of_self = get_type_of_self(ctx.getDynamicContext(),
				self_is_module);

		// this is the numbr of locals used by RBC 0..N-1
		int num_code_locals = iseq.getNumLocals();
		int locals_in_use = num_code_locals + 1;

		// _idx refers to java-level local index
		// _loc refers to ruby-level local index

		int this_method_idx = is_static ? -1 : 0;
		int local0_idx = this_method_idx + 1;
		int self_idx = (iseq.getCodeType() == ISEQ_TYPE_BLOCK ? -1 : local0_idx);
		int first_parm_idx = (iseq.getCodeType() == ISEQ_TYPE_BLOCK ? local0_idx
				: local0_idx + 1);

		int block_idx = -1;

		if (iseq.getCodeType() != ISEQ_TYPE_BLOCK) {
			if (iseq.code_args == -1) {
				block_idx = first_parm_idx + 1;
			} else {
				block_idx = first_parm_idx + iseq.code_args;
			}
		}

		int result_loc_idx = locals_in_use++;

		// emit_load_nil(call);
		// call.rbStoreLocal(result_loc_idx, IRUBYOBJECT);

		int dvars_idx = -1;

		// prepare evalstate
		if (iseq.code_calls_eval || iseq.num_dynamics > 0 
			|| context_has_dvars) {
			dvars_idx = locals_in_use++;

			if (iseq.getCodeType() == ISEQ_TYPE_BLOCK) {
				call.newInstance(COMPILED_BLOCK_DVARS);
				call.dup();
				
				call.loadThis();
				
				if (context_has_dvars) {
					call.loadThis();
					call.getField(compiled_self_type, "dvars", EXPOSED_LOCALS_TYPE);
					
					call.invokeConstructor(COMPILED_BLOCK_DVARS, COMPILED_BLOCK_DVARS_CONSTRUCTOR);
				} else {
					assert false;
				}
			} else {
				call.newInstance(COMPILED_METHOD_DVARS);
				call.dup();
				call.rbLoadLocal(1, IRUBYOBJECT);
				call.rbLoadLocal(0, self_type);
				
				call.rbLoadLocal(block_idx, RUBYBLOCK);
				call.invokeConstructor(COMPILED_METHOD_DVARS,
						COMPILED_METHOD_DVARS_CONSTRUCTOR);
			}
			call.rbStoreLocal(dvars_idx, COMPILED_METHOD_DVARS);
		}

		// generate arg handing

		int parms_size = 1 // / receiver (loc0 is always used)
				+ iseq.min_parm_count // normal args
				+ iseq.default_parm_count // default parms
				+ (iseq.has_block_parm ? 1 : 0) // block parm
				+ (iseq.has_rest_parm ? 1 : 0); // rest parm

		int real_given_args_count_idx = -1;

		boolean[] handled_dynamics = new boolean[iseq.num_dynamics];

		if (iseq.getCodeType() == ISEQ_TYPE_METHOD) {

			if (iseq.code_args == -1) {
				// varargs call
				real_given_args_count_idx = locals_in_use++;
				call.rbLoadLocal(first_parm_idx, IRUBYOBJECT_ARR);
				call.dup();
				call.arrayLength();
				call.rbStoreLocal(real_given_args_count_idx, Type.INT_TYPE);

				// check arg count
				check_varg_count(call, iseq, real_given_args_count_idx);

				// save block arg in new register
				call.rbLoadLocal(block_idx, RUBYBLOCK);
				block_idx = locals_in_use++;
				call.rbStoreLocal(block_idx, RUBYBLOCK);

				// expand args
				int parm_loc = 1;
				int arg_num = 0;
				for (int i = 0; i < iseq.min_parm_count; i++) {
					call.dup();
					call.push(i);
					call.arrayLoad(IRUBYOBJECT);

					set_parm(handled_dynamics, call, iseq, dvars_idx, self_idx,
							parm_loc++);
					arg_num++;
				}

				// expand default parms (unroll loop)
				for (int i = 0; i < iseq.default_parm_count; i++) {

					call.push(arg_num);
					call.rbLoadLocal(real_given_args_count_idx, Type.INT_TYPE);
					Label put_null_in_default_arg = call.newLabel();
					Label repeat_loop = call.newLabel();
					call.ifICmp(GeneratorAdapter.GE, put_null_in_default_arg);

					call.dup(); // dup the IRubyObject[]
					call.push(arg_num);
					call.arrayLoad(IRUBYOBJECT);

					set_parm(handled_dynamics, call, iseq, dvars_idx, self_idx,
							parm_loc);

					call.goTo(repeat_loop);

					call.mark(put_null_in_default_arg);
					emit_load_nil(call);
					set_parm(handled_dynamics, call, iseq, dvars_idx, self_idx,
							parm_loc);

					call.mark(repeat_loop);
					arg_num++;
					parm_loc++;
				}

				if (iseq.has_block_parm) {
					emit_load_runtime(call);
					call.rbLoadLocal(block_idx, RUBYBLOCK);
					call.invokeVirtual(LOADED_RUBY_RUNTIME, NEW_PROC);
					set_parm(handled_dynamics, call, iseq, dvars_idx, self_idx,
							parm_loc++);
					arg_num++;
				}

				if (iseq.has_rest_parm) {
					call.push(arg_num);
					emit_load_runtime(call);
					call.invokeStatic(COMPILEDCODEUTIL, JAVA_ARRAY_REST_METHOD);
					set_parm(handled_dynamics, call, iseq, dvars_idx, self_idx,
							parm_loc++);
				} else {
					call.pop(); // pop IRubyObject[]
				}

			} else if (iseq.has_block_parm) {
				emit_load_runtime(call);
				call.rbLoadLocal(block_idx, RUBYBLOCK);
				call.invokeVirtual(LOADED_RUBY_RUNTIME, NEW_PROC);
				set_parm(handled_dynamics, call, iseq, dvars_idx, self_idx,
						iseq.code_args + 1);
			}

		} else {
			// this is a block or something else, we should not need
			// to do anything

		}

		// initialize locals beyond parms
		for (int i = parms_size + 1; i < iseq.getNumLocals(); i++) {
			emit_load_nil(call);
			set_parm(handled_dynamics, call, iseq, dvars_idx, local0_idx, i);
		}

		// generate method body

		Map<Integer, Label> labels = new HashMap<Integer, Label>();

		if (iseq.getCodeType() == ISEQ_TYPE_METHOD
				&& iseq.default_parm_count > 0) {

			if (iseq.code_args == -1) {
				call.push(iseq.default_parm_count);
				call.rbLoadLocal(real_given_args_count_idx, Type.INT_TYPE);
				call.push(iseq.min_parm_count);
				call.math(ISUB, Type.INT_TYPE);
				call.invokeStatic(MATH_TYPE, MATH_MIN_INT);
			} else {
				call.push(Math.min(iseq.default_parm_count, iseq.code_args
						- iseq.min_parm_count));
			}

			Label[] default_labels = new Label[iseq.default_parm_count + 1];
			for (int i = 0; i < default_labels.length; i++) {
				Label l = call.newLabel();
				labels.put(new Integer(iseq.eval_default_args[i]), l);
				default_labels[i] = l;
			}

			call.visitTableSwitchInsn(0, iseq.default_parm_count,
					default_labels[0], default_labels);

		}

		byte[] code = iseq.getCode();
		int pc = 0;
		int opcode = -1;
		int insn_pc = -1;

		next_insn: while (pc < code.length) {
			int last_opcode = opcode;
			int last_insn_pc = insn_pc;

			Label l = labels.get(new Integer(pc));
			if (l != null) {
				call.mark(l);
			} else {
				labels.put(new Integer(pc), l = call.mark());
			}

			call.visitLineNumber(pc, l);

			switch (opcode = code[insn_pc = pc++]) {

			case Instructions.TRACE: {
				int kind = code[pc++];
				int line = ui(code[pc++], code[pc++]);
				// call.visitLineNumber(line, call.mark());
				continue next_insn;
			}

				// case Instructions.NOP: {
				// call.visitInsn(Opcodes.NOP);
				// continue next_insn;
				// }

			case Instructions.SWAP: {
				call.swap();
				continue next_insn;
			}

			case Instructions.POP: {
				call.pop();
				continue next_insn;
			}

			case Instructions.DUP: {
				call.dup();
				continue next_insn;
			}

			case Instructions.PUSHNIL: {
				emit_load_nil(call);
				continue next_insn;
			}

			case SETLOCAL: {
				call.rbStoreLocal(local0_idx + ui(code[pc++]), IRUBYOBJECT);
				continue next_insn;
			}

			case GETLOCAL: {
				call.rbLoadLocal(local0_idx + ui(code[pc++]), IRUBYOBJECT);
				continue next_insn;
			}

			case SETGLOBAL: {
				call.getStatic(self_type, globalName(code[pc++]), GLOBAL_TYPE);
				call.swap();
				call.invokeVirtual(GLOBAL_TYPE, SET_TAKING_OBJECT);
				continue next_insn;
			}

			case GETGLOBAL: {
				call.getStatic(self_type, globalName(code[pc++]), GLOBAL_TYPE);
				call.invokeVirtual(GLOBAL_TYPE, GET_RETURNING_OBJECT);
				continue next_insn;
			}

			case SETCONSTANT: {
				call.getStatic(self_type, getConstName(ui(code[pc++],
						code[pc++])), CONSTANT_ACCESSOR);
				call.swap();
				call.invokeVirtual(CONSTANT_ACCESSOR, SET_TAKING_OBJECT);
				continue next_insn;
			}

			case GETCONSTANT: {
				call.getStatic(self_type, getConstName(ui(code[pc++],
						code[pc++])), CONSTANT_ACCESSOR);
				call.invokeVirtual(CONSTANT_ACCESSOR, GET_RETURNING_OBJECT);
				continue next_insn;
			}

			case SETINSTANCEVARIABLE: {
				int idx = ui(code[pc++], code[pc++]);

				RubyIvarAccessor acc = ctx.getIVarAccessors()[idx];
				if (acc.isCompiled()) {
					call.dup();
					emit_load_self(call, iseq, compiled_self_type,
							type_of_self, self_idx);
					call.invokeInterface(IRUBYOBJECT, ASSIGN_INTO_CHECK);
					emit_load_self(call, iseq, compiled_self_type,
							type_of_self, self_idx);
					call.swap();
					call.putField(type_of_self, acc.getFieldName(),
									IRUBYOBJECT);
				} else {
					int value_loc = locals_in_use++;
					call.rbStoreLocal(value_loc, IRUBYOBJECT);
					call.getStatic(self_type, ivarAccessorName(idx),
							RUBY_IVAR_ACCESSOR_TYPE);
					emit_load_self(call, iseq, compiled_self_type,
							type_of_self, self_idx);
					call.rbLoadLocal(value_loc, IRUBYOBJECT);
					call.invokeVirtual(RUBY_IVAR_ACCESSOR_TYPE, IVAR_SET);

					locals_in_use--;
				}

				continue next_insn;

			}

			case GETINSTANCEVARIABLE: {
				int idx = ui(code[pc++], code[pc++]);

				RubyIvarAccessor acc = ctx.getIVarAccessors()[idx];
				if (acc.isCompiled()) {
					emit_load_self(call, iseq, compiled_self_type,
							type_of_self, self_idx);
					call
							.getField(type_of_self, acc.getFieldName(),
									IRUBYOBJECT);
					call.dup();
					Label ok = call.newLabel();
					call.ifNonNull(ok);
					call.pop();
					emit_load_nil(call);
					call.mark(ok);
				} else {
					call.getStatic(self_type, ivarAccessorName(idx),
							RUBY_IVAR_ACCESSOR_TYPE);
					emit_load_self(call, iseq, compiled_self_type,
							type_of_self, self_idx);
					call.invokeVirtual(RUBY_IVAR_ACCESSOR_TYPE, IVAR_GET);
				}

				continue next_insn;
			}

			case GETDYNAMIC: {
				int level = ui(code[pc++]);
				int idx = ui(code[pc++]);

				if (iseq.getCodeType() == ISEQ_TYPE_METHOD) {
					assert level == 0;
					call.rbLoadLocal(dvars_idx, COMPILED_METHOD_DVARS);
					call.push(idx);
					call.invokeVirtual(COMPILED_METHOD_DVARS, GET_DVAR_IDX);
				} else {
					assert dvars_idx != -1;
					call.rbLoadLocal(dvars_idx, COMPILED_METHOD_DVARS);
					call.push(level);
					call.push(idx);
					call.invokeVirtual(EXPOSED_LOCALS_TYPE, GET_DYNAMIC_IDX);
					call.checkCast(IRUBYOBJECT);
				}

				continue next_insn;
			}

			case SETDYNAMIC: {
				int level = ui(code[pc++]);
				int idx = ui(code[pc++]);

				if (iseq.getCodeType() == ISEQ_TYPE_METHOD) {
					assert level == 0;
					call.rbLoadLocal(dvars_idx, COMPILED_METHOD_DVARS);
					call.push(idx);
					call.invokeStatic(COMPILED_METHOD_DVARS, SET_DVAR);
				} else {
					assert (false);
				}

				continue next_insn;
			}

			case BRANCHUNLESS: {
				int offset = si(code[pc++], code[pc++]);
				call.invokeInterface(IRUBYOBJECT, IS_FALSE);
				int target = pc - 3 + offset;
				Label t = getLabel(labels, target, call);
				call.ifZCmp(call.NE, t);
				continue next_insn;
			}

			case BRANCHIF: {
				int offset = si(code[pc++], code[pc++]);
				call.invokeInterface(IRUBYOBJECT, IS_TRUE);
				int target = pc - 3 + offset;
				Label t = getLabel(labels, target, call);
				call.ifZCmp(call.NE, t);
				continue next_insn;
			}

			case JUMP: {
				int offset = si(code[pc++], code[pc++]);
				int target = pc - 3 + offset;
				Label t = getLabel(labels, target, call);
				call.goTo(t);
				continue next_insn;
			}
			
			case INTERNAL_TO_A: {
				call.dup();
				call.instanceOf(IRUBYARRAY);
				Label ok = call.newLabel();
				call.ifZCmp(call.NE, ok);

				call.push(1);
				call.newArray(IRUBYOBJECT);
				int arr_pos = locals_in_use++;
				call.rbStoreLocal(arr_pos, IRUBYOBJECT_ARR);
				
				pop_args_into(call, 1, arr_pos);

				emit_load_runtime(call);
				call.rbLoadLocal(arr_pos, IRUBYOBJECT_ARR);
				call.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_ARRAY_METHOD);
				
				call.mark(ok);
				continue next_insn;
			}

			case ARRAY_AT: {
				int idx = code[pc++];
				call.dup();
				//call.checkCast(IRUBYARRAY);
				call.push(idx);
				call.invokeInterface(IRUBYARRAY, IRUBYARRAY_INT_AT);
				continue next_insn;
			}

			case PROC2BLOCK: {
				call.checkCast(IRUBYPROC);
				call.invokeInterface(IRUBYPROC, PROC_TO_BLOCK);
				continue next_insn;
			}
			
			// arg1, arg2, ..., argN, (rest[M])? -> NEWARRAY(N, rest?)
			// -> array[N+M]
		case Instructions.NEWARRAY: {
			int size = code[pc++];
			boolean has_rest_arg = (code[pc++] == 1);

			// System.out.println("NEWARRAY " + size + ", " +
			// has_rest_arg);

			int all_args_idx = locals_in_use++;

			if (has_rest_arg) {
				assert false;
				/*
				IRubyObject[] extra = (IRubyObject[]) stack[--sp];
				if (size == 0) {
					arr = getRuntime().newArray(extra);
				} else {
					arr = getRuntime().newArray(size + extra.length);
					for (int i = 0; i < extra.length; i++) {
						arr.int_at_put(size + i, extra[i]);
					}
				}
				*/
			} else {
				call.push(size);
				call.newArray(IRUBYOBJECT);

				call.rbStoreLocal(all_args_idx, IRUBYOBJECT_ARR);

				pop_args_into(call, size, all_args_idx);
			}

			emit_load_runtime(call);
			call.rbLoadLocal(all_args_idx, IRUBYOBJECT_ARR);
			call.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_ARRAY_METHOD);
			
			continue next_insn;
		}


			case INVOKEBLOCK: {

				int save = locals_in_use;

				// check that block is non-null
				call.rbLoadLocal(block_idx, RUBYBLOCK);
				emit_load_runtime(call);
				call.invokeStatic(COMPILEDCODEUTIL, CHECK_BLOCK_METHOD);

				int flags = code[pc++];
				int nargs = ui(code[pc++]); // number of known args at call-site

				int all_args_idx = -1;

				if ((flags & FLAG_REST_ARG) == FLAG_REST_ARG) {

					if (nargs == 0) {
						call.rbLoadLocal(block_idx, RUBYBLOCK);
						call.swap();
						call.invokeVirtual(RUBYBLOCK, BLOCK_CALLN);
					} else {

						int size_idx = locals_in_use++;
						all_args_idx = locals_in_use++;

						// arg1, arg2, .., argN, [argN+1, ...., argM]

						// rest
						call.dup();
						// rest rest
						call.arrayLength();
						// rest rest.length
						call.dup();
						// rest rest.length rest.length
						call.rbStoreLocal(size_idx, Type.INT_TYPE);
						// rest rest.length
						call.push(nargs);
						// rest rest.length nargs
						call.math(call.ADD, Type.INT_TYPE);
						// rest (rest.length+nargs)
						call.newArray(IRUBYOBJECT);
						// rest all_args
						call.rbStoreLocal(all_args_idx, IRUBYOBJECT_ARR);

						// rest
						call.push(0);
						// rest 0
						call.rbLoadLocal(all_args_idx, IRUBYOBJECT_ARR);
						// rest 0 all_args
						call.push(nargs);
						// rest 0 all_args nargs
						call.rbLoadLocal(size_idx, Type.INT_TYPE);
						// rest 0 all_args nargs rest.length
						call
								.invokeStatic(Type.getType(System.class),
										new Method("arraycopy", Type.VOID_TYPE,
												new Type[] { OBJECT_ARR_TYPE,
														Type.INT_TYPE,
														OBJECT_ARR_TYPE,
														Type.INT_TYPE,
														Type.INT_TYPE }));
						// -

						pop_args_into(call, nargs, all_args_idx);

						call.rbLoadLocal(block_idx, RUBYBLOCK);
						call.rbLoadLocal(all_args_idx, IRUBYARRAY);
						call.invokeVirtual(RUBYBLOCK, BLOCK_CALLN);
					}

				} else {

					switch (nargs) {
					case 0:
						call.rbLoadLocal(block_idx, RUBYBLOCK);
						call.invokeVirtual(RUBYBLOCK, BLOCK_CALL0);
						break;

					case 1:
						call.rbLoadLocal(block_idx, RUBYBLOCK);
						call.swap();
						call.invokeVirtual(RUBYBLOCK, BLOCK_CALL1);
						break;

					case 2: {
						int arg2_idx = locals_in_use++;
						call.rbStoreLocal(arg2_idx, IRUBYOBJECT);

						call.rbLoadLocal(block_idx, RUBYBLOCK);
						call.swap();
						call.rbLoadLocal(arg2_idx, IRUBYOBJECT);

						call.invokeVirtual(RUBYBLOCK, BLOCK_CALL2);
					}
						break;
					default:
						call.push(nargs);
						call.newArray(IRUBYOBJECT);

						all_args_idx = locals_in_use++;
						call.rbStoreLocal(all_args_idx, IRUBYOBJECT_ARR);

						pop_args_into(call, nargs, all_args_idx);

						call.invokeVirtual(RUBYBLOCK, BLOCK_CALLN);
					}

				}

				if ((flags & FLAG_PUSH_RESULT) == 0) {
					call.pop();
				}

				locals_in_use = save;

				continue next_insn;
			}

			case SELFSEND:
			case SEND: {
				int save = locals_in_use;

				// special flags
				int flags = code[pc++];

				// compile-time known args (N in the above)
				int nargs = ui(code[pc++]);

				// the selector sits here...
				int selector_pos = ui(code[pc++], code[pc++]);

				int imm_block_pos = ui(code[pc++], code[pc++]);

				boolean has_amp_block_arg = (flags & FLAG_BLOCK_ARG) != 0;

				int call_block_idx = -1;

				if (has_amp_block_arg) {
					call_block_idx = locals_in_use++;
					call.rbStoreLocal(call_block_idx, RUBYBLOCK);
				}

				// proc.new handling missing

				Type block_type = null;
				Method block_init_method = null;
				if (imm_block_pos != 0) {
					ISeq block_iseq = (ISeq) iseq.getCPool()[imm_block_pos];
					block_type = compile_block(block_iseq, dvars_idx != -1);
					block_init_method = (dvars_idx == -1) 
						? new Method("<init>", Type.VOID_TYPE,
							new Type[] { IRUBYOBJECT, RUBYBLOCK })
					    : new Method("<init>", Type.VOID_TYPE,
								new Type[] { IRUBYOBJECT, RUBYBLOCK, EXPOSED_LOCALS_TYPE });
				}

				Label handler_start = call.mark();

				if ((flags & FLAG_REST_ARG) == FLAG_REST_ARG) {
					throw new InternalError("cannot compile code with *arg");
				} else {

					int last_arg_loc = -1;
					int all_arg_loc = -1;
					if (nargs > 2) {
						call.push(nargs);
						call.newArray(IRUBYOBJECT);
						all_arg_loc = locals_in_use++;
						call.rbStoreLocal(all_arg_loc, IRUBYOBJECT_ARR);

						pop_args_into(call, nargs, all_arg_loc);
					} else {

						// pop all arguments into locals
						last_arg_loc = locals_in_use;
						for (int i = 0; i < nargs; i++) {
							int save_pos = locals_in_use++;
							// System.out.println("saving to " + save_pos);
							call.rbStoreLocal(save_pos, IRUBYOBJECT);
						}
					}

					int receiver_loc = locals_in_use++;

					// now receiver is on stack

					if (opcode == SELFSEND) {
						call.rbStoreLocal(receiver_loc, IRUBYOBJECT);
						call.getStatic(self_type,
								selfSendMethodAccessorName(selector_pos),
								METHODACCESSOR_TYPE);
						call.invokeVirtual(METHODACCESSOR_TYPE,
								METHODACCESSOR_GET);
						call.rbLoadLocal(receiver_loc, IRUBYOBJECT);
					} else {
						call.dup(); // dup receiver for do_select
						call.rbStoreLocal(receiver_loc, IRUBYOBJECT);
						emit_push_selector(call, selector_pos);
						call.invokeInterface(IRUBYOBJECT, DO_SELECT_METHOD);
						call.rbLoadLocal(receiver_loc, IRUBYOBJECT);
					}

					if (last_arg_loc != -1) {
						for (int i = 0; i < nargs; i++) {
							int restore_pos = last_arg_loc + nargs - i - 1;
							// System.out.println("restoring from " +
							// restore_pos);
							call.rbLoadLocal(restore_pos, IRUBYOBJECT);
						}
					} else {
						call.rbLoadLocal(all_arg_loc, IRUBYOBJECT_ARR);
					}

					if (call_block_idx != -1) {
						call.rbLoadLocal(call_block_idx, RUBYBLOCK);
					} else if (block_type != null) {

						// instantiate block
						call.newInstance(block_type);
						call.dup();

						emit_load_self(call, iseq, compiled_self_type,
								type_of_self, self_idx);

						call.rbLoadLocal(block_idx, RUBYBLOCK);
						
						if (dvars_idx != -1) {
							call.rbLoadLocal(dvars_idx, EXPOSED_LOCALS_TYPE);
						}

						call.invokeConstructor(block_type, block_init_method);

					} else {
						call.push((String) null);
					}

					switch (nargs) {
					case 0:
						call.invokeVirtual(RUBYMETHOD, CALL0);
						break;
					case 1:
						assert (last_arg_loc != -1);
						call.invokeVirtual(RUBYMETHOD, CALL1);
						break;
					case 2:
						assert (last_arg_loc != -1);
						call.invokeVirtual(RUBYMETHOD, CALL2);
						break;
					default:
						assert (all_arg_loc != -1);
						call.invokeVirtual(RUBYMETHOD, CALLN);
					}

					if ((flags & FLAG_PUSH_RESULT) == 0) {
						call.pop();
					}
				}

				locals_in_use = save;
				continue next_insn;
			}

			case PUSHOBJECT: {
				int op1 = ui(code[pc++]);
				if (self_type == null || cpool_types == null) {
					System.out.print("");
				}
				call.getStatic(self_type, getConstPoolName(op1),
						cpool_types[op1]);
				continue next_insn;
			}

			case PUSHOBJECT2: {
				int op1 = ui(code[pc++], code[pc++]);
				call.getStatic(self_type, getConstPoolName(op1),
						cpool_types[op1]);
				continue next_insn;
			}

			case CONCATSTRINGS: {
				int howmany = ui(code[pc++]);

				call.newInstance(STRING_BUILDER);
				call.dup();
				call.invokeConstructor(STRING_BUILDER, NOARG_CONSTRUCTOR);
				int save = locals_in_use;

				int sb = locals_in_use++;
				call.rbStoreLocal(sb, STRING_BUILDER);

				for (int i = 0; i < howmany; i++) {
					call.rbStoreLocal(locals_in_use++, IRUBYOBJECT);
				}

				emit_load_runtime(call);
				call.rbLoadLocal(sb, STRING_BUILDER);

				for (int i = 0; i < howmany; i++) {
					call.rbLoadLocal(--locals_in_use, IRUBYOBJECT);
					call.cast(IRUBYOBJECT, IRUBYSTRING);
					call.invokeInterface(IRUBYSTRING, AS_SYMBOL);
					call.invokeVirtual(STRING_BUILDER, APPEND_STRING);
				}

				call.invokeVirtual(STRING_BUILDER, TO_STRING);

				call.invokeVirtual(RUBY_RUNTIME_TYPE, NEW_STRING);

				locals_in_use = save;
				continue next_insn;
			}

			case PUSHSELF: {
				if (self_idx != -1) {
					call.rbLoadLocal(self_idx, IRUBYOBJECT);
				} else {
					if (iseq.getCodeType() == ISEQ_TYPE_BLOCK) {
						call.loadThis();
						call.getField(compiled_self_type, "self", type_of_self);
					} else {
						// in blocks, we get self from a field
						assert (false);
					}
				}
				continue next_insn;
			}

			case LEAVE: {
				call.returnValue();
				continue next_insn;
			}

			case FAST_LT: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_LT_METHOD);
				continue next_insn;
			}

			case FAST_LE: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_LE_METHOD);
				continue next_insn;
			}

			case FAST_PLUS: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_PLUS_METHOD);
				continue next_insn;
			}

			case FAST_MINUS: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_MINUS_METHOD);
				continue next_insn;
			}

			case FAST_EQ2: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_EQ2_METHOD);
				continue next_insn;
			}

			case FAST_GT: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_GT_METHOD);
				continue next_insn;
			}

			case FAST_BIT_XOR: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_BIT_XOR_METHOD);
				continue next_insn;
			}

			case FAST_BIT_AND: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_BIT_AND_METHOD);
				continue next_insn;
			}

			case FAST_BIT_OR: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_BIT_OR_METHOD);
				continue next_insn;
			}

			case FAST_BIT_NOT: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_BIT_NOT_METHOD);
				continue next_insn;
			}

			case FAST_RSHIFT: {
				int sel_idx = ui(code[pc++], code[pc++]);
				emit_push_selector(call, sel_idx);
				call.invokeInterface(IRUBYOBJECT, FAST_RSHIFT_METHOD);
				continue next_insn;
			}

			// a non-local return
			case Instructions.RETURN: {
				
				int result_idx = locals_in_use++;
				call.rbStoreLocal(result_idx, IRUBYOBJECT);
				
				// only blocks can do non-local return
				assert iseq.getCodeType() == ISEQ_TYPE_BLOCK;
				call.newInstance(NONLOCAL_RETURN_EXCEPTION);
				call.dup();
				
				call.push((String)null);
				call.rbLoadLocal(result_idx, IRUBYOBJECT);
				
				call.invokeConstructor(NONLOCAL_RETURN_EXCEPTION, NONLOCAL_RETURN_CONSTRUCTOR);
				call.throwException();
				
				locals_in_use--;
				
				continue next_insn;
			}
			
			default:
				throw new InternalError("unhandled opcode in compiler: "
						+ opcode + "; last_pc:" + last_insn_pc
						+ "; last_opcode:" + last_opcode);

			}
		}
	}

	private void emit_push_selector(RubyAdapter call, int sel_idx) {
		Selector sel = this.bound_method.getSelector(sel_idx);
		Type sel_type = Type.getType(sel.getClass());
		call.getStatic(sel_type, "instance", sel_type);
	}

	private void emit_load_self(RubyAdapter call, ISeq iseq,
			Type compiled_self_type, Type type_of_self, int self_idx) {
		if (self_idx != -1) {
			call.rbLoadLocal(self_idx, IRUBYOBJECT);
			call.checkCast(type_of_self);
		} else {
			call.rbLoadLocal(0, compiled_self_type);
			call.getField(compiled_self_type, "self", type_of_self);
		}

	}

	private Type get_type_of_self(MetaModule meta, boolean self_is_module) {

		if (self_is_module) {
			return Type.getType("L"
					+ meta.get_base_class_name().replace('.', '/') + ";");
		} else {
			return Type.getType("L"
					+ ((MetaClass) meta).get_instance_class_name().replace('.',
							'/') + ";");
		}
	}

	private Type compile_block(ISeq iseq, boolean context_has_dvars) {

		MethodCompiler compiler = new MethodCompiler(runtime, method_context);
		compiler.self_type = self_type;
		compiler.cpool_types = cpool_types;
		return compiler.compile_as_block(iseq, context_has_dvars);

	}

	private Type compile_as_block(ISeq iseq, boolean context_has_dvars) {

		assert iseq.getCodeType() == Instructions.ISEQ_TYPE_BLOCK;

		bound_method = method_context.bound_method;
		// MetaModule dyn_meta = bound.getDynamicContext();
		MetaModule lex_meta = bound_method.getLexicalContext();
		String packageName = lex_meta.getPackageName();
		String className = "Block$" + nextCount();
		String fullName = "com.trifork.hotruby.compiled." + packageName
				+ className;

		Type block_self_type = Type.getType("L" + fullName.replace('.', '/')
				+ ";");

		ClassWriter cw = new ClassWriter(true);

		Method impl_method;
		int code_args = iseq.code_args;
		switch (code_args) {
		case -1:
			super_class = COMPILEDBLOCKN;
			impl_method = BLOCK_CALLN;
			break;
		case 0:
			super_class = COMPILEDBLOCK0;
			impl_method = BLOCK_CALL0;
			break;
		case 1:
			super_class = COMPILEDBLOCK1;
			impl_method = BLOCK_CALL1;
			break;
		case 2:
			super_class = COMPILEDBLOCK2;
			impl_method = BLOCK_CALL2;
			break;
		default:
			throw new InternalError();
		}

		cw.visit(V1_5, ACC_PUBLIC, block_self_type.getInternalName(), null,
				super_class.getInternalName(), new String[0]);

		cw.visitSource(iseq.getFile(), null);

		gen_get_dvar_names(cw, iseq);
		
		gen_block_constructor(cw, block_self_type, context_has_dvars);

		Type type_of_self = get_type_of_self(bound_method.getDynamicContext(),
				bound_method.selfIsModule());

		GeneratorAdapter g = begin_method(cw, ACC_PUBLIC, new Method("getSelf", type_of_self, new Type[0]));
		g.loadThis();
		g.getField(block_self_type, "self", type_of_self);
		g.returnValue();
		g.endMethod();
		
		g = begin_method(cw, ACC_PUBLIC, new Method("getBlock", RUBYBLOCK, new Type[0]));
		g.loadThis();
		g.getField(block_self_type, "block", RUBYBLOCK);
		g.returnValue();
		g.endMethod();
		
		// generate code //
		RubyAdapter ra = new RubyAdapter(cw.visitMethod(ACC_PUBLIC, impl_method
				.getName(), impl_method.getDescriptor(), null, new String[0]),
				ACC_PUBLIC, impl_method.getName(), impl_method.getDescriptor());

		gen_code(ra, iseq, bound_method, block_self_type, bound_method
				.selfIsModule(), context_has_dvars);

		ra.endMethod();

		byte[] bytes = cw.toByteArray();

		try {
			String name = fullName + ".rclass";
			FileOutputStream fo = new FileOutputStream(name);
			fo.write(bytes);
			fo.close();
			System.out.println("wrote " + name);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		Class bc = runtime.getClassLoader().doDefineClass(fullName, bytes);

		return block_self_type;
	}

	private void gen_block_constructor(ClassWriter cw, Type block_self_type, boolean context_has_dvars) {
		Type[] cons_args = context_has_dvars 
			? new Type[] { IRUBYOBJECT, RUBYBLOCK, EXPOSED_LOCALS_TYPE }
			: new Type[] { IRUBYOBJECT, RUBYBLOCK };
		
		GeneratorAdapter cons = begin_method(cw, ACC_PUBLIC,
				new Method("<init>", Type.VOID_TYPE, cons_args));
		cons.loadThis();
		cons.invokeConstructor(super_class, NOARG_CONSTRUCTOR);

		Type type_of_self = get_type_of_self(bound_method.getDynamicContext(),
				bound_method.selfIsModule());

		cw.visitField(ACC_PRIVATE, "self", type_of_self.getDescriptor(), null,
				null).visitEnd();
		cw.visitField(ACC_PRIVATE, "block", RUBYBLOCK.getDescriptor(), null,
				null).visitEnd();
		if (context_has_dvars) {
		cw.visitField(ACC_PRIVATE, "dvars", EXPOSED_LOCALS_TYPE.getDescriptor(), null,
				null).visitEnd();
		}
		
		cons.loadThis();
		cons.loadArg(0);
		cons.checkCast(type_of_self);
		cons.putField(block_self_type, "self", type_of_self);

		cons.loadThis();
		cons.loadArg(1);
		cons.putField(block_self_type, "block", RUBYBLOCK);

		if (context_has_dvars) {
			cons.loadThis();
			cons.loadArg(2);
			cons.putField(block_self_type, "dvars", EXPOSED_LOCALS_TYPE);
		}
		
		cons.returnValue();
		cons.endMethod();
	}

	private void pop_args_into(RubyAdapter call, int nargs, int all_args_idx) {
		for (int i = nargs - 1; i >= 0; i--) {
			// argN
			call.rbLoadLocal(all_args_idx, IRUBYOBJECT_ARR);
			// argN all_args
			call.swap();
			// all_args argN
			call.push(i);
			// all_args argN N
			call.swap();
			// all_args N argN
			call.arrayStore(IRUBYOBJECT);
		}
	}

	private Label getLabel(Map<Integer, Label> labels, int target,
			GeneratorAdapter ga) {
		Integer t = new Integer(target);
		Label l = labels.get(t);
		if (l == null) {
			labels.put(t, l = ga.newLabel());
		}
		return l;
	}

	private int si(int msb, int lsb) {
		return (msb << 8) | (lsb & 0xff);
	}

	private int ui(int msb, int lsb) {
		return ((msb & 0xff) << 8) | (lsb & 0xff);
	}

	private int ui(int b) {
		return 0xff & b;
	}

	private void emit_load_nil(RubyAdapter call) {
		call.getStatic(LOADED_RUBY_RUNTIME, "NIL", RUBYNILCLASS);
	}

	/**
	 * we now know the value of parm (local_idx self=0). decide where to put it.
	 * 
	 * @param handled_dynamics
	 */
	private void set_parm(boolean[] handled_dynamics, RubyAdapter ga,
			ISeq iseq, int dvars_loc, int local0_idx, int loc) {
		// what to do with it?
		for (int di = 0; di < iseq.num_dynamics; di++) {
			int dvar_from_parm = iseq.dynamics_from[di];
			if (dvar_from_parm == loc) {
				ga.rbLoadLocal(dvars_loc, COMPILED_METHOD_DVARS);
				ga.push(di);
				ga.invokeStatic(COMPILEDMETHOD, SET_DVAR);
				handled_dynamics[di] = true;
				return;
			}
		}

		// else ...
		ga.rbStoreLocal(local0_idx + loc, IRUBYOBJECT);
	}

	private void check_varg_count(GeneratorAdapter ga, ISeq iseq,
			int real_given_args_loc) {
		ga.loadLocal(real_given_args_loc, Type.INT_TYPE);
		ga.push(iseq.min_parm_count);
		Label ok_1_label = ga.newLabel();
		ga.ifICmp(GeneratorAdapter.GE, ok_1_label);
		emit_load_runtime(ga);
		ga.loadLocal(real_given_args_loc, Type.INT_TYPE);
		ga.invokeStatic(COMPILEDCODEUTIL, WRONG_ARGS_METHOD);
		ga.throwException();
		ga.mark(ok_1_label);
	}

}
