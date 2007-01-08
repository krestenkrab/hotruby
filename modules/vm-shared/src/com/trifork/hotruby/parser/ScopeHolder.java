package com.trifork.hotruby.parser;

import com.trifork.hotruby.ast.RubyCode;

public interface ScopeHolder {
	RubyCode scope();
}
