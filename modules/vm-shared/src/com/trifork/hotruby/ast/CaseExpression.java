package com.trifork.hotruby.ast;

import java.util.List;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Label;

public class CaseExpression extends Expression {

	private final Expression test;
	private final List<SequenceExpression> conds;
	private final List<Expression> codes;
	private final Expression elseExpr;

	public CaseExpression(int line, Expression test, List<SequenceExpression> conds,
			List<Expression> codes, Expression elseExpr) {
		super(line);
		this.test = test;
		this.conds = conds;
		this.codes = codes;
		this.elseExpr = elseExpr;
	}

	@Override
	void compile(CompileContext ctx, boolean push) {

		test.compile(ctx, true);
		int val = ctx.alloc_temp(1);
		ctx.emit_setlocal(val);
		
		Label after_case_expr = ctx.new_label();
		for (int i = 0; i < conds.size(); i++) {

			SequenceExpression cond = conds.get(i);
			
			Label body = ctx.new_label();
			for (int j = 0; j < cond.size(); j++) {
				
				cond.get(j).compile(ctx, true);
				ctx.emit_getlocal(val);
				ctx.emit_eq3();
				
				ctx.emit_branch_if(body);
			}
			
			Label next_test = ctx.new_label();
			ctx.emit_goto(next_test);
			
			ctx.mark(body);
			
			codes.get(i).compile(ctx, push);
			
			ctx.emit_goto(after_case_expr);
			
			ctx.mark(next_test);
			
		}
		
		if (elseExpr != null) {
			elseExpr.compile(ctx, push);
		}
		
		ctx.mark(after_case_expr);
		ctx.free_temp(val);
		
	}

}
