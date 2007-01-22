package com.trifork.hotruby.interp;

public class FinallyBlockInfo {

	public FinallyBlockInfo(int i, Label label) {
		this.ensure_label = label;
	}

	public Label ensure_label;

}
