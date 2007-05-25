package com.trifork.hotruby.modules;

import com.trifork.hotruby.classes.RubyClassModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.RubyClass;
import com.trifork.hotruby.objects.RubyModule;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.PublicMethodN;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyMethod;
import com.trifork.hotruby.runtime.Selector;
import com.trifork.hotruby.util.L4RootManager;

public final class RubyModuleObjectSpace extends RubyModule {
	static public RubyModuleObjectSpace instance;
	
	static com.trifork.hotruby.util.L4RootManager roots = new L4RootManager();
	

	public static void register(IRubyObject obj) 
	{
		roots.registerRoot(obj);
	}

	public void init(MetaModule meta) {
		instance = (RubyModuleObjectSpace) this;
		super.init(meta);
		
		meta.register_module_method("each_object", new PublicMethodN() {

			@Override
			public IRubyObject call(IRubyObject receiver, IRubyObject[] args, RubyBlock block) {

				if (block == null) throw getRuntime().newArgumentError("missing block");
				
				Object[] value = roots.getRoots();
				
				System.out.println("world has "+value.length+" objects");
				
				int size = value.length;
				
				loop: for (int i = 0; i < size; i++) {
					body: do {
						try {
							block.call((IRubyObject)value[i]);
						} catch (NonLocalBreak e) {
							break loop;
						} catch (NonLocalNext e) {
							continue loop;
						} catch (NonLocalRedo e) {
							continue body;
						}
					} while (false);
				}
				
				return LoadedRubyRuntime.NIL;
			}});
	}

	public interface SelectObjectSpaceMethod {
		RubyMethod get_methodForObjectSpace();
	}

	public RubyMethod select(Selector sel) {
		if (sel instanceof SelectObjectSpaceMethod) {
			return ((SelectObjectSpaceMethod) sel).get_methodForObjectSpace();
		} else {
			return LoadedRubyRuntime.resolve_method((RubyModuleObjectSpace) this,
					sel, SelectObjectSpaceMethod.class);
		}
	}

	public RubyClass get_class() {
		return RubyClassModule.instance;
	}

}
