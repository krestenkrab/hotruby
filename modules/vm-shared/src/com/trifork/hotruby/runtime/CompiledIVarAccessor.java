package com.trifork.hotruby.runtime;

import java.lang.reflect.Field;

import com.trifork.hotruby.objects.IRubyObject;

public class CompiledIVarAccessor extends RubyIvarAccessor {

	Field reflectedField;
	
	public boolean isCompiled() {
		return true;
	}

	public CompiledIVarAccessor(String name) {
		super(name);
	}
	
	public String getFieldName() {
		return "ivar_" + name.substring(1); // skip @
	}
	
	// must be called when instance class is loaded
	public void setBaseClass(Class base)
	{
		try {
			reflectedField = base.getDeclaredField(getFieldName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalError("unable to get field");
		}
	}

	@Override
	public IRubyObject get(IRubyObject self) {
		IRubyObject result;
		try {
			result = (IRubyObject) reflectedField.get(self);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalError("unable to access ivar");
		}
		if (result == null) {
			return self.getRuntime().getNil();
		} else {
			return result;
		}
	}

	@Override
	public void set(IRubyObject self, IRubyObject value) {
		if (self.isFrozen()) { throw new RuntimeException("target frozen"); }
		if (value.isTaint())  {
			self.setTaint(true);
		}

		try {
			reflectedField.set(self, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalError("unable to access ivar");
		}
	}

}
