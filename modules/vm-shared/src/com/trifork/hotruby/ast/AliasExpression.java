package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;


public class AliasExpression extends Expression {

	private String sym1, sym2;

	public AliasExpression(SequenceExpression args) {
		sym1 = ((SymbolExpression) args.get(0)).asString();
		sym2 = ((SymbolExpression) args.get(1)).asString();
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		int sym1 = ctx.getConstantPool().add_symbol(this.sym1);
		int sym2 = ctx.getConstantPool().add_symbol(this.sym2);
		
		ctx.emit_alias(sym1, sym2);
		
		if (push) {
			ctx.emit_push_nil();
		}
	}
}
