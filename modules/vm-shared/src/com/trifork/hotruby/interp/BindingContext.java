package com.trifork.hotruby.interp;

import com.trifork.hotruby.compiler.MethodCompiler;
import com.trifork.hotruby.interp.ISeq.Loop;
import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyProc;
import com.trifork.hotruby.objects.IRubyRegexp;
import com.trifork.hotruby.objects.IRubyString;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.Global;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.NewProcException;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalJump;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;
import com.trifork.hotruby.runtime.RaisedException;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyIvarAccessor;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyMethodAccessor;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.runtime.ThreadState;
import com.trifork.hotruby.runtime.ThreadState.ModuleFrame;

public class BindingContext implements Instructions {

	// this is the exact type of self, because an BindingContext is always
	// specialized
	final MetaModule dynamic_context;

	// this is the template method
	final ISeq method_template;

	final LexicalBindings lexical_bindings;

	public RubyMethodAccessor[] self_methods;

	private RubyIvarAccessor[] ivar_accessors;

	// will this run with a self of type module/class
	final boolean self_is_module;

	public BindingContext(ISeq method_template, MetaModule lexical_context,
			MetaModule dynamic_context, ModuleFrame frame,
			boolean self_is_module) {
		this(method_template, new LexicalBindings(method_template,
				lexical_context, frame, self_is_module), dynamic_context,
				self_is_module);
	}

	public BindingContext(ISeq method_template,
			LexicalBindings lexical_bindings, MetaModule dynamic_context,
			boolean self_is_module) {
		this.method_template = method_template;
		this.dynamic_context = dynamic_context;
		this.lexical_bindings = lexical_bindings;
		this.self_is_module = self_is_module;

		self_methods = method_template.getMethodAccessors(dynamic_context,
				self_is_module);
		ivar_accessors = method_template.getIVarAccessors(dynamic_context,
				self_is_module);
	}

	public BindingContext bind(MetaModule dynamic_context,
			boolean self_is_module) {
		return new BindingContext(method_template, lexical_bindings,
				dynamic_context, self_is_module);
	}

	public IRubyObject eval(IRubyObject object) {
		ThreadState state = ThreadState.get();
		state.pushFrame(method_template);
		try {
			state.push(object);
			eval(object, state, 0, null, null);
			return state.pop();
		} finally {
			state.popFrame();
		}
	}

	public void eval(IRubyObject self, ThreadState state, int given_args,
			RubyBlock block, ExposedLocals context_dvars) {

		// local jumps (break|redo|next) cannot escape method boundaries.

		try {
			internal_eval(self, state, given_args, block, context_dvars,
					method_template);
		} catch (NonLocalBreak e) {
			throw getRuntime().newLocalJumpError("unhandled break", e);
		} catch (NonLocalRedo e) {
			throw getRuntime().newLocalJumpError("unhandled redo", e);
		} catch (NonLocalNext e) {
			throw getRuntime().newLocalJumpError("unhandled next", e);
		}
	}

