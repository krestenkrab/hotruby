package com.trifork.hotruby.runtime;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.trifork.hotruby.ast.RubyCode;
import com.trifork.hotruby.ast.TopLevelCode;
import com.trifork.hotruby.interp.BindingContext;
import com.trifork.hotruby.interp.Instructions;
import com.trifork.hotruby.interp.InterpCompileContext;
import com.trifork.hotruby.interp.MethodISeq;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyFixnum;
import com.trifork.hotruby.objects.IRubyFloat;
import com.trifork.hotruby.objects.IRubyHash;
import com.trifork.hotruby.objects.IRubyInteger;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyProc;
import com.trifork.hotruby.objects.IRubyRange;
import com.trifork.hotruby.objects.IRubyRegexp;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.parser.RubyParser;

public abstract class RubyRuntime {

	private static final String COM_TRIFORK_HOTRUBY_SELECTORS = "com.trifork.hotruby.selectors.";

	private static final String LOADED_RUNTIME = "com.trifork.hotruby.runtime.LoadedRubyRuntime";

	private static final String PREFIX = "ruby";

	protected IRubyObject the_nil;

	protected IRubyObject the_true;

	protected IRubyObject the_false;

	protected MetaClass meta_object;

	protected MetaClass meta_module;

	protected RubyClassLoader loader;

	protected CodeGen gen = new CodeGen(this);

	private File rubyHome;

	File rubyHome() {
		if (rubyHome == null) {

			URL shared_url = RubyRuntime.class.getProtectionDomain()
					.getCodeSource().getLocation();
			File shared = new File(shared_url.getFile());
			String home = shared.getParent();

			String rubyHome = System.getProperty(PREFIX + ".home", home);
			this.rubyHome = new File(rubyHome);

		}

		return rubyHome;
	}

	@SuppressWarnings("unchecked")
	static public RubyRuntime newRuntime() {
		RubyClassLoader loader = new RubyClassLoader();
		try {
			Class<RubyRuntime> klass = (Class<RubyRuntime>) loader
					.loadClass(LOADED_RUNTIME);
			RubyRuntime rr = klass.newInstance();
			rr.shared_init();
			return rr;
		} catch (Exception e) {
			throw new RuntimeException("unable to load RubyRuntime", e);
		}
	}

	public static void main(String[] args) throws IOException {
		RubyRuntime rt = newRuntime();

		FileInputStream fis = new FileInputStream("rb/richards.rbc");
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.skip(4); // RBCM
		UnmarshalStream ums = new UnmarshalStream(rt, bis);

		IRubyObject obj = ums.unmarshalObject(CallContext.NULL);

		System.out.println(obj.inspect());
	}

	protected RubyRuntime() {
		loader = (RubyClassLoader) getClass().getClassLoader();
		loader.setRuntime(this);
	}

	public IRubyClass getClass(String className) {
		MetaModule mm = meta_object.get_module(className, false);
		if (mm instanceof MetaClass) {
			return ((MetaClass) mm).get_base_class();
		} else {
			throw newArgumentError(className + " is not a class");
		}
	}

	public IRubyClass getClassFromPath(String path) {

		IRubyModule mm = getModuleFromPath(path);
		if (!(mm instanceof IRubyClass)) {
			throw newTypeError("class path " + path + " does not point class");
		}
		return (IRubyClass) mm;
	}

	public IRubyModule getModuleFromPath(String path) {

		MetaModule meta = getMetaModulefromPath(path, "::", false);

		return meta.get_base_module();
	}

	private MetaModule getMetaModulefromPath(String path, String delim,
			boolean use_const_missing) {
		if (path.charAt(0) == '#') {
			throw newArgumentError("can't retrieve anonymous " + path);
		}

		MetaModule meta = meta_object;

		int start = 0;
		while (meta != null) {
			int idx = path.indexOf(delim, start);
			if (idx != -1) {
				String next = path.substring(start, idx);
				meta = meta.get_module(next, use_const_missing);
				start = idx + 2;
			} else {
				meta = meta.get_module(path, use_const_missing);
				break;
			}
		}

		if (meta == null) {
			throw newTypeError("class path " + path + " does not point class");
		}
		return meta;
	}

	public IRubyModule getModule(String className) {
		MetaModule mm = meta_object.get_module(className, false);
		if (mm != null) {
			return mm.get_base_module();
		} else {
			throw newArgumentError(className + " is not a class");
		}
	}

	public IRubyObject getNil() {
		return the_nil;
	}

	public IRubyObject getTrue() {
		return the_true;
	}

