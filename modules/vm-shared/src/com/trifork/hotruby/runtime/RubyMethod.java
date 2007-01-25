package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;

public abstract class RubyMethod {

	protected static final IRubyObject[] NO_ARGS = new IRubyObject[0];
	private static final String[] NO_STRINGS = new String[0];

	/** return names of ivars that are referenced in this method */
	protected String[] getIVarNames() { return NO_STRINGS; }
	
	protected RuntimeException wrongArgs(IRubyObject receiver, int given) {
		throw receiver.getRuntime().newArgumentError("wrong #args. given " + given
				+ " expecting " + ((getArity() < 0) ? "at least " : "")
				+ min_args() + " (in "+getClass().getName()+")");
	}

	public abstract int getArity();

	public boolean isPrivate() {
		return false;
	}

	int min_args() {
		if (getArity() >= 0) {
			return getArity();
		} else {
			return -(getArity() + 1);
		}
	}

	int max_args() {
		if (getArity() < 0) {
			return Integer.MAX_VALUE;
		} else {
			return getArity();
		}
	}

	public abstract IRubyObject call(IRubyObject receiver, RubyBlock block);

	public abstract IRubyObject call(IRubyObject receiver, IRubyObject arg,
			RubyBlock block);

	public abstract IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2, RubyBlock block);

	public abstract IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block);

	/**
	 * call from interpreter. stack contains receiver and args.
	 * 
	 * @return
	 * @throws CallerControlException 
	 */
	public IRubyObject interp_call(IRubyObject receiver, ThreadState state,
			int arg_count, RubyBlock block)  {

		switch (arg_count) {
		case 0: {
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call(receiver, block);
		}

		case 1: {
			IRubyObject arg1 = state.pop();
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call(receiver, arg1, block);
		}

		case 2: {
			IRubyObject arg2 = state.pop();
			IRubyObject arg1 = state.pop();
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call(receiver, arg1, arg2, block);

		}

		default: {
			IRubyObject[] args = state.pop(arg_count);
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call(receiver, args, block);
		}
		}
	}

	public RubyMethod specialize_for(MetaModule module, boolean is_module) {
		return this;
	}

	final public IRubyObject call(IRubyObject receiver) {
		return call(receiver, (RubyBlock) null);
	}

	final public IRubyObject call(IRubyObject receiver, IRubyObject arg) {
		return call(receiver, arg, (RubyBlock) null);
	}

	final public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2) {
		return call(receiver, arg1, arg2, (RubyBlock) null);
	}

	final public IRubyObject call(IRubyObject receiver, IRubyObject[] args) {
		return call(receiver, args, (RubyBlock) null);
	}

	public IRubyObject call_eval(IRubyObject receiver, IRubyObject[] args, RubyBlock block, EvalContext ctx) {
		return call(receiver, args, block);
	}

	public IRubyObject call_eval(IRubyObject receiver, IRubyObject arg1, IRubyObject arg2, RubyBlock block, EvalContext ctx) {
		return call(receiver, arg1, arg2, block);
	}

	public IRubyObject call_eval(IRubyObject receiver, IRubyObject arg1, RubyBlock block, EvalContext ctx) {
		return call(receiver, arg1, block);
	}

	public IRubyObject call_eval(IRubyObject receiver, RubyBlock block, EvalContext ctx) {
		return call(receiver, block);
	}

	public IRubyObject interp_call_eval(IRubyObject receiver, ThreadState state, int arg_count, RubyBlock block, EvalContext ctx) {
		switch (arg_count) {
		case 0: {
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call_eval(receiver, block, ctx);
		}

		case 1: {
			IRubyObject arg1 = state.pop();
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call_eval(receiver, arg1, block, ctx);
		}

		case 2: {
			IRubyObject arg2 = state.pop();
			IRubyObject arg1 = state.pop();
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call_eval(receiver, arg1, arg2, block, ctx);

		}

		default: {
			IRubyObject[] args = state.pop(arg_count);
			IRubyObject rcv = state.pop();
			assert rcv == receiver;

			return call_eval(receiver, args, block, ctx);
		}
		}
	}

}