	void internal_eval(IRubyObject self, ThreadState state, int given_args,
			RubyBlock block, ExposedLocals context_dvars, ISeq template)
			throws NonLocalBreak, NonLocalRedo, NonLocalNext {

		int pc = 0;
		int sp = state.getStackPointer();
		final Object[] stack = state.getStack();

		// whenever this eval procedure returns, it will "finally-always"
		// store the value of the variable "result" into stack[self_sp],
		// and set state.setStackPointer(self_sp+1).

		final int frame_sp = sp
				- (given_args + (template.getCodeType() == ISEQ_TYPE_BLOCK ? 0
						: 1));
		int arg0_idx = frame_sp
				+ (template.getCodeType() == ISEQ_TYPE_BLOCK ? 0 : 1);
		int local0_idx = frame_sp
				- (template.getCodeType() == ISEQ_TYPE_BLOCK ? (template.code_args == -1 ? 1
						: 0)
						: 0);
		Object result = method_template.getCPool()[CPOOL_NIL];

		IterpExposedLocals dvars = null;

		if (template.code_calls_eval || template.num_dynamics > 0
				|| context_dvars != null) {
			dvars = new IterpExposedLocals(context_dvars, template, block,
					self, lexical_bindings.getCaller());
		}

		state.pushFrame(template);

		switch (template.getCodeType()) {
		case ISEQ_TYPE_CLASS:
		case ISEQ_TYPE_MODULE:
			getRuntime().trace(state, TRACE_CLASS, template.getFile(),
					template.firstLine(), null, null, self);
			break;

		case ISEQ_TYPE_METHOD:
			getRuntime().trace(state, TRACE_CALL, template.getFile(),
					template.firstLine(), template.getMethod(), null, self);
		}

		int empty_stack = -1;
		try {

			int parms_size = 1 // / receiver
					+ template.min_parm_count // normal args
					+ template.default_parm_count // default parms
					+ (template.has_block_parm ? 1 : 0) // block parm
					+ (template.has_rest_parm ? 1 : 0); // rest parm

			int real_given_args = (template.code_args == -1 ? ((IRubyObject[]) stack[arg0_idx]).length
					: template.code_args);

			if (template.getCodeType() == ISEQ_TYPE_METHOD) {

				if (template.code_args == -1) {
					// varargs call

					IRubyObject[] args = (IRubyObject[]) stack[arg0_idx];

					sp = arg0_idx;
					int arg_num = 0;

					int given_default_args = args.length
							- template.min_parm_count;

					if (given_default_args < 0) {
						throw getRuntime().newArgumentError(
								"wrong number of arguments (" + args.length
										+ " for " + template.min_parm_count
										+ ")");
					}

					for (int i = 0; i < template.min_parm_count; i++) {
						stack[sp++] = args[arg_num++];
					}

					for (int i = 0; i < template.default_parm_count; i++) {

						if (arg_num < args.length) {
							// given default arg
							stack[sp++] = args[arg_num++];

						} else {
							// computed default arg
							stack[sp++] = null;

							// default parm should evaluate here...

						}

					}

					if (template.has_block_parm) {
						stack[sp++] = getRuntime().newProc(block);
					}

					if (template.has_rest_parm) {
						stack[sp++] = java_array_rest(args, arg_num);
					}

				} else if (template.has_block_parm) {

					stack[sp++] = getRuntime().newProc(block);
				}

				assert parms_size == (sp - frame_sp);

			}

			while (sp < frame_sp + template.getNumLocals()) {
				stack[sp++] = method_template.getCPool()[0];
			}

			// this corresponds to an "empty stack"
			empty_stack = sp;

			// final int fp = sp;

			// If any of the parameters are also dvars
			// inside this method, then we copy the
			// parameter value to the dvar and use that one
			// frome here onwards...
			for (int i = 0; i < template.num_dynamics; i++) {
				int dvar_from_parm = template.dynamics_from[i];
				if (dvar_from_parm >= 0) {
					dvars.set(i, stack[frame_sp + dvar_from_parm]);
					stack[frame_sp + dvar_from_parm] = null;
				} else {
					dvars.set(i, method_template.getCPool()[0]);
				}
			}

			if (template.getCodeType() == ISEQ_TYPE_METHOD) {

				int given_default_args = Math.min(template.default_parm_count,
						real_given_args - template.min_parm_count);

				// starting point depends on number of given default args.
				if (template.eval_default_args.length > 0) {
					pc = template.eval_default_args[given_default_args];
				}
			}

			// template.dump_locals(stack, frame_sp, sp, dvars);

			byte[] code = template.code;
			Object[] cpool = method_template.getCPool();

			int opcode;
			next_insn: while (true) {
				try {
					switch (opcode = code[pc++]) {

					case TRACE: {
						int kind = code[pc++];
						int line = ui(code[pc++], code[pc++]);

						getRuntime().trace(state, kind, template.getFile(),
								line, template.getMethod(), dvars, self);

						continue next_insn;
					}

					case NOP:
						continue next_insn;

					case SWAP: {
						Object val1 = stack[sp - 2];
						stack[sp - 2] = stack[sp - 1];
						stack[sp - 1] = val1;
						continue next_insn;
					}

					case SETLOCAL:
						stack[local0_idx + ui(code[pc++])] = stack[--sp];
						continue next_insn;

					case GETLOCAL:
						stack[sp++] = stack[local0_idx + ui(code[pc++])];
						continue next_insn;

					case SETSPECIAL:
						setspecial(ui(code[pc++]), (IRubyObject) stack[--sp]);
						continue next_insn;

					case GETSPECIAL:
						stack[sp++] = getspecial(ui(code[pc++]));
						continue next_insn;

					case SETGLOBAL: {
						Global g = lexical_bindings.globals[(ui(code[pc++]))];
						g.set((IRubyObject) stack[--sp]);
						continue next_insn;
					}

					case GETGLOBAL: {
						Global g = lexical_bindings.globals[(ui(code[pc++]))];
						stack[sp++] = g.get();
						continue next_insn;
					}

					case GETDYNAMIC:
						stack[sp++] = dvars.getdynamic(ui(code[pc++]),
								ui(code[pc++]));
						continue next_insn;

					case SETDYNAMIC:
						dvars.setdynamic(ui(code[pc++]), ui(code[pc++]),
								(IRubyObject)stack[--sp]);
						continue next_insn;

					case BRANCHUNLESS: {
						int offset = si(code[pc++], code[pc++]);
						if (is_false((IRubyObject) stack[--sp])) {
							pc = pc - 3 + offset;
						}
						continue next_insn;
					}

					case BRANCHIF: {
						int offset = si(code[pc++], code[pc++]);
						if (is_true((IRubyObject) stack[--sp])) {
							pc = pc - 3 + offset;
						}
						continue next_insn;
					}

					case JUMP: {
						int offset = si(code[pc++], code[pc++]);
						pc = pc - 3 + offset;
						continue next_insn;
					}

					case LOCAL_JSR: {
						int offset = si(code[pc++], code[pc++]);
						stack[sp++] = new Integer(pc);
						pc = pc - 3 + offset;
						continue next_insn;
					}

					case LOCAL_RETURN: {
						Integer new_pc = (Integer) stack[local0_idx + ui(code[pc++], code[pc++])];
						pc = new_pc.intValue();
						continue next_insn;
					}

						// yield
					case INVOKEBLOCK: {
						if (block == null) {
							throw getRuntime().newLocalJumpError(
									"no block given", null);
						}

						int flags = code[pc++];
						int nargs = ui(code[pc++]); // number of known args at
						// call-site

						if ((flags & FLAG_REST_ARG) == FLAG_REST_ARG) {

							IRubyObject[] rest = (IRubyObject[]) stack[--sp];
							for (int i = 0; i < rest.length; i++) {
								stack[sp++] = rest[i];
							}

							nargs += rest.length;
						}

						state.setStackPointer(sp);
						try {
							block.interp_call(state, nargs);

						} catch (NonLocalJump e) {

							ISeq.Loop loop = find_loop_or_rethrow(pc,
									template.loops, e);
							pc = loop.get(e.kind);
							continue next_insn;

						} catch (NonLocalReturn nlr) {
							if (nlr.scope == dvars) {
								// this will put the return value in the right
								// place..
								result = nlr.value;
								return;

							} else {
								// this is someone else's return value
								throw nlr;
							}
						}

						sp -= (nargs);
						sp += 1; // result

						assert state.getStackPointer() == sp;

						// pop result if requested...
						if ((flags & FLAG_PUSH_RESULT) == 0) {
							sp -= 1;
						}

						continue next_insn;
					}

						//
						// stack: receiver, arg1, arg2, ... argN, [splat1,
						// splat2,
						// ..., splatM, Integer(M)], block_arg
						//
					case INVOKESUPER:
					case SELFSEND:
					case SEND: {
						// special flags
						int flags = code[pc++];

						// compile-time known args (N in the above)
						int nargs = ui(code[pc++]);

						// the selector sits here...
						int selector_pos = (opcode == INVOKESUPER) ? -1 : ui(
								code[pc++], code[pc++]);

						int imm_block_pos = ui(code[pc++], code[pc++]);

						boolean has_amp_block_arg = (flags & FLAG_BLOCK_ARG) != 0;

						RubyBlock call_block = null;

						if (has_amp_block_arg) {
							call_block = (RubyBlock) stack[--sp];
						}

						RubyBlock old_block = null;
						if ((flags & FLAG_PROC_NEW) == FLAG_PROC_NEW) {
							old_block = state.setCallersBlock(block);
						} else if ((flags & FLAG_SEND_EVAL) != 0) {

						}

						if (imm_block_pos != 0) {
							assert call_block == null;

							ISeq block_template = (ISeq) cpool[imm_block_pos];
							call_block = new BlockISeq(block_template, self,
									dvars, block, this);
						}

						if (opcode == INVOKESUPER && call_block == null) {
							call_block = block;
						}

						int result_pos = -512;

						try {

							if ((flags & FLAG_REST_ARG) == FLAG_REST_ARG) {

								IRubyObject[] all_args = (IRubyObject[]) stack[--sp];
								IRubyObject receiver = (IRubyObject) stack[--sp];

								result_pos = sp;

								state.setStackPointer(sp);
								RubyMethod method;

								if (opcode == INVOKESUPER) {
									method = lexical_bindings
											.get_super_method();
								} else if (opcode == SELFSEND) {
									method = self_methods[selector_pos].get();
								} else {
									Selector sel = lexical_bindings.selectors[selector_pos];
									method = receiver.do_select(sel);
								}

								if ((flags & FLAG_SEND_EVAL) != 0) {
									result = method.call_eval(receiver,
											all_args, call_block, dvars);
								} else {
									result = method.call(receiver, all_args,
											call_block);
								}

							} else {

								// all the args are on the stack...
								int receiver_pos = sp - nargs - 1;
								IRubyObject receiver = (IRubyObject) stack[receiver_pos];

								state.setStackPointer(sp);

								result_pos = receiver_pos;

								RubyMethod method;

								if (opcode == INVOKESUPER) {
									method = lexical_bindings
											.get_super_method();
								} else if (opcode == SELFSEND) {
									method = self_methods[selector_pos].get();
								} else {
									Selector sel = lexical_bindings.selectors[selector_pos];
									method = receiver.do_select(sel);
								}

								if ((flags & FLAG_SEND_EVAL) != 0) {
									result = method.interp_call_eval(receiver,
											state, nargs, call_block, dvars);
								} else {
									result = method.interp_call(receiver,
											state, nargs, call_block);
								}

								sp = receiver_pos;
							}

						} catch (NewProcException ex) {

							System.out.println(">> caught NewProc exception");

							assert flags == code[pc - 6];
							// mark this as a call to proc new
							code[pc - 6] |= FLAG_PROC_NEW;

							sp = result_pos;
							result = getRuntime().newProc(block);

						} catch (NonLocalReturn nlr) {
							if (nlr.scope == dvars) {
								// this will put the return value in the right
								// place..
								result = nlr.value;
								return;

							} else {
								// this is someone else's return value
								throw nlr;
							}

						} finally {

							if ((flags & FLAG_PROC_NEW) == FLAG_PROC_NEW) {
								state.setCallersBlock(old_block);
							}

						}

						if ((flags & FLAG_PUSH_RESULT) != 0) {
							stack[sp++] = result;
						}

						continue next_insn;
					}

						// arg1, arg2, ..., argN, ryby-array[M] ->
						// EXPANDARRAY(N) ->
						// java-array[N+M]
					case EXPAND_REST_ARG: {
						// compiler uses the operand to be able to generate code
						// with known stack size.
						int n_pre_args = code[pc++];

						IRubyObject rest_arg = (IRubyObject) stack[--sp];

						IRubyArray arr;
						if (rest_arg instanceof IRubyArray) {
							arr = (IRubyArray) rest_arg;

						} else {
							state.setStackPointer(sp);
							arr = convert_rest_arg_to_array(rest_arg);
						}

						IRubyObject[] args = new IRubyObject[n_pre_args
								+ arr.int_size()];
						sp = popinto(stack, sp, args, n_pre_args);

						for (int i = 0; i < arr.int_size(); i++) {
							args[n_pre_args + i] = arr.int_at(i);
						}

						stack[sp++] = args;
						continue next_insn;
					}

					case PUSHOBJECT: {
						int op1 = ui(code[pc++]);
						stack[sp++] = cpool[op1];
						continue next_insn;
					}

					case PUSHOBJECT2: {
						int op1 = ui(code[pc++], code[pc++]);
						stack[sp++] = cpool[op1];
						continue next_insn;
					}

					case CONCATSTRINGS: {
						int howmany = ui(code[pc++]);
						StringBuilder sb = new StringBuilder();
						sp -= howmany;
						for (int i = 0; i < howmany; i++) {
							IRubyString string = (IRubyString) stack[sp + i];
							sb.append(string.asSymbol());
						}
						stack[sp++] = getRuntime().newString(sb.toString());
						continue next_insn;
					}

					case PUSHSELF: {
						stack[sp++] = self;
						continue next_insn;
					}

					case NEWHASH: {
						pc += 1; // unused size
						stack[sp++] = getRuntime().newHash();
						continue next_insn;
					}

						// "regex" -> NEWREGEX(flags) -> regex
					case NEWREGEX: {
						int flags = code[pc++];
						IRubyString string = (IRubyString) stack[--sp];
						IRubyRegexp regex = getRuntime().newRegexp(string,
								flags);
						stack[sp++] = regex;
						continue next_insn;
					}

					case UNWRAP_RAISE: {
						RaisedException val = (RaisedException) stack[sp - 1];
						stack[sp - 1] = val.getRubyException();
						continue next_insn;
					}
					
					case DUP: {
						Object val = stack[sp - 1];
						stack[sp++] = val;
						continue next_insn;
					}

					case POP: {
						sp -= 1;
						continue next_insn;
					}

						// evaluation of &x in argument context
					case PROC2BLOCK: {
						stack[sp - 1] = proc_to_block((IRubyObject) stack[sp - 1]);
						continue next_insn;
					}

					case NEWSYMBOL: {
						IRubyString string = (IRubyString) stack[sp - 1];
						stack[sp - 1] = getRuntime().newSymbol(
								string.asSymbol());
						continue next_insn;
					}

					case NEWRANGE: {
						boolean inclusive = code[pc++] == 1;
						IRubyObject end = (IRubyObject) stack[--sp];
						IRubyObject start = (IRubyObject) stack[--sp];
						stack[sp++] = getRuntime().newRange(start, end, inclusive);
						continue next_insn;
					}
					
					case INTERNAL_TO_A: {
						if (stack[sp - 1] instanceof IRubyArray) {
							// do nothing
						} else {
							IRubyObject o = (IRubyObject) stack[sp - 1];
							IRubyArray arr = getRuntime().newArray(1);
							arr.int_at_put(0, o);
							stack[sp - 1] = arr;
						}
						continue next_insn;
					}

						// arg1, arg2, ..., argN, (rest[M])? -> NEWARRAY(N,
						// rest?)
						// -> array[N+M]
					case NEWARRAY: {
						int size = code[pc++];
						boolean has_rest_arg = (code[pc++] == 1);

						// System.out.println("NEWARRAY " + size + ", " +
						// has_rest_arg);

						IRubyArray arr;
						if (has_rest_arg) {
							IRubyObject[] extra = (IRubyObject[]) stack[--sp];
							if (size == 0) {
								arr = getRuntime().newArray(extra);
							} else {
								arr = getRuntime()
										.newArray(size + extra.length);
								for (int i = 0; i < extra.length; i++) {
									arr.int_at_put(size + i, extra[i]);
								}
							}
						} else {
							arr = getRuntime().newArray(size);
						}

						sp -= size;
						for (int i = 0; i < size; i++) {
							IRubyObject val = (IRubyObject) stack[sp + i];
							arr.int_at_put(i, val);
						}

						stack[sp++] = arr;
						continue next_insn;
					}

						// convert array of size 1 to first element,
						// array of size 0 to nil
						// other value is left alone.
					case MAKERETURN: {
						Object val = stack[sp - 1];
						val = make_return_value(val);
						stack[sp - 1] = val;
						continue next_insn;
					}

					case NONLOCAL_BREAK:
						throw new NonLocalBreak();

					case NONLOCAL_NEXT:
						throw new NonLocalNext();

					case NONLOCAL_REDO:
						throw new NonLocalRedo();
						
					case NONLOCAL_RETURN: {
						IRubyObject retval = (IRubyObject) stack[--sp];
						throw new NonLocalReturn(dvars.get_parent() , retval);
					}

					case LEAVE: {
						result = stack[--sp];
						return;
					}

					case PUSHNIL: {
						stack[sp++] = getRuntime().getNil();
						continue next_insn;
					}

					case ARRAY_AT: {
						int idx = code[pc++];
						IRubyArray arr = (IRubyArray) stack[sp - 1];
						stack[sp++] = arr.int_at(idx);
						continue next_insn;
					}

					case ARRAY_REST: {
						int idx = code[pc++];
						IRubyArray arr = (IRubyArray) stack[sp - 1];
						stack[sp++] = arr.int_rest(idx);
						continue next_insn;
					}

					case JAVA_ARRAY_AT: {
						int idx = code[pc++];
						IRubyObject[] arr = (IRubyObject[]) stack[--sp];
						stack[sp++] = java_array_at(arr, idx);
						continue next_insn;
					}

					case JAVA_ARRAY_REST: {
						int idx = code[pc++];
						IRubyObject[] arr = (IRubyObject[]) stack[--sp];
						stack[sp++] = java_array_rest(arr, idx);
						continue next_insn;
					}

					case DUPN: {
						int idx = code[pc++];
						Object val = stack[sp - idx];
						stack[sp++] = val;
						continue next_insn;
					}

					case POPN: {
						sp -= ui(code[pc++]);
						continue next_insn;
					}

					case DEFINEMETHOD: {
						boolean singleton = code[pc++] == 1;
						int name_idx = ui(code[pc++], code[pc++]);
						int iseq_idx = ui(code[pc++], code[pc++]);

						IRubyString name = (IRubyString) cpool[name_idx];
						ISeq body = (ISeq) cpool[iseq_idx];

						MetaModule mc;
						IRubyObject receiver = null;
						boolean is_module_method;
						if (singleton) {
							receiver = (IRubyObject) stack[--sp];
							mc = receiver.get_singleton_meta_class();
							is_module_method = (receiver instanceof IRubyModule);
						} else {
							mc = this.dynamic_context;
							is_module_method = false;
						}

						BindingContext bound = new BindingContext(body, mc, mc,
								state.getModuleStack(), is_module_method);
						String method_name = name.asSymbol();

						MethodISeq method = new MethodISeq(bound, dvars,
								method_name);

						RubyMethod cm = method;

						if ((mc instanceof MetaClass) || is_module_method) {
							cm = new
								MethodCompiler(getRuntime()).compile(method);
						}

						if (is_module_method) {
							mc.register_module_method(method_name, cm);
						} else {
							mc.register_instance_method(method_name, cm);
						}
						continue next_insn;
					}

					case GETCONSTANT: {
						int idx = ui(code[pc++], code[pc++]);
						state.setStackPointer(sp);
						stack[sp++] = lexical_bindings.constants[idx].get();
						continue next_insn;
					}

					case SETCONSTANT: {
						int idx = ui(code[pc++], code[pc++]);
						IRubyObject value = (IRubyObject) stack[--sp];
						state.setStackPointer(sp);
						lexical_bindings.constants[idx].set(value);
						continue next_insn;
					}

					case DEFINECLASS: {
						IRubyObject super_class = null;
						int flags = code[pc++];
						boolean super_class_given = ((flags & FLAG_DEFINECLASS_SUPER_CLASS_GIVEN) != 0);
						boolean is_singleton_def = ((flags & FLAG_DEFINECLASS_IS_SINGLETON) != 0);
						int name_idx = ui(code[pc++], code[pc++]);
						int iseq_idx = ui(code[pc++], code[pc++]);

						if (super_class_given) {
							super_class = (IRubyObject) stack[--sp];
						}

						IRubyString name = is_singleton_def ? null
								: (IRubyString) cpool[name_idx];
						ISeq iseq = (ISeq) cpool[iseq_idx];

						state.setStackPointer(sp);
						eval_defineclass(self, super_class, name, iseq, state,
								is_singleton_def);

						continue next_insn;
					}

					case DEFINEMODULE: {
						int name_idx = ui(code[pc++], code[pc++]);
						int iseq_idx = ui(code[pc++], code[pc++]);

						IRubyString name = (IRubyString) cpool[name_idx];
						ISeq iseq = (ISeq) cpool[iseq_idx];

						state.setStackPointer(sp);
						eval_definemodule(self, name, iseq, state);

						continue next_insn;
					}

					case FAST_LT: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_lt(arg, sel);
						continue next_insn;
					}

					case FAST_GT: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_gt(arg, sel);
						continue next_insn;
					}

					case FAST_EQ2: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_eq2(arg, sel);
						continue next_insn;
					}

					case FAST_BIT_OR: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_bit_or(arg, sel);
						continue next_insn;
					}

					case FAST_BIT_XOR: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_bit_xor(arg, sel);
						continue next_insn;
					}

