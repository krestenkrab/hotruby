package com.trifork.hotruby.ast;

import java.util.List;

import com.trifork.hotruby.interp.CompileContext;

public class SuperExpression extends VariableExpression {

	public static Expression instance = new SuperExpression();

	private SuperExpression() {
		super("super");
		// TODO Auto-generated constructor stub
	}

	// TODO: figure out if this is evaluated as part of a default arg
	@Override
	void compile(CompileContext ctx, boolean push) {
		List<Expression> parms = ctx.get_this_method_parms();
		ctx.emit_push_self();
		int arg_count = 0;
		boolean has_rest_arg = false;
		boolean has_block_arg = false;
		
		int compiling_optional_idx = ctx.get_is_compiling_default_values();
		
		for (int i = 0; i < parms.size(); i++) {
			Expression parm = parms.get(i);
			
			if (parm instanceof BlockArgExpression) {
				BlockArgExpression rparm = (BlockArgExpression) parm;
				has_block_arg = true;
				rparm.compile(ctx, true);
			} else if (parm instanceof RestArgExpression) {
				RestArgExpression rparm = (RestArgExpression) parm;
				has_rest_arg = true;
				rparm.compile(ctx, true, arg_count);
			} else {
				arg_count += 1;
				if (compiling_optional_idx != -1 && i >= compiling_optional_idx) {
					ctx.emit_push_nil();
				} else {
					parm.compile(ctx, true);
				}
			}
		}
		
		ctx.emit_supersend(arg_count, has_rest_arg, has_block_arg, push, null);
	}
}
