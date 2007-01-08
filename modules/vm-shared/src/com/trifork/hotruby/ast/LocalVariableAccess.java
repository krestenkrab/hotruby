package com.trifork.hotruby.ast;

import com.trifork.hotruby.interp.CompileContext;

public class LocalVariableAccess {

	private final int level;

	private final LocalVariable var;

	public LocalVariableAccess(int level, LocalVariable var) {
		this.level = level;
		this.var = var;
	}

	boolean isDynamic() {
		return level > 0;
	}

	public String getName() {
		return var.name;
	}

	public void compile_assignment(CompileContext ctx) {
		if (!var.isDynamic) {
			ctx.emit_setlocal(var.getIndex());
		} else {
			ctx.emit_setdynamic(level, var.getDynamicIndex());
		}
	}

	public void compile(CompileContext ctx, boolean push) {
		if (push) {
			if (!var.isDynamic) {
				ctx.emit_getlocal(var.getIndex());
			} else {
				ctx.emit_getdynamic(level, var.getDynamicIndex());
			}
		}
	}
}
