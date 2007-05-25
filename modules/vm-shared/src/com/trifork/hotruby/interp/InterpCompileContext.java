package com.trifork.hotruby.interp;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.trifork.hotruby.ast.BlockCode;
import com.trifork.hotruby.ast.ClassCode;
import com.trifork.hotruby.ast.Expression;
import com.trifork.hotruby.ast.MethodCode;
import com.trifork.hotruby.ast.ModuleCode;
import com.trifork.hotruby.ast.RubyCode;
import com.trifork.hotruby.interp.ISeq.Loop;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.ThreadState;
import com.trifork.hotruby.runtime.ThreadState.ModuleFrame;

public class InterpCompileContext implements CompileContext, Instructions {

	ISeqBuilder iseq;

	private RubyRuntime runtime;

	public InterpCompileContext(RubyRuntime runtime) {
		this.runtime = runtime;
	}

	public BindingContext compile(RubyCode source, MetaModule module,
			ModuleFrame frame, boolean is_module_code) {
		iseq = new ISeqBuilder(runtime, source, null, source.code_type(),
				source.getMethodName());
		source.compile(this);
		ISeq is = iseq.finish();
		return new BindingContext(is, module, module, frame, is_module_code);
	}

	public RubyRuntime getRuntime() {
		return runtime;
	}

	public void emit_alias(int new_sym1, int orig_sym2) {
		iseq.add_insn(ALIAS, upper16(new_sym1), lower16(new_sym1),
				upper16(orig_sym2), lower16(orig_sym2));
	}

	private void emit_branch(int insn, Label target) {
		final int insn_pos = iseq.add_insn(insn, 0, 0);
		target.patch(new BranchPatch(insn_pos));
	}

	public void emit_branch_unless(Label target) {
		if (finallys.size() == 0) {
			emit_branch(BRANCHUNLESS, target);
		} else {
			Label if_follow_branch = new_label();
			Label if_goto_next_insn = new_label();
			emit_branch(BRANCHUNLESS, if_follow_branch);
			emit_branch(JUMP, if_goto_next_insn);
			
			mark(if_follow_branch);
			emit_goto (target);
			
			mark(if_goto_next_insn);
		}
	}

	public void emit_branch_if(Label target) {
		if (finallys.size() == 0) {
			emit_branch(BRANCHIF, target);
		} else {
			Label if_follow_branch = new_label();
			Label if_goto_next_insn = new_label();
			emit_branch(BRANCHIF, if_follow_branch);
			emit_branch(JUMP, if_goto_next_insn);
			
			mark(if_follow_branch);
			emit_goto (target);
			
			mark(if_goto_next_insn);
		}
		
		
	}

	public void emit_goto(Label target) {
		do_finalizers_before_jump(target);
		emit_branch(JUMP, target);
	}

	private void do_finalizers_before_jump(Label target) {
		if (finallys.size() != 0) {
			for (int i = finallys.size() - 1; i >= 0; i--) {
				Label ensure_label = finallys.get(i).ensure_label;
				if (target.level() <= ensure_label.level()) {
					emit_local_jsr(ensure_label);
				}
			}
		}
	}
	
	private void do_finalizers_before_return() {
		for (int i = finallys.size() - 1; i >= 0; i--) {
			Label ensure_label = finallys.get(i).ensure_label;
			emit_local_jsr(ensure_label);
		}
	}

	public void emit_make_blockarg() {
		iseq.add_insn(PROC2BLOCK);
	}

	public void emit_make_restarg(int args_before_rest) {
		iseq.add_insn(EXPAND_REST_ARG, args_before_rest);
	}

	public void emit_make_returnvalue() {
		iseq.add_insn(MAKERETURN);
	}

	public void emit_return() {
		do_finalizers_before_return();
		if (iseq.source.code_type() == ISEQ_TYPE_BLOCK) {
			iseq.add_insn(NONLOCAL_RETURN);
		} else {
			iseq.add_insn(LEAVE);
		}
	}

	public void emit_invoke_block(int argc, boolean has_rest_arg,
			boolean push_result) {
		int flag = (has_rest_arg ? FLAG_REST_ARG : 0)
				| (push_result ? FLAG_PUSH_RESULT : 0);

		iseq.add_insn(INVOKEBLOCK, flag, argc);
	}

	public void emit_new_array(int size, boolean has_rest_arg) {
		iseq.add_insn(NEWARRAY, size, has_rest_arg ? 1 : 0);
	}
	
