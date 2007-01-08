package com.trifork.hotruby.ast;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.ConstantPool;
import com.trifork.hotruby.objects.IRubyHash;

public class HashExpression extends Expression implements AssocHolder {

	static class Assoc {
		public Assoc(Expression key, Expression value) {
			this.key = key;
			this.value = value;
		}

		Expression key, value;
	}

	List<Assoc> values = new ArrayList<Assoc>();

	public HashExpression() {
		
	}
	
	public HashExpression(Expression key, Expression value) {
		this();
		addAssoc(key, value);
	}

	public void addAssoc(Expression key, Expression value) {
		values.add(new Assoc(key, value));
	}
	
	@Override
	void compile(CompileContext ctx, boolean push) {
		
		if (push) {
			ctx.emit_new_hash(values.size());
		}
		
		for (Assoc a : values) {
			if (push) {
				// dup hash
				ctx.emit_dup();
			}
			
			a.key.compile(ctx, push);
			
			if (a.value == null) {
				if (push) ctx.emit_push_nil();
			} else {
				a.value.compile(ctx, push);
			}
			
			if (push) {
				ctx.emit_send("[]=", 2, false, false, false, null);
			}
		}
	}

}
