package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ConstantStringExpression extends Expression implements HereDocHolder {

	@Override
	public String toString() {
		return '"' + getText() + '"';
	}
	
	public static final int DOUBLE_QUOTE = 2;

	public static final int SINGLE_QUOTE = 1;

	public static final int HEREDOC = 3;

	public static final int REGEX = 2;

	public static final int COMMAND_OUTPUT = 2;

	/** don't transform string content */
	public static int NONE = 0;

	private String text;

	public ConstantStringExpression(String text, int kind) {
		switch (kind) {
		case SINGLE_QUOTE:
		//	System.out.println("Single quote");
			break;
		case DOUBLE_QUOTE:
		//	System.out.println("Double quote, regexp, command output");
			text = text.replace("\\n", "\n");
			break;
		case HEREDOC:
		//	System.out.println("Heredoc");
			break;
		}
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			int idx = ctx.getConstantPool().add_string(text);
			ctx.emit_push_constant(idx);
		}
	}
}
