package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class RegularExpression extends Expression {

    public static final int REGEX_OPTION_I = 0x01;
    public static final int REGEX_OPTION_X = 0x02;
    public static final int REGEX_OPTION_M = 0x04;
    public static final int REGEX_OPTION_O = 0x08;
    public static final int REGEX_OPTION_N = 0x10;
    public static final int REGEX_OPTION_E = 0x20;
    public static final int REGEX_OPTION_U = 0x40;
    public static final int REGEX_OPTION_S = 0x80;
    
	private final Expression string;
	private final int flags;

	public RegularExpression(Expression string, int flags) {
		this.string = string;
		this.flags = flags;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {

		if (string instanceof ConstantStringExpression) {
			ConstantStringExpression cse = (ConstantStringExpression) string;
			try {
				int idx = ctx.getConstantPool().add_regex(cse.getText(), flags);
				if (push) {
					ctx.emit_push_constant(idx);
				}
				return;
			} catch (RuntimeException ex /* regex compile error */) {
			}
		}

		string.compile(ctx, push);
		ctx.emit_new_regex(flags);
		if (!push) {
			ctx.emit_pop();
		}

	}

}
