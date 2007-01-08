package com.trifork.hotruby.ast;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.interp.CompileContext;

public class StringConcatExpression extends Expression {
	
	List<Expression> expressions = new ArrayList<Expression>();
	
	// see ConstantStringExpression
	private int substitution_type;

	public StringConcatExpression(int line, int type, Expression base) {
		this(line, type);
		
		expressions.add(base);
	}
	
	public StringConcatExpression(int line, int type) {
		super(line);
		this.substitution_type = type;
	}

	public void add(Expression exp) {
		expressions.add(exp);
	}
	
	public void add(String exp) {
		expressions.add(new ConstantStringExpression(exp, substitution_type));
	}

	@Override
	void compile(CompileContext ctx, boolean push) {
		int empty_strings = 0;
		for (int i = 0; i < expressions.size(); i++) {
			Expression expr = expressions.get(i);
			if (expr instanceof ConstantStringExpression) {
				ConstantStringExpression cse = (ConstantStringExpression) expr;
				if (cse.getText().length() > 0) {
					expr.compile(ctx, push);
				} else {
					empty_strings += 1;
				}
			} else {
				MethodCallExpression mc = new MethodCallExpression(null, line, expr, "to_s", null, null, false);
				mc.compile(ctx, push);
			}			
		}
		if (push) {
			ctx.emit_stringconcat(expressions.size() - empty_strings);
		}
	}	
	
}
