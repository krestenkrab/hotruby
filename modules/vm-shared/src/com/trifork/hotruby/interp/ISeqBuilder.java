package com.trifork.hotruby.interp;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.ast.RubyCode;
import com.trifork.hotruby.interp.ISeq.Loop;
import com.trifork.hotruby.interp.InterpCompileContext.ControlContext;
import com.trifork.hotruby.runtime.RubyRuntime;

public class ISeqBuilder implements ConstantPool, Instructions {

	private static final Loop[] NO_LOOPS = new Loop[0];

	RubyRuntime runtime;

	List<Object> cpool;

	private List<String> globals;

	List<String> selfmethods = new ArrayList<String>();

	List<String> selectors = new ArrayList<String>();

	List<String> constants = new ArrayList<String>();

	final ISeqBuilder parent_iseq;

	int stack_depth = 0;

	final RubyCode source;

	private final int code_type;

	private int temp_pos;

	private int max_temp;

	private final int first_temp;

	public ISeqBuilder(RubyRuntime runtime, RubyCode source,
			ISeqBuilder parent_iseq, int code_type, String method_name) {
		this.runtime = runtime;
		this.source = source;
		this.parent_iseq = parent_iseq;
		this.code_type = code_type;
		this.cpool = new ArrayList<Object>();
		this.globals = new ArrayList<String>();
		this.first_temp = this.temp_pos = source.assign_local_indexes();
		this.max_temp = temp_pos;
		add_cpool(runtime.getNil());
		add_cpool(runtime.getTrue());
		add_cpool(runtime.getFalse());
		this.method_name = method_name;
	}

	byte[] data = new byte[10];

	int pos = 0;

	void append(int value) {
		if (data.length < pos + 1) {
			byte[] new_data = new byte[data.length * 2];
			System.arraycopy(data, 0, new_data, 0, data.length);
			data = new_data;
		}

		assert (value & 0xffffff00) == 0 || (value & 0xffffff00) == 0xffffff00;
		// assert value == (byte) value;

		data[pos++] = (byte) value;
	}

	private void adjust_stack(int pos) {
		int old_pos = stack_depth;
		adjust_stack0(pos);
		if (stack_depth < 0) {
			throw new InternalError("stack emptied: was:" + old_pos
					+ "; after: " + stack_depth);
		}
	}

