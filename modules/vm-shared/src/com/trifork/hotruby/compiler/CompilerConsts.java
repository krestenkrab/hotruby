package com.trifork.hotruby.compiler;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyFixnum;
import com.trifork.hotruby.objects.IRubyFloat;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyMethodAccessor;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;

public interface CompilerConsts {

	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static final Type METHODACCESSOR_TYPE = Type
			.getType(RubyMethodAccessor.class);

	public static final Type SELECTOR_TYPE = Type.getType(Selector.class);

	public static final String SELECTOR_CLASS_NAME = SELECTOR_TYPE
			.getInternalName();

	public static final Method NOARG_CONSTRUCTOR = new Method("<init>", "()V");

	public static final Method CLASS_INITIALIZER = new Method("<clinit>",
	"()V");

	public static final Type IRUBYOBJECT_ARR = Type
			.getType(IRubyObject[].class);

	public static final Type IRUBYOBJECT = Type.getType(IRubyObject.class);

	public static final Type RUBYBLOCK = Type.getType(RubyBlock.class);

	public static final Method CALL0 = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT, RUBYBLOCK });

	public static final Method CALL1 = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT, IRUBYOBJECT, RUBYBLOCK });

	public static final Method CALL2 = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT, IRUBYOBJECT, IRUBYOBJECT, RUBYBLOCK });

	public static final Method CALLN = new Method("call", IRUBYOBJECT,
			new Type[] { IRUBYOBJECT, IRUBYOBJECT_ARR, RUBYBLOCK });

	public static final Method GET_ARITY = new Method("getArity",
			Type.INT_TYPE, new Type[0]);

	public static final Type COMPILEDMETHOD = Type
	.getType(CompiledMethod.class);

	public static final Type COMPILEDMETHOD0 = Type
	.getType(CompiledMethod0.class);

	public static final Type COMPILEDMETHOD1 = Type
			.getType(CompiledMethod1.class);

	public static final Type COMPILEDMETHOD2 = Type
			.getType(CompiledMethod2.class);

	public static final Type COMPILEDMETHODN = Type
			.getType(CompiledMethodN.class);

	public static final Type METAMODULE_TYPE = Type.getType(MetaModule.class);
	public static final Type METACLASS_TYPE = Type.getType(MetaClass.class);

	public static final Type STRING_TYPE = Type.getType(String.class);
	
	public static final Method GET_METHOD_ACCESSOR = new Method("getMethodAccessor", METHODACCESSOR_TYPE, new Type[] { STRING_TYPE, Type.BOOLEAN_TYPE });
	public static final Method GET_SUPER_METHOD_ACCESSOR = new Method("getSuperMethodAccessor", METHODACCESSOR_TYPE, new Type[] { STRING_TYPE, Type.BOOLEAN_TYPE });
	public static final Method GET_SELECTOR = new Method("getSelector", SELECTOR_TYPE, new Type[] { STRING_TYPE });

	public static final Type RUBY_RUNTIME_TYPE = Type.getType(RubyRuntime.class);

	public static final Type LOADED_RUBY_RUNTIME = Type.getType("Lcom/trifork/hotruby/runtime/LoadedRubyRuntime;");

	public static final Method GET_META_FROM_PACKAGE_NAME = new Method("getMetaFromPackageName", METAMODULE_TYPE, new Type[] { STRING_TYPE });

	public static final Type IRUBYSTRING = Type.getType(IRubyString.class);

	public static final Method NEW_STRING = new Method("newString", IRUBYSTRING, new Type[] { STRING_TYPE });

	public static final Type IRUBYFIXNUM = Type.getType(IRubyFixnum.class);
	public static final Type IRUBYSYMBOL = Type.getType(IRubySymbol.class);

	public static final Method NEW_FIXNUM = new Method("newFixnum", IRUBYFIXNUM, new Type[] { Type.INT_TYPE });

	public static final Type IRUBYFLOAT = Type.getType(IRubyFloat.class);

	public static final Method NEW_FLOAT = new Method("newFloat", IRUBYFLOAT, new Type[] { Type.DOUBLE_TYPE });
	public static final Method NEW_SYMBOL = new Method("newSymbol", IRUBYSYMBOL, new Type[] { STRING_TYPE });

	public static final Type RUBYFIXNUM = Type.getType("Lcom/trifork/hotruby/objects/RubyFixnum;");
	public static final Type RUBYNILCLASS = Type.getType("Lcom/trifork/hotruby/objects/RubyNilClass;");
	public static final Type RUBYTRUECLASS = Type.getType("Lcom/trifork/hotruby/objects/RubyTrueClass;");
	public static final Type RUBYFALSECLASS = Type.getType("Lcom/trifork/hotruby/objects/RubyFalseClass;");

	public static final Type RUBYMODULE_TYPE = Type
	.getType("Lcom/trifork/hotruby/objects/RubyModule;");

	public static final Type RUBYCLASS_TYPE = Type
	.getType("Lcom/trifork/hotruby/objects/RubyClass;");

	public static final Type RUBYOBJECT_TYPE = Type
	.getType("Lcom/trifork/hotruby/objects/RubyObject;");

	public static final Method MODULE_INIT_METHOD = new Method("init",
			Type.VOID_TYPE, new Type[] { METAMODULE_TYPE });

	public static final Type IRUBYCLASS_TYPE = Type.getType(IRubyClass.class);

	public static final Method GET_CLASS_METHOD = new Method("get_class",
			RUBYCLASS_TYPE, new Type[0]);

	public static final Type RUBYCLASSMODULE_TYPE = Type
	.getType("Lcom/trifork/hotruby/classes/RubyClassModule;");

	public static final Type RUBYCLASSCLASS_TYPE = Type
	.getType("Lcom/trifork/hotruby/classes/RubyClassClass;");

	public static final Type RUBYMETHOD = Type.getType(RubyMethod.class);

	public static final Method SELECT_METHOD = new Method("select",
			RUBYMETHOD, new Type[] { SELECTOR_TYPE });

	public static final Type RUBYOBJECT = Type
			.getType("Lcom/trifork/hotruby/objects/RubyObject;");

	public static final Method RESOLVE_METHOD = new Method("resolve_method",
			RUBYMETHOD, new Type[] { RUBYOBJECT, SELECTOR_TYPE, STRING_TYPE });

	public static final Type OBJECT_TYPE = Type.getType(Object.class);

	public static final Method CLASS_INIT_METHOD = new Method("init",
			Type.VOID_TYPE, new Type[] { METACLASS_TYPE });

	public static final Method NEWINSTANCE_METHOD = new Method("newInstance", IRUBYOBJECT, new Type[0]);

	public static final Method SELECTOR_SET = new Method("set", Type.VOID_TYPE, new Type[] { SELECTOR_TYPE });

	public static final Method SELECTOR_GET = new Method("get", SELECTOR_TYPE, new Type[0]);


}
