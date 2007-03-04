package com.trifork.hotruby.ast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Instructions;
import com.trifork.hotruby.objects.IRubyObject;

public abstract class RubyCode {

	public abstract int code_type();

	List<LocalVariable> locals = new ArrayList<LocalVariable>();

	protected final RubyCode context;

	private final int line;

	private String fileName;

	/** return number of actual args passed in call (Java-level) */
	public int call_args() {
		return Math.abs(getCodeArgs());
	}

	protected RubyCode(int line, RubyCode context, String fileName) {
		this.line = line;
		this.context = context;
		this.fileName = fileName;
		assert fileName != null;
	}

	protected Expression body;

	private boolean locals_assigned;

	public void setBody(Expression exp) {
		this.body = exp;
	}
	
	public Expression getBody() {
		return body;
	}

	public int line() {
		return line;
	}

	public void compile(CompileContext ctx) {
		assign_local_indexes();
		if (body == null) {
			ctx.emit_push_self();
			ctx.emit_leave();
			return;
		}
		
		ctx.emit_trace(Instructions.TRACE_LINE, body.line());;
		
		body.compile(ctx, true);
		ctx.emit_leave(); // return
	}

	public void assignToLocal(String name, boolean isParmDecl) {
		
		if (isParmDecl) {

			for (LocalVariable l : locals) {
				if (name.equals(l.name)) {
					l.isParm = true;
					return;
				}
			}

			locals.add(new LocalVariable(name, true));
			return;
		}

		if (getLocal(name, 0, false) == null) {
			locals.add(new LocalVariable(name, false));
		}
	}

	public LocalVariableAccess lookup(String name) {
		LocalVariableAccess result = acessLocal(name, 0);

		// System.out.println("unresolved local? "+name);

		return result;
	}

	protected LocalVariable getLocal(String name, int level, boolean create) {

		for (LocalVariable l : locals) {
			if (name.equals(l.name)) {
				if (level != 0) {
					l.isDynamic = true;
				}
				return l;
			}
		}

		return get_local_in_context(name, level, create);
	}

	protected LocalVariable get_local_in_context(String name, int level, boolean create) {
		if (context != null) {
			return context.getLocal(name, level + 1, create);
		} else {
			return null;
		}
	}

	protected LocalVariableAccess acessLocal(String name, int level) {

		if (name == null) {
			throw new NullPointerException();
		}

		for (LocalVariable l : locals) {
			if (name.equals(l.name)) {
				if (level != 0) {
					l.isDynamic = true;
				}
				return new LocalVariableAccess(level, l);
			}
		}

		return access_local_in_context(name, level);
	}

	protected LocalVariableAccess access_local_in_context(String name, int level) {
		if (context != null) {
			return context.acessLocal(name, level + 1);
		} else {
			return null;
		}
	}

	public int getNumLocals() {
		return assign_local_indexes();
	}

	public int getNumDynamics() {
		int count = 0;
		for (LocalVariable l : locals) {
			if (l.isDynamic)
				count++;
		}
		return count;
	}

	public int getMinParmCount() {
		return 0;
	}

	public boolean hasRestParm() {
		return false;
	}

	public int[] getDefaultArgEvaluation() {
		return new int[0];
	}

	public int[] getDynamicsFromArgs() {
		int[] result = new int[getNumDynamics()];
		for (int i = 0; i < result.length; i++) {
			result[i] = -1;
		}
		return result;
	}

	public String[] getAllParmNames() {
		return new String[0];
	}

	int local_size = 0;

	private boolean this_code_calls_eval;
	public int assign_local_indexes() {
		if (locals_assigned)
			return local_size;
		locals_assigned = true;

		int l_idx = assign_parms() + 1;
		int d_idx = 0;
		for (LocalVariable loc : locals) {
			if (this_code_calls_eval) {
				loc.isDynamic = true;
			}
			
			if (loc.isDynamic) {
				loc.setDynamicIndex(d_idx++);
			} else if (!loc.isParm) {
				loc.setIndex(l_idx++);
			}
		}
		return local_size=l_idx;
	}

	protected int assign_parms() {
		String[] parms = getAllParmNames();
		int idx = 1;
		
		

		parameter: for (String name : parms) {
			for (LocalVariable loc : locals) {
				if (name.equals(loc.name)) {
					
					// System.out.println("local "+loc.name+" = "+idx);
					
					loc.setIndex(idx++);
					continue parameter;
				}
			}
			
			throw new InternalError("did not locate parameter " + name);
		}

		return idx;
	}

	public abstract int getCodeArgs();

	public boolean hasBlockParm() {
		return false;
	}

	public void describe_local(int i, Object value) {
		for (LocalVariable loc : locals) {
			if (loc.getIndex() == i) {
				System.out.println("local["+loc.name+":"+i+"]="+value);
				return;
			}
		}
		System.out.println("local[?:"+i+"]="+value);
	}

	public void describe_dvar(int i, IRubyObject value) {
		for (LocalVariable loc : locals) {
			if (loc.getDynamicIndex() == i) {
				System.out.println("dvar["+loc.name+":"+i+"]="+value);
				return;
			}
		}
		System.out.println("dvar[?:"+i+"]="+value);
	}

	public String getFileName() {
		return fileName;
	}

	public String getMethodName() {
		return null;
	}

	public String[] getDynamicNames() {
		String[] result = new String[getNumDynamics()];
		
		for (LocalVariable l : locals) {
			if (l.isDynamic) {
				result[l.getDynamicIndex()] = l.name;
			}
		}
		return result;

	}

	static Set<String> special_methods = new HashSet<String>();
	static {
		special_methods.add("eval");
		special_methods.add("local_variables");
		special_methods.add("binding");
		special_methods.add("block_given?");
	}
	
	public void method_call_here(String fname) {
		if (special_methods.contains(fname)) {
			
		//	System.out.println("call to "+fname+" in "+this);
			
			this.this_code_calls_eval = true;
		}
		
		if (context != null) {
			context.method_call_here(fname);
		}
	}

	public static boolean is_eval_like(String method) {
		return special_methods.contains(method);
	}

	public boolean codeCallsEval() {
		return this_code_calls_eval;
	}

}
