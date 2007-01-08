package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.Instructions;

public class ClassCode extends RubyCode {

	public ClassCode(int line, RubyCode context, String fileName) {
		super(line, context, fileName);
	}

	@Override
	public int code_type() {
		return Instructions.ISEQ_TYPE_CLASS;
	}

	@Override
	public int getCodeArgs() {
		return 1; // arg is the class itself
	}

}
