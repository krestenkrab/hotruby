/**
 * 
 */
package com.trifork.hotruby.interp;

import com.trifork.hotruby.ast.LocalVariable;
import com.trifork.hotruby.ast.LocalVariableAccess;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;

public class IterpExposedLocals extends ExposedLocals {
	
	private final ExposedLocals parent;

	private final ISeq iseq;

	private Object[] values;

	private final RubyBlock block;

	private final IRubyObject self;

	private final MetaModule lexical_context;

	public IterpExposedLocals(ExposedLocals parent, ISeq seq, RubyBlock block, IRubyObject self, MetaModule lexical_context) {
		super(parent);
		this.parent = parent;
		this.iseq = seq;
		this.block = block;
		this.self = self;
		this.lexical_context = lexical_context;
		this.values = new Object[seq.num_dynamics];
	}

	public IRubyObject get(int local) {
		assert local >= 0 && local < iseq.num_dynamics;
		return (IRubyObject) values[local];
	}

	public void set(int local, Object object) {
		assert local >= 0 && local < iseq.num_dynamics;
		values[local] = object;
	}

	public int count() {
		return values.length;
	}

	public RubyBlock get_block() {
		return block;
	}

	public MetaModule get_lexical_context() {
		return lexical_context;
	}

	public ExposedLocals get_locals() {
		return this;
	}

	public IRubyObject get_self() {
		return self;
	}

	public LocalVariableAccess access_local(String name, int level) {
		String[] dynamics = iseq.name_dynamics;
		for (int i = 0; i < dynamics.length; i++) {
			if (name.equals(dynamics[i])) {
				return new LocalVariableAccess(level, new LocalVariable(name, i, true));
			}
		}

		if (parent != null) {
			return parent.access_local(name, level+1);
		} else {
			return null;
		}
	}

	public LocalVariable get_local(String name, int level, boolean create) {
		
		String[] dynamics = iseq.name_dynamics;
		for (int i = 0; i < dynamics.length; i++) {
			if (name.equals(dynamics[i])) {
				return new LocalVariable(name, i, true);
			}
		}

		if (parent != null) {
			return parent.get_local(name, level+1, create);
		} else {
			return null;
		}
	}

	public ExposedLocals get_parent() {
		return parent;
	}

}