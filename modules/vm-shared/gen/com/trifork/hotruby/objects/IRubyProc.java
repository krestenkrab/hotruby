package com.trifork.hotruby.objects;
import com.trifork.hotruby.runtime.RubyBlock;
public interface IRubyProc
	extends IRubyObject
{
	RubyBlock get_block();
}
