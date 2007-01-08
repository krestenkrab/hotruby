package com.trifork.hotruby.objects;
import com.trifork.hotruby.runtime.RubyBlock;
public class RubyProc
	extends RubyBaseProc
{

	RubyBlock block;
	
	public RubyProc(RubyBlock block) {
		this.block = block;
	}
	
	public RubyProc() {
	}

	

	public RubyBlock get_block() {
		return block;
	}
	
}
