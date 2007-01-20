package com.trifork.hotruby.interp;

import com.trifork.hotruby.ast.RubyCode;
import com.trifork.hotruby.runtime.ConstantAccessor;
import com.trifork.hotruby.runtime.Global;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.NonLocalJump;
import com.trifork.hotruby.runtime.RubyIvarAccessor;
import com.trifork.hotruby.runtime.RubyMethodAccessor;
import com.trifork.hotruby.runtime.RubyRuntime;

public class ISeq implements Instructions {

	public final int code_args;

	private RubyRuntime runtime;

	private int code_type;

	private final String[] constants;
	final String[] globals;

	private RubyCode source;

	public final String[] selfmethods;

	public final String[] ivars;

	public ISeq(ISeqBuilder builder) {
		this.runtime = builder.runtime;
		this.code = builder.getCode();
		this.loops = builder.getLoops();
		this.cpool = builder.getConstantPool();
		this.setCodeType(builder.code_type());
		this.globals = builder.getGlobals();
		this.ivars = builder.getIvars();
		this.super_access = builder.calls_super;
		this.source = builder.source;
		this.handlers = builder.getExceptionHanders();
		num_locals = source.getNumLocals() + builder.getTemps();
		num_dynamics = source.getNumDynamics();
		min_parm_count = source.getMinParmCount();
		has_rest_parm = source.hasRestParm();
		has_block_parm = source.hasBlockParm();
		eval_default_args = source.getDefaultArgEvaluation();
		default_parm_count = eval_default_args.length-1;
		dynamics_from = source.getDynamicsFromArgs();
		selectors = builder.getSelectors();
		selfmethods = builder.getSelfMethods();
		constants = builder.getConstants();		
		code_args = source.getCodeArgs();
		this.fileName = source.getFileName();
		this.methodName = source.getMethodName();
		this.firstLine =source.line();
		name_dynamics = source.getDynamicNames();
		this.code_calls_eval = source.codeCallsEval();
	}

	RubyRuntime getRuntime() {
		return runtime;
	}

	static class Loop {
		// range of loop
		int start_pos, end_pos;

		// target of non-local branches
		int next_pos, redo_pos, break_pos;

		int get(int kind) {
			switch (kind) {
			case NonLocalJump.NONLOCAL_BREAK:
				return break_pos;
			case NonLocalJump.NONLOCAL_NEXT:
				return next_pos;
			case NonLocalJump.NONLOCAL_REDO:
				return redo_pos;
			default:
				throw new InternalError("unknown kind for non-local jump");
			}
		}
	}

	final Loop[] loops;
	final ExceptionHandler[] handlers;

	int visibility;

	private final int num_locals;

	public final int num_dynamics; // local vars accessed from inner scope

	final String[] name_dynamics;
	
	public final int[] dynamics_from; // copy dynamic[i] from stack[self_sp +

	// dynamics_from[i]]

	public final int min_parm_count; // required args

	public final int default_parm_count; // default args

	public final boolean has_rest_parm;

	private final Object[] cpool;

	final byte[] code;

	public final int[] eval_default_args;

	private final String[] selectors;

	public boolean has_block_parm;

	private String methodName;

	private String fileName;

	private int firstLine;

	public final boolean code_calls_eval;

	private boolean super_access;

	public boolean callsSuper() {
		return super_access;
	}
	
	public int getArity() {
		if (default_parm_count > 0 || has_rest_parm) {
			return -1 - min_parm_count;
		} else {
			return min_parm_count;
		}
	}

	public Global[] getGlobals(RubyRuntime runtime) {
		Global[] result = new Global[globals.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = runtime.getGlobal(globals[i]);
		}
		return result;
	}

	public ConstantAccessor[] getConstants(MetaModule module) {
		ConstantAccessor[] result = new ConstantAccessor[constants.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = module.getConstantAccessor(constants[i]);
		}
		return result;
	}

	public RubyMethodAccessor[] getMethodAccessors(MetaModule module, boolean self_is_module) {
		RubyMethodAccessor[] result = new RubyMethodAccessor[selfmethods.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = module.getMethodAccessor(selfmethods[i], self_is_module);
		}
		return result;
	}
	

	public void dump_locals(Object[] stack, int frame_sp, int sp, IterpExposedLocals dvars) {

		if (getCodeType() == Instructions.ISEQ_TYPE_METHOD) {
			System.out.println("method self="+stack[frame_sp]);
			
			for (int i = frame_sp+1; i < sp; i++) {
				source.describe_local(i-frame_sp, stack[i]);
			}
			
			for (int i = 0; i < dvars.count(); i++) {
				source.describe_dvar(i, dvars.get(i));
			}
		}
		
	}

	public String getFile() {
		return fileName;
	}
	
	public String getMethod() {
		return methodName;
	}
	
	public int firstLine() {
		return firstLine;
	}

	public byte[] getCode() {
		return code;
	}

	public int getNumLocals() {
		return num_locals;
	}

	public String[] getSelfMethods() {
		return selfmethods;
	}

	public String[] getSelectors() {
		return selectors;
	}

	public Object[] getCPool() {
		return cpool;
	}

	public void setCodeType(int code_type) {
		this.code_type = code_type;
	}

	public int getCodeType() {
		return code_type;
	}

	public String[] getConstants() {
		return constants;
	}

	public RubyIvarAccessor[] getIVarAccessors(MetaModule dynamic_context, boolean self_is_module) {
		RubyIvarAccessor[] result = new RubyIvarAccessor[ivars.length];
		for (int i = 0; i < ivars.length; i++) {
			if (self_is_module) {
				result[i] = dynamic_context.getModuleIVarAccessor(ivars[i]);
			} else {
				result[i] = dynamic_context.getInstanceIVarAccessor(ivars[i], true);
			}
		}
		return result;
	}

	public String[] getDVarNames() {
		return name_dynamics;
	}

	public RubyMethodAccessor getSuperMethod(MetaModule lexical_context, boolean self_is_module) {
		if (super_access) {
			return lexical_context.getSuperMethodAccessor(methodName, self_is_module);
		} else {
			return null;
		}
	}

	public ExceptionHandler[] getExceptionHandlers() {
		return handlers;
	}

}
