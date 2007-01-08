package com.trifork.hotruby.runtime;

import com.trifork.hotruby.ast.LocalVariable;
import com.trifork.hotruby.ast.LocalVariableAccess;
import com.trifork.hotruby.objects.IRubyObject;

public interface EvalContext {

	/** the lexical context we're running in*/
	MetaModule get_lexical_context();
	
	/** the value of self */
	IRubyObject get_self();
	
	/** locals available in this scope */
	ExposedLocals get_locals();
	
	/** block available in scope, if any */
	RubyBlock get_block();

	LocalVariable get_local(String name, int level, boolean create);

	LocalVariableAccess access_local(String name, int level);

}
