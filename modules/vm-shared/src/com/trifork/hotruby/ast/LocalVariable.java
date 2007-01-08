package com.trifork.hotruby.ast;

public class LocalVariable {
	boolean isParm;

	String name;

	public boolean isDynamic;

	private int index = -1;
	private int d_index = -1;

	LocalVariable(String name, boolean isParm) {
		this.name = name;
		this.isParm = isParm;
	}

	public LocalVariable (String name, int idx, boolean isDynamic) {
		this.name = name;
		if (isDynamic) {
			d_index = idx;
		} else {
			index = idx;
		}
		this.isDynamic = isDynamic;
	}
	
	public int getIndex() {
		if (index == -1) {
			throw new InternalError("index not assigned for local variable "
					+ name);
		} else {
			return index;
		}
	}

	public int getDynamicIndex() {
		return d_index;
	}

	public void setIndex(int i) {
		this.index = i;
	}

	public void setDynamicIndex(int i) {
		this.d_index = i;
	}
}
