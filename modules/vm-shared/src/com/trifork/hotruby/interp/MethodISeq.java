package com.trifork.hotruby.interp;

import com.trifork.hotruby.compiler.MethodCompiler;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.EvalContext;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.ThreadState;

public class MethodISeq extends RubyMethod {

	@Override
	protected String[] getIVarNames() {
		return context.method_template.ivars;
	}
	
	public RubyMethod specialize_for(MetaModule module) {
		if (module == context.dynamic_context) {
			return this;
		} 
			
		return new MethodCompiler(context.getRuntime()).compile(this);

//		return new MethodISeq(context.bind(module), locals_when_bound, name);
	}
	
	private final BindingContext context;

	final ExposedLocals locals_when_bound;

	private final String name;

	public MethodISeq(BindingContext context, ExposedLocals locals, String name) {
		this.context = context;
		this.locals_when_bound = locals;
		this.name = name;
		
	}

	@Override
	public IRubyObject interp_call(IRubyObject receiver, ThreadState state,
			int arg_count, RubyBlock block) {
		context.eval(receiver, state, fixup_args(state, arg_count), block, locals_when_bound);
		return state.pop();
	}

	@Override
	public IRubyObject interp_call_eval(IRubyObject receiver, ThreadState state, int arg_count, RubyBlock block, EvalContext ctx) {
		context.eval(receiver, state, fixup_args(state, arg_count), block, locals_when_bound);
		return state.pop();
	}
	
	//
	// re-organize arguments to be in the form expected by the callee
	//
	int fixup_args(ThreadState state, int given)
	{
		int code_args = context.code_args();
		if (given == code_args) return given;
		switch (code_args) {
		case -1:
			IRubyObject[] args = new IRubyObject[given];
			for (int i = given-1; i >= 0; i--) {
				args[i] = state.pop();
			}
			state.push(args);
			return 1;
			
		case 0:
		case 1:
		case 2:
			if (given != code_args) {
				throw context.getRuntime().newArgumentError("wrong number of arguments ("+given+" for "+code_args+")");
			}
			return given;
		}
		
		throw new InternalError("cannot happen");
	}
	

	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		// context.check_visibility(ctx, receiver, name);
		ThreadState ts = ThreadState.get();
		ts.push(receiver);
		context.eval(receiver, ts, fixup_args(ts, 0), block, locals_when_bound);
		return ts.pop();
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			RubyBlock block) {
		// context.check_visibility(ctx, receiver, name);
		ThreadState ts = ThreadState.get();
		ts.push(receiver);
		ts.push(arg1);
		context.eval(receiver, ts, fixup_args(ts, 1), block, locals_when_bound);
		return ts.pop();
	}


	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2, RubyBlock block) {
		// context.check_visibility(ctx, receiver, name);
		ThreadState ts = ThreadState.get();
		ts.push(receiver);
		ts.push(arg1);
		ts.push(arg2);
		context.eval(receiver, ts, fixup_args(ts, 2), block, locals_when_bound);
		return ts.pop();
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block) {
		// context.check_visibility(ctx, receiver, name);
		ThreadState ts = ThreadState.get();
		ts.push(receiver);
		if (context.code_args() == -1) {
			ts.push(args);
			context.eval(receiver, ts, 1, block, locals_when_bound);
		} else {
			int nargs = args.length;
			for (int i = 0; i < nargs; i++) {
				ts.push(args[i]);
			}
			context.eval(receiver, ts, fixup_args(ts, nargs), block, locals_when_bound);
		}
		return ts.pop();
	}

	@Override
	public int getArity() {
		return context.method_template.getArity();
	}

	public BindingContext binding() {
		return context;
	}

	public String getName() {
		return name;
	}

}
