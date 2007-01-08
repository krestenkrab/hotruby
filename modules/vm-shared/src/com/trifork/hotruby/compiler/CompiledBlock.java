package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;

public abstract class CompiledBlock extends RubyBlock {
	abstract IRubyObject nil();

	public abstract RubyBlock getBlock();
	public abstract IRubyObject getSelf();

	public abstract String[] getDVarNames();
}
