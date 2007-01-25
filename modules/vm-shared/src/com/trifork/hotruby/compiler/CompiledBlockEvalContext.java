package com.trifork.hotruby.compiler;

import java.lang.reflect.InvocationTargetException;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.EvalContext;
import com.trifork.hotruby.runtime.ExposedLocals;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;

public class CompiledBlockEvalContext extends CompiledEvalContext {


	private final CompiledBlock owner;
	private final Object[] dvars;

	public CompiledBlockEvalContext(CompiledBlock owner, ExposedLocals parent) {
		super(parent);
		this.owner = owner;
		this.dvars = new Object[ getDVarCount() ];
	}

	public RubyBlock get_block() {
		return owner.getBlock();
	}

	public MetaModule get_lexical_context() {
		return ((CompiledMethodEvalContext)parent).get_lexical_context();
	}

	public ExposedLocals get_locals() {
		return this;
	}

	public IRubyObject get_self() {
		return owner.getSelf();
	}

	@Override
	protected String[] getDVarNames() {
		//System.out.println(owner.getClass());
		try {
			String[] result = (String[]) owner.getClass().getDeclaredMethod("getDVarNames", new Class[0]).invoke(owner, new Object[0]);
			return result;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return owner.getDVarNames();
	}

	@Override
	protected EvalContext getParent() {
		return null;
	}

	@Override
	public Object get(int idx) {
		return dvars[idx];
	}

	@Override
	public void set(int idx, Object value) {
		dvars[idx] = value;
	}

}
