package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.Instructions;
import com.trifork.hotruby.runtime.EvalContext;

public class EvalCode extends RubyCode {

	private final EvalContext eval_context;

	public EvalCode(int line, EvalContext context, String fileName) {
		super(line, null, fileName);
		eval_context = context;
	}

	@Override
	public int code_type() {
		return Instructions.ISEQ_TYPE_BLOCK;
	}

	@Override
	public int getCodeArgs() {
		return 0;
	}

	@Override
	protected LocalVariable get_local_in_context(String name, int level, boolean create) {
		if (eval_context == null) { return null; }
		return eval_context.get_local(name, level+1, create);
	}
	
	@Override
	protected LocalVariableAccess access_local_in_context(String name, int level) {
		if (eval_context == null) { return null; }
		return eval_context.access_local(name, level+1);
	}
	
}
