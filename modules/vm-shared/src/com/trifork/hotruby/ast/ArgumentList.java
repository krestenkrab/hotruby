package com.trifork.hotruby.ast;

import java.util.List;

public class ArgumentList implements AssocHolder {

	List<Expression> args;
	
	public void addAssoc(Expression key, Expression value) {
		if (value == null) {
			addArgument(key);
		} else {
			HashExpression last = lastArgIfHash();
			if (last == null) {
				args.add(new HashExpression(key, value));
			} else {
				last.addAssoc(key, value);
			}
		}
	}

	private HashExpression lastArgIfHash() {
		if (args.size() == 0) { return null; }
		Expression val = args.get(args.size()-1); 
		if (val instanceof HashExpression) {
			return (HashExpression)val;
		} 
		return null;
	}

	public void setRestArg(Expression expr) {
		// TODO Auto-generated method stub
		
	}

	public void setBlockArg(Expression expr) {
		
	}
	
	public void addArgument(Expression expr) {
		args.add(expr);
	}
}
