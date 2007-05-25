package com.trifork.hotruby.interp;

import java.util.List;

import com.trifork.hotruby.ast.BlockCode;
import com.trifork.hotruby.ast.ClassCode;
import com.trifork.hotruby.ast.Expression;
import com.trifork.hotruby.ast.MethodCode;
import com.trifork.hotruby.ast.ModuleCode;
import com.trifork.hotruby.runtime.RubyRuntime;

public interface CompileContext {

	ConstantPool getConstantPool();

	void emit_push_constant(int idx);

	/** make a new label which has no position */
	Label new_label();
	
	/** set label to current position */
	void mark(Label label);

	void emit_branch_unless(Label target);

	void emit_goto(Label label);

	void emit_push_nil();
	void emit_push_self();

	/** convert top-of-stack to an array 
	 *  (used when evaluating *x, evaluation of x is on top of stack) */
	void emit_make_restarg(int count);

	/** unwrap proc and leave real RubyBlock object on stack */
	void emit_make_blockarg();

	/** construct a new array by popping size elements (+1 if has_rest_arg) */
	void emit_new_array(int size, boolean has_rest_arg);

	/** emit send instruction  
	 * 
	 * stack: receiver, arg1, arg2, ..., argN, [rest], [block-instance]
	 * 
	 **/
	void emit_send    (String method, int arg_count, boolean has_rest_arg, boolean has_block_arg, boolean push_result, BlockCode block);
	void emit_selfsend(String method, int arg_count, boolean has_rest_arg, boolean has_block_arg, boolean push_result, BlockCode block);


	/** */
	void emit_alias(int sym1, int sym2);

	void emit_stringconcat(int i);

	RubyRuntime getRuntime();

	void emit_new_hash(int i);

	void emit_dup();

	// string object is on stack
	void emit_new_regex(int flags);

	void emit_pop();

	void emit_invoke_block(int argc, boolean has_rest_arg, boolean push_result);

	void emit_new_symbol();

	void emit_branch_if(Label start_label);

	void push_loop_context(Label break_label, Label redo_label, Label next_label);

	void pop_loop_context();

	void emit_break();

	void emit_next();

	void emit_redo();

	void emit_return();

	void emit_make_returnvalue();

	void emit_retry();

	int get_code_position();

	void emit_setlocal(int index);

	void emit_setdynamic(int level, int index);

	void emit_getlocal(int index);

	void emit_getdynamic(int level, int index);

	void emit_push_false();

	void emit_leave();

	void emit_push_true();

	void emit_internal_to_a();

	// used in multi-assignment
	void emit_array_at(int idx);
	void emit_array_rest(int idx);

	void emit_dup_n(int idx);

	void emit_pop_n(int count);

	int get_stack_depth();

	void set_stack_depth(int depth);

	void emit_java_array_at(int i);

	void emit_java_array_rest(int i);

	int alloc_temp(int howmany);

	void free_temp(int first_temp);

	void emit_getglobal(String name);
	void emit_setglobal(String name);

	void emit_def(String name, MethodCode code, boolean singleton);

	void emit_getconst(String name);

	void emit_setconst(String name);

	void emit_push_class_object();

	void emit_class(String name, ClassCode code, boolean super_class_given);
	
	void emit_trace(int event, int line);

	void emit_plus();

	void emit_minus();

	void emit_lt();

	void emit_module(String name, ModuleCode code);

	void emit_getivar(String name);
	void emit_setivar(String name);

	void emit_bit_or();

	void emit_bit_not();

	void emit_bit_and();

	void emit_eq2();

	void emit_gt();

	void emit_swap();

	void emit_bit_xor();

	void emit_rshft();

	void emit_le();

	List<Expression> set_method_parms(List<Expression> parms);

	List<Expression> get_this_method_parms();

	void emit_supersend(int arg_count, boolean has_rest_arg, boolean has_block_arg, boolean push, BlockCode block);

	void set_is_compiling_default_value(int i);

	int get_is_compiling_default_values();

	void emit_getclassvar(String name);

	void emit_setclassvar(String name);

	void emit_eq3();

	void push_finally_handler(Label ensure_label);

	void pop_finally_handler();

	void emit_local_jsr(Label ensure_label);

	void emit_local_return(int local_register);

	void add_exception_handler(Label body_start, Label body_end, Label handler_label);

	void emit_new_range(boolean inclusive);

	void emit_unwrap_raise();

	void emit_ge();

	void emit_eqtilde();

	void emit_throw();
}