	public void emit_new_range(boolean inclusive) {
		iseq.add_insn(NEWRANGE, inclusive ? 1 : 0);
	}

	public void emit_unwrap_raise() {
		iseq.add_insn(UNWRAP_RAISE);
	}
	
	public void emit_throw() {
		iseq.add_insn(THROW);
	}
	
	// arr -> arr
	// other -> [other]
	public void emit_internal_to_a() {
		iseq.add_insn(INTERNAL_TO_A);
	}

	public void emit_push_constant(int idx) {
		if (idx < 256) {
			iseq.add_insn(PUSHOBJECT, idx);
		} else {
			iseq.add_insn(PUSHOBJECT2, idx >>> 8, idx & 0xff);
		}
	}

	public void emit_push_nil() {
		iseq.add_insn(PUSHNIL);
	}

	public void emit_push_self() {
		iseq.add_insn(PUSHSELF);
	}

	public void emit_stringconcat(int howmany) {
		iseq.add_insn(CONCATSTRINGS, howmany);
	}

	private int compile_block(BlockCode block) {

		ISeqBuilder save = iseq;
		iseq = new ISeqBuilder(getRuntime(), block, save, ISEQ_TYPE_BLOCK, null);

		block.compile((CompileContext) this);
		ISeq block_iseq = iseq.finish();

		iseq = save;

		int idx = iseq.add_iseq(block_iseq);

		return idx;
	}

	private int compile_code(RubyCode code) {

		ISeqBuilder save = iseq;
		iseq = new ISeqBuilder(getRuntime(), code, null, code.code_type(), code
				.getMethodName());

		code.compile((CompileContext) this);
		ISeq method_iseq = iseq.finish();

		iseq = save;

		int idx = iseq.add_iseq(method_iseq);

		return idx;
	}

	public void emit_send(String method, int n, boolean has_rest_arg,
			boolean has_block_arg, boolean push_result, BlockCode block) {

		int flags = (has_block_arg ? FLAG_BLOCK_ARG : 0)
				| (has_rest_arg ? FLAG_REST_ARG : 0)
				| (push_result ? FLAG_PUSH_RESULT : 0)
				| (block != null ? FLAG_IMM_BLOCK : 0);

		if (RubyCode.is_eval_like(method)) {
			flags |= FLAG_SEND_EVAL;
		}

		int selpos = iseq.add_selector(method);

		int blockpos = 0;
		if (block != null) {
			blockpos = compile_block(block);
		}

		iseq.add_insn(SEND, flags, n, upper16(selpos), lower16(selpos),
				upper16(blockpos), lower16(blockpos));
	}

	private int upper16(int selpos) {
		return (selpos >>> 8) & (0xff);
	}

	private int lower16(int blockpos) {
		return blockpos & 0xff;
	}

	public void emit_def(String name, MethodCode code, boolean singleton) {
		int methodpos = compile_code(code);
		int namepos = iseq.add_string(name);
		iseq.add_insn(DEFINEMETHOD, singleton ? 1 : 0, upper16(namepos),
				lower16(namepos), upper16(methodpos), lower16(methodpos));
	}

	public void emit_selfsend(String method, int n, boolean has_rest_arg,
			boolean has_block_arg, boolean push_result, BlockCode block) {

		int flags = (has_block_arg ? FLAG_BLOCK_ARG : 0)
				| (has_rest_arg ? FLAG_REST_ARG : 0)
				| (push_result ? FLAG_PUSH_RESULT : 0)
				| (block != null ? FLAG_IMM_BLOCK : 0);

		if (RubyCode.is_eval_like(method)) {
			flags |= FLAG_SEND_EVAL;
		}

		int selfm_pos = iseq.add_selfmethod(method);

		int blockpos = 0;
		if (block != null) {
			blockpos = compile_block(block);
		}

		iseq.add_insn(SELFSEND, flags, n, upper16(selfm_pos),
				lower16(selfm_pos), upper16(blockpos), lower16(blockpos));
	}

	public void emit_new_hash(int count) {
		iseq.add_insn(NEWHASH, count);
	}

	public void emit_dup() {
		iseq.add_insn(DUP);
	}

	public void emit_pop() {
		iseq.add_insn(POP);
	}

	public void emit_swap() {
		iseq.add_insn(SWAP);
	}

	public void emit_new_regex(int flags) {
		iseq.add_insn(NEWREGEX, flags);
	}

	public ConstantPool getConstantPool() {
		return iseq;
	}

	public void mark(Label label) {
		((LabelImpl) label).mark(iseq.position());
	}
	
