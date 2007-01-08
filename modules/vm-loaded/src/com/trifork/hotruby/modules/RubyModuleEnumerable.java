package com.trifork.hotruby.modules;

import com.trifork.hotruby.classes.RubyClassModule;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyModule;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;

public final class RubyModuleEnumerable extends RubyModule {
	static public RubyModuleEnumerable instance;

	public void init(MetaModule meta) {
		instance = (RubyModuleEnumerable) this;
		super.init(meta);
	}

	public interface SelectEnumerableMethod {
		RubyMethod get_methodForEnumerable();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectEnumerableMethod) {
			return ((SelectEnumerableMethod) sel).get_methodForEnumerable();
		} else {
			return LoadedRubyRuntime.resolve_method((RubyModuleEnumerable) this,
					sel, SelectEnumerableMethod.class);
		}
	}

	public RubyClass get_class() {
		return RubyClassModule.instance;
	}

}
