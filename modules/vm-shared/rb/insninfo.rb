
module InsnInfo
  
 public
  
  VM_CALL_ARGS_SPLAT_BIT   =  0x01
  VM_CALL_ARGS_BLOCKARG_BIT = 0x02
  VM_CALL_FCALL_BIT        =  0x04
  VM_CALL_VCALL_BIT        =  0x08
  VM_CALL_TAILCALL_BIT     =  0x10
  VM_CALL_TAILRECURSION_BIT = 0x20
  VM_CALL_SUPER_BIT        =  0x40
  
  TS_ISEQ = 'S'[0]
  TS_GENTRY = 'G'[0]
  TS_OFFSET = 'O'[0]
  TS_DINDEX = 'D'[0]
  TS_VARIABLE = '.'[0]
  TS_CDHASH = 'H'[0]
  TS_IC = 'C'[0]
  TS_ID = 'I'[0]
  TS_VALUE = 'V'[0]
  TS_LINDEX = 'L'[0]
  TS_NUM = 'N'[0]
  
  INSN_NAME_INFO = [
  "nop",
  "getlocal",
  "setlocal",
  "getspecial",
  "setspecial",
  "getdynamic",
  "setdynamic",
  "getinstancevariable",
  "setinstancevariable",
  "getclassvariable",
  "setclassvariable",
  "getconstant",
  "setconstant",
  "getglobal",
  "setglobal",
  "putnil",
  "putself",
  "putundef",
  "putobject",
  "putstring",
  "concatstrings",
  "tostring",
  "toregexp",
  "newarray",
  "duparray",
  "expandarray",
  "concatarray",
  "splatarray",
  "checkincludearray",
  "newhash",
  "newrange",
  "putnot",
  "pop",
  "dup",
  "dupn",
  "swap",
  "reput",
  "topn",
  "emptstack",
  "definemethod",
  "alias",
  "undef",
  "defined",
  "postexe",
  "trace",
  "defineclass",
  "send",
  "invokesuper",
  "invokeblock",
  "leave",
  "finish",
  "throw",
  "jump",
  "branchif",
  "branchunless",
  "getinlinecache",
  "onceinlinecache",
  "setinlinecache",
  "opt_case_dispatch",
  "opt_checkenv",
  "opt_plus",
  "opt_minus",
  "opt_mult",
  "opt_div",
  "opt_mod",
  "opt_eq",
  "opt_lt",
  "opt_le",
  "opt_ltlt",
  "opt_aref",
  "opt_aset",
  "opt_length",
  "opt_succ",
  "opt_regexpmatch1",
  "opt_regexpmatch2",
  "opt_call_native_compiled",
  "bitblt",
  "answer",
  "getlocal_OP_2",
  "getlocal_OP_3",
  "getlocal_OP_4",
  "setlocal_OP_2",
  "setlocal_OP_3",
  "setlocal_OP_4",
  "getdynamic_OP__WC__0",
  "getdynamic_OP_1_0",
  "getdynamic_OP_2_0",
  "getdynamic_OP_3_0",
  "getdynamic_OP_4_0",
  "setdynamic_OP__WC__0",
  "setdynamic_OP_1_0",
  "setdynamic_OP_2_0",
  "setdynamic_OP_3_0",
  "setdynamic_OP_4_0",
  "putobject_OP_INT2FIX_O_0_C_",
  "putobject_OP_INT2FIX_O_1_C_",
  "putobject_OP_Qtrue",
  "putobject_OP_Qfalse",
  "send_OP__WC___WC__Qfalse_0__WC_",
  "send_OP__WC__0_Qfalse_0__WC_",
  "send_OP__WC__1_Qfalse_0__WC_",
  "send_OP__WC__2_Qfalse_0__WC_",
  "send_OP__WC__3_Qfalse_0__WC_",
  "send_OP__WC___WC__Qfalse_0x04__WC_",
  "send_OP__WC__0_Qfalse_0x04__WC_",
  "send_OP__WC__1_Qfalse_0x04__WC_",
  "send_OP__WC__2_Qfalse_0x04__WC_",
  "send_OP__WC__3_Qfalse_0x04__WC_",
  "send_OP__WC__0_Qfalse_0x0c__WC_",
  "UNIFIED_putobject_putobject",
  "UNIFIED_putobject_putstring",
  "UNIFIED_putobject_setlocal",
  "UNIFIED_putobject_setdynamic",
  "UNIFIED_putstring_putstring",
  "UNIFIED_putstring_putobject",
  "UNIFIED_putstring_setlocal",
  "UNIFIED_putstring_setdynamic",
  "UNIFIED_dup_setlocal",
  "UNIFIED_getlocal_getlocal",
  "UNIFIED_getlocal_putobject"
  ]
  
  INSN_OPERAND_INFO = [
  "", 
  "L", 
  "L", 
  "NN", 
  "N", 
  "DN", 
  "DN", 
  "I", 
  "I", 
  "I", 
  "IV", 
  "I", 
  "I", 
  "G", 
  "G", 
  "", 
  "", 
  "", 
  "V", 
  "V", 
  "N", 
  "", 
  "N", 
  "N", 
  "V", 
  "NN", 
  "", 
  "V", 
  "V", 
  "N", 
  "N", 
  "", 
  "", 
  "", 
  "N", 
  "", 
  "", 
  "N", 
  "", 
  "ISN", 
  "VII", 
  "I", 
  "NVV", 
  "S", 
  "NV", 
  "ISN", 
  "INSNC", 
  "NSN", 
  "NN", 
  "", 
  "", 
  "N", 
  "O", 
  "O", 
  "O", 
  "CO", 
  "CO", 
  "O", 
  "HO", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "V", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "D", 
  "", 
  "", 
  "", 
  "", 
  "D", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "", 
  "INC", 
  "IC", 
  "IC", 
  "IC", 
  "IC", 
  "INC", 
  "IC", 
  "IC", 
  "IC", 
  "IC", 
  "IC", 
  "VV", 
  "VV", 
  "VL", 
  "VDN", 
  "VV", 
  "VV", 
  "VL", 
  "VDN", 
  "L", 
  "LL", 
  "LV", 
  ]
  
  
  INSN_LEN_INFO = [  
  1,
  2,
  2,
  3,
  2,
  3,
  3,
  2,
  2,
  2,
  3,
  2,
  2,
  2,
  2,
  1,
  1,
  1,
  2,
  2,
  2,
  1,
  2,
  2,
  2,
  3,
  1,
  2,
  2,
  2,
  2,
  1,
  1,
  1,
  2,
  1,
  1,
  2,
  1,
  4,
  4,
  2,
  4,
  2,
  3,
  4,
  6,
  4,
  3,
  1,
  1,
  2,
  2,
  2,
  2,
  3,
  3,
  2,
  3,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  2,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  2,
  1,
  1,
  1,
  1,
  2,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  4,
  3,
  3,
  3,
  3,
  4,
  3,
  3,
  3,
  3,
  3,
  3,
  3,
  3,
  4,
  3,
  3,
  3,
  4,
  2,
  3,
  3
  ]
  
  
  INSN_STACK_PUSH_NUM_INFO = [  
  0,
  1,
  0,
  1,
  0,
  1,
  0,
  1,
  0,
  1,
  0,
  1,
  0,
  1,
  0,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  2,
  1,
  1,
  1,
  0,
  2,
  1,
  2,
  1,
  1,
  1,
  0,
  0,
  0,
  1,
  0,
  0,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  0,
  0,
  0,
  1,
  1,
  1,
  0,
  0,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  0,
  1,
  1,
  1,
  1,
  1,
  0,
  0,
  0,
  1,
  1,
  1,
  1,
  1,
  0,
  0,
  0,
  0,
  0,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  1,
  2,
  2,
  0,
  0,
  2,
  2,
  0,
  0,
  1,
  2,
  2,
  ]    
  
  public
  
  INSN_NAME_INFO.each_index {|idx|
    const_set "INSN_#{INSN_NAME_INFO[idx]}", idx
  }
  
  MAX_INSTRUCTION_SIZE = INSN_NAME_INFO.length+1
  
  def InsnInfo.insn_stack_increase(depth,insn,opes)
    
    case insn
    when INSN_nop: 
      return depth
      
    when INSN_getlocal: 
      return depth+1
      
    when INSN_setlocal: 
      return depth-1
      
    when INSN_getspecial:
      return depth + 1;
      
    when INSN_setspecial:
      return depth + -1;
      
    when INSN_getdynamic:
      return depth + 1;
      
    when INSN_setdynamic:
      return depth + -1;
      
    when INSN_getinstancevariable:
      return depth + 1;
      
    when INSN_setinstancevariable:
      return depth + -1;
      
    when INSN_getclassvariable:
      return depth + 1;
      
    when INSN_setclassvariable:
      return depth + -1;
      
    when INSN_getconstant:
      return depth + 0;
      
    when INSN_setconstant:
      return depth + -2;
      
    when INSN_getglobal:
      return depth + 1;
      
    when INSN_setglobal:
      return depth + -1;
      
    when INSN_putnil:
      return depth + 1;
      
    when INSN_putself:
      return depth + 1;
      
    when INSN_putundef:
      return depth + 1;
      
    when INSN_putobject:
      return depth + 1;
      
    when INSN_putstring:
      return depth + 1;
      
    when INSN_concatstrings:
      int inc = 0
      num = opes[0]
      inc += 1 - num
      return depth + inc
      
    when INSN_tostring:
      return depth + 0;
      
    when INSN_toregexp:
      return depth + 0;
      
    when INSN_newarray:
      int inc = 0;
      num = opes[0];
      inc += 1 - num;;
      return depth + inc;
      
    when INSN_duparray:
      return depth + 1;
      
    when INSN_expandarray:
      int inc = 0;
      num = opes[0];
      flag = opes[1];
      inc += (num > 0) ? (num - 1 + (flag ? 1 : 0)) : (num + 1 - (flag ? 1 : 0))
      return depth + inc;
      
    when INSN_concatarray:
      return depth + -1;
      
    when INSN_splatarray:
      return depth + 0;
      
    when INSN_checkincludearray:
      return depth + 0;
      
    when INSN_newhash:
      int inc = 0;
      num = opes[0];
      inc += 1 - num;;
      return depth + inc;
      
    when INSN_newrange:
      return depth + -1;
      
    when INSN_putnot:
      return depth + 0;
      
    when INSN_pop:
      return depth + -1;
      
    when INSN_dup:
      return depth + 1;
      
    when INSN_dupn:
      int inc = 0;
      n = opes[0];
      inc += n;;
      return depth + inc;
      
    when INSN_swap:
      return depth + 0;
      
    when INSN_reput:
      int inc = 0;
      inc += 0;;
      return depth + inc;
      
    when INSN_topn:
      int inc = 0;
      n = opes[0];
      inc += 1;;
      return depth + inc;
      
    when INSN_emptstack:
      int inc = 0;
      inc = 0; depth = 0;;
      return depth + inc;
      
    when INSN_definemethod:
      return depth + -1;
      
    when INSN_alias:
      return depth + 0;
      
    when INSN_undef:
      return depth + 0;
      
    when INSN_defined:
      return depth + 0;
      
    when INSN_postexe:
      return depth + 0;
      
    when INSN_trace:
      return depth + 0;
      
    when INSN_defineclass:
      return depth + -1;
      
    when INSN_send:
      int inc = 0;
      op_argc = opes[1];
      op_flag = opes[3];
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT) == VM_CALL_ARGS_BLOCKARG_BIT ? 1 : 0));;
      return depth + inc;
      
    when INSN_invokesuper:
      int inc = 0;
      op_argc = opes[0];
      flag = opes[2];
      inc += - op_argc;;
      return depth + inc;
      
    when INSN_invokeblock:
      int inc = 0;
      num = opes[0];
      flag = opes[1];
      inc += 1 - num;;
      return depth + inc;
      
    when INSN_leave:
      return depth + 0;
      
    when INSN_finish:
      return depth + 0;
      
    when INSN_throw:
      return depth + 0;
      
    when INSN_jump:
      return depth + 0;
      
    when INSN_branchif:
      return depth + -1;
      
    when INSN_branchunless:
      return depth + -1;
      
    when INSN_getinlinecache:
      return depth + 1;
      
    when INSN_onceinlinecache:
      return depth + 1;
      
    when INSN_setinlinecache:
      return depth + 0;
      
    when INSN_opt_case_dispatch: 
      inc = 0;
      inc += -1
      return depth + inc;
      
    when INSN_opt_checkenv:
      return depth + 0;
      
    when INSN_opt_plus:
      return depth + -1;
      
    when INSN_opt_minus:
      return depth + -1;
      
    when INSN_opt_mult:
      return depth + -1;
      
    when INSN_opt_div:
      return depth + -1;
      
    when INSN_opt_mod:
      return depth + -1;
      
    when INSN_opt_eq:
      return depth + -1;
      
    when INSN_opt_lt:
      return depth + -1;
      
    when INSN_opt_le:
      return depth + -1;
      
    when INSN_opt_ltlt:
      return depth + -1;
      
    when INSN_opt_aref:
      return depth + -1;
      
    when INSN_opt_aset:
      return depth + -2;
      
    when INSN_opt_length:
      return depth + 0;
      
    when INSN_opt_succ:
      return depth + 0;
      
    when INSN_opt_regexpmatch1:
      return depth + 0;
      
    when INSN_opt_regexpmatch2:
      return depth + -1;
      
    when INSN_opt_call_native_compiled:
      return depth + 0;
      
    when INSN_bitblt:
      return depth + 1;
      
    when INSN_answer:
      return depth + 1;
      
    when INSN_getlocal_OP_2:
      return depth + 1;
      
    when INSN_getlocal_OP_3:
      return depth + 1;
      
    when INSN_getlocal_OP_4:
      return depth + 1;
      
    when INSN_setlocal_OP_2:
      return depth + -1;
      
    when INSN_setlocal_OP_3:
      return depth + -1;
      
    when INSN_setlocal_OP_4:
      return depth + -1;
      
    when INSN_getdynamic_OP__WC__0:
      return depth + 1;
      
    when INSN_getdynamic_OP_1_0:
      return depth + 1;
      
    when INSN_getdynamic_OP_2_0:
      return depth + 1;
      
    when INSN_getdynamic_OP_3_0:
      return depth + 1;
      
    when INSN_getdynamic_OP_4_0:
      return depth + 1;
      
    when INSN_setdynamic_OP__WC__0:
      return depth + -1;
      
    when INSN_setdynamic_OP_1_0:
      return depth + -1;
      
    when INSN_setdynamic_OP_2_0:
      return depth + -1;
      
    when INSN_setdynamic_OP_3_0:
      return depth + -1;
      
    when INSN_setdynamic_OP_4_0:
      return depth + -1;
      
    when INSN_putobject_OP_INT2FIX_O_0_C_:
      return depth + 1;
      
    when INSN_putobject_OP_INT2FIX_O_1_C_:
      return depth + 1;
      
    when INSN_putobject_OP_Qtrue:
      return depth + 1;
      
    when INSN_putobject_OP_Qfalse:
      return depth + 1;
      
    when INSN_send_OP__WC___WC__Qfalse_0__WC_:
      int inc = 0;
      op_argc = opes[1];
      op_flag = 0;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__0_Qfalse_0__WC_:
      int inc = 0;
      op_argc = 0;
      op_flag = 0;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__1_Qfalse_0__WC_:
      int inc = 0;
      op_argc = 1;
      op_flag = 0;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__2_Qfalse_0__WC_:
      int inc = 0;
      op_argc = 2;
      op_flag = 0;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__3_Qfalse_0__WC_:
      int inc = 0;
      op_argc = 3;
      op_flag = 0;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC___WC__Qfalse_0x04__WC_:
      int inc = 0;
      op_argc = opes[1];
      op_flag = 0x04;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__0_Qfalse_0x04__WC_:
      int inc = 0;
      op_argc = 0;
      op_flag = 0x04;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__1_Qfalse_0x04__WC_:
      int inc = 0;
      op_argc = 1;
      op_flag = 0x04;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__2_Qfalse_0x04__WC_:
      int inc = 0;
      op_argc = 2;
      op_flag = 0x04;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__3_Qfalse_0x04__WC_:
      int inc = 0;
      op_argc = 3;
      op_flag = 0x04;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_send_OP__WC__0_Qfalse_0x0c__WC_:
      int inc = 0;
      op_argc = 0;
      op_flag = 0x0c;
      inc += - (op_argc + ((op_flag & VM_CALL_ARGS_BLOCKARG_BIT)== VM_CALL_ARGS_BLOCKARG_BIT  ? 1 : 0));;
      return depth + inc;
      
    when INSN_UNIFIED_putobject_putobject:
      return depth + 2;
      
    when INSN_UNIFIED_putobject_putstring:
      return depth + 2;
      
    when INSN_UNIFIED_putobject_setlocal:
      return depth + 0;
      
    when INSN_UNIFIED_putobject_setdynamic:
      return depth + 0;
      
    when INSN_UNIFIED_putstring_putstring:
      return depth + 2;
      
    when INSN_UNIFIED_putstring_putobject:
      return depth + 2;
      
    when INSN_UNIFIED_putstring_setlocal:
      return depth + 0;
      
    when INSN_UNIFIED_putstring_setdynamic:
      return depth + 0;
      
    when INSN_UNIFIED_dup_setlocal:
      return depth + 0;
      
    when INSN_UNIFIED_getlocal_getlocal:
      return depth + 2;
      
    when INSN_UNIFIED_getlocal_putobject:
      return depth + 2;
      
    else
      return 0
    end
  end
  
  
  def InsnInfo.insn_len(insn)
    INSN_LEN_INFO[insn]
  end
  
  def InsnInfo.insn_name(insn)
    INSN_NAME_INFO[insn]
  end
  
  def InsnInfo.insn_op_types(insn)
    INSN_OPERAND_INFO[insn]
  end          
  
  def InsnInfo.insn_op_type(insn,pos)
    len = insn_len(insn)-1
    if (pos<len) 
      return INSN_OPERAND_INFO[insn][pos]
    else
      return 0
    end
  end
  
  def InsnInfo.insn_ret_num(insn) 
    INSN_STACK_PUSH_NUM_INFO[insn]
  end                    
  
end