	public void add_exception_handler(final Label body_start, final Label body_end, final Label handler_label) {
		final ExceptionHandlerInfo self = new ExceptionHandlerInfo();
		iseq.addExceptionHandler(self);
		
		body_start.patch(new LabelPatch() {
			public void defined(int label_pos) {
				self.start_pc = label_pos;
			}
		});

		body_end.patch(new LabelPatch() {
			public void defined(int label_pos) {
				self.end_pc = label_pos;
			}
		});

		handler_label.patch(new LabelPatch() {
			public void defined(int label_pos) {
				self.handler_pc = label_pos;
			}
		});
}

	public Label new_label() {
		return new LabelImpl();
	}

	private final class BranchPatch implements LabelPatch {
		private final int insn_pos;

		private BranchPatch(int insn_pos) {
			this.insn_pos = insn_pos;
		}

		public void defined(int target_pos) {
			int offset = target_pos - insn_pos;
			if (offset < Short.MIN_VALUE || offset > Short.MAX_VALUE) {
				throw new InternalError("branch too long");
			}
			iseq.set_operand(insn_pos, 1, upper16(offset));
			iseq.set_operand(insn_pos, 2, lower16(offset));
		}
	}

	class LabelImpl implements Label {
		int pos = -1;

		int level = finallys.size();
		
		public int level() {
			return level;
		}

		List<LabelPatch> patches;

		private ISeqBuilder owner;

		public LabelImpl() {
			owner = iseq;
		}

		public ISeqBuilder getIseq() {
			return owner;
		}

		public int position() {
			if (pos == -1) {
				throw new InternalError("label not defined");
			}
			return pos;
		}

		public void patch(LabelPatch patch) {
			if (patches == null) {
				patches = new ArrayList<LabelPatch>();
			}
			patches.add(patch);

			if (pos != -1) {
				patch.defined(pos);
			}

		}

		public void mark(int pos) {
			this.pos = pos;

			if (patches != null) {
				for (LabelPatch p : patches) {
					p.defined(pos);
				}
			}
		}
	}

	public void emit_new_symbol() {
		iseq.add_insn(NEWSYMBOL);
	}

	Stack<ControlContext> loops = new Stack<ControlContext>();

	class ControlContext {
		Label redo_label;

		Label next_label;

		Label break_label;

		final int start_pos;

		int end_pos;

		public ControlContext(Label break_label, Label redo_label,
				Label next_label, int start_pos) {
			this.redo_label = redo_label;
			this.break_label = break_label;
			this.next_label = next_label;
			this.start_pos = start_pos;
		}

		public Loop makeLoop() {
			Loop result = new Loop();
			result.break_pos = break_label.position();
			result.next_pos = next_label.position();
			result.redo_pos = redo_label.position();

			result.start_pos = start_pos;
			result.end_pos = end_pos;

			return result;
		}
	}

	public void push_loop_context(Label break_label, Label redo_label,
			Label next_label) {
		ControlContext cc = new ControlContext(break_label, redo_label,
				next_label, iseq.position());
		loops.push(cc);
		iseq.add_control_context(cc);
	}

	public void pop_loop_context() {
		ControlContext cc = loops.pop();
		cc.end_pos = iseq.position();
	}

	public void emit_break() {
		for (int i = loops.size(); i > 0; i--) {
			ControlContext lc = loops.get(i - 1);
			if (lc.break_label != null) {
				emit_goto(lc.break_label);
				return;
			}
		}

		this.do_finalizers_before_return();
		iseq.add_insn(NONLOCAL_BREAK);
	}

	public void emit_next() {
		for (int i = loops.size(); i > 0; i--) {
			ControlContext lc = loops.get(i - 1);
			if (lc.next_label != null) {
				emit_goto(lc.next_label);
				return;
			}
		}

		this.do_finalizers_before_return();
		iseq.add_insn(NONLOCAL_NEXT);
	}

	public void emit_redo() {
		for (int i = loops.size(); i > 0; i--) {
			ControlContext lc = loops.get(i - 1);
			if (lc.redo_label != null) {
				emit_goto(lc.redo_label);
				return;
			}
		}

		this.do_finalizers_before_return();
		iseq.add_insn(NONLOCAL_REDO);
	}

	public void emit_retry() {
		throw new InternalError("not implemented");
	}

	public void emit_setdynamic(int level, int index) {
		iseq.add_insn(SETDYNAMIC, level, index);
	}

