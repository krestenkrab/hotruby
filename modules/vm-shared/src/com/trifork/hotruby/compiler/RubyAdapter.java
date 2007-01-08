package com.trifork.hotruby.compiler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

public class RubyAdapter extends GeneratorAdapter {

	public RubyAdapter(MethodVisitor mv, int access, String name, String desc) {
		super(mv, access, name, desc);
	}

	public void rbStoreLocal(int index, Type type) {
		mv.visitVarInsn(type.getOpcode(Opcodes.ISTORE), index);		
	}

	public void rbLoadLocal(int index, Type type) {
		mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), index);
	}

}
