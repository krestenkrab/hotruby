package com.trifork.hotruby.objects;

import com.trifork.hotruby.modules.RubyModuleObjectSpace;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyIvarAccessor;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.runtime.SingletonState;
import com.trifork.hotruby.util.L4RootManager;

public class RubyObject extends RubyBaseObject implements IRubyObject {
	SingletonState singleton;
	
	public RubyObject() {
		RubyModuleObjectSpace.register(this);
	}

	public boolean isFrozen() {
		return singleton != null && singleton.frozen;
	}

	public boolean isTaint() {
		return singleton != null && singleton.taint;
	}

	public void setFrozen(boolean frozen) {
		if (isFrozen() != frozen) {
			if (singleton == null) {
				singleton = new SingletonState();
			}
			singleton.frozen = frozen;
		}
	}
	
	public void setTaint(boolean taint) {
		if (isTaint() != taint) {
			if (singleton == null) {
				singleton = new SingletonState();
			}
			singleton.taint = taint;
		}
	}

	public void assignInto(IRubyObject o) {
		if (o.isFrozen()) {
			throw throw_frozen_error(o);
		}

		if (isTaint()) {
			o.setTaint(true);
		}
		
	}

	private static RuntimeException throw_frozen_error(IRubyObject o) {
		throw new RuntimeException("asignment to frozed "
				+ o.get_class().inspect());
	}

	public RubyMethod do_select(Selector sel) {
		if (singleton == null) {
			return select(sel);
		}
		return singleton.select(this, sel);
	}

	public boolean isFalse() {
		return false;
	}

	public boolean isTrue() {
		return true;
	}

	public String asSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	public IRubyObject callMethod(String string, IRubyObject[] args,
			CallContext ctx) {
		Selector sel = LoadedRubyRuntime.instance.getSelector(ctx, string);
		return do_select(sel).call(this, args);
	}

	public IRubyObject callMethod(String string, IRubyObject[] args,
			RubyBlock block, CallContext ctx) {
		Selector sel = LoadedRubyRuntime.instance.getSelector(ctx, string);
		return do_select(sel).call(this, args, block);
	}

	public IRubyObject fast_to_str(Selector selector) {
		return do_select(selector).call(this);
	}

	public IRubyObject fast_call(Selector selector) {
		return do_select(selector).call(this);
	}

	public IRubyObject fast_initialize(IRubyObject[] args, Selector selector) {
		return do_select(selector).call(this, args);
	}

	public IRubyObject fast_to_f(Selector selector) {
		return do_select(selector).call(this);
	}

	public IRubyObject fast_to_i(Selector selector) {
		return do_select(selector).call(this);
	}

	public IRubyObject fast_to_s(Selector selector) {
		return do_select(selector).call(this);
	}

	public IRubyObject fast_ge(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_gt(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_le(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_lt(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_eq2(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_eq3(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_eqtilde(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_cmp(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_plus(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_minus(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_bit_or(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_bit_and(IRubyObject arg, Selector selector) {
		return do_select(selector).call(this, arg);
	}

	public IRubyObject fast_bit_not(Selector selector) {
		return do_select(selector).call(this);
	}

	public IRubyObject fast_rshift(IRubyObject arg, Selector sel) {
		return do_select(sel).call(this, arg);
	}

	public IRubyObject fast_bit_xor(IRubyObject arg, Selector sel) {
		return do_select(sel).call(this, arg);
	}
	
	protected final IRubyObject bool(boolean value) {
		if (value) {
			return LoadedRubyRuntime.TRUE;
		}
		return LoadedRubyRuntime.FALSE;
	}

	public String inspect() {
		RubyClass c = get_class();
		MetaClass m = c.get_meta_class();
		String cn = m.getName();
		return "#<" + cn + ":0x"
				+ Integer.toHexString(System.identityHashCode(this)) + ">";
	}

	public String toString() {
		return inspect();
	}

	public boolean isNil() {
		return false;
	}

	public void setInstanceVariable(String string, IRubyObject object) {
		get_class().get_meta_class()
				.set_instance_variable(this, string, object);
	}

	public MetaClass get_meta_class() {
		if (singleton == null) {
			return get_class().get_meta_class();
		}
		return singleton.get_meta_class(this, false);
	}

	public MetaModule get_meta_module() {
		if (singleton == null) {
			return get_class().get_meta_module();
		}
		return singleton.get_meta_class(this, false);
	}

	public MetaClass get_singleton_meta_class() {
		if (singleton == null) {
			singleton = new SingletonState();
		}
		return singleton.get_meta_class(this, true);
	}

	public MetaModule get_singleton_meta_module() {
		return get_singleton_meta_class();
	}

	public RubyRuntime getRuntime() {
		return get_class().get_meta_class().getRuntime();
	}

	public SingletonState get_singleton_state(boolean create) {
		if (create) {
			if (singleton == null) {
				singleton = new SingletonState();
			}
		}
		return singleton;
	}

	public IRubyObject get_ivar(String name) {
		RubyIvarAccessor acc = get_meta_class().getInstanceIVarAccessor(name, false);
		if (acc == null) { return LoadedRubyRuntime.NIL; }
		return acc.get(this);
	}
}