	public void emit_setlocal(int index) {
		iseq.add_insn(SETLOCAL, index);
	}

	public void emit_getdynamic(int level, int index) {
		iseq.add_insn(GETDYNAMIC, level, index);
	}

	public void emit_getlocal(int index) {
		iseq.add_insn(GETLOCAL, index);
	}

	public int get_code_position() {
		return iseq.position();
	}

	public void emit_push_false() {
		emit_push_constant(CPOOL_FALSE);
	}

	public void emit_push_true() {
		emit_push_constant(CPOOL_TRUE);
	}

	public void emit_leave() {
		iseq.add_insn(LEAVE);
	}

	public void emit_array_at(int idx) {
		iseq.add_insn(ARRAY_AT, idx);
	}

	public void emit_array_rest(int idx) {
		iseq.add_insn(ARRAY_REST, idx);
	}

	public void emit_dup_n(int idx) {
		assert idx > 0;
		if (idx == 1) {
			emit_dup();
		} else {
			iseq.add_insn(DUPN, idx);
		}
	}

	public void emit_pop_n(int count) {
		if (count == 0) {
			return;
		} else if (count == 1) {
			emit_pop();
		} else {
			assert count > 0;
			iseq.add_insn(POPN, count);
		}
	}

	public int get_stack_depth() {
		return iseq.stack_depth;
	}

	public void set_stack_depth(int depth) {
		iseq.stack_depth = depth;
	}

	public void emit_java_array_at(int i) {
		iseq.add_insn(JAVA_ARRAY_AT, i);
	}

	public void emit_java_array_rest(int i) {
		iseq.add_insn(JAVA_ARRAY_REST, i);
	}

	public int alloc_temp(int howmany) {
		return iseq.alloc_temp(howmany);
	}

	public void free_temp(int first_temp) {
		iseq.free_temp(first_temp);
	}

	public void emit_getglobal(String name) {
		int idx = iseq.addGlobal(name);
		iseq.add_insn(GETGLOBAL, idx);
	}

	public void emit_setglobal(String name) {
		int idx = iseq.addGlobal(name);
		iseq.add_insn(SETGLOBAL, idx);
	}

	public void emit_getivar(String name) {
		int idx = iseq.addIVar(name);
		iseq.add_insn(GETINSTANCEVARIABLE, upper16(idx), lower16(idx));
	}

	public void emit_setivar(String name) {
		int idx = iseq.addIVar(name);
		iseq.add_insn(SETINSTANCEVARIABLE, upper16(idx), lower16(idx));
	}

	public void emit_getconst(String name) {
		int idx = iseq.add_const(name);
		iseq.add_insn(GETCONSTANT, upper16(idx), lower16(idx));

	}

	public void emit_setconst(String name) {
		int idx = iseq.add_const(name);
		iseq.add_insn(SETCONSTANT, upper16(idx), lower16(idx));
	}

	public void emit_push_class_object() {
		emit_getconst("::Object");
	}

	// stack contains superclass (if given)
	public void emit_class(String name, ClassCode code,
			boolean super_class_given) {

		int name_id = name == null ? 0 : iseq.add_string(name);
		int iseq_id = compile_code(code);
		iseq
				.add_insn(
						DEFINECLASS,
						(super_class_given ? Instructions.FLAG_DEFINECLASS_SUPER_CLASS_GIVEN
								: 0)
								| (name == null ? Instructions.FLAG_DEFINECLASS_IS_SINGLETON
										: 0), upper16(name_id),
						lower16(name_id), upper16(iseq_id), lower16(iseq_id));

	}

	public void emit_module(String name, ModuleCode code) {
		int name_id = iseq.add_string(name);
		int iseq_id = compile_code(code);
		iseq.add_insn(DEFINEMODULE, upper16(name_id), lower16(name_id),
				upper16(iseq_id), lower16(iseq_id));
	}

	boolean last_was_line = false;

	int last_line;

	private List<Expression> parms;

	private int optional_idx = -1;

	// the finallys that are current
	private Stack<FinallyBlockInfo> finallys = new Stack<FinallyBlockInfo>();

	// finallys which have been finishes
	private Stack<FinallyBlockInfo> finallys_done = new Stack<FinallyBlockInfo>();

	public void emit_trace(int event, int line) {
		if (event == TRACE_LINE && ((last_was_line && line == last_line) ||  line != -1)) {
			return;
		}
		iseq.add_insn(TRACE, event, upper16(line), lower16(line));
		last_line = line;
		last_was_line = (event == TRACE_LINE);
	}