	private void adjust_stack0(int pos) {
		int insn = data[pos];
		switch (insn) {

		case RETURN:
		case FAST_LT:
		case FAST_LE:
		case FAST_GT:
		case FAST_PLUS:
		case FAST_MINUS:
		case FAST_BIT_OR:
		case FAST_BIT_AND:
		case FAST_BIT_XOR:
		case FAST_RSHIFT:
		case FAST_EQ2:
		case FAST_EQ3:
			stack_depth -= 1;
			return;
		
		case UNWRAP_RAISE:
		case SWAP:
		case FAST_BIT_NOT:
		case DEFINEMODULE:
		case JUMP:
		case NOP:
		case ALIAS:
		case PROC2BLOCK:
			return;

		case SETINSTANCEVARIABLE:
		case SETCONSTANT:
		case SETGLOBAL:
		case POP:
		case LEAVE:
		case BRANCHIF:
		case BRANCHUNLESS:
		case SETDYNAMIC:
		case SETSPECIAL:
		case SETLOCAL:
			stack_depth -= 1;
			return;

		case GETINSTANCEVARIABLE:
		case GETCONSTANT:
		case GETGLOBAL:
		case PUSHNIL:
		case PUSHOBJECT:
		case PUSHOBJECT2:
		case PUSHSELF:
		case GETDYNAMIC:
		case GETSPECIAL:
		case GETLOCAL:
			stack_depth += 1;
			return;

		case NEWRANGE: {
			stack_depth -= 2;
			stack_depth += 1;
			return;
		}
			
		case NEWARRAY: {
			int nelems = data[pos + 1];
			boolean has_rest_arg = data[pos + 2] != 0;

			stack_depth -= nelems;
			if (has_rest_arg) {
				stack_depth -= 1;
			}
			stack_depth += 1;
			return;
		}

		case CONCATSTRINGS: {
			int nstrings = data[pos + 1];
			stack_depth = stack_depth - nstrings + 1;
			return;
		}

		case INVOKEBLOCK: {
			int flags = data[pos + 1];
			int nargs = data[pos + 2] & 0xff;

			boolean has_rest_arg = (flags & FLAG_REST_ARG) != 0;
			boolean push_result = (flags & FLAG_PUSH_RESULT) != 0;

			int delta = 0; // there is no receiver for block
			if (has_rest_arg) {
				delta -= 1;
			} else {
				delta -= nargs;
			}

			if (push_result) {
				delta += 1;
			}

			stack_depth += delta;
			return;

		}

		case SEND:
		case SELFSEND: {
			int flags = data[pos + 1];
			int nargs = data[pos + 2];

			boolean has_amp_block_arg = (flags & FLAG_BLOCK_ARG) != 0;
			boolean has_rest_arg = (flags & FLAG_REST_ARG) != 0;
			boolean push_result = (flags & FLAG_PUSH_RESULT) != 0;

			int delta = -1; // receiver is popped
			if (has_rest_arg) {
				delta -= 1;
			} else {
				delta -= nargs;
			}

			if (has_amp_block_arg) {
				delta -= 1;
			}

			if (push_result) {
				delta += 1;
			}

			stack_depth += delta;
			return;
		}

		case INVOKESUPER: {
			int flags = data[pos + 1];
			int nargs = data[pos + 2];

			boolean has_amp_block_arg = (flags & FLAG_BLOCK_ARG) != 0;
			boolean has_rest_arg = (flags & FLAG_REST_ARG) != 0;
			boolean push_result = (flags & FLAG_PUSH_RESULT) != 0;

			int delta = -1; // receiver is popped
			if (has_rest_arg) {
				delta -= 1;
			} else {
				delta -= nargs;
			}

			if (has_amp_block_arg) {
				delta -= 1;
			}

			if (push_result) {
				delta += 1;
			}

			stack_depth += delta;
			return;
		}

		case ARRAY_AT: {
			stack_depth += 1;
			return;
		}

		case ARRAY_REST: {
			stack_depth += 1;
			return;
		}

		case INTERNAL_TO_A: {
			return;
		}

		case DUP: {
			stack_depth += 1;
			return;
		}

		case DUPN: {
			stack_depth += 1;
			return;
		}

		case POPN: {
			int count = data[pos + 1];
			stack_depth -= count;
			return;
		}

		case EXPAND_REST_ARG: {
			stack_depth -= data[pos + 1];
			return;
		}

		case TRACE: {
			return;
		}

		case DEFINEMETHOD: {
			if (data[pos + 1] == 1) {
				// pop whom to define method into (for singleton methods)
				stack_depth -= 1;
			}
			return;
		}

		case JAVA_ARRAY_AT:
		case JAVA_ARRAY_REST: {
			// take array, leave value
			return;
		}

		case NEWREGEX: {
			// do nothing
			return;
		}

		case NEWHASH: {
			stack_depth += 1;
			return;
		}

		case DEFINECLASS: {
			if ((data[pos+1] & Instructions.FLAG_DEFINECLASS_SUPER_CLASS_GIVEN) != 0) {
				stack_depth -= 1; // super class
			}
			return;
		}
		
		case LOCAL_JSR: {
			// stack_depth += 1;
			return;
		}
		
		case LOCAL_RETURN: {
			// stack_depth -= 1;
			return;
		}
		
		case NONLOCAL_BREAK: 
		case NONLOCAL_NEXT: 
		case NONLOCAL_REDO: 
		{
			return;
		}

		default:
			throw new InternalError("unhandled instruction: " + insn);
		}

	}

	public int addGlobal(String gval) {
		int idx = globals.indexOf(gval);
		if (idx == -1) {
			idx = globals.size();
			globals.add(gval);
		}
		return idx;
	}

	public int add_insn(int insn) {
		int result = pos;
		append(insn);
		adjust_stack(result);
		return result;
	}

	public int add_insn(int insn, int op1) {
		int result = pos;
		append(insn);
		append(op1);
		adjust_stack(result);
		return result;
	}

	public int add_insn(int insn, int op1, int op2) {
		int result = pos;
		append(insn);
		append(op1);
		append(op2);
		adjust_stack(result);
		return result;
	}

	public int add_insn(int insn, int op1, int op2, int op3) {
		int result = pos;
		append(insn);
		append(op1);
		append(op2);
		append(op3);
		adjust_stack(result);
		return result;
	}

	public int add_insn(int insn, int op1, int op2, int op3, int op4) {
		int result = pos;
		append(insn);
		append(op1);
		append(op2);
		append(op3);
		append(op4);
		adjust_stack(result);
		return result;
	}

	public int add_insn(int insn, int op1, int op2, int op3, int op4, int op5) {
		int result = pos;
		append(insn);
		append(op1);
		append(op2);
		append(op3);
		append(op4);
		append(op5);
		adjust_stack(result);
		return result;
	}

	public int add_insn(int insn, int op1, int op2, int op3, int op4, int op5,
			int op6) {
		int result = pos;
		append(insn);
		append(op1);
		append(op2);
		append(op3);
		append(op4);
		append(op5);
		append(op6);
		adjust_stack(result);
		return result;
	}

	public void set_operand(int insn_pos, int operand, int new_value) {
		// assert new_value == (byte) new_value;
		data[insn_pos + operand] = (byte) new_value;
	}

	public int position() {
		return pos;
	}

