package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.Instructions;

public class TopLevelCode extends RubyCode {

	@Override
	public int code_type() {
		return Instructions.ISEQ_TYPE_TOPLEVEL;
	}

	@Override
	public int getCodeArgs() {
		return 0;
	}
	
	
	public TopLevelCode(String filename) {
		super(0, null, filename);
	}

}
