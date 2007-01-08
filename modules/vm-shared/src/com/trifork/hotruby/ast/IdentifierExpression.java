package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class IdentifierExpression extends VariableExpression {

	private LocalVariableAccess ref;

	public IdentifierExpression(RubyCode here, String text) {
		super(text);
		ref = here.lookup(text);
		if (ref == null) {
			new Throwable("TRACE").printStackTrace();
			System.out.println("local variable "+text+" not found");
			System.exit(1);
		}
	}

	public void compile_assignment(CompileContext ctx, boolean push) {
		if (push) { ctx.emit_dup(); }
		ref.compile_assignment(ctx);
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		if (push) {
			ref.compile(ctx, push);
		}
	}
}
