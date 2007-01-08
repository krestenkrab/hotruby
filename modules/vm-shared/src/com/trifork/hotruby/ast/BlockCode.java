package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Instructions;

public class BlockCode extends RubyCode {

	@Override
	public int code_type() {
		return Instructions.ISEQ_TYPE_BLOCK;
	}
	
	private SequenceExpression parms;

	public BlockCode(int line, RubyCode context, String fileName) {
		super(line, context, fileName);
		this.parms = new SequenceExpression();
	}

	public SequenceExpression parms() {
		return parms;
	}
	
	@Override
	public boolean hasRestParm() {
		return parms.has_rest_arg();
	}

	@Override
	public int getCodeArgs() {
		if (hasRestParm() || parms.size() > 2) {
			return -1;
		} else {
			return parms.size();
		}
	}
	
	@Override
	public void compile(CompileContext ctx) {

		assign_local_indexes();

		if (getCodeArgs() == -1) {
			ctx.emit_getlocal(1); // push Object[] vargs
			parms.compile_assignment_from_vargs(ctx, false);

		} else if (getCodeArgs() != 0) {
			parms.compile_assignment_from_locals(ctx, 0, parms.size(), false);
		}	

		// TODO Auto-generated method stub
		super.compile(ctx);
	}
}
