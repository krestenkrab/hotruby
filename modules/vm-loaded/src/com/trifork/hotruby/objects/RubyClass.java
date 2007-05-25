package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassClass;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaClass;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.Selector;

public abstract class RubyClass extends RubyBaseClass {
	private MetaClass meta_class;

	Selector calln_initialize;

	public IRubyString get_name() {
		return LoadedRubyRuntime.instance.newString(meta_class.getName());
	}

	public MetaClass get_singleton_meta_class() {
		return meta_class;
	}

	public void init(MetaClass meta) {
		super.init((MetaModule) meta);
		this.meta_class = meta;
	}

	Selector get_initialize() {
		if (calln_initialize == null) {
			calln_initialize = LoadedRubyRuntime.instance.getSelector(
					RubyClassClass.instance.get_meta_module(), "initialize");
		}

		return calln_initialize;
	}

	public IRubyClass newClass(IRubyObject superCls) {
		MetaClass super_meta;

		if (superCls == null) {
			super_meta = LoadedRubyRuntime.META_OBJECT;
			MetaClass cls = super_meta.new_subclass(null);
			return cls.get_base_class();
		} else if (superCls instanceof RubyClass) {
			RubyClass superClass = (RubyClass) superCls;
			MetaClass meta = superClass.get_meta_class().new_subclass(null);
			return meta.get_base_class();
		} else {
			throw LoadedRubyRuntime.instance
					.newArgumentError("argument is not a class ("
							+ superCls.inspect() + " given)");
		}
	}

	public MetaClass get_meta_class() {
		if (meta_class == null) {
			throw new RuntimeException("get_meta_class too early");
		}
		return meta_class;
	}

	public IRubyObject newInstance(IRubyObject[] args, CallContext ctx) {
		IRubyObject obj = newInstance();
		obj.do_select(get_initialize()).call(obj, args, null);
		return obj;
	}

	public abstract IRubyObject newInstance();

	public static IRubyObject unmarshalFrom(UnmarshalStream stream) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IRubyObject fast_lt(IRubyObject arg, Selector selector) {
		return op_lt(arg);
	}

	public IRubyObject op_lt(IRubyObject arg) {
		if (arg instanceof RubyClass) {
			return this.is_subclass_of ((RubyClass)arg);
		}
		
		return bool(false);
	}

	@Override
	public IRubyObject fast_gt(IRubyObject arg, Selector selector) {
		return op_gt(arg);
	}

	public IRubyObject op_gt(IRubyObject arg) {
		if (arg instanceof RubyClass) {
			return ((RubyClass)arg).is_subclass_of(this);
		}
		
		return bool(false);
	}

	private IRubyObject is_subclass_of(RubyClass class1) {
		boolean result = this.get_meta_class().is_subclass_of(class1.get_meta_class());
		return bool(result);
	}

	@Override
	public IRubyObject fast_eq3(IRubyObject arg, Selector selector) {
		return op_eq3(arg);
	}
	
	public IRubyObject op_eq3(IRubyObject arg) {
		// test if receiver is an instance of this class
		
		if (arg == this) return bool(true);
		
		RubyClass clazz = (RubyClass) arg.get_class();
		if (clazz == this) return bool(true);
		return clazz.is_subclass_of(this);
	}

	@Override
	public IRubyObject fast_ge(IRubyObject arg, Selector selector) {
		return op_ge(arg);
	}
	
	public IRubyObject op_ge(IRubyObject arg) {
		if (arg == this) return bool(true);
		
		if (arg instanceof RubyClass) {
			return ((RubyClass)arg).is_subclass_of ((RubyClass)this);
		}
		
		return bool(false);
	}

}
