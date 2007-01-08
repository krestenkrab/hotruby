package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public abstract class RubyBlock {

	public abstract IRubyObject call() throws NonLocalBreak, NonLocalNext,
			NonLocalRedo, NonLocalReturn;

	public abstract IRubyObject call(IRubyObject arg1) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn;

	public abstract IRubyObject call(IRubyObject arg1, IRubyObject arg2)
			throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn;

	public abstract IRubyObject call(IRubyObject[] args) throws NonLocalBreak,
			NonLocalNext, NonLocalRedo, NonLocalReturn;

	public void interp_call(ThreadState state, int arg_count)
			throws NonLocalBreak, NonLocalNext, NonLocalRedo, NonLocalReturn {

		IRubyObject result;
		
		switch (arg_count) {
		case 0: {
			result = call();
			break;
		}

		case 1: {
			IRubyObject arg1 = state.pop();
			result =  call(arg1);
			break;
		}

		case 2: {
			IRubyObject arg2 = state.pop();
			IRubyObject arg1 = state.pop();
			result =  call(arg1, arg2);
			break;

		}

		default: {
			IRubyObject[] args = state.pop(arg_count);
			result =  call(args);
			break;
		}
		}
		
		state.push(result);
	}
}