	public IRubyObject getFalse() {
		return the_false;
	}

	public Selector getSelector(CallContext call_context, String name) {
		Class sel_class = null;
		String class_name = getSelectorClassName(call_context, name);

		try {
			sel_class = loader.loadClass(class_name);
		} catch (ClassNotFoundException ex) {
			// ignore //
		}

		if (sel_class == null) {
			sel_class = gen.make_selector_class(class_name);
		}

		try {
			return (Selector) sel_class.newInstance();
		} catch (Exception e) {
			throw new InternalError("cannot instantiate " + sel_class);
		}
	}

	public static String decodeMethodNameFromSelectorClassName(
			String selectorClassName) {
		int idx = selectorClassName.lastIndexOf(".call");
		String encoded = selectorClassName.substring(idx + ".call_".length());
		int ii = encoded.indexOf('$');
		if (ii == -1) {
			return encoded;
		}
		StringBuilder sb = new StringBuilder();

		int start = 0;
		do {
			sb.append(encoded.substring(start, ii));
			sb.append((char) Integer.parseInt(
					encoded.substring(ii + 1, ii + 3), 16));
			start = ii + 3;
			ii = encoded.indexOf('$', start);
		} while (ii != -1);

		sb.append(encoded.substring(start));

		return sb.toString();
	}

	static String getSelectorClassName(CallContext call_context, String name) {
		StringBuilder out = new StringBuilder(COM_TRIFORK_HOTRUBY_SELECTORS);
		out.append(call_context == null ? "" : call_context.getCaller()
				.getPackageName());
		out.append("call_");
		encode(name, out);
		return out.toString();
	}

