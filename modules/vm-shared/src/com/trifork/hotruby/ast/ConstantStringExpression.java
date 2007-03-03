package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class ConstantStringExpression extends Expression implements HereDocHolder {
	public static final int SINGLE_QUOTE = 1;
	public static final int DOUBLE_QUOTE = 2;
	public static final int HEREDOC = 3;
	public static final int REGEX = 4;

	public static final int COMMAND_OUTPUT = 2;
	
	/** don't transform string content */
	public static int NONE = 0;

	private static char[] SUBSTITUTIONS_DOUBLE_QUOTE = new char[] {
		'\\', '\\',
		'\'', '\'',
		'a', (char)0x07,
		'b', '\b',
		'e', (char)0x1b,
		'f', '\f',
		'n', '\n',
		'r', '\r',
		's', (char)0x20,
		't', '\t',
		'v', (char)0x0b
	};
	
	private static char[] SUBSTITUTIONS_SINGLE_QUOTE = new char[] {
		'\\', '\\',
		'\'', '\''
	};
	
	@Override
	public String toString() {
		return '"' + getText() + '"';
	}

	private String text;

	public ConstantStringExpression(String text, int kind) {
		switch (kind) {
		case DOUBLE_QUOTE:
			this.text = doEscape(text, true, SUBSTITUTIONS_DOUBLE_QUOTE);
			break;
		case SINGLE_QUOTE:
			this.text = doEscape(text, false, SUBSTITUTIONS_SINGLE_QUOTE);
			break;
		default:
			this.text = text;
		}
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
	
	String doEscape(String s, boolean isDoubleQuote, char[] substitutions) {
		StringBuilder builder = new StringBuilder(s.length());
		int length = s.length();
		for (int i=0; i<length; i++) {
			char c = s.charAt(i);
			if (c == '\\' && i < length-1) {
				boolean found = false;
				char c2 = s.charAt(i+1);
				for (int j=0; j<substitutions.length; j+=2) {
					if (substitutions[j] == c2) {
						builder.append(substitutions[j+1]);
						i++;
						found = true;
						break;
					}
				}
				if (isDoubleQuote && Character.isDigit(c2)) {
					// Octal escape
					int octalValue = 0;
					int j=0;
					for (; j<3 && i+j+1<length && Character.isDigit(s.charAt(i+j+1)); j++) {
						octalValue *= 8;
						octalValue += s.charAt(i+j+1) - '0';
					}
					if (octalValue < 256) {
						builder.append((char)octalValue);
						i += j;
					}
					found = true;
				}
				if (isDoubleQuote && c2 == 'x') {
					// Hex escape
					int hexValue = 0;
					int j=0;
					for (; j<2 && i+j+2<length && Character.isDigit(s.charAt(i+j+2)); j++) {
						hexValue *= 8;
						hexValue += s.charAt(i+j+2) - '0';
					}
					builder.append((char)hexValue);
					i += j + 1;
					found = true;
				}
				if (found || isDoubleQuote) {
					continue;
				}
			}
			builder.append(c);
		}
		return builder.toString();
	}
}