	public int add_integer(String text, int radix) {
		return add_cpool(runtime.newInteger(text, radix));
	}

	private int add_cpool(Object value) {
		if (parent_iseq != null) {
			return parent_iseq.add_cpool(value);
		}
		int idx = cpool.indexOf(value);
		if (idx != -1) {
			return idx;
		} else {
			idx = cpool.size();
			cpool.add(value);
			return idx;
		}
	}

	public int add_integer(int intvalue) {
		return add_cpool(runtime.newFixnum(intvalue));
	}

	public int add_symbol(String sym1) {
		return add_cpool(runtime.newSymbol(sym1));
	}

	public int add_regex(String regex, int flags) {
		return add_cpool(runtime.newRegexp(regex, flags));
	}

	// argument is the call context to run this iseq in
	public ISeq finish() {
		return new ISeq(this);
	}

	public int add_iseq(ISeq block_iseq) {
		return add_cpool(block_iseq);
	}

	public int add_selector(String method) {

		if (parent_iseq != null) {
			return parent_iseq.add_selector(method);
		}

		int idx = selectors.indexOf(method);
		if (idx != -1) {
			return idx;
		} else {
			idx = selectors.size();
			selectors.add(runtime.intern(method));
			return idx;
		}
	}

	public int add_float(double val) {
		return add_cpool(runtime.newFloat(val));
	}

	public int add_string(String text) {
		return add_cpool(runtime.newString(runtime.intern(text)));
	}

	List<ControlContext> loop_contexts = new ArrayList<ControlContext>();

	private List<String> ivars = new ArrayList<String>();

	private String method_name;

	boolean calls_super;

	private List<ExceptionHandlerInfo> exceptionHandlers = new ArrayList<ExceptionHandlerInfo>();

	public void add_control_context(ControlContext cc) {
		loop_contexts.add(cc);
	}

	public byte[] getCode() {
		byte[] result = new byte[pos];
		System.arraycopy(data, 0, result, 0, pos);
		return result;
	}

	public Loop[] getLoops() {
		if (loop_contexts == null || loop_contexts.size() == 0) {
			return NO_LOOPS;
		} else {
			Loop[] result = new Loop[loop_contexts.size()];
			for (int i = 0; i < loop_contexts.size(); i++) {
				result[i] = loop_contexts.get(i).makeLoop();
			}
			return result;
		}
	}

	public Object[] getConstantPool() {
		return cpool.toArray();
	}

	public String[] getSelectors() {
		return selectors.toArray(new String[selectors.size()]);
	}

	public String[] getSelfMethods() {
		return selfmethods.toArray(new String[selfmethods.size()]);
	}

	public int code_type() {
		return code_type;
	}

	public String[] getGlobals() {
		return globals.toArray(new String[globals.size()]);
	}

	public int add_const(String name) {
		if (parent_iseq != null) {
			return parent_iseq.add_const(name);
		}

		int idx = constants.indexOf(name);
		if (idx != -1) {
			return idx;
		} else {
			idx = constants.size();
			constants.add(runtime.intern(name));
			return idx;
		}
	}

	public String[] getConstants() {
		return constants.toArray(new String[constants.size()]);
	}

	public int alloc_temp(int howmany) {
		int result = temp_pos;
		temp_pos += howmany;
		max_temp = Math.max(temp_pos, max_temp);
		return result;
	}

	public void free_temp(int first_temp) {
		temp_pos = first_temp;
	}

	public int getTemps() {
		return max_temp - first_temp;
	}

	public int add_selfmethod(String method) {

		if (parent_iseq != null) {
			return parent_iseq.add_selfmethod(method);
		}

		int idx = selfmethods.indexOf(method);
		if (idx != -1) {
			return idx;
		} else {
			idx = selfmethods.size();
			selfmethods.add(runtime.intern(method));
			return idx;
		}
	}

	public void add_supermethod() {

		if (parent_iseq != null) {
			parent_iseq.add_supermethod();
		}

		this.calls_super = true;
	}

	public int addIVar(String name) {
		if (parent_iseq != null) {
			return parent_iseq.addIVar(name);
		}

		int idx = ivars.indexOf(name);
		if (idx != -1) {
			return idx;
		} else {
			idx = ivars.size();
			ivars.add(runtime.intern(name));
			return idx;
		}
	}

	public String[] getIvars() {
		return ivars.toArray(new String[ivars.size()]);
	}

	public String getMethodName() {
		if (parent_iseq != null) {
			return parent_iseq.getMethodName();
		} else {
			return this.method_name;
		}
	}

	void addExceptionHandler(ExceptionHandlerInfo handler)
	{
		exceptionHandlers.add(handler);
	}
	
	public ExceptionHandlerInfo[] getExceptionHanders() {
		return exceptionHandlers.toArray(new ExceptionHandlerInfo[exceptionHandlers.size()]);
	}

}
