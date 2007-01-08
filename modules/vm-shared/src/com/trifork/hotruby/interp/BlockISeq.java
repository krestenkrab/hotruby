package com.trifork.hotruby.interp;

import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.ThreadState;

public class BlockISeq extends RubyBlock {

	private final ISeq block_iseq;

	private final ExposedLocals locals;

	private final IRubyObject self;

	private final RubyBlock block;

	private final BindingContext context;

	public BlockISeq(ISeq iseq, IRubyObject self,
			IterpExposedLocals dvars, RubyBlock block, BindingContext context) {
		this.block_iseq = iseq;
		this.self = self;
		this.locals = dvars;
		this.block = block;
		this.context = context;
	}

	@Override
	public void interp_call(ThreadState state, int arg_count)
			throws NonLocalBreak, NonLocalRedo, NonLocalNext {

		int sp = state.getStackPointer();

		int nargs = fixup_args(state, arg_count);
		
		context.internal_eval(self, state, nargs, block, locals,
				block_iseq);
		
		IRubyObject result = state.pop();
		//state.push(result);
		state.push(result);

		int expected = sp-arg_count+1;
		if (expected != state.getStackPointer()) {
			System.out.println("expected: "+(expected)+" but was: "+state.getStackPointer());
		}
		
		//assert expected == state.getStackPointer();
	}

	private int fixup_args(ThreadState state, int arg_count) {
		int code_args = block_iseq.code_args;
		switch (code_args) {
		case -1: // vararg
			IRubyObject[] val = new IRubyObject[arg_count];
			for (int i = val.length-1; i >= 0; i--) {
				val[i] = state.pop();
			}
			state.push(val);
			return 1;
			
		case 0:
		case 1:
		case 2:
			if (arg_count > code_args) {
				state.setStackPointer(state.getStackPointer()
						- (arg_count - code_args));
			} else {
				while (arg_count++ < code_args) {
					state.push(block_iseq.getRuntime().getNil());
				}
			}
			
		}
		return code_args;
	}

	@Override
	public IRubyObject call() throws NonLocalBreak, NonLocalRedo, NonLocalNext {
		ThreadState state = ThreadState.get();
		
		int args = fixup_args(state, 0);
		
		context.internal_eval(self, state, args, block, locals, block_iseq);
		return state.pop();
	}

	@Override
	public IRubyObject call(IRubyObject arg1) throws NonLocalBreak,
			NonLocalRedo, NonLocalNext {
		ThreadState state = ThreadState.get();
		state.push(arg1);
		int arg_count = fixup_args(state, 1);
		
		context.internal_eval(self, state, arg_count, block, locals, block_iseq);
		return state.pop();
	}

	@Override
	public IRubyObject call(IRubyObject arg1, IRubyObject arg2)
			throws NonLocalBreak, NonLocalRedo, NonLocalNext {
		ThreadState state = ThreadState.get();
		state.push(arg1);
		state.push(arg2);

		int arg_count = fixup_args(state, 2);
		
		context.internal_eval(self, state, arg_count, block, locals, block_iseq);
		return state.pop();
	}

	@Override
	public IRubyObject call(IRubyObject[] args) throws NonLocalBreak,
			NonLocalRedo, NonLocalNext {
		ThreadState state = ThreadState.get();
		for (int i = 0; i < args.length; i++) {
			state.push(args[i]);
		}

		int arg_count = fixup_args(state, args.length);
		
		context.internal_eval(self, state, arg_count, block, locals,
				block_iseq);
		return state.pop();
	}

}