	private static void encode(String name, StringBuilder out) {
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (Character.isJavaIdentifierPart(ch)) {
				out.append(ch);
			} else {
				out.append('$');
				String hex = Integer.toHexString(ch);
				if (hex.length() == 1) {
					out.append('0');
				}
				out.append(hex);
			}
		}
	}

	public String encodeMethodName(String name) {
		StringBuilder sb = new StringBuilder();
		encode(name, sb);
		return sb.toString();
	}

	public MetaModule computeModuleForLoadedSelector(Object selector) {
		String name = selector.getClass().getName();
		if (!name.startsWith(COM_TRIFORK_HOTRUBY_SELECTORS)) {
			throw new Error("bad");
		}

		int first = COM_TRIFORK_HOTRUBY_SELECTORS.length();
		int last = name.lastIndexOf(".call");

		if (last <= first) {
			return this.meta_object;
		}

		String path = name.substring(first, last).replace(".", "::");
		return getMetaModulefromPath(path, ".", true);
	}

	public MetaClass new_system_class(String name) {
		return new_system_class(name, meta_object);
	}

	@SuppressWarnings("unchecked")
	public MetaClass new_system_class(String name, MetaClass super_meta) {
		MetaClass cls = new MetaClass(this, meta_object, name, super_meta);
		try {
			cls.set_base_level_class((Class<IRubyModule>) loader
					.loadClass("com.trifork.hotruby.classes.RubyClass" + name));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to locate class " + name, e);
		}
		meta_object.const_set(name, cls.get_base_class());
		return cls;
	}

	@SuppressWarnings("unchecked")
	public MetaModule new_system_module(String name) {
		MetaModule module = new MetaModule(this, meta_object, name);
		try {
			module
					.set_base_level_class((Class<IRubyModule>) loader
							.loadClass("com.trifork.hotruby.modules.RubyModule"
									+ name));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to locate module " + name, e);
		}
		meta_object.const_set(name, module.get_base_module());
		return module;
	}

	public abstract IRubyObject unmarshalStringFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalFixnumFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalFloatFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalBignumFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalArrayFrom(UnmarshalStream stream,
			CallContext ctx) throws IOException;

	public abstract IRubyObject unmarshalStructFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalSymbolFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalHashFrom(UnmarshalStream stream,
			CallContext ctx) throws IOException;

	public abstract IRubyObject unmarshalClassFrom(UnmarshalStream stream)
			throws IOException;

	public abstract IRubyObject unmarshalModuleFrom(UnmarshalStream stream)
			throws IOException;

	public abstract RuntimeException newLocalJumpError(String string,
			NonLocalJump ex);

	public abstract RuntimeException newNoMethodError(String string);

	public abstract RuntimeException newArgumentError(String string);

	public abstract RuntimeException newTypeError(String string);

	public abstract IRubyHash newHash();

	public abstract IRubyRegexp newRegexp(IRubyString string, int flags);

	public abstract IRubyRegexp newRegexp(String string, int flags);

	public abstract IRubyArray newArray();

	public abstract IRubyArray newArray(int size);

	public abstract IRubyFixnum newFixnum(int intvalue);

	public abstract IRubySymbol newSymbol(String sym1);

	public abstract IRubyString newString(String marshaled);

	public abstract IRubyInteger newInteger(String text, int radix);

	public abstract IRubyFloat newFloat(double d);

	public abstract IRubyArray arrayWithOneNil();

	public abstract MetaClass meta_Object();

	public abstract IRubyClass getObject();

	public abstract IRubyArray newArray(IRubyObject[] extra);

	Map<String, Global> globals = new HashMap<String, Global>();

	public Global getGlobal(String name) {
		synchronized (globals) {
			Global gval = globals.get(name);
			if (gval == null) {
				name = intern(name);
				globals.put(name, gval = new Global(name, getNil()));
			}
			return gval;
		}
	}

	public abstract MetaClass meta_Class();

	WeakHashMap<String, String> interned_strings = new WeakHashMap<String, String>();

	public String intern(String value) {
		synchronized (interned_strings) {
			String uniq = interned_strings.get(value);
			if (uniq == null) {
				interned_strings.put(
						new String(value) /* will share representation */,
						uniq = value);
			}
			return uniq;
		}
	}

	public abstract IRubyProc newProc(RubyBlock block);

	public abstract RuntimeException newSyntaxError(String string);

	static Map<String, String> method_to_name_map = new HashMap<String, String>();

	public void register_method(RubyMethod callable, String name) {
		if (callable instanceof MethodISeq) {
			return;
		}

		Class<RubyMethod> klass = (Class<RubyMethod>) callable.getClass();
		method_to_name_map.put(intern(klass.getName()), intern(name));
		// todo: MAKE THIS WEAK?

	}

	String BINDING_CONTEXT = intern(BindingContext.class.getName());

	public IRubyArray caller(int skip) {
		ThreadState state = ThreadState.get();
		String[] icallers = state.getInterpreterCallers();

		StackTraceElement[] trace = new Throwable().getStackTrace();
		ArrayList<IRubyString> result = new ArrayList<IRubyString>();

		int icount = 0;

		for (int i = 0; i < trace.length; i++) {

			StackTraceElement elem = trace[i];
			String type = elem.getClassName();

			String method_name = method_to_name_map.get(type);
			if (method_name != null) {
				if (skip-- <= 0) {
					result.add(newString(elem.getFileName() + ":"
							+ elem.getLineNumber() + " in `" + method_name
							+ "'"));
				}
			} else if (type.equals(BINDING_CONTEXT)
					&& "internal_eval".equals(elem.getMethodName())) {
				if (skip-- <= 0) {
					result.add(newString(icallers[icount++]));
				} else {
					icount++;
				}
			}
		}

		return newArray(result.toArray(new IRubyObject[result.size()]));
	}

	RubyBlock trace_func;

	IRubyString[] event_names;

	IRubyString empty_string;

	private void shared_init() {
		trace_func = null;
		event_names = new IRubyString[6];

		event_names[Instructions.TRACE_LINE] = newString("line");
		event_names[Instructions.TRACE_CALL] = newString("call");
		event_names[Instructions.TRACE_RETURN] = newString("return");
		event_names[Instructions.TRACE_CLASS] = newString("class");
		event_names[Instructions.TRACE_END] = newString("end");
		event_names[Instructions.TRACE_RAISE] = newString("raise");

		empty_string = newString("");

		init_properties();

		load("core.rb", getObject());

	}

	private void init_properties() {

		String path = System.getProperty(PREFIX + ".path");

		if (path == null) {
			path = rubyHome().getPath() + File.separator + "core"
					+ File.pathSeparator + rubyHome().getPath()
					+ File.separator + "ruby";
		}

		String[] elms = path.split(File.pathSeparator);
		IRubyArray arr = newArray();
		for (int i = 0; i < elms.length; i++) {
			arr.add(newString(elms[i]));
		}

		Global gpath = getGlobal("$:");
		gpath.set(arr);

		IRubyArray arr2 = newArray();
		arr2.add(newString("core.rb"));

		getGlobal("$\"").set(arr2);
	}

	IRubyArray getPath() {
		Global gpath = getGlobal("$:");
		return (IRubyArray) gpath.get();
	}

	public IRubyObject load(String file, IRubyObject run_in) {
		File f = new File(file);
		if (!f.isAbsolute() && !f.exists()) {

			IRubyArray path = getPath();
			for (int i = 0; i < path.int_size(); i++) {
				f = new File(path.int_at(i).asSymbol(), file);
				if (f.exists()) {
					break;
				}
			}

		}

		if (!f.exists()) {
			throw newArgumentError("cannot find " + file);
		}

		return loadFile(f, run_in);
	}

	private IRubyObject loadFile(File f, IRubyObject run_in) {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e1) {
			throw newLoadError("No such file to load -- " + f);
		}
		RubyParser parser = null;

		TopLevelCode code;
		try {
			parser = new RubyParser(reader, f.getPath());
			code = parser.program();
		} catch (RecognitionException e) {
			e.printStackTrace();
			throw newSyntaxError(e.getMessage());
		} catch (TokenStreamException e) {
			throw newSyntaxError(e.getMessage());
		}

		InterpCompileContext icc = new InterpCompileContext(this);

		MetaModule type = run_in.get_meta_class();
		boolean is_module_or_class = run_in instanceof IRubyModule;
		BindingContext iseq = icc.compile(code, type, null, is_module_or_class);

		IRubyObject obj = iseq.eval(run_in);

		return obj;
	}

	public abstract RuntimeException newLoadError(String string);

	public void set_tracec_func(RubyBlock block) {
		this.trace_func = block;
	}

	// todo: optimize
	public void trace(ThreadState state, int kind, String file, int line,
			String method, ExposedLocals dvars, IRubyObject self) {

		// System.out.println("trace "+event_names[kind]+" " + file + ":" + line
		// + " "+ (method==null?"":method) + " "+self);

		if (kind == Instructions.TRACE_LINE) {
			(state == null ? ThreadState.get() : state).setLine(line);
		}

		if (trace_func != null) {
			RubyBlock save = trace_func;
			trace_func = null;
			try {
				save
						.call(new IRubyObject[] {
								event_names[kind],
								newString(file),
								newFixnum(line),
								method == null ? empty_string
										: newString(method),
								getNil(),
								newString(self.get_class().get_meta_class()
										.getName()) });
			} catch (NonLocalJump e) {
				throw newLocalJumpError("exception in trace func", e);
			} finally {
				trace_func = save;
			}
		}
	}

	public IRubyObject eval(String text, EvalContext ctx, String filename,
			int first_line) {

		StringReader sr = new StringReader(text);
		RubyParser parser = new RubyParser(sr, filename, first_line);

		System.out.println("eval: " + text);

		RubyCode code;
		try {
			code = parser.eval_body(ctx);

		} catch (RecognitionException e) {
			e.printStackTrace();
			throw newSyntaxError(e.getMessage());
		} catch (TokenStreamException e) {
			e.printStackTrace();
			throw newSyntaxError(e.getMessage());
		}

		IRubyObject self = ctx == null ? meta_Object().get_base_class()
				.newInstance() : ctx.get_self();
		MetaModule lex_context = ctx == null ? this.meta_Object() : ctx
				.get_lexical_context();

		ThreadState state = ThreadState.get();

		InterpCompileContext icc = new InterpCompileContext(this);
		BindingContext vv = icc.compile(code, lex_context, state
				.getModuleStack(), self instanceof IRubyModule);

		state.push(self);
		vv.eval(self, ThreadState.get(), 0, null, ctx.get_locals());
		return state.pop();

	}

	public RubyClassLoader getClassLoader() {
		return loader;
	}

	MetaModule getMetaFromPackageName(String pkgName) {
		throw new InternalError("not implemented");
	}

	public IRubyModule make_module(MetaModule module) {
		return this.gen.make_module(module);
	}

	public IRubyModule make_class(MetaClass klass) {
		return this.gen.make_class(klass);
	}

	public MetaClass meta_Module() {
		return meta_module;
	}

	public abstract RuntimeException newNameError(String string);

	public void alias_global(String new_name, String orig_name) {
		// TODO Auto-generated method stub

	}

	// access to these should be synchronized on 
	private WeakHashMap<Integer, IRubyObject> id_to_object = new WeakHashMap<Integer, IRubyObject>();
	private int object_id = 1;

	public IRubyObject get_object_id(IRubyObject receiver) {
		
		SingletonState ss = receiver.get_singleton_state(true);
		if (ss.object_id == 0) {
			synchronized (id_to_object) {
				ss.object_id = object_id++;
				id_to_object.put(new Integer(ss.object_id), receiver);
			}
		}
		
		return newFixnum(ss.object_id);
	}

	public RaiseException newRuntimeError(IRubyString string) {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract IRubyRange newRange(IRubyObject start, IRubyObject end, boolean inclusive);
}
