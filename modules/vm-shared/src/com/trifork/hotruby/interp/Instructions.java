package com.trifork.hotruby.interp;

public interface Instructions {
    public static final int NOP = 0;
    public static final int GETLOCAL = 1;
    public static final int SETLOCAL = 2;
    public static final int GETSPECIAL = 3;
    public static final int SETSPECIAL = 4;
    public static final int GETDYNAMIC = 5;
    public static final int SETDYNAMIC = 6;
    public static final int GETINSTANCEVARIABLE = 7;
    public static final int SETINSTANCEVARIABLE = 8;
    public static final int GETCLASSVARIABLE = 9;
    public static final int SETCLASSVARIABLE = 10;
    public static final int GETCONSTANT = 11;
    public static final int SETCONSTANT = 12;
    public static final int GETGLOBAL = 13;
    public static final int SETGLOBAL = 14;
    public static final int PUSHNIL = 15; // ok
    public static final int PUSHSELF = 16; // ok
    public static final int PUTUNDEF = 17;
    public static final int PUSHOBJECT = 18; // ok
    public static final int PUSHOBJECT2 = 19; // ok
    public static final int CONCATSTRINGS = 20; // ok
    public static final int TOSTRING = 21;
    public static final int NEWREGEX = 22; // ok
    public static final int NEWARRAY = 23; // ok
    public static final int UNWRAP_RAISE = 24;
    public static final int EXPAND_REST_ARG = 25;
    public static final int CONCATARRAY = 26;
    public static final int SPLATARRAY = 27;
    public static final int CHECKINCLUDEARRAY = 28;
    public static final int NEWHASH = 29; // ok
    public static final int NEWRANGE = 30;
    public static final int PUTNOT = 31;
    public static final int POP = 32; // ok
    public static final int DUP = 33; // ok
    public static final int DUPN = 34; // dup the N'th element on stack
    public static final int SWAP = 35;
    public static final int REPUT = 36;
    public static final int POPN = 37;
    public static final int SETN = 38;
    public static final int EMPTSTACK = 38;
    public static final int DEFINEMETHOD = 39; // ok
    public static final int ALIAS = 40;
    public static final int UNDEF = 41;
    public static final int DEFINED = 42;
    public static final int POSTEXE = 43;
    public static final int TRACE = 44;
    public static final int DEFINECLASS = 45;
    public static final int SEND = 46; // ok
    public static final int INVOKESUPER = 47;
    public static final int INVOKEBLOCK = 48; // ok
    public static final int RETURN = 49; // non-local return from block
    public static final int LEAVE = 50; // last insn of code block
    public static final int THROW = 51;
    
    public static final int JUMP = 52; // ok
    public static final int BRANCHIF = 53; // ok
    public static final int BRANCHUNLESS = 54; // ok
    
    // exactly like "SEND", except we know that the receiver is "self"
    public static final int SELFSEND = 55;
    
    // 
    public static final int INTERNAL_TO_A = 56;
    
    // push block [2-byte immediate]
    public static final int UNUSED_57 = 57;
    
    // extract block reference from a proc object
    public static final int PROC2BLOCK = 58;
    
    // make sybol from string
    public static final int NEWSYMBOL = 59;
    
    // 
    public static final int MAKERETURN = 60;
    
    
    
    public static final int NONLOCAL_BREAK = 61;
    public static final int NONLOCAL_NEXT = 62;
    public static final int NONLOCAL_REDO = 63;

    public static final int ARRAY_AT = 64;
    public static final int ARRAY_REST = 65;
    
    public static final int JAVA_ARRAY_AT = 66;
    public static final int JAVA_ARRAY_REST = 67;
    
    public static final int DEFINEMODULE = 68;
    
    public static final int FAST_PLUS = 70;
    public static final int FAST_MINUS = 71;
    public static final int FAST_LT = 74;
    public static final int FAST_LE = 82;
    public static final int FAST_BIT_OR = 75;
    public static final int FAST_BIT_NOT = 76;
    public static final int FAST_BIT_AND = 77;
    public static final int FAST_EQ2 = 78;
    public static final int FAST_GT = 79;
    public static final int FAST_BIT_XOR = 80;
    public static final int FAST_RSHIFT = 81;
    public static final int FAST_EQ3 = 83;
    
    public static final int LOCAL_JSR = 100;
    public static final int LOCAL_RETURN = 101;

    // a call that has a *arg
    public static final int FLAG_REST_ARG = 0x01;
    
    // a call that has a &arg
    public static final int FLAG_BLOCK_ARG = 0x02;
    
    // a call that has an { .. } arg
    public static final int FLAG_IMM_BLOCK = 0x04;
    
    // expression should push return value
    public static final int FLAG_PUSH_RESULT = 0x08;
    
    public static final int FLAG_PROC_NEW = 0x10;
    
    // send opcode that may call eval
    public static final int FLAG_SEND_EVAL = 0x20;
    
    
    
    public static final int REGEX_OPTION_I = 0x01;
    public static final int REGEX_OPTION_O = 0x02;
    public static final int REGEX_OPTION_M = 0x04;
    public static final int REGEX_OPTION_X = 0x08;
    public static final int REGEX_OPTION_N = 0x10;
    public static final int REGEX_OPTION_E = 0x20;
    public static final int REGEX_OPTION_U = 0x40;
    public static final int REGEX_OPTION_S = 0x80;
    
    public static final int VISIBILITY_PUBLIC     = 0;
    public static final int VISIBILITY_PROTECTED  = 1;
    public static final int VISIBILITY_PRIVATE    = 2;
    
    public static final int CPOOL_NIL = 0;
    public static final int CPOOL_TRUE = 1;
    public static final int CPOOL_FALSE = 2;
    
	public static final int ISEQ_TYPE_TOPLEVEL = 1;
	public static final int ISEQ_TYPE_BLOCK = 2;
	public static final int ISEQ_TYPE_METHOD = 3;
	public static final int ISEQ_TYPE_MODULE = 4;
	public static final int ISEQ_TYPE_CLASS = 5;
	
	// kind of trace
	public static final int TRACE_LINE = 0;
	public static final int TRACE_CALL = 1;
	public static final int TRACE_RETURN = 2;
	public static final int TRACE_CLASS = 3;
	public static final int TRACE_END = 4;
	public static final int TRACE_RAISE = 5;
	public static final int FLAG_DEFINECLASS_SUPER_CLASS_GIVEN = 1;
	public static final int FLAG_DEFINECLASS_IS_SINGLETON = 2;
	
}