					case FAST_RSHIFT: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_rshift(arg, sel);
						continue next_insn;
					}

					case FAST_BIT_AND: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_bit_and(arg, sel);
						continue next_insn;
					}

					case FAST_BIT_NOT: {
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_bit_not(sel);
						continue next_insn;
					}

					case FAST_PLUS: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_plus(arg, sel);
						continue next_insn;
					}

					case FAST_LE: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_le(arg, sel);
						continue next_insn;
					}

					case FAST_MINUS: {
						IRubyObject arg = (IRubyObject) stack[--sp];
						IRubyObject rcv = (IRubyObject) stack[--sp];
						Selector sel = lexical_bindings.selectors[ui(
								code[pc++], code[pc++])];
						state.setStackPointer(sp);
						stack[sp++] = rcv.fast_minus(arg, sel);
						continue next_insn;
					}

					case SETINSTANCEVARIABLE: {
						int idx = ui(code[pc++], code[pc++]);
						ivar_accessors[idx]
								.set(self, (IRubyObject) stack[--sp]);
						continue next_insn;
					}

					case GETINSTANCEVARIABLE: {
						int idx = ui(code[pc++], code[pc++]);
						stack[sp++] = ivar_accessors[idx].get(self);
						continue next_insn;
					}

					case ALIAS: {
						int new_idx = ui(code[pc++], code[pc++]);
						int orig_idx = ui(code[pc++], code[pc++]);

						IRubySymbol new_sym = (IRubySymbol) cpool[new_idx];
						IRubySymbol orig_sym = (IRubySymbol) cpool[orig_idx];

						self.get_meta_class().alias(new_sym, orig_sym,
								self_is_module);

						continue next_insn;
					}

					default:
						throw new InternalError("unhandled insn: "
								+ code[pc - 1]);
					}
					
				} catch (RaisedException ex) {

					if (empty_stack == -1) {
						throw new InternalError("exception in arg setup");
					}

					// find exception handler...
					int handler_pc = find_exception_handler(pc, template);
					
					if (handler_pc == -1) {
						throw ex;
					}
					
					pc = handler_pc;

					// empty the stack
					
					sp = empty_stack;
					
					// push exception
					stack[sp++] = ex;
					
					
					continue next_insn;
				}
			}

		} finally {
			stack[frame_sp] = result;
			state.setStackPointer(frame_sp + 1);

			switch (template.getCodeType()) {
			case ISEQ_TYPE_CLASS:
			case ISEQ_TYPE_MODULE:
				getRuntime().trace(state, TRACE_END, template.getFile(),
						state.getLine(), null, null, self);
				break;

			case ISEQ_TYPE_METHOD:
				getRuntime().trace(state, TRACE_RETURN, template.getFile(),
						state.getLine(), template.getMethod(), null, self);

			}

			state.popFrame();
		}
	}

	private int find_exception_handler(int pc, ISeq template) {
		ExceptionHandlerInfo[] handlers = template.getExceptionHandlers();
		for (int i = handlers.length-1; i >= 0; i--) {
			ExceptionHandlerInfo h = handlers[i];
			if (h.start_pc <= pc && h.end_pc <= pc) {
				return h.handler_pc;
			}
		}
		return -1;
	}

	private void eval_definemodule(IRubyObject self, IRubyString name,
			ISeq iseq, ThreadState state) {

//		if (!(self instanceof IRubyModule)) {
//			throw new InternalError("self is not module, but: "+self.inspect());
//		}
		
		//IRubyModule decl_context = (self instanceof IRubyModule) ? ((IRubyModule) self) : (self.get_class());
		MetaModule decl_meta = self.get_meta_module();

		String newName = name.asSymbol();
		MetaModule new_module_meta = decl_meta.get_module(newName, false);

		if (new_module_meta == null) {

			// resolve decl context, if not a simple name
			int idx = newName.lastIndexOf("::");
			if (idx != -1) {
				if (idx == 0) {
					decl_meta = getRuntime().meta_Object();
				} else {
					decl_meta = decl_meta.get_module(newName.substring(0, idx),
							true);
					if (decl_meta == null) {
						// TODO: this should invoke const_missing
						throw getRuntime().newTypeError("context missing?");
					}
				}

				newName = newName.substring(idx + 2);
				//decl_context = decl_meta.get_base_module();
			}

			new_module_meta = new MetaModule(getRuntime(), decl_meta, newName);

		}

		// todo: make this a lazy class
		IRubyModule target = new_module_meta.get_base_module();

		BindingContext ctx = new BindingContext(iseq, new_module_meta,
				new_module_meta, state.getModuleStack(), true);

		state.pushModule(new_module_meta);
		try {
			ctx.eval(target, state, 0, null, null);
		} finally {
			state.popModule();
		}
	}

	private void eval_defineclass(IRubyObject self, IRubyObject super_class,
			IRubyString name, ISeq iseq, ThreadState state, boolean is_singleton) {

		if (!(self instanceof IRubyModule)) {
			throw getRuntime()
					.newSyntaxError("class definition in method body");
		}

		MetaClass new_class_meta = null;

		if (is_singleton) {
			new_class_meta = super_class.get_singleton_meta_class();
		} else {

			if (super_class != null) {
				if (super_class == getRuntime().getNil()) {
					super_class = null;
				} else if (!(super_class instanceof IRubyClass)) {
					throw getRuntime().newSyntaxError(
							"given super class is not a class");
				}
			}
		}

		IRubyModule decl_context = (IRubyModule) self;
		MetaModule decl_meta = decl_context.get_meta_module();

		if (is_singleton) {
			
		} else {
			IRubyClass decl_super = (IRubyClass) super_class;
			MetaClass decl_super_meta = super_class == null ? null : decl_super
					.get_meta_class();

			String newName = name.asSymbol();
			new_class_meta = decl_meta.get_class(newName, false);

			if (new_class_meta != null) {
				// found existing class

				if (decl_super != null) {

					MetaClass original_super_class = new_class_meta
							.get_super_class();

					if (original_super_class != decl_super_meta) {
						throw getRuntime()
								.newTypeError(
										"super class mismatch: was "
												+ original_super_class
														.getName() + " given "
												+ decl_super_meta.getName());
					}

				}
			} else {
				// resolve decl context, if not a simple name
				int idx = newName.lastIndexOf("::");
				if (idx != -1) {
					if (idx == 0) {
						decl_meta = getRuntime().meta_Object();
					} else {
						decl_meta = decl_meta.get_module(newName.substring(0,
								idx), true);
						if (decl_meta == null) {
							// TODO: this should invoke const_missing
							throw getRuntime().newTypeError("context missing?");
						}
					}

					newName = newName.substring(idx + 2);
					decl_context = decl_meta.get_base_module();
				}

				// we need to create a new class

				if (decl_super_meta == null) {
					decl_super_meta = getRuntime().meta_Object();
				}

				new_class_meta = new MetaClass(getRuntime(), decl_meta,
						newName, decl_super_meta);

			}
		}

		// todo: make this a lazy class
		IRubyClass target = new_class_meta.get_base_class();

		BindingContext ctx = new BindingContext(iseq, new_class_meta,
				new_class_meta, state.getModuleStack(), true);

		state.pushModule(new_class_meta);
		try {
			ctx.eval(target, state, 0, null, null);
		} finally {
			state.popModule();
		}
	}

	private IRubyArray java_array_rest(IRubyObject[] arr, int idx) {
		int alen = arr.length;
		if (alen > idx) {
			int rlen = alen - idx;
			IRubyObject[] result = new IRubyObject[rlen];
			System.arraycopy(arr, idx, result, 0, rlen);
			return getRuntime().newArray(result);
		} else {
			return getRuntime().newArray();
		}
	}

	private IRubyObject java_array_at(IRubyObject[] arr, int idx) {
		if (arr.length > idx) {
			return arr[idx];
		} else {
			return getRuntime().getNil();
		}
	}

	private Loop find_loop_or_rethrow(int pc, Loop[] loops, NonLocalJump e)
			throws NonLocalBreak, NonLocalRedo, NonLocalNext {
		for (int i = loops.length - 1; i >= 0; i--) {
			Loop loop = loops[i];
			if (pc >= loop.start_pos && pc <= loop.end_pos) {
				return loop;
			}
		}

		switch (e.kind) {
		case NonLocalJump.NONLOCAL_BREAK:
			throw (NonLocalBreak) e;
		case NonLocalJump.NONLOCAL_REDO:
			throw (NonLocalRedo) e;
		case NonLocalJump.NONLOCAL_NEXT:
			throw (NonLocalNext) e;
		default:
			throw new InternalError("unknown non-local jump");
		}
	}

	private Object make_return_value(Object val) {
		if (val instanceof IRubyArray) {
			IRubyArray arr = (IRubyArray) val;
			if (arr.int_size() == 1) {
				val = arr.int_at(0);
			} else if (arr.int_size() == 0) {
				val = getRuntime().getNil();
			}
		}
		return val;
	}

	private int popinto(Object[] stack, int sp, IRubyObject[] args, int howmany) {
		while (howmany > 0) {
			args[--howmany] = (IRubyObject) stack[--sp];
		}

		return sp;
	}

	private RubyBlock proc_to_block(IRubyObject proc) {
		if (proc == getRuntime().getNil()) {
			return null;
		} else if (proc instanceof IRubyProc) {
			return ((IRubyProc) proc).get_block();
		} else {
			throw getRuntime().newTypeError(
					"TypeError: wrong argument type "
							+ proc.get_class().inspect() + " (expected Proc)");
		}
	}

	private IRubyArray convert_rest_arg_to_array(IRubyObject object) {

		if (object instanceof IRubyArray) {
			return (IRubyArray) object;
		}
		if (object == getRuntime().getNil()) {
			return getRuntime().newArray(1);
		} else {
			return getRuntime().newArray(new IRubyObject[] { object });
			// return (IRubyArray)
			// object.do_select(getRuntime().getSelector(lexical_bindings,
			// "to_a")).call(object, lexical_bindings);
		}
	}

	private boolean is_true(IRubyObject object) {
		return object.isTrue();
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

	private boolean is_false(IRubyObject object) {
		return object.isFalse();
	}

	private IRubyObject getspecial(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setspecial(int i, IRubyObject object) {
		// TODO Auto-generated method stub

	}

	RubyRuntime getRuntime() {
		return dynamic_context.getRuntime();
	}

	public void check_visibility(CallContext ctx, IRubyObject self, String name) {
		switch (method_template.visibility) {
		case VISIBILITY_PROTECTED:

			if (!ctx.getCaller().can_see_protected_method_in(
					lexical_bindings.getCaller())) {
				throw getRuntime().newNoMethodError(
						"protected method `" + name + "' called for "
								+ self.inspect());
			} else {
				return;
			}

		case VISIBILITY_PRIVATE:
			if (lexical_bindings.getCaller() != ctx.getCaller()) {
				throw getRuntime().newNoMethodError(
						"private method `" + name + "' called for "
								+ self.inspect());
			}
		}
	}

	public int code_args() {
		return method_template.code_args;
	}

	public MetaModule getDynamicContext() {
		return dynamic_context;
	}

	public ISeq getISeq() {
		return method_template;
	}

	public MetaModule getLexicalContext() {
		return lexical_bindings.getCaller();
	}

	public String[] getGlobals() {
		return method_template.globals;
	}

	public RubyIvarAccessor[] getIVarAccessors() {
		return ivar_accessors;
	}

	public boolean selfIsModule() {
		return self_is_module;
	}

	public Selector getSelector(int sel_idx) {
		return lexical_bindings.selectors[sel_idx];
	}

	public ModuleFrame getModuleStack() {
		return lexical_bindings.getModuleStack();
	}

}