	public void emit_lt() {
		int sel = iseq.add_selector("<");
		iseq.add_insn(FAST_LT, upper16(sel), lower16(sel));
	}

	public void emit_le() {
		int sel = iseq.add_selector("<=");
		iseq.add_insn(FAST_LE, upper16(sel), lower16(sel));
	}

	public void emit_ge() {
		int sel = iseq.add_selector(">=");
		iseq.add_insn(FAST_GE, upper16(sel), lower16(sel));
	}

	public void emit_eqtilde() {
		int sel = iseq.add_selector("=~");
		iseq.add_insn(FAST_EQTILDE, upper16(sel), lower16(sel));
	}

	public void emit_gt() {
		int sel = iseq.add_selector("<");
		iseq.add_insn(FAST_GT, upper16(sel), lower16(sel));
	}

	public void emit_bit_or() {
		int sel = iseq.add_selector("|");
		iseq.add_insn(FAST_BIT_OR, upper16(sel), lower16(sel));
	}

	public void emit_bit_xor() {
		int sel = iseq.add_selector("^");
		iseq.add_insn(FAST_BIT_XOR, upper16(sel), lower16(sel));
	}

	public void emit_rshft() {
		int sel = iseq.add_selector(">>");
		iseq.add_insn(FAST_RSHIFT, upper16(sel), lower16(sel));
	}

	public void emit_bit_and() {
		int sel = iseq.add_selector("&");
		iseq.add_insn(FAST_BIT_AND, upper16(sel), lower16(sel));
	}

	public void emit_eq2() {
		int sel = iseq.add_selector("==");
		iseq.add_insn(FAST_EQ2, upper16(sel), lower16(sel));
	}

	public void emit_eq3() {
		int sel = iseq.add_selector("===");
		iseq.add_insn(FAST_EQ3, upper16(sel), lower16(sel));
	}

	public void emit_bit_not() {
		int sel = iseq.add_selector("~");
		iseq.add_insn(FAST_BIT_NOT, upper16(sel), lower16(sel));
	}

	public void emit_plus() {
		int sel = iseq.add_selector("+");
		iseq.add_insn(FAST_PLUS, upper16(sel), lower16(sel));
	}

	public void emit_minus() {
		int sel = iseq.add_selector("-");
		iseq.add_insn(FAST_MINUS, upper16(sel), lower16(sel));
	}

	public List<Expression> set_method_parms(List<Expression> parms) {
		List<Expression> result = this.parms;
		this.parms = parms;
		return result;
	}

	public List<Expression> get_this_method_parms() {
		return this.parms;
	}

	public void emit_supersend(int n, boolean has_rest_arg,
			boolean has_block_arg, boolean push_result, BlockCode block) {

		int flags = (has_block_arg ? FLAG_BLOCK_ARG : 0)
				| (has_rest_arg ? FLAG_REST_ARG : 0)
				| (push_result ? FLAG_PUSH_RESULT : 0)
				| (block != null ? FLAG_IMM_BLOCK : 0);

		String method = iseq.getMethodName();

		if (RubyCode.is_eval_like(method)) {
			flags |= FLAG_SEND_EVAL;
		}

		iseq.add_supermethod();

		int blockpos = 0;
		if (block != null) {
			blockpos = compile_block(block);
		}

		iseq.add_insn(INVOKESUPER, flags, n, upper16(blockpos),
				lower16(blockpos));

	}

	public void set_is_compiling_default_value(int optional_idx) {
		this.optional_idx = optional_idx;
	}

	// while compiling default values (rhs of an optional parm), this
	// value equals the optional parameter number
	public int get_is_compiling_default_values() {
		return optional_idx == -1 ? -1 : iseq.source.getMinParmCount()
				+ optional_idx;
	}

	public void emit_getclassvar(String name) {
		throw new InternalError("not implemented: get classvar");
	}

	public void emit_setclassvar(String name) {
		throw new InternalError("not implemented: set classvar");
	}

	public void emit_local_jsr(Label ensure_label) {
		emit_branch(LOCAL_JSR, ensure_label);
	}

	public void emit_local_return(int local_register) {
		iseq.add_insn(LOCAL_RETURN, upper16(local_register), lower16(local_register));
	}

	public void pop_finally_handler() {
		finallys_done.push(finallys.pop());
	}

	public void push_finally_handler(Label ensure_label) {
		finallys.push(new FinallyBlockInfo(iseq.position(), ensure_label));
	}

}
