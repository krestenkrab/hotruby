package com.trifork.hotruby.runtime;

import java.io.IOException;
import java.math.BigInteger;

import com.trifork.hotruby.classes.RubyClassObject;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyFixnum;
import com.trifork.hotruby.objects.IRubyFloat;
import com.trifork.hotruby.objects.IRubyHash;
import com.trifork.hotruby.objects.IRubyInteger;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyRange;
import com.trifork.hotruby.objects.IRubyRegexp;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.objects.RubyArray;
import com.trifork.hotruby.objects.RubyBignum;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyFalseClass;
import com.trifork.hotruby.objects.RubyFixnum;
import com.trifork.hotruby.objects.RubyFloat;
import com.trifork.hotruby.objects.RubyHash;
import com.trifork.hotruby.objects.RubyInteger;
import com.trifork.hotruby.objects.RubyModule;
import com.trifork.hotruby.objects.RubyNilClass;
import com.trifork.hotruby.objects.RubyObject;
import com.trifork.hotruby.objects.RubyProc;
import com.trifork.hotruby.objects.RubyRange;
import com.trifork.hotruby.objects.RubyRegexp;
import com.trifork.hotruby.objects.RubyString;
import com.trifork.hotruby.objects.RubyStruct;
import com.trifork.hotruby.objects.RubySymbol;
import com.trifork.hotruby.objects.RubyTrueClass;

public class LoadedRubyRuntime extends RubyRuntime {

	public static LoadedRubyRuntime instance;

	public static RubyFalseClass FALSE = null;

	public static RubyTrueClass TRUE = null;

	public static RubyNilClass NIL = null;

	public static MetaClass META_OBJECT = null;

	private MetaClass meta_class;

	public LoadedRubyRuntime() {
		instance = this;
		init();
	}

	public static void main(String[] args) {
		new LoadedRubyRuntime();
	}

	private void init() {
		meta_object = new MetaClass(this, null, null, null);
		meta_object.set_context(meta_object, "Object");
		META_OBJECT = meta_object;
		
		MetaClass _module = new_system_class("Module");
		super.meta_module = _module;
		meta_class = new_system_class("Class", _module);

		meta_object.set_base_level_class(RubyClassObject.class);
		meta_object.const_set("Object", meta_object.get_base_class());

		MetaModule kernel = new_system_module("Kernel");
		
		// include the kernel module in object
		meta_object.include(kernel);

		MetaClass _true = new_system_class("TrueClass");
		MetaClass _false = new_system_class("FalseClass");
		MetaClass _nil = new_system_class("NilClass");

		TRUE = (RubyTrueClass) (the_true = _true.get_base_class().newInstance());
		meta_object.const_set("TRUE", TRUE);

		FALSE = (RubyFalseClass) (the_false = _false.get_base_class().newInstance());
		meta_object.const_set("FALSE", FALSE);

		NIL = (RubyNilClass) (the_nil = _nil.get_base_class().newInstance());
		meta_object.const_set("NIL", NIL);

		MetaClass _numeric = new_system_class("Numeric");
		MetaClass _integer = new_system_class("Integer", _numeric);
		MetaClass _bignum = new_system_class("Bignum", _integer);
		MetaClass _fixnum = new_system_class("Fixnum", _integer);
		MetaClass _float = new_system_class("Float", _numeric);

		MetaClass string_class = new_system_class("String");
		new_system_class("Symbol");
		new_system_class("Array");
		new_system_class("Struct");
		new_system_class("Regexp");
		new_system_class("MatchData");
		new_system_class("Proc");
		new_system_class("Hash");
		new_system_class("Time");
		new_system_class("Binding");
		new_system_class("Range");
		
		RubyString.init(string_class);
	}

	@Override
	public IRubyClass getObject() {
		return META_OBJECT.get_base_class();
	}
	
	@Override
	public MetaClass meta_Object() {
		return META_OBJECT;
	}
	
	@Override
	public MetaClass meta_Class() {
		return meta_class;
	}

