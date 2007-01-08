package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.Instructions;

public class ModuleCode extends RubyCode {

	public ModuleCode(int line, RubyCode context, String fileName) {
		super(line, context, fileName);
	}

	@Override
	public int code_type() {
		return Instructions.ISEQ_TYPE_MODULE;
	}

	@Override
	public int getCodeArgs() {
		return 1; // takes module as argument
	}

}
