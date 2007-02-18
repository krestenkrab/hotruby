package com.trifork.hotruby.classes;

import com.trifork.hotruby.objects.RubyRegexp;
import com.trifork.hotruby.runtime.MetaClass;

public class RubyClassRegexp
	extends RubyBaseClassRegexp
{
	@Override
	public void init(MetaClass meta) {
		super.init(meta);
		
		RubyRegexp.static_init(meta.getRuntime());
	}
}
