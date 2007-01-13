package com.trifork.hotruby.interp;


public interface Label {

	ISeqBuilder getIseq();
	void patch(LabelPatch patch);
	int position();
	int level();

}