	// this is the slow lookup
	public static RubyMethod resolve_method(RubyObject object, Selector sel,
			String selector_class_name) {
		Class selectorClass;
		try {
			selectorClass = object.getClass().getClassLoader().loadClass(selector_class_name);
		} catch (ClassNotFoundException e) {
			throw new InternalError("cannot load selector class");
		}
		return resolve_method(object, sel, selectorClass);
	}
	
	
	// this is the slow lookup
	public static RubyMethod resolve_method(RubyObject object, Selector sel,
			Class selectorClass) {
		
		if (object instanceof RubyModule) {
			return ((RubyModule)object).get_meta_module().resolve_method(object, sel, selectorClass);
		}
		
		/// System.out.println("resolving "+object.get_class().inspect()+"."+sel.getName()+" from "+sel.getCaller().getName());
		return object.get_meta_class().resolve_method(object, sel, selectorClass);
	}

	@Override
	public RuntimeException newArgumentError(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RubyString newString(String value) {
		return new RubyString(value);
	}

	@Override
	public RuntimeException newTypeError(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRubyObject unmarshalArrayFrom(UnmarshalStream stream,
			CallContext ctx) throws IOException {
		return RubyArray.unmarshalFrom(stream, ctx);
	}

	@Override
	public IRubyObject unmarshalBignumFrom(UnmarshalStream stream)
			throws IOException {
		return RubyBignum.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalClassFrom(UnmarshalStream stream)
			throws IOException {
		return RubyClass.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalFixnumFrom(UnmarshalStream stream)
			throws IOException {
		return RubyFixnum.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalFloatFrom(UnmarshalStream stream)
			throws IOException {
		return RubyFloat.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalHashFrom(UnmarshalStream stream, CallContext ctx)
			throws IOException {
		return RubyHash.unmarshalFrom(stream, ctx);
	}

	@Override
	public IRubyObject unmarshalModuleFrom(UnmarshalStream stream)
			throws IOException {
		return RubyModule.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalStringFrom(UnmarshalStream stream)
			throws IOException {
		return RubyString.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalStructFrom(UnmarshalStream stream)
			throws IOException {
		return RubyStruct.unmarshalFrom(stream);
	}

	@Override
	public IRubyObject unmarshalSymbolFrom(UnmarshalStream stream)
			throws IOException {
		return RubySymbol.unmarshalFrom(stream);
	}

	@Override
	public IRubyArray arrayWithOneNil() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRubyArray newArray() {
		return new RubyArray();
	}
	
	@Override
	public IRubyRange newRange(IRubyObject start, IRubyObject end, boolean inclusive) {
		return new RubyRange().init(start, end, inclusive ? TRUE : FALSE);
	}
	
	@Override
	public IRubyArray newArray(IRubyObject[] args) {
		return new RubyArray(args);
	}

	@Override
	public IRubyArray newArray(int size) {
		return new RubyArray().initialize(size);
	}

	@Override
	public IRubyFixnum newFixnum(int intvalue) {
		return new RubyFixnum(intvalue);
	}

	@Override
	public IRubyFloat newFloat(double d) {
		return new RubyFloat(d);
	}

	@Override
	public IRubyHash newHash() {
		return new RubyHash();
	}

	@Override
	public IRubyInteger newInteger(String text, int radix) {
		return RubyInteger.newInteger(new BigInteger(text, radix));
	}

	@Override
	public RuntimeException newLocalJumpError(String string, NonLocalJump ex) {
		// TODO: FIX
		throw new RuntimeException(string);
	}

	@Override
	public RuntimeException newNoMethodError(String string) {
		// TODO Auto-generated method stub
		throw new RuntimeException(string);
	}

	@Override
	public IRubyRegexp newRegexp(IRubyString string, int flags) {
		// TODO Auto-generated method stub
		return new RubyRegexp(string.asSymbol(), flags);
	}

	@Override
	public IRubyRegexp newRegexp(String string, int flags) {
		return new RubyRegexp(string, flags);
	}

	@Override
	public IRubySymbol newSymbol(String sym1) {
		return RubySymbol.get(sym1);
	}
	
	@Override
	public RubyProc newProc(RubyBlock block) {
		return new RubyProc(block);
	}

	@Override
	public RuntimeException newSyntaxError(String string) {
		throw new RuntimeException(string);
	}

	@Override
	public RuntimeException newLoadError(String string) {
		throw new RuntimeException(string);
	}
	
	@Override
	public RuntimeException newNameError(String string) {
		throw new RuntimeException(string);
	}

	public IRubyObject newString(CharSequence seq) {
		return newString(seq.toString());
	}
}
