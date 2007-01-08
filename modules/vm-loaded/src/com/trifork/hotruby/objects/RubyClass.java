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

}
