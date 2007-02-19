package com.trifork.hotruby.interp;

import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.ConstantAccessor;
import com.trifork.hotruby.runtime.Global;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyMethodAccessor;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.runtime.ThreadState.ModuleFrame;

public class LexicalBindings implements CallContext {
	public Global[] globals;
	public ConstantAccessor[] constants;

	// selectors are customized for call context
	final Selector[] selectors;
	private final MetaModule lexical_context;
	
	// we need the frame to resolve constants correctly
	private final ModuleFrame frame;
	private RubyMethodAccessor super_method;

	public LexicalBindings(ISeq iseq, MetaModule lexical_context, ModuleFrame frame, boolean self_is_module) {
		this.lexical_context = lexical_context;
		this.frame = frame;
		globals = iseq.getGlobals(lexical_context.getRuntime());
		constants = iseq.getConstants(lexical_context, frame);
		selectors = prepare_selectors(iseq.getSelectors());
		super_method = iseq.getSuperMethod(lexical_context, self_is_module);
	}
	
	public ModuleFrame getModuleStack() {
		return frame;
	}

	private Selector[] prepare_selectors(String[] names) {
		Selector[] result = new Selector[names.length];
		for (int i = 0; i < names.length; i++) {
			result[i] = lexical_context.getSelector(names[i]);
		}
		return result;
	}

	public MetaModule getCaller() {
		return lexical_context;
	}

	public RubyMethod get_super_method() {
		return this.super_method.get();
	}
}
