package com.trifork.hotruby.runtime;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.interp.ISeq;
import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;

public class ThreadState {

	static ThreadLocal<ThreadState> state = new  ThreadLocal<ThreadState>() {
		@Override
		protected ThreadState initialValue() {
			return new ThreadState();
		}
	};

	public static ThreadState get() {
		return state.get();
	}

	Object[] stack = new Object[512];
	int sp = 0;
	private RubyBlock callers_block;
	private Frame frame;
	
	public void push(IRubyObject receiver) {
		stack[sp++] = receiver;
	}

	public int getStackPointer() {
		return sp;
	}

	public Object[] getStack() {
		return stack;
	}

	public void setStackPointer(int sp) {
		this.sp = sp;
	}

	public IRubyObject pop() {
		return (IRubyObject) stack[--sp];
	}

	public RubyBlock popBlock() {
		return (RubyBlock) stack[--sp];
	}

	public IRubyObject[] pop(int count) {
		IRubyObject[] args = new IRubyObject[count];
		System.arraycopy(stack, sp-count, args, 0, count);
		sp -= count;
		return args;
	}

	public void pushBlock(RubyBlock block) {
		stack[sp++] = block;
	}

	public void push(IRubyObject[] val) {
		stack[sp++] = val;
	}

	public RubyBlock setCallersBlock(RubyBlock callers_block) {
		RubyBlock result = callers_block;
		this.callers_block = callers_block;
		return result;
	}

	public RubyBlock getCallersBlock() {
		return callers_block;
	}

	public String[] getInterpreterCallers() {
		List<String> frames = new ArrayList<String>();
		
		for (Frame f = frame; f != null; f = f.parent) {
			frames.add(f.getCallerString());
		}
		
		return frames.toArray(new String[frames.size()]);
	}
	
	static class Frame {
		private final ISeq iseq;

		public Frame(Frame frame, ISeq iseq) {
			this.parent = frame;
			this.iseq = iseq;
		}
		
		Frame parent;		
		int line;
		
		public String getCallerString() {
			if (line == 0) { line = iseq.firstLine(); }
			if (iseq.getMethod() == null) {
				return iseq.getFile() + ":" + line;
			} else {
				return iseq.getFile() + ":" + line + " in `" + iseq.getMethod() + "'";
			}
		}

	}
	

	public void setLine(int number) {
		this.frame.line = number;
	}
	
	public void pushFrame(ISeq iseq) {
		frame = new Frame(frame, iseq);
	}
	
	public void popFrame() {
		this.frame = frame.parent;
	}

	public int getLine() {
		return this.frame.line;
	}

	public static class ModuleFrame {
		ModuleFrame(ModuleFrame current, MetaModule module) {
			this.parent = current;
			this.module = module;
		}
		public final ModuleFrame parent;
		public final MetaModule module;
	}
	
	ModuleFrame current;
	
	public void pushModule(MetaModule module) {
		current = new ModuleFrame(current, module);
	}
	
	public void popModule() {
		current = current.parent;
	}
	
	public IRubyArray getModuleNesting(RubyRuntime runtime)
	{
		IRubyArray arr = runtime.newArray();
		for (ModuleFrame now = current; now != null; now = now.parent) {
			arr.add(now.module.get_base_module());
		}
		return arr;		
	}
	
	public ModuleFrame getModuleStack() {
		return current;
	}
}
