package com.trifork.hotruby.runtime;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubySymbol;

public class MissingMethod extends RubyMethod {

	
	private Selector missing_method_selector;
	private IRubySymbol symbol;
	

	public MissingMethod(RubyRuntime runtime, Selector selector) {
		// todo: OPTIMIZE THIS
		this.missing_method_selector = runtime.getSelector(runtime.computeModuleForLoadedSelector(selector), "missing_method");
		this.symbol = runtime.newSymbol(selector.getName());
	}

	@Override
	public IRubyObject call(IRubyObject receiver, RubyBlock block) {
		return receiver.do_select(missing_method_selector).call(receiver, symbol, block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg,
			RubyBlock block) {
		return receiver.do_select(missing_method_selector).call(receiver, symbol, arg, block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject arg1,
			IRubyObject arg2, RubyBlock block) {
		return receiver.do_select(missing_method_selector).call(receiver, new IRubyObject[] {symbol, arg1, arg2} , block);
	}

	@Override
	public IRubyObject call(IRubyObject receiver, IRubyObject[] args,
			RubyBlock block) {
		IRubyObject[] call_args = new IRubyObject[args.length+1];
		call_args[0] = symbol;
		System.arraycopy(args, 0, call_args, 1, args.length);
		return receiver.do_select(missing_method_selector).call(receiver, call_args, block);
	}

	@Override
	public int getArity() {
		return -1; // any number of args will do for me
	}

}
