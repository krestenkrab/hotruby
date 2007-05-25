package com.trifork.hotruby.objects;

import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.runtime.SingletonState;

public interface IRubyObject
{
	/** Check an assignment into other (frozen), and also
	 *  set taintness of other.  
	 * @param other
	 */
	void assignInto(IRubyObject other);
	
	boolean isNil();
	boolean isFalse();
	boolean isTrue();
	
	RubyMethod select(Selector sel);	
	RubyMethod do_select(Selector instance);
	
	String asSymbol();
	
	IRubyObject callMethod(String string, IRubyObject[] objects, CallContext ctx);
	
	IRubyObject fast_call(Selector selector);
	IRubyObject fast_initialize(IRubyObject[] args, Selector selector);
	IRubyObject fast_to_s(Selector selector);
	IRubyObject fast_to_i(Selector selector);
	IRubyObject fast_to_f(Selector selector);
	IRubyObject fast_to_str(Selector selector);
	
	// comparison
	IRubyObject fast_eq3(IRubyObject arg, Selector selector);
	IRubyObject fast_eq2(IRubyObject arg, Selector selector);
	IRubyObject fast_eqtilde(IRubyObject arg, Selector selector);
	IRubyObject fast_ge(IRubyObject arg, Selector selector);
	IRubyObject fast_gt(IRubyObject arg, Selector selector);
	IRubyObject fast_lt(IRubyObject arg, Selector selector);
	IRubyObject fast_le(IRubyObject arg, Selector selector);
	IRubyObject fast_cmp(IRubyObject arg, Selector selector);
	

	IRubyObject fast_plus(IRubyObject arg, Selector selector);
	IRubyObject fast_minus(IRubyObject arg, Selector selector);
	IRubyObject fast_bit_or(IRubyObject arg, Selector selector);
	IRubyObject fast_bit_and(IRubyObject arg, Selector selector);
	IRubyObject fast_bit_not(Selector sel);
	IRubyObject fast_bit_xor(IRubyObject arg, Selector sel);
	IRubyObject fast_rshift(IRubyObject arg, Selector sel);

	String inspect();
	void setInstanceVariable(String string, IRubyObject object);
	
	IRubyClass get_class();
	MetaClass get_singleton_meta_class();
	MetaModule get_singleton_meta_module();
	RubyRuntime getRuntime();
	
	SingletonState get_singleton_state(boolean create);

	boolean isFrozen();
	boolean isTaint();
	void setTaint(boolean taint);
	void setFrozen(boolean frozen);

	IRubyObject get_ivar(String string);

	MetaModule get_meta_class();

	MetaModule get_meta_module();


}
