package com.trifork.hotruby.runtime;

/** 
 * NonLocalJumps are loop control statements break, next, redo,
 * which can escape a block evaluation
 * 
 * @author krab
 */
public abstract class NonLocalJump extends Exception {

	public static final int NONLOCAL_REDO = 0;
	public static final int NONLOCAL_BREAK = 1;
	public static final int NONLOCAL_NEXT = 2;

	public final int kind;
	
	public NonLocalJump(int kind) {
		this.kind = kind;
	}
}
