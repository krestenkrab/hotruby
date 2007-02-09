header {
package com.trifork.hotruby.parser;

import com.trifork.hotruby.ast.*;
}

/*
 [The "BSD licence"]
 Copyright (c) 2005-2006 Xue Yong Zhi (http://seclib.blogspot.com)
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

//----------------------------------------------------------------------------
// The Ruby parser. Do not use this one, use RubyParser instead!
//----------------------------------------------------------------------------
class RubyParserBase extends Parser;
options {
	k = 1;                           // Will add prediction manually if we need k > 1.
	exportVocab=Ruby;
	codeGenMakeSwitchThreshold = 2;
	codeGenBitsetTestThreshold = 999;
	defaultErrorHandler = false;
	//analyzerDebug = true;
}

tokens {
	// "imaginary" tokens
	RPAREN_IN_METHOD_DEFINITION;
	SYMBOL;
}

{
	private boolean seen_rparen_ = false;
	private boolean can_be_command_ = false;
	private boolean is_lhs_ = false;
	
	private RubyCode scope_;
	
	public RubyCode scope() { return scope_; }
	protected void scope(RubyCode code) { scope_ = code; }
	
	public String getFilename() { assert(false); return null; }
	
	protected Expression assignToLocal(Expression expr) {
	   if (expr instanceof FunctionExpression) {
	   	  FunctionExpression fe = (FunctionExpression)expr;
		  scope().assignToLocal(fe.getName(), false);
	   	  expr = new IdentifierExpression(fe.line(), scope(), fe.getName());
	   } else 
	   if (expr instanceof IdentifierExpression) {
	   	  VariableExpression e = (VariableExpression)expr;
		  scope().assignToLocal(e.getName(), false);
	   } else {
	   	  // throw new Error("assign to local for unknown type: "+expr.getClass());
	   }	
	   return expr;
	}
	
	protected int line() { 
		try {
			return LT(1).getLine();
		} catch (TokenStreamException e) {
			return 0;
		}  
	}

	protected int get_regexp_options() { assert(false); return 0; }
	protected Expression nextHereDoc() { assert(false); return null; }
	protected void declareParameter(String name) { scope().assignToLocal(name, true); }
	protected void tell_lexer_we_have_finished_parsing_methodparameters()	{assert(false);}
	protected void tell_lexer_we_have_finished_parsing_symbol()	{assert(false);}
	protected void tell_lexer_we_have_finished_parsing_string_expression_substituation()	{assert(false);}
	protected void tell_lexer_we_have_finished_parsing_regex_expression_substituation()	{assert(false);}
	
	protected <T> java.util.ArrayList<T> append(java.util.ArrayList<T> list, T val) {
	   if (list==null) { list=new java.util.ArrayList(); }
	   list.add(val);	
	   return list;
	}
	
	protected String concat(Token t1, Token t2) {
		StringBuffer sb = new StringBuffer();
		if (t1 != null) { sb.append(t1.getText()); }	
		if (t2 != null) { sb.append(t2.getText()); }	
		return sb.toString();
	}
	
}

program returns [TopLevelCode code = null]
{ Expression expr = null; } 
		: { scope(code = new TopLevelCode(getFilename())); }
			(expr=compoundStatement { code.setBody(expr); } )? EOF!
		  { scope(null); } 
		;
		
eval_body[com.trifork.hotruby.runtime.EvalContext ctx] returns [RubyCode code = null]
{ Expression expr = null; } 
		: { scope(code=new EvalCode(0, ctx, getFilename())); }
			(expr=compoundStatement { code.setBody(expr); } )? EOF!
		  { scope(null); }
;

compoundStatement returns [Expression expr=null]
		:	terminal	(expr=statements | {expr=NilExpression.instance;})
		|	expr=statements
		;

statements returns [Expression expr]
{ SequenceExpression seq = null; Expression stmt; }
		:	expr=statement
			(
				terminal
				{
					if (	(EOF == LA(1))	||	//script end
						(RCURLY == LA(1))	||	(LITERAL_end == LA(1))	||	//block end
						(RPAREN == LA(1))	||
						(LITERAL_else == LA(1)) || (LITERAL_elsif == LA(1))	||
						(LITERAL_rescue == LA(1)) || (LITERAL_ensure == LA(1))	||
						(LITERAL_when == LA(1))
					)
					break;
				}
				stmt=statement	
				{ 
					if (seq==null) { seq = new SequenceExpression(); seq.addExpression(expr); expr=seq; }
					seq.addExpression(stmt);
				} 
			)*
		;

terminal
		:	SEMI!
		|	LINE_BREAK!
		;

statement returns [Expression expr]
{ Expression expr2; int line = line(); java.util.List<RescueClause> rescues = null; }
		:	expr=statementWithoutModifier
			(IF_MODIFIER		expr2=expression { expr=new IfThenElseExpression(line, expr2, expr, NilExpression.instance); }
			|UNLESS_MODIFIER	expr2=expression { expr=new IfThenElseExpression(line, expr2, NilExpression.instance, expr); }
			|WHILE_MODIFIER		expr2=expression { expr=new WhileExpression(line, expr2, expr, true); }
			|UNTIL_MODIFIER		expr2=expression { expr=new WhileExpression(line, new UnaryExpression("not", expr2), expr, true); }
			|RESCUE_MODIFIER	{ line = line(); } expr2=expression
				{ rescues = new java.util.ArrayList<RescueClause>(); 
				  rescues.add (new RescueClause(line, null, null, expr2));
				  expr = new RescueExpression(expr, rescues, null, null); } 
			)*
		;

statementWithoutModifier returns [Expression expr=null]
{ String op; SequenceExpression args=null; final int line = line(); }
		:	keyword_alias	args=alias_parameter[args]	(LINE_BREAK)?	args=alias_parameter[args]		
				{ expr = new AliasExpression(args); }
		|	keyword_undef	args=undef_parameter[args]	(COMMA	(LINE_BREAK)? args=undef_parameter[args])*			
				{ expr = new UndefExpression(args); }
		|	keyword_BEGIN LCURLY_BLOCK (expr=compoundStatement | ) RCURLY
		|	keyword_END LCURLY_BLOCK (expr=compoundStatement | ) RCURLY
		
		|	expr=expression	(expr=parallelAssignmentLeftOver[assignToLocal(expr)])?
		|	REST_ARG_PREFIX	expr=dotAccess[true]	op=operator_ASSIGN	args=mrhs[null] 
			{   SequenceExpression lhs = new SequenceExpression();
				lhs.setRestArg(assignToLocal(expr));
				expr = new MultiAssignmentExpression(lhs, args); 
			}
		;

protected
undef_parameter [SequenceExpression args] returns [SequenceExpression args_out]
{ args_out = args==null? new SequenceExpression() : args; 
  String name = null; Expression expr=null; final int line = line(); }
		:(	i:IDENTIFIER	(ass1:ASSIGN_WITH_NO_LEADING_SPACE)? { name=concat(i,ass1); }
		|	c:CONSTANT		(ass2:ASSIGN_WITH_NO_LEADING_SPACE)? { name=concat(c,ass2); }
		|	f:FUNCTION		(ass3:ASSIGN_WITH_NO_LEADING_SPACE)? { name=concat(f,ass3); }
		|	expr=symbol[false]
		|	name=keywordAsMethodName
		|	name=operatorAsMethodname
		)
		{
			if (expr==null) { expr=new SymbolExpression(line, name); } 
			args_out.addExpression(expr);
		}
		;

protected
alias_parameter [SequenceExpression args] returns [SequenceExpression args_out]
{ args_out = args==null? new SequenceExpression() : args; final int line = line(); } 
		:	args_out=undef_parameter[args_out]
		|	gvar:GLOBAL_VARIBLE { args_out.addExpression ( new SymbolExpression(line, gvar.getText()) ); }
		;

//LPAREN can start primaryExpression, have to use syntactic predicate here.
mlhs_item[SequenceExpression lhs] returns [boolean last=false]
{ Expression expr = null; SequenceExpression nested = null; boolean done; }
		:	(LPAREN	mlhs_item[null]	COMMA)=>	
			LPAREN	
				{ nested = new SequenceExpression(); }
				done=mlhs_item[nested]
			  	({!done}?  COMMA	done=mlhs_item[nested] )*
				{ lhs.addExpression(nested); }
			RPAREN
		|	REST_ARG_PREFIX (expr=dotAccess[true] { expr=assignToLocal(expr); } )? 
				{ lhs.setRestArg(expr); last=true; }
		|	expr=dotAccess[true] 
				{ expr=assignToLocal(expr); lhs.addExpression(expr); }
		;

protected
empty_true returns[boolean result=true]
		:
		;

protected
empty_false returns[boolean result=false]
		:
		;

// first is the first 
parallelAssignmentLeftOver[Expression first] returns [Expression expr=null]
{ 
	Expression expr2; 
	SequenceExpression lhs = new SequenceExpression(); 
	lhs.addExpression(first); 
	SequenceExpression rhs = null;
	String op; 
	boolean done=false;
}
		:	({!done}? 
			   COMMA 
			   ({LA(1)==ASSIGN||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE}? done=empty_true
			   |done=mlhs_item[lhs])
			)+
			
			(op=operator_ASSIGN	rhs=mrhs[null]
				{ expr = new MultiAssignmentExpression(lhs, rhs); }
			|
				{ expr = lhs; }
			)
			
		;

mrhs[SequenceExpression appendTo] returns[SequenceExpression rhs=new SequenceExpression()]
{ Expression expr; boolean done=false; final int line = line(); if(appendTo!=null) rhs=appendTo; }
		:	expr=expression	{ rhs.addExpression(expr); }
			({!done}?
			COMMA 
			( REST_ARG_PREFIX expr=expression
				{ rhs.setRestArg(expr); done=true; }
			| expr=expression
				{ rhs.addExpression(expr); }
			| done=empty_true
			)
			)*
		|	REST_ARG_PREFIX	expr=expression 
				{ rhs.addExpression(new RestArgExpression(expr)); }
		;

block_vars[BlockCode block] 
{ boolean done = false; SequenceExpression parms = block.parms(); }
		:	done=mlhs_item[parms]		
			({!done}? 
				COMMA	
				((BOR|"in")=> done=empty_true
				| done=mlhs_item[parms])
			)*
		;

codeBlock returns [BlockCode block=null]
{ RubyCode context = scope(); int line = line(); }
		:
		{ scope(block = new BlockCode(line, context, getFilename())); }
		(	keyword_do	
			blockContent[block]
			"end"			
		|	LCURLY_BLOCK
			blockContent[block]
			RCURLY		
		)
		{ scope(context); }
		;

blockContent[BlockCode block] 
{ 
	Expression body = NilExpression.instance;
}
		:	(BOR (block_vars[block])? BOR
			|LOGICAL_OR)?
			(body=compoundStatement { block.setBody(body); } )?
		;

symbol[boolean string_allowed] returns [Expression expr=null]
{ String name = null; final int line = line(); }
                :       COLON_WITH_NO_FOLLOWING_SPACE
                                        (iden:IDENTIFIER    (options{greedy=true;}: ass1:ASSIGN_WITH_NO_LEADING_SPACE)? { name=concat(iden,ass1); } 
                                        |fun:FUNCTION       (options{greedy=true;}: ass2:ASSIGN_WITH_NO_LEADING_SPACE)? { name=concat(fun,ass2); } 
                                        |con:CONSTANT       (options{greedy=true;}: ass3:ASSIGN_WITH_NO_LEADING_SPACE)? { name=concat(con,ass3); } 
                                        |gvar:GLOBAL_VARIBLE				{ name = gvar.getText(); }
                                        |ivar:INSTANCE_VARIBLE				{ name = ivar.getText(); }
                                        |cvar:CLASS_VARIBLE					{ name = cvar.getText(); }
                                        |unary:UNARY_PLUS_MINUS_METHOD_NAME	{ name = unary.getText(); }
                                        |name=operatorAsMethodname
                                        |name=keyword
                                        |{string_allowed}? expr=string { expr=SymbolExpression.make(line, expr); } )
			{
				// System.out.println("read symbol "+name);
				tell_lexer_we_have_finished_parsing_symbol();
			 	if (expr==null) { expr = new SymbolExpression(line, name); }
			}
                ;


		
		
/*
symbol2 returns [Expression expr]
{ String name = null; Expression expr2; }
		:	COLON_WITH_NO_FOLLOWING_SPACE
					(IDENTIFIER	(options{greedy=true;} ASSIGN_WITH_NO_LEADING_SPACE)? 
					|fun:FUNCTION	(options{greedy=true;} ASSIGN_WITH_NO_LEADING_SPACE)?
					|con:CONSTANT	(options{greedy=true;} ASSIGN_WITH_NO_LEADING_SPACE)?
					|gvar:GLOBAL_VARIBLE				{ name = gvar.getText(); }
					|ivar:INSTANCE_VARIBLE				{ name = ivar.getText(); }
					|cvar:CLASS_VARIBLE					{ name = cvar.getText(); }
					|unary:UNARY_PLUS_MINUS_METHOD_NAME	{ name = unary.getText(); }
					|name=operatorAsMethodname
					|name=keyword
					|expr2=string { expr=SymbolExpression.make(expr2); } )
			{
				tell_lexer_we_have_finished_parsing_symbol();
			 	if (expr==null) { expr = new SymbolExpression(name); }
			}
		;
*/

/*
 *Operator expressions
 *lowest->
 *		and or
 *		not
 *		? :
 *		= += -= *= /= %= &= ^= |= <<= >>= **=
 *		.. ...
 *		||
 *		&&
 *		<=> ==  === !=  =~  !~
 *		>  >=  <  <=
 *		|	 ^
 *		&
 *		<<  >>
 *		+  -
 *		*  /  %
 *		-(unary)  +(unary)  !  ~
 *		**
 *		[]
 *		::
 *highest<-
 */

expression returns [Expression expr]
		:	expr=andorExpression
		;

//and or
andorExpression returns [Expression expr] { String op=null; Expression expr2; }
		:	expr=assignmentExpression 
			(options{greedy=true;/*caused by command*/}:(keyword_and {op="and";}| keyword_or{op="or";}) 
			expr2=assignmentExpression
			{ expr= new BinaryExpression(expr, op, expr2); } )*
		;


//= += -= *= /= %= **= &= ^= |= <<= >>= &&= ||=
assignmentExpression returns [Expression expr] 
{ String op=null; Expression expr2; }
		:	expr=notExpression
			(options{greedy=true;/*caused by command*/}:
			(	op=operator_ASSIGN 	
					{ 
						expr = assignToLocal(expr);
					}
			|	op=operator_PLUS_ASSIGN	
			|	op=operator_MINUS_ASSIGN
			|	op=operator_STAR_ASSIGN	
			|	op=operator_DIV_ASSIGN	
			|	op=operator_MOD_ASSIGN	
			|	op=operator_POWER_ASSIGN
			|	op=operator_BAND_ASSIGN	
			|	op=operator_BXOR_ASSIGN	
			|	op=operator_BOR_ASSIGN	
			|	op=operator_LEFT_SHIFT_ASSIGN	
			|	op=operator_RIGHT_SHIFT_ASSIGN
			|	op=operator_LOGICAL_AND_ASSIGN
			|	op=operator_LOGICAL_OR_ASSIGN)
			(REST_ARG_PREFIX expr2=rangeExpression { expr2=new RestArgExpression(expr2); }
			| expr2=notExpression )
			{ expr= AssignmentExpression.build (expr, op, expr2); }
			)*
		;

//not
notExpression returns [Expression expr]
		:	keyword_not expr=notExpression { expr = new UnaryExpression("not", expr); }
		|	expr=ternaryIfThenElseExpression
		;

ternaryIfThenElseExpression returns [Expression expr] { Expression expr2, expr3; String op; int line=0; }
		:	expr=rangeExpression 
				(options{greedy=true;/*caused by command*/}: 
				{ line = line(); }
				op=operator_QUESTION 	
				expr2=ternaryIfThenElseExpression	
				op=operator_COLON	
					expr3=ternaryIfThenElseExpression 
				{ expr = new IfThenElseExpression(line, expr, expr2, expr3); }
				)?
		;


//.. ...
rangeExpression returns [Expression expr] { String op=null; Expression expr2; }
		:	expr=logicalOrExpression 
			(options{greedy=true;/*caused by command*/}:
			( op=operator_INCLUSIVE_RANGE
			| op=operator_EXCLUSIVE_RANGE ) 
			expr2=logicalOrExpression { expr=new BinaryExpression(expr, op, expr2); } )* 
		;

//||
logicalOrExpression returns [Expression expr] { String op=null; Expression expr2; }
		:	expr=logicalAndExpression 
			(options{greedy=true;/*caused by command*/}:op=operator_LOGICAL_OR expr2=logicalAndExpression
				{ expr = new LogicalOrExpression(expr, expr2); }
			)*
		;

//&&
logicalAndExpression returns [Expression expr] { String op=null; Expression expr2; }
		:	expr=equalityExpression 
			(options{greedy=true;/*caused by command*/}:op=operator_LOGICAL_AND expr2=equalityExpression
				{ expr = new LogicalAndExpression(expr, expr2); }
			)*
		;

//<=> ==  === !=  =~  !~
equalityExpression  returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=relationalExpression (options{greedy=true;/*caused by command*/}:
			(	op=operator_COMPARE	
			|	op=operator_EQUAL		
			|	op=operator_CASE_EQUAL	
			|	op=operator_NOT_EQUAL
			|	op=operator_MATCH	
			|	op=operator_NOT_MATCH
			)
			expr2=relationalExpression
			{ 
				if ("!=".equals(op)) {
					expr=new UnaryExpression ("not", new BinaryExpression(expr, "==", expr2)); 
				} else if ("!~".equals(op)) {
					expr=new UnaryExpression ("not", new BinaryExpression(expr, "=~", expr2)); 
				} else {
					expr=new BinaryExpression(expr, op, expr2); 
				}
			}
			)*
		;


//>  >=  <  <=
relationalExpression  returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=orExpression (options{greedy=true;/*caused by command*/}:
			(	op=operator_LESS_THAN	
			|	op=operator_GREATER_THAN
			|	op=operator_LESS_OR_EQUAL	
			|	op=operator_GREATER_OR_EQUAL
			)
			expr2=orExpression
			{ expr = new BinaryExpression(expr, op, expr2); }
			)*
		;

//|  ^
orExpression returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=andExpression (options{greedy=true;/*caused by command*/}:
				(op=operator_BXOR |op=operator_BOR ) 
				expr2=andExpression
			{ expr = new BinaryExpression(expr, op, expr2); }
			)*
		;

//&
andExpression returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=shiftExpression (options{greedy=true;/*caused by command*/}:op=operator_BAND expr2=shiftExpression
			{ expr = new BinaryExpression(expr, op, expr2); }
		)*
		;



//<<  >>
shiftExpression returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=additiveExpression (options{greedy=true;/*caused by command*/}:
				(op=operator_LEFT_SHIFT
				|op=operator_RIGHT_SHIFT) 
				expr2=additiveExpression
				{ expr = new BinaryExpression(expr, op, expr2); }
			)*
		;



//+  -
additiveExpression returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=multiplicativeExpression (options{greedy=true;/*caused by command*/}:
			(op=operator_PLUS|op=operator_MINUS)
			expr2=multiplicativeExpression
			{ expr = new BinaryExpression(expr, op, expr2); }
			)*
		;

//*  /  %
multiplicativeExpression returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=powerExpression	(options{greedy=true;/*caused by command*/}:
			(op=operator_STAR|op=operator_DIV|op=operator_MOD)
			expr2=powerExpression
			{ expr = new BinaryExpression(expr, op, expr2); }
			)*
		;


//**
powerExpression returns [Expression expr] { Expression expr2; String op=null;  }
		:	expr=unaryExpression (options{greedy=true;/*caused by command*/}:op=operator_POWER expr2=unaryExpression
			{ expr = new BinaryExpression(expr, op, expr2); }
			)*
		;


//-(unary)  +(unary)  !  ~
unaryExpression returns [Expression expr] { Expression expr2; String op=null; java.util.ArrayList<String> ops=null;  }
			:(	op=operator_UNARY_PLUS { ops=append(ops,op); }
			|	op=operator_UNARY_MINUS { ops=append(ops,op); }
			|	op=operator_BNOT { ops=append(ops,op); }
			|	op=operator_NOT { ops=append(ops,"not"); }
			)*
			expr=dotAccess[false /*also allow non-lhs*/]
			{
				if (ops!=null) {
					for(int i = ops.size()-1; i>=0; i--) {
						expr=new UnaryExpression(ops.get(i), expr);	
					}	
				}	
			}
		;




dotAccess[boolean lhs] returns [Expression expr] 
		:	expr=command[lhs]
		;

commandPart[Expression base] returns [Expression expr]
{ 
	expr = base; 
	SequenceExpression args = null;  
	int line = line();
}
	:
		  DOT expr=methodCallAfterDot[expr, false] 
		| COLON2 expr=methodCallAfterDot[expr, true] 
		| (LBRACK_ARRAY_ACCESS|LBRACK)
				(args=arrayReferenceArgument)?
		  RBRACK! { expr = new MethodCallExpression(scope(), line, expr, "[]", args, null, false); }
		  
	;

command[boolean lhs_only] returns [Expression expr=null]
{ String method = null; 
  SequenceExpression args = null;  
  BlockCode block = null; 
  is_lhs_ = false;
  seen_rparen_ = false;
  MethodCallExpression mc = null;
  boolean maybe_command = false;
  int line = 0;
}
		:
		(expr=primaryExpressionCannotBeCommand     
			seen_rparen_=empty_false
			maybe_command=empty_false
		| 
		
			expr=primaryExpressionCanBeCommand
				{
					MethodDenominator denom = (MethodDenominator)expr;
					method = denom.getMethodName();
				}
				
			expr=restOfMethodCall[null, expr, method, false]
		    maybe_command=empty_true
		)

        (options{greedy=true;/*caused by command*/}: expr=commandPart[expr] maybe_command=empty_true is_lhs_=empty_true )*
        
		({!lhs_only && maybe_command && !seen_rparen_}?

				{ mc = MethodCallExpression.make(scope(), expr); }
				args=methodInvocationArgumentWithoutParen[new SequenceExpression(),false] 
				({!args.hasBlockArg()}? block=codeBlock)? 
				{ mc.setArgs(args); mc.setBlock(block);  expr = mc; }
				is_lhs_=empty_false
								
		)?
		
		// validate that either this is an lhs, or it is not required
		({is_lhs_ || !lhs_only}? { /*OK*/} 
		| { throw new NoViableAltException(LT(1), getFilename()); }
		) 

		
	|{!lhs_only}?
			{ line = line(); method = LT(1).getText(); }
			("return"|"break"|"next"|"retry"|"redo")	 
			(options{greedy=true;/*caused by command*/}:args=methodInvocationArgumentWithoutParen[args,false])?
			{ expr = new MethodCallExpression(scope(), line, null, method, args, null, false); }
		;


methodCallCanBeCommand[Expression base, boolean base_must_be_class_or_module] returns [Expression expr=null]
{ SequenceExpression args=null; String method=null; is_lhs_ = true; seen_rparen_=false; }
		:	expr=primaryExpressionCanBeCommand
				{
					MethodDenominator denom = (MethodDenominator)expr;
					method = denom.getMethodName();
				}
				
			expr=restOfMethodCall[base, expr, method, base_must_be_class_or_module]
		;

methodCallAfterDot[Expression base, boolean base_must_be_class_or_module] returns [Expression expr=null]
{ SequenceExpression args=null; String method=null; is_lhs_ = true; seen_rparen_=false; }
		:	( method=operatorAsMethodname
			| method=keywordAsMethodName
			| expr=variableCanBeCommand
				{
					MethodDenominator denom = (MethodDenominator)expr;
					method = denom.getMethodName();
				}
			)
			
			expr=restOfMethodCall[base,expr,method,base_must_be_class_or_module]
		;
		
restOfMethodCall[Expression base, Expression expr_in, String method, boolean base_must_be_class_or_module] 
	returns[Expression expr=expr_in]
{ SequenceExpression args = null; BlockCode block = null; int line = line();; }
:
			(LPAREN)=> args=methodInvocationArgumentWithParen is_lhs_=empty_false seen_rparen_=empty_true
			  (options{greedy=true;}: block=codeBlock  is_lhs_=empty_false)?
			  { expr = new MethodCallExpression(scope(), line, base, method, args, block, base_must_be_class_or_module); }
			|(LCURLY_BLOCK|"do")=> block=codeBlock  is_lhs_=empty_false
			  { expr = new MethodCallExpression(scope(), line, base, method, null, block, base_must_be_class_or_module); }
			| 
              { if (base != null) expr = new MethodCallExpression(scope(), line, base, method, null, block, base_must_be_class_or_module); }
;		
methodInvocationArgumentWithParen returns [SequenceExpression args=null]
		:	LPAREN
				(args=methodInvocationArgumentWithoutParen[null, true])?
				(LINE_BREAK!)?
			RPAREN
		;

methodInvocationArgumentWithoutParen[SequenceExpression args, boolean should_ignore_line_break] returns[SequenceExpression out_args]
{boolean seen_star_or_band = false; args = out_args = (args==null)?new SequenceExpression():args; }
		:	normalMethodInvocationArgument[args, should_ignore_line_break]
						(options{greedy=true;/*caused by command*/}:
						 {!seen_star_or_band}?	
							COMMA 
							(options{greedy=true;}: LINE_BREAK!)?	
							((REST_ARG_PREFIX|BLOCK_ARG_PREFIX)=> seen_star_or_band=empty_true
							| normalMethodInvocationArgument[args, should_ignore_line_break])
						)*
						({seen_star_or_band}?	restMethodInvocationArgument[args]
						|{seen_star_or_band}?	blockMethodInvocationArgument[args]
						)?
			|	restMethodInvocationArgument[args]
			|	blockMethodInvocationArgument[args]
		;

normalMethodInvocationArgument[SequenceExpression args, boolean should_ignore_line_break] 
{ Expression expr, expr2=null; }
		:	expr=expression	
			(options{greedy=true;/*caused by command*/}:ASSOC	expr2=expression )?
			  { args.addAssoc(expr, expr2); } 
			({should_ignore_line_break}?	LINE_BREAK!)?
		;

restMethodInvocationArgument[SequenceExpression args]
{ Expression expr; }
		:	REST_ARG_PREFIX	expr=expression { args.setRestArg(expr); }	
			(options{greedy=true;/*caused by command*/}:COMMA	blockMethodInvocationArgument[args])?
		;

blockMethodInvocationArgument[SequenceExpression args]
{ Expression expr; }
		:	BLOCK_ARG_PREFIX expr=expression { args.setBlockArg(expr); }
		;

protected
predefinedValue returns [Expression expr=null]
		:	"nil"			{ expr=NilExpression.instance; }
		|	"self"			{ expr= SelfExpression.instance;}
		|	"true"			{ expr=TrueExpression.instance; }
		|	"false"			{ expr=FalseExpression.instance; }
		|	file:"__FILE__" { expr=new FileExpression(getFilename()); }
		|	line:"__LINE__" { expr=new LineExpression(line.getLine()); } 
		;

variable returns [Expression expr=null]
{can_be_command_ = false;}
		:   expr=variableCanBeCommand
		|   expr=syntaxedVariable
		;
		
variableCanBeCommand returns[Expression expr=null]
{can_be_command_ = true;}
		:   expr=predefinedValue is_lhs_=empty_false
		|   expr=normalVariable  is_lhs_=empty_true
		; 
		
normalVariable returns [Expression expr=null]
{ int line = line(); }
		:   id1:FUNCTION			{ expr=new FunctionExpression(line, scope(), false, id1.getText()); }
		|	id4:IDENTIFIER			{ expr=new IdentifierExpression(line, scope(), id4.getText());}	//this is OK: p = 1; p 1
		|	id5:CONSTANT			{ expr=new ConstVarExpression(line, id5.getText());}
		;
		
syntaxedVariable returns[Expression expr=null]
{can_be_command_ = false; int line = line(); }
		:	id6:INSTANCE_VARIBLE	{expr=new InstanceVarExpression(line, id6.getText());}
		|	id7:GLOBAL_VARIBLE		{expr=new GlobalVarExpression(line, id7.getText());}
		|	id8:CLASS_VARIBLE		{expr=new ClassVarExpression(line, id8.getText());}
		; 

string returns [Expression expr=null]
{ StringConcatExpression exc = null; Expression exp2; }
		:	double_quote_string_value:DOUBLE_QUOTE_STRING	
			{expr = new ConstantStringExpression(double_quote_string_value.getText(), ConstantStringExpression.DOUBLE_QUOTE);}
		|	single_quote_string_value:SINGLE_QUOTE_STRING	
			{expr = new ConstantStringExpression(single_quote_string_value.getText(), ConstantStringExpression.SINGLE_QUOTE);}
		|	s1:STRING_BEFORE_EXPRESSION_SUBSTITUTION exp2=expression_substituation
			{ expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.DOUBLE_QUOTE); exc.add(s1.getText()); exc.add(exp2); }
				(s3:STRING_BETWEEN_EXPRESSION_SUBSTITUTION	exp2=expression_substituation
					{ exc.add(s3.getText()); exc.add(exp2); } 
				)*
			s2:STRING_AFTER_EXPRESSION_SUBSTITUTION
			{ exc.add(s2.getText()); }
		;

protected
expression_substituation returns [Expression expr=null]
		:	(	LCURLY_BLOCK	expr=compoundStatement	RCURLY
			|   expr=syntaxedVariable )
			{tell_lexer_we_have_finished_parsing_string_expression_substituation();}
		;

w_array returns [Expression expr=null]
{ StringConcatExpression exc = null; Expression exp2; }
		:
		(	double_quote_string_value:DOUBLE_QUOTE_WARRAY	
			{expr = new ConstantStringExpression(double_quote_string_value.getText(), ConstantStringExpression.DOUBLE_QUOTE);}
		|	single_quote_string_value:SINGLE_QUOTE_WARRAY	
			{expr = new ConstantStringExpression(single_quote_string_value.getText(), ConstantStringExpression.SINGLE_QUOTE);}
		|	s1:WARRAY_BEFORE_EXPRESSION_SUBSTITUTION exp2=expression_substituation
			{ expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.DOUBLE_QUOTE); exc.add(s1.getText()); exc.add(exp2); }
				(s3:STRING_BETWEEN_EXPRESSION_SUBSTITUTION	exp2=expression_substituation
					{ exc.add(s3.getText()); exc.add(exp2); } 
				)*
			s2:STRING_AFTER_EXPRESSION_SUBSTITUTION
			{ exc.add(s2.getText()); }
		)
		{ expr = new WArrayExpression(expr); }
		;

regex returns [Expression expr=null]
{ StringConcatExpression exc = null; Expression exp2; }
		:
		(	reg:REGEX 
			{ expr = new ConstantStringExpression(reg.getText(), ConstantStringExpression.REGEX); }
		|	s1:REGEX_BEFORE_EXPRESSION_SUBSTITUTION
			exp2=expression_substituation	
			{ expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.REGEX); exc.add(s1.getText()); exc.add(exp2); }
			{tell_lexer_we_have_finished_parsing_regex_expression_substituation();}
			(s3:STRING_BETWEEN_EXPRESSION_SUBSTITUTION	exp2=expression_substituation	
			 { exc.add(s3.getText()); exc.add(exp2); } 
			 {tell_lexer_we_have_finished_parsing_regex_expression_substituation();}
			)*
			s2:STRING_AFTER_EXPRESSION_SUBSTITUTION
			{ exc.add(s2.getText()); }
		)
		{ expr = new RegularExpression(expr, get_regexp_options()); }
		;

command_output returns[Expression expr=null]
{ StringConcatExpression exc = null; Expression exp2; }
		:
		(	cmd:COMMAND_OUTPUT { expr = new ConstantStringExpression(cmd.getText(), ConstantStringExpression.COMMAND_OUTPUT); }
		|	s1:COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION
			exp2=expression_substituation	
			{ expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.COMMAND_OUTPUT); exc.add(s1.getText()); exc.add(exp2); }
			(s3:STRING_BETWEEN_EXPRESSION_SUBSTITUTION	exp2=expression_substituation
					{ exc.add(s3.getText()); exc.add(exp2); } 
			)*
			s2:STRING_AFTER_EXPRESSION_SUBSTITUTION
			{ exc.add(s2.getText()); }
		)
		{ expr = new CommandOutputExpression(expr); }
		;

literal returns[Expression expr=null]
{ Expression expr2; StringConcatExpression exc = null; }
		:	expr=regex
		|	expr=string (options{greedy=true;/*caused by command*/}:
				expr2=string { 
					if (exc == null) { exc = new StringConcatExpression(expr.line(), 0, expr); expr = exc; }
					exc.add(expr2);
				} 
			)*
		|	HERE_DOC_BEGIN { expr=nextHereDoc(); }
		|	expr=command_output
		|	expr=symbol[true]
		|   expr=w_array
		;

numeric returns[Expression expr=null]
		:	integer_value:INTEGER	{expr = new IntegerExpression(integer_value.getText(), 10);}
		|	hex_value:HEX			{expr = new IntegerExpression(hex_value.getText(), 16);}
		|	binary_value:BINARY		{expr = new IntegerExpression(binary_value.getText(), 2);}
		|	octal_value:OCTAL		{expr = new IntegerExpression(octal_value.getText(), 8);}
		|	float_value:FLOAT		{expr = new FloatExpression(float_value.getText());}
		|	ascii_value:ASCII_VALUE	{ expr=new IntegerExpression(String.valueOf((int)ascii_value.getText().charAt(1)), 10); } 
		;


primaryExpressionCannotBeCommand returns [Expression expr]
		:
		(	expr=hashExpression	{can_be_command_ = false;}
		|	LPAREN! expr=compoundStatement RPAREN!	{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=ifExpression				{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=unlessExpression			{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=whileExpression			{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=untilExpression			{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=caseExpression				{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=forExpression				{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=exceptionHandlingExpression	{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=moduleDefinition			{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=classDefinition			{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=methodDefinition			{can_be_command_ = false;seen_rparen_ = false;}
		|	expr=numeric			{can_be_command_ = false;}
		|	expr=literal			{can_be_command_ = false;}
		|   expr=syntaxedVariable   is_lhs_=empty_true
		|	expr=arrayExpression	{can_be_command_ = false;}
		)
		;

primaryExpressionCanBeCommand returns [Expression expr=null]
{ int line = line(); }
		:	LEADING_COLON2	fun:FUNCTION { expr = new FunctionExpression(line, scope(), true, fun.getText()); }
		|	expr=variableCanBeCommand
		|	"super"						{ expr = SuperExpression.instance; }
		|	"yield"						{ expr = YieldExpression.instance; }
		|	keyword_defined				{ expr = DefinedExpression.instance; }
		;


arrayReferenceArgument returns [SequenceExpression args = new SequenceExpression()]
{ Expression expr; boolean done=false; }
		:	keyValuePair[args]
			({!done}?
				COMMA 
				( (ASSIGN|RBRACK)=> done=empty_true
				| REST_ARG_PREFIX expr=expression (LINE_BREAK!)? done=empty_true { args.setRestArg(expr); } 
				| keyValuePair[args] )		
			)* 
		|	REST_ARG_PREFIX	expr=expression { args.setRestArg(expr); }	(LINE_BREAK!)?
		;

arrayAccess returns [SequenceExpression args = null]
		:	LBRACK_ARRAY_ACCESS
				(args=arrayReferenceArgument)?
			RBRACK!
		;

arrayExpression returns [ArrayExpression expr = null]
		{ SequenceExpression args = null; }
		:	LBRACK!
				(args=arrayReferenceArgument)?
			RBRACK! 
		{ expr = new ArrayExpression(args); }
		;

keyValuePair[AssocHolder assoc]
{ Expression key = null, value = null; }
		:	key=expression	(ASSOC	value=expression)?	
			{ assoc.addAssoc(key,value); }
			(LINE_BREAK!)?
		;

hashExpression returns [HashExpression expr = new HashExpression()]
		:	LCURLY_HASH
				(
					keyValuePair[expr]
					(options{greedy=true;}:{RCURLY != LA(2)}?	COMMA!	keyValuePair[expr])*
					(COMMA)?
				)?
			RCURLY
		;

protected
bodyStatement returns[Expression expr=SelfExpression.instance]
{ Expression body = null, elseStmt=null, ensureStmt=null;  
  RescueClause resc = null;
  java.util.List<RescueClause> rescues = new java.util.ArrayList<RescueClause>();
  SequenceExpression args = null;
  int line = 0;
  boolean hasExceptions = false;
  String name=null;
}
		:	(expr=compoundStatement)?
			("rescue" rescueBody[rescues] { hasExceptions=true;} )*
			("else" (elseStmt=compoundStatement)? { hasExceptions=true; } )?
			("ensure" (ensureStmt=compoundStatement)? { hasExceptions=true; } )?
			
			{ 
				if (hasExceptions) {
					expr = new RescueExpression(expr, rescues, elseStmt, ensureStmt); 
				}
			}
		;

rescueBody [java.util.List<RescueClause> rescues]
{ IdentifierExpression id=null; Expression exp2 = null; int line = line(); Expression body=NilExpression.instance; SequenceExpression args=new SequenceExpression(); String name = null; } 
	:
				    
			    ( exp2=expression { args.addExpression(exp2); } (COMMA args=mrhs[args])? )?
			    (ASSOC name=idOrFun { scope().assignToLocal(name, false); id = new IdentifierExpression(line, scope(), name); } )?
			    thenOrTermialOrColon (body=compoundStatement)?
				{ rescues.add(new RescueClause(line, args, id, body)); }
	;

exceptionHandlingExpression returns[Expression expr=null]
		:	"begin"
			expr=bodyStatement
			"end"
			{ expr = new BeginEndExpression(expr); } // has special meaning for loops
		;

ifExpression returns[Expression expr=null]
{ Expression test = null, thenExpr=null, elseExpr=null, elsifExpr = null; int line=line(); 
  java.util.Stack<IfThenElseExpression> elses = new java.util.Stack<IfThenElseExpression>(); 
}
		:	"if" test=expression thenOrTermialOrColon (thenExpr=compoundStatement)?
		
			("elsif" (expr=expression)? thenOrTermialOrColon (elsifExpr=compoundStatement)?
				{ elses.push(new IfThenElseExpression(line, expr, elsifExpr, NilExpression.instance)); }
			)*
			
			("else" (elseExpr=compoundStatement)?)?
			
			{ 
				while (!elses.isEmpty()) {
					IfThenElseExpression ite = elses.pop();
					ite.setElseExpression(elseExpr);
					elseExpr = ite;
				}
				
				expr = new IfThenElseExpression(line, test, thenExpr, elseExpr); 
			}
			"end"
		;

unlessExpression returns[Expression expr=null]
{ Expression test = null; Expression unlessExpr = null; Expression elseExpr = null; int line=line(); }
		:	"unless" test=expression thenOrTermialOrColon (unlessExpr=compoundStatement)?
			("else" (elseExpr=compoundStatement)?)?
			"end"
			{ expr = new IfThenElseExpression(line, test, elseExpr, unlessExpr); } 
		;

caseExpression returns[Expression expr=null]
{   int line = line(); 
	Expression test = null; 
	java.util.List<SequenceExpression> conds = new java.util.ArrayList<SequenceExpression>();
	java.util.List<Expression> codes = new java.util.ArrayList<Expression>();
	SequenceExpression expr1 = null;
	Expression expr2 = null;
	Expression elseExpr = null;
} 
		:	"case" (test=compoundStatement)?
			(keyword_when 
				expr1=mrhs[null]	thenOrTermialOrColon 
				(expr2=compoundStatement
				{ conds.add(expr1); codes.add(expr2); }	
				|
				{ conds.add(expr1); codes.add(NilExpression.instance); }	
				)
			)+
			("else" (elseExpr=compoundStatement)?)?
			
			{ expr = new CaseExpression(line, test, conds, codes, elseExpr); }
			
			"end"
		;

forExpression returns[Expression expr=null]
{ String op; SequenceExpression vars = null; Expression values = null; Expression body=null; BlockCode block = null; RubyCode context = scope(); int line=line(); }
		:	keyword_for 
			{ scope(block = new BlockCode(line(), context, getFilename())); }
			block_vars[block]	keyword_in 
			{ scope(context); /*pop context*/}
			values=expression 
			doOrTermialOrColon
			( { scope(block); } 
				body=compoundStatement
			  { scope(context); block.setBody(body); }	
			)?
			"end"
			{
				expr = new MethodCallExpression(scope(), line, values, "each", null, block, false);
			}
		;

whileExpression returns[Expression expr=null]
{ Expression test=null, body=null; int line=line(); }
		:	keyword_while test=expression doOrTermialOrColon
			(body=compoundStatement)?
			"end"
			{ expr = new WhileExpression(line,test,body, false); }
		;

untilExpression returns[Expression expr=null]
{ Expression test=null, body=null; int line=line(); }
		:	keyword_until test=expression doOrTermialOrColon
			(body=compoundStatement)?
			"end"
			{ expr = new WhileExpression(line, new UnaryExpression("!", test),body, false); }
		;

moduleDefinition returns[Expression expr=null]
{ ModuleCode code = null; RubyCode context = scope(); Expression body=null; String name; int line=line(); }
		:	keyword_module
			name=moduleName (options {greedy=true;}:terminal)?	
			{ scope(code=new ModuleCode(line, null, getFilename())); }
			body=bodyStatement { code.setBody(body); }
			"end"			
			{ 
				scope(context);
				expr=new ModuleExpression(name, code); 
			}
		;

moduleName returns [String name = null]
{ StringBuilder sb = new StringBuilder(); }
		:	c:CONSTANT { sb.append(c.getText()); }
			(COLON2 f:FUNCTION { sb.append("::"); sb.append(f.getText()); } )*
			{ name = sb.toString(); }
		;

classDefinition returns[Expression expr=null]
{ ClassCode code = null; RubyCode context = scope(); Expression body=null; String name; Expression superExpression=null; int line=line(); }
		:	keyword_class
			(	name=className	(LESS_THAN superExpression=expression)?
				terminal			
				{  scope(code = new ClassCode(line, null, getFilename())); }
				body=bodyStatement
				"end"			
				{ 
					code.setBody(body);
					scope(context); 
				  	expr = new ClassExpression(name, superExpression, code); 
				}
				
			|	LEFT_SHIFT	superExpression=expression
				terminal			
				{  scope(code = new ClassCode(line, context, getFilename())); }
				body=bodyStatement
				"end"			
				{ 
					code.setBody(body);
					scope(context); 
				  	expr = new SingletonClassExpression(superExpression, code); 
				}
			)
		;

/*
FIXME
cname	: tIDENTIFIER 
		| tCONSTANT
		;

cpath	: tCOLON3 cname 
		| cname 
		| primary_value tCOLON2 cname 
		;
*/
className returns [String name=null]
{ StringBuilder sb = new StringBuilder(); }
		:
		(	(c:CONSTANT {sb.append(c.getText());} |f1:FUNCTION {sb.append(f1.getText());})	
		|	(LEADING_COLON2	f2:FUNCTION {sb.append("::").append(f2.getText());})
		)
		(COLON2	f3:FUNCTION {sb.append("::").append(f3.getText());})*
		{ name = sb.toString(); }
		;

methodDefinition returns[Expression defexpr=null]
{ Expression expr = null; String name = null; boolean colon2=false; MethodCode code = null; RubyCode context = scope(); int line=line(); }
		:	keyword_def
			// { System.out.println("after def: "+LT(1)); }
			( name=operatorAsMethodname 
			| name=keywordAsMethodName	(ASSIGN_WITH_NO_LEADING_SPACE {name=name+"="; })?
			| tok:UNARY_PLUS_MINUS_METHOD_NAME { name = tok.getText(); }
			| expr=syntaxedVariable colon2=colon2orDot name=methodNameSupplement
			| expr=variableCanBeCommand 
				( ASSIGN_WITH_NO_LEADING_SPACE { MethodDenominator den = (MethodDenominator)expr;  name = den.getMethodName()+"="; expr = null; }  
				| colon2=colon2orDot name=methodNameSupplement 
				| { MethodDenominator den = (MethodDenominator)expr;  name = den.getMethodName(); expr = null; })
			) 
			{ 
				scope(code = new MethodCode(line, null, expr, name, getFilename()));
			}
			methodDefinitionArgument[code]
			expr=bodyStatement { code.setBody(expr); }
			"end"		
			{  scope(context); defexpr = new DefExpression(line, name, code);  }
		;

protected
colon2orDot returns [boolean isColon2 = false]
	: COLON2 isColon2=empty_true
	| DOT
	;

protected
methodNameSupplement returns [String name=null]
		:	
			(   (id:IDENTIFIER { name = (id.getText()); } 
			 	|fu:FUNCTION   { name = (fu.getText()); } 
			 	|co:CONSTANT   { name = (co.getText()); }) 
			 	(ASSIGN_WITH_NO_LEADING_SPACE { name += '='; } )?
			 	
			|	name=operatorAsMethodname 
			|	name=keyword 
			)
		;

operatorAsMethodname returns [String name]
{ name = LT(1).getText(); }
		:	LEFT_SHIFT
		|	RIGHT_SHIFT
		|	EQUAL
		|	CASE_EQUAL
		|	GREATER_THAN
		|	GREATER_OR_EQUAL
		|	LESS_THAN
		|	LESS_OR_EQUAL
		|	PLUS
		|	MINUS
		|	STAR
		|	DIV
		|	MOD
		|	POWER
		|	BAND
		|	BOR
		|	BXOR
		|	(LBRACK|LBRACK_ARRAY_ACCESS)	RBRACK	{name="[]";} 
				(options{greedy=true;}:ASSIGN_WITH_NO_LEADING_SPACE {name="[]=";})?
		|	MATCH
		|	COMPARE
		|	BNOT
		|	SINGLE_QUOTE
		|   LOGICAL_OR
		|   LOGICAL_AND
		|   NOT
		;

keyword returns [String name]
	{ name = LT(1).getText(); }
		:	name=keywordAsMethodName
		|	"nil"
		|	"self"
		|	"true"
		|	"false"
		|	"__FILE__"
		|	"__LINE__"
		;

keywordAsMethodName returns [String name]
	{ name = LT(1).getText(); }
		:	"and"
		|	"begin"
		|	"BEGIN"
		|	"def"
		|	"defined?"
		|	"end"
		|	"END"
		|	"ensure"
		|	"if"
		|	"in"
		|	"or"
		|	"module"
		|	"redo"
		|	"super"
		|	"unless"
		|	"until"
		|	"break"
		|	"do"
		|	"next"
		|	"rescue"
		|	"then"
		|	"when"
		|	"case"
		|	"else"
		|	"for"
		|	"retry"
		|	"while"
		|	"alias"
		|	"class"
		|	"elsif"
		|	"not"
		|	"return"
		|	"undef"
		|	"yield"
		;

methodDefinitionArgument[MethodCode def]
		:	LPAREN
				(methodDefinitionArgumentWithoutParen[def])?
			RPAREN	{tell_lexer_we_have_finished_parsing_methodparameters();}
			(options {greedy=true;}:terminal)?
		|	(methodDefinitionArgumentWithoutParen[def])?	terminal
		;

idOrFun returns[String name=null]
	: id:IDENTIFIER { return id.getText(); }
	| fu:FUNCTION { return fu.getText(); }
	;

methodDefinitionArgumentWithoutParen[MethodCode def]
{boolean seen_star_or_band = false;}
		:	    normalMethodDefinitionArgument[def]
					({!seen_star_or_band}? 
					    COMMA	
					    ((REST_ARG_PREFIX|BLOCK_ARG_PREFIX)=> seen_star_or_band=empty_true
						|normalMethodDefinitionArgument[def])
					)*
					(	 {seen_star_or_band}?	restMethodDefinitionArgument[def]
						|{seen_star_or_band}?	blockMethodDefinitionArgument[def]
					)?
			|	restMethodDefinitionArgument[def]
			|	blockMethodDefinitionArgument[def]
		;

normalMethodDefinitionArgument[MethodCode def]
{ Expression expr = null; String name=null; }
		:	name=idOrFun { /*todo*/ }
			((ASSIGN|ASSIGN_WITH_NO_LEADING_SPACE)	expr=expression)?
			{ def.addParameter(name, expr); } 
		;

restMethodDefinitionArgument[MethodCode def]
{ String name=null; }
		:	REST_ARG_PREFIX	
			( name=idOrFun
				{ def.addRestParameter(name); }
				(COMMA	blockMethodDefinitionArgument[def])?
			|
				{ def.addRestParameter(null); /*anonymous rest-arg*/ }
			)
		;

blockMethodDefinitionArgument[MethodCode def]
{ String name = null; }
		:	BLOCK_ARG_PREFIX	
			name=idOrFun { def.addBlockParameter(name); }
		;


thenOrTermialOrColon
		:	"then"
		|	terminal	("then")?
		|	COLON
		;

doOrTermialOrColon
		:	DO_IN_CONDITION
		|	terminal
		|	COLON
		;

operator_QUESTION		returns [String op=LT(1).getText()] 	:	QUESTION			(options{greedy=true;}:LINE_BREAK!)?;
operator_COLON			returns [String op=LT(1).getText()] 	:	(COLON|COLON_WITH_NO_FOLLOWING_SPACE)			(options{greedy=true;}:LINE_BREAK!)?;
operator_ASSIGN			returns [String op=LT(1).getText()] 	:	(ASSIGN|ASSIGN_WITH_NO_LEADING_SPACE)				(options{greedy=true;}:LINE_BREAK!)?;
operator_PLUS_ASSIGN 	returns [String op=LT(1).getText()] :	PLUS_ASSIGN			(options{greedy=true;}:LINE_BREAK!)?;
operator_MINUS_ASSIGN	returns [String op=LT(1).getText()] 	:	MINUS_ASSIGN		(options{greedy=true;}:LINE_BREAK!)?;
operator_STAR_ASSIGN	returns [String op=LT(1).getText()] 	:	STAR_ASSIGN		(options{greedy=true;}:LINE_BREAK!)?;
operator_DIV_ASSIGN		returns [String op=LT(1).getText()] 	:	DIV_ASSIGN			(options{greedy=true;}:LINE_BREAK!)?;
operator_MOD_ASSIGN		returns [String op=LT(1).getText()] 	:	MOD_ASSIGN			(options{greedy=true;}:LINE_BREAK!)?;
operator_POWER_ASSIGN	returns [String op=LT(1).getText()] 	:	POWER_ASSIGN		(options{greedy=true;}:LINE_BREAK!)?;
operator_BAND_ASSIGN	returns [String op=LT(1).getText()] 	:	BAND_ASSIGN		(options{greedy=true;}:LINE_BREAK!)?;
operator_BXOR_ASSIGN	returns [String op=LT(1).getText()] 	:	BXOR_ASSIGN		(options{greedy=true;}:LINE_BREAK!)?;
operator_BOR_ASSIGN		returns [String op=LT(1).getText()] 	:	BOR_ASSIGN			(options{greedy=true;}:LINE_BREAK!)?;
operator_LEFT_SHIFT_ASSIGN	returns [String op=LT(1).getText()] :	LEFT_SHIFT_ASSIGN	(options{greedy=true;}:LINE_BREAK!)?;
operator_RIGHT_SHIFT_ASSIGN	returns [String op=LT(1).getText()] :	RIGHT_SHIFT_ASSIGN	(options{greedy=true;}:LINE_BREAK!)?;
operator_LOGICAL_AND_ASSIGN	returns [String op=LT(1).getText()] :	LOGICAL_AND_ASSIGN	(options{greedy=true;}:LINE_BREAK!)?;
operator_LOGICAL_OR_ASSIGN	returns [String op=LT(1).getText()] :	LOGICAL_OR_ASSIGN	(options{greedy=true;}:LINE_BREAK!)?;
operator_INCLUSIVE_RANGE	returns [String op=LT(1).getText()] 	:	INCLUSIVE_RANGE		(options{greedy=true;}:LINE_BREAK!)?;
operator_EXCLUSIVE_RANGE	returns [String op=LT(1).getText()] :	EXCLUSIVE_RANGE	(options{greedy=true;}:LINE_BREAK!)?;
operator_LOGICAL_OR			returns [String op=LT(1).getText()] :	LOGICAL_OR			(options{greedy=true;}:LINE_BREAK!)?;
operator_LOGICAL_AND		returns [String op=LT(1).getText()] :	LOGICAL_AND		(options{greedy=true;}:LINE_BREAK!)?;
operator_COMPARE			returns [String op=LT(1).getText()] :	COMPARE			(options{greedy=true;}:LINE_BREAK!)?;
operator_EQUAL				returns [String op=LT(1).getText()] :	EQUAL				(options{greedy=true;}:LINE_BREAK!)?;
operator_CASE_EQUAL			returns [String op=LT(1).getText()] :	CASE_EQUAL			(options{greedy=true;}:LINE_BREAK!)?;
operator_NOT_EQUAL			returns [String op=LT(1).getText()] :	NOT_EQUAL			(options{greedy=true;}:LINE_BREAK!)?;
operator_MATCH				returns [String op=LT(1).getText()] :	MATCH				(options{greedy=true;}:LINE_BREAK!)?;
operator_NOT_MATCH			returns [String op=LT(1).getText()] :	NOT_MATCH			(options{greedy=true;}:LINE_BREAK!)?;
operator_LESS_THAN			returns [String op=LT(1).getText()] :	LESS_THAN			(options{greedy=true;}:LINE_BREAK!)?;
operator_GREATER_THAN		returns [String op=LT(1).getText()] :	GREATER_THAN		(options{greedy=true;}:LINE_BREAK!)?;
operator_LESS_OR_EQUAL		returns [String op=LT(1).getText()] :	LESS_OR_EQUAL		(options{greedy=true;}:LINE_BREAK!)?;
operator_GREATER_OR_EQUAL	returns [String op=LT(1).getText()] :	GREATER_OR_EQUAL	(options{greedy=true;}:LINE_BREAK!)?;
operator_BXOR				returns [String op=LT(1).getText()] :	BXOR				(options{greedy=true;}:LINE_BREAK!)?;
operator_BOR				returns [String op=LT(1).getText()] 	:	BOR					(options{greedy=true;}:LINE_BREAK!)?;
operator_BAND				returns [String op=LT(1).getText()] :	BAND				(options{greedy=true;}:LINE_BREAK!)?;
operator_LEFT_SHIFT			returns [String op=LT(1).getText()] :	LEFT_SHIFT			(options{greedy=true;}:LINE_BREAK!)?;
operator_RIGHT_SHIFT		returns [String op=LT(1).getText()] 	:	RIGHT_SHIFT			(options{greedy=true;}:LINE_BREAK!)?;
operator_PLUS				returns [String op=LT(1).getText()] :	PLUS				(options{greedy=true;}:LINE_BREAK!)?;
operator_MINUS				returns [String op=LT(1).getText()] :	MINUS				(options{greedy=true;}:LINE_BREAK!)?;
operator_STAR				returns [String op=LT(1).getText()] :	STAR				(options{greedy=true;}:LINE_BREAK!)?;
operator_DIV				returns [String op=LT(1).getText()] 	:	DIV					(options{greedy=true;}:LINE_BREAK!)?;
operator_MOD				returns [String op=LT(1).getText()] 	:	MOD					(options{greedy=true;}:LINE_BREAK!)?;
operator_POWER				returns [String op=LT(1).getText()] :	POWER				(options{greedy=true;}:LINE_BREAK!)?;
operator_UNARY_PLUS			returns [String op="+@"] :	UNARY_PLUS			(options{greedy=true;}:LINE_BREAK!)?;
operator_UNARY_MINUS		returns [String op="-@"] :	UNARY_MINUS		(options{greedy=true;}:LINE_BREAK!)?;
operator_BNOT				returns [String op=LT(1).getText()] :	BNOT				(options{greedy=true;}:LINE_BREAK!)?;
operator_NOT				returns [String op=LT(1).getText()] 	:	NOT					(options{greedy=true;}:LINE_BREAK!)?;

//LINE_BREAK is preserved after keyword, we have to do that because keyword can be used as function name.
//for example:
//  def class
//        Registry
//      end
//So the following rules are created so that we do not hve to put (LINE_BREAK)? everywhere.
//
keyword_BEGIN	:	"BEGIN"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_END		:	"END"		(options{greedy=true;}:LINE_BREAK!)?;
keyword___FILE__	:	"__FILE__"	(options{greedy=true;}:LINE_BREAK!)?;
keyword___LINE__	:	"__LINE__"	(options{greedy=true;}:LINE_BREAK!)?;
keyword_alias		:	"alias"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_and		:	"and"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_begin	:	"begin"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_break	:	"break"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_case		:	"case"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_class	:	"class"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_def		:	"def"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_defined	:	"defined?"	(options{greedy=true;}:LINE_BREAK!)?;
keyword_do		:	"do"			(options{greedy=true;}:LINE_BREAK!)?;
keyword_else		:	"else"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_elsif		:	"elsif"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_end		:	"end"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_ensure	:	"ensure"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_false		:	"false"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_for		:	"for"			(options{greedy=true;}:LINE_BREAK!)?;
keyword_if		:	"if"			(options{greedy=true;}:LINE_BREAK!)?;
keyword_in		:	"in"			(options{greedy=true;}:LINE_BREAK!)?;
keyword_module	:	"module"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_next		:	"next"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_nil		:	"nil"			(options{greedy=true;}:LINE_BREAK!)?;
keyword_not		:	"not"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_or		:	"or"			(options{greedy=true;}:LINE_BREAK!)?;
keyword_redo		:	"redo"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_rescue	:	"rescue"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_retry	:	"retry"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_return	:	"return"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_self		:	"self"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_super	:	"super"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_then		:	"then"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_true		:	"true"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_undef	:	"undef"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_unless	:	"unless"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_until		:	"until"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_when	:	"when"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_while	:	"while"		(options{greedy=true;}:LINE_BREAK!)?;
keyword_yield		:	"yield"		(options{greedy=true;}:LINE_BREAK!)?;


//----------------------------------------------------------------------------
// The Ruby scanner. Do not use this one, use RubyLexer instead!
//----------------------------------------------------------------------------
class RubyLexerBase extends Lexer;

options {
	exportVocab=Ruby;	// call the vocabulary "Ruby"
	testLiterals=false;		// don't automatically test for literals
	k=3;				// "<=>" etc need this
	charVocabulary='\u0000'..'\uFFFE';
	//codeGenMakeSwitchThreshold = 2;
	codeGenBitsetTestThreshold=20;
}

{
	public int regex_flags = 0;
	
	//The following methods are to be implemented in the subclass.
	//In fact they should be "abstract", but antlr refuses to generate
	//abstract class. We can either insert "abstract" keyword manually
	//after the lexer is generated, or simply use assert() to prevent 
	//these function to run (so you have to overide them). I choosed
	//the later approach.
	protected boolean expect_operator(int k) throws CharStreamException		{assert(false);return false;}
	protected boolean expect_unary()	 throws CharStreamException			{assert(false);return false;}
	protected boolean expect_hash()					{assert(false);return false;}
	protected boolean expect_heredoc()				{assert(false);return false;}
	protected boolean expect_leading_colon2()		{assert(false);return false;}
	protected boolean expect_heredoc_content()		{assert(false);return false;}
	protected boolean expect_array_access()				{assert(false);return false;}
	protected boolean last_token_is_dot_or_colon2()		{assert(false);return false;}
	protected boolean last_token_is_semi()				{assert(false);return false;}
	protected boolean last_token_is_keyword_def_or_colon()			{assert(false);return false;}
	protected boolean last_token_is_colon_with_no_following_space()			{assert(false);return false;}
	protected boolean should_ignore_linebreak()			{assert(false);return false;}
	protected int track_delimiter_count(char next_char, char delimeter, int delimeter_count)	{assert(false);return 0;}
	protected boolean is_delimiter(String next_line, String delimiter)	{assert(false);return false;}
	protected boolean is_ascii_value_terminator(char value)	{assert(false);return false;}
	protected boolean just_seen_whitespace()	{assert(false);return false;}
	protected void set_seen_whitespace()			{assert(false);}
	protected boolean expression_substitution_is_next()	throws CharStreamException	{assert(false);return false;}
	protected boolean space_is_next()	throws CharStreamException	{assert(false);return false;}
	protected void set_current_special_string_delimiter(char delimiter, int delimiter_count)	{assert(false);}
	protected void update_current_special_string_delimiter_count(int delimiter_count)	{assert(false);}
}

//QUESTION			:	'?'		;
LPAREN				:	'('		;
RPAREN				:	')'		;
LBRACK				:	'['		{if (expect_array_access()) {$setType(LBRACK_ARRAY_ACCESS);}};
RBRACK				:	']'		;
LCURLY_HASH			:	'{'		{if (!expect_hash()) {$setType(LCURLY_BLOCK);}};
RCURLY				:	'}'		;
COMMA				:	','!		;
COLON				:	':'		{if (!space_is_next())	{$setType(COLON_WITH_NO_FOLLOWING_SPACE);}};
COLON2				:	"::"		{if (expect_leading_colon2())	{$setType(LEADING_COLON2);}};

NOT					:	'!'		;
BNOT				:	'~'		;
//DIV				:	'/'		;
PLUS				:	'+'		{if (expect_unary())	{$setType(UNARY_PLUS);}};
MINUS				:	'-'		{if (expect_unary())	{$setType(UNARY_MINUS);}};
//MOD				:	'%'		;
STAR				:	'*'		{if (!expect_operator(1)) {$setType(REST_ARG_PREFIX);}};	//'f * g' can parsed as 'f(*g)' or '(f) * (g)'
LESS_THAN			:	'<'		;
GREATER_THAN		:	'>'		;
BXOR				:	'^'		;
BOR					:	'|'		;
BAND				:	'&'		{if (!expect_operator(1)) {$setType(BLOCK_ARG_PREFIX);}};
POWER				:	"**"		;
COMPARE			:	"<=>"	;
GREATER_OR_EQUAL	:	">="	;
LESS_OR_EQUAL		:	"<="	;
EQUAL				:	"=="	;
CASE_EQUAL			:	"==="	;
NOT_EQUAL			:	"!="		;
MATCH				:	"=~"	;
NOT_MATCH			:	"!~"		;
//LEFT_SHIFT			:	"<<"	;
RIGHT_SHIFT			:	">>"	;

ASSOC				:	"=>"	;
LOGICAL_AND		:	"&&"		;
LOGICAL_OR			:	"||"		;

ASSIGN				:	'='		{if (!just_seen_whitespace()) {$setType(ASSIGN_WITH_NO_LEADING_SPACE);}};
PLUS_ASSIGN			:	"+="	;
MINUS_ASSIGN		:	"-="		;
STAR_ASSIGN		:	"*="		;
//DIV_ASSIGN		:	"/="		;
//MOD_ASSIGN		:	"%="	;
POWER_ASSIGN		:	"**="	;
BAND_ASSIGN		:	"&="		;
BXOR_ASSIGN		:	"^="	;
BOR_ASSIGN			:	"|="		;
//LEFT_SHIFT_ASSIGN	:	"<<="	;
RIGHT_SHIFT_ASSIGN	:	">>="	;
LOGICAL_AND_ASSIGN	:	"&&="	;
LOGICAL_OR_ASSIGN	:	"||="	;


//ANTLR's linear approximate lookahead will cause trouble if you list UNARY_PLUS and UNARY_MINUS as two separated rules.
UNARY_PLUS_MINUS_METHOD_NAME
		:	{last_token_is_keyword_def_or_colon()}? ("+@"|"-@")
		;

SEMI
		:	';'!	(WHITE_SPACE_CAHR!		|	LINE_FEED!	|	';'!)*
			{
				if (last_token_is_semi())
				{
					$setType(Token.SKIP);
				}
			}
		;

//treat "\n\n\n\n;" as SEMI
LINE_BREAK
		:	{expect_heredoc_content()}?	LINE_FEED!
		|	PURE_LINE_BREAK!	(SEMI!	{$setType(SEMI);})?
			{
				if ((LINE_BREAK == _ttype) && should_ignore_linebreak())
				{
					$setType(Token.SKIP);
				}
			}
		;

//If we use "ignore=WHITE_SPACE_CAHR", can not shutdown the warnings.
protected
PURE_LINE_BREAK
		:	LINE_FEED	(LINE_FEED!|WHITE_SPACE_CAHR!)*
		;

//'\r' is no longer used as line break since Mac OSX, but still in use in  C:\ruby\samples\hello.rb
protected
LINE_FEED
		:	('\n'
			|	'\r'	(options{greedy=true;}:'\n')?
			)
			{newline();}
		;

protected
REGEX_MODIFIER
{ regex_flags = 0; }
		:	(	'i'!	{ regex_flags |= RegularExpression.REGEX_OPTION_I; }
			|	'o'!	{ regex_flags |= RegularExpression.REGEX_OPTION_O; }
			|	'm'!	{ regex_flags |= RegularExpression.REGEX_OPTION_M; }
			|	'x'!	{ regex_flags |= RegularExpression.REGEX_OPTION_X; }	
			|	'n'!	{ regex_flags |= RegularExpression.REGEX_OPTION_N; }
			|	'e'!	{ regex_flags |= RegularExpression.REGEX_OPTION_E; } 
			|	'u'!	{ regex_flags |= RegularExpression.REGEX_OPTION_U; }
			|	's'!	{ regex_flags |= RegularExpression.REGEX_OPTION_S; }
			)*
		;

COMMAND_OUTPUT
		:	{!last_token_is_keyword_def_or_colon()}?
			delimiter:'`'!
			({LA(1) != delimiter && !expression_substitution_is_next()}?	STRING_CHAR)*
			end:.!//skip delimiter
			{
				if (end != delimiter)
				{
					$setType(COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter, 1);
				}
			}
		|	'`'	{$setType(SINGLE_QUOTE);}
		;

SINGLE_QUOTE_STRING
		:	'\''!
				(ESC
				|LINE_FEED
				|~('\''|'\\'|'\r'|'\n')
				)*
			'\''!
		;

//DIVIDE and REGEX both starts with '/', here we use semantic predicate to disambiguate.
REGEX
		:	{!expect_operator(2)}?
			delimiter:'/'!
			({LA(1) != delimiter && !expression_substitution_is_next()}?	STRING_CHAR)*
			end:.!//skip delimiter
			{
				if (end != delimiter)
				{
					$setType(REGEX_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter, 1);
				}
				else
				{
					mREGEX_MODIFIER(false);
				}
			}
		|	"/="	{$setType(DIV_ASSIGN);}
		|	'/'	{$setType(DIV);}
		;

DOUBLE_QUOTE_STRING
		:	delimiter:'\"'!
			({LA(1) != delimiter && !expression_substitution_is_next()}?	STRING_CHAR)*
			end:.!//skip delimiter
			{
				if (end != delimiter)
				{
					$setType(STRING_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter, 1);
				}
			}
		;

protected
STRING_BETWEEN_EXPRESSION_SUBSTITUTION[char delimiter, int delimiter_count]
		:	({(delimiter_count > 0) && (delimiter_count = track_delimiter_count(LA(1), delimiter, delimiter_count)) != 0&& !expression_substitution_is_next()}?	STRING_CHAR)*
			{
				//match and skip delimiter, there maybe no delimiter, e.g. ':#{cmd_name}'
				if (LA(1) != EOF_CHAR)
				{
					_saveIndex=text.length();
					matchNot(EOF_CHAR);
					text.setLength(_saveIndex);
				}

				if (0 == delimiter_count)
				{
					$setType(STRING_AFTER_EXPRESSION_SUBSTITUTION);
				}
				else
				{
					update_current_special_string_delimiter_count(delimiter_count);
				}
			}
		;

SPECIAL_STRING
{
	int delimiter_count = 1;
}
		:	'%'!	'q'!	delimiter1:.!
			({(delimiter_count = track_delimiter_count(LA(1), delimiter1, delimiter_count)) != 0}?	STRING_CHAR)*
			.!//skip delimiter
			{$setType(SINGLE_QUOTE_STRING);}
		|	'%'!	'Q'!	delimiter2:.!
			({(delimiter_count = track_delimiter_count(LA(1), delimiter2, delimiter_count)) != 0 && !expression_substitution_is_next()}?	STRING_CHAR)*
			{
				//match and skip delimiter
				_saveIndex=text.length();
				matchNot(EOF_CHAR);
				text.setLength(_saveIndex);
			
				if (0 == delimiter_count)
				{
					$setType(DOUBLE_QUOTE_STRING);
				}
				else
				{
					$setType(STRING_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter2, delimiter_count);
				}
			}
		|	'%'!	'r'!	delimiter3:.!
			({(delimiter_count = track_delimiter_count(LA(1), delimiter3, delimiter_count)) != 0 && !expression_substitution_is_next()}?	STRING_CHAR)*
			{
				//match and skip delimiter
				_saveIndex=text.length();
				matchNot(EOF_CHAR);
				text.setLength(_saveIndex);

				if (0 == delimiter_count)
				{
					mREGEX_MODIFIER(false);
					$setType(REGEX);
				}
				else
				{
					$setType(REGEX_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter3, delimiter_count);
				}
			}
		|	'%'!	'x'!	delimiter4:.!
			({(delimiter_count = track_delimiter_count(LA(1), delimiter4, delimiter_count)) != 0 && !expression_substitution_is_next()}?	STRING_CHAR)*
			{
				//match and skip delimiter
				_saveIndex=text.length();
				matchNot(EOF_CHAR);
				text.setLength(_saveIndex);

				if (0 == delimiter_count)
				{
					$setType(COMMAND_OUTPUT);
				}
				else
				{
					$setType(COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter4, delimiter_count);
				}
			}
		|	'%'! 'w'!	delimiter5:.!
			({(delimiter_count = track_delimiter_count(LA(1), delimiter5, delimiter_count)) != 0}?	STRING_CHAR)*
			.!	//skip delimiter
			{$setType(SINGLE_QUOTE_WARRAY);}
		|	'%'! 'W'!	delimiter7:.!
			({(delimiter_count = track_delimiter_count(LA(1), delimiter7, delimiter_count)) != 0 && !expression_substitution_is_next()}?	STRING_CHAR)*
			{
				//match and skip delimiter
				_saveIndex=text.length();
				matchNot(EOF_CHAR);
				text.setLength(_saveIndex);
			
				if (0 == delimiter_count)
				{
					$setType(DOUBLE_QUOTE_WARRAY);
				}
				else
				{
					$setType(WARRAY_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter7, delimiter_count);
				}
			}
		|	{!expect_operator(2)}?	'%'!
			{_saveIndex=text.length();}					//Ignore delimiter2 (for unknown reason, antlr does not do it for us, even if we specified !)
			delimiter6:~('=' | 'a'..'z' | 'A'..'Z' | '0'..'9')		//"%=" is always MOD_ASSIGN. English character is not allowed to avoid collison with %q %Q etc.
			{text.setLength(_saveIndex);}
			({(delimiter_count = track_delimiter_count(LA(1), delimiter6, delimiter_count)) != 0 && !expression_substitution_is_next()}?	STRING_CHAR)*
			{
				//match and skip delimiter
				_saveIndex=text.length();
				matchNot(EOF_CHAR);
				text.setLength(_saveIndex);

				if (0 == delimiter_count)
				{
					$setType(DOUBLE_QUOTE_STRING);
				}
				else
				{
					$setType(STRING_BEFORE_EXPRESSION_SUBSTITUTION);
					set_current_special_string_delimiter(delimiter6, delimiter_count);
				}
			}
		|	"%="	{$setType(MOD_ASSIGN);}
		|	"%"		{$setType(MOD);}
		;

protected
STRING_CHAR
		:	~('\r'|'\n'|'\\')
		|	LINE_FEED
		|	ESC
		;


//The first '-' after "<<" is alway interpreted as heredoc's special meaning, so be greedy
HERE_DOC_BEGIN
		:	{expect_heredoc()}?	"<<"!	delimiter:HERE_DOC_DELIMITER
		|	"<<="	{$setType(LEFT_SHIFT_ASSIGN);}
		|	"<<"		{$setType(LEFT_SHIFT);}
		;

protected
HERE_DOC_CONTENT[String delimiter]
		:	(next_line:ANYTHING_OTHER_THAN_LINE_FEED	
			 LINE_FEED	{if (is_delimiter(next_line.getText(), delimiter)) break;})+
			{
				//skip delimiter
				text.setLength(text.length() - next_line.getText().length() - 1);
			}
		;

protected
HERE_DOC_DELIMITER
		:	(options{greedy=true;}:'-')?
				((options{greedy=true;}:	~(' ' | '\n' | '\r' | '\'' | '"' | ',' | ')' | '=' | '.' | ';'))+
				|	'\''!	(~('\'' | '\r' | '\n'))+ { /* TODO:SingleQuote*/ } 	'\''!
				|	'"'!	(~('"' | '\r' | '\n'))+	{ /* TODO:DoubleQuote */ } '"'!
				)
		;

RDOC
		:	{getColumn()==1}?	"=begin"
			(options {greedy=false;}:	LINE)*
			{getColumn()==1}?	"=end"
			{
				$setType(Token.SKIP);
			}
		;

protected
ANYTHING_OTHER_THAN_LINE_FEED
		:	(~('\r'|'\n'))*
		;

protected
LINE
		:	ANYTHING_OTHER_THAN_LINE_FEED	LINE_FEED
		;

//It's OK as long as the next char(whatever it is) is eaten
protected
ESC
		:	'\\' .
		;

// An identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
//Following this initial character, an identifier can be any combination
//of letters, digits, and underscores
IDENTIFIER
options{testLiterals=true;}
		:	('a'..'z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*		
			('?'	{$setType(FUNCTION);}	//PREDICATE_FUNCTION
			|'!'	{$setType(FUNCTION);}	//DESTRUCTIVE_FUNCTION
			| {if (last_token_is_dot_or_colon2()) {$setType(FUNCTION);}}
			)
		;


GLOBAL_VARIBLE
		:	'$'	('-')?	IDENTIFIER_CONSTANT_AND_KEYWORD
		|	'$'	(options{greedy=true;}:'0'..'9')+
		|	'$'	('!'|'@'|'&'|'`'|'\''|'+'|'~'|'='|'/'|'\\'|','|';'|'.'|'<'|'>'|'*'|'$'|'?'|':'|'\"')
		;

protected
IDENTIFIER_CONSTANT_AND_KEYWORD
		:	('a'..'z'|'A'..'Z'|'_')	(options{greedy=true;}:	'a'..'z'|'A'..'Z'|'_'|'0'..'9')*
												
		;

INSTANCE_VARIBLE
		:	'@'	IDENTIFIER_CONSTANT_AND_KEYWORD
		;

CLASS_VARIBLE
		:	'@'	INSTANCE_VARIBLE
		;

CONSTANT
options{testLiterals=true;}
		:	('A'..'Z')	('a'..'z'|'A'..'Z'|'_'|'0'..'9')*	
			('?'	{$setType(FUNCTION);}	//PREDICATE_FUNCTION
			|'!'	{$setType(FUNCTION);}	//DESTRUCTIVE_FUNCTION
			| {if (last_token_is_dot_or_colon2()) {$setType(FUNCTION);}}
			)
		;

INTEGER
		:	'0'!	OCTAL_CONTENT			{$setType(OCTAL);}
		|	'0'!	('b'!|'B'!)	BINARY_CONTENT	{$setType(BINARY);}
		|	'0'!	('x'!|'X'!)	HEX_CONTENT	{$setType(HEX);}
		|	'0'	(
					//Use semantic prediction to avoid 0.times
					{(LA(2)>='0')&&(LA(2)<='9')}? FLOAT_WITH_LEADING_DOT {$setType(FLOAT);} 
					|/*none*/
				)
		|	NON_ZERO_DECIMAL	(
									//Use semantic prediction to avoid things like "2..3", "2...3", "2.times"
									{(LA(2)>='0')&&(LA(2)<='9')}? FLOAT_WITH_LEADING_DOT {$setType(FLOAT);}
									|EXPONENT {$setType(FLOAT);}
									|/*none*/
								)
		|	FLOAT_WITH_LEADING_DOT	{$setType(FLOAT);}
		|	'.'	{$setType(DOT);}
		|	".."	{$setType(INCLUSIVE_RANGE);}
		|	"..."	{$setType(EXCLUSIVE_RANGE);}
		|	'?'	(
					{is_ascii_value_terminator(LA(2))}?	(~('\\'|' '|'\n'|'\r'))	{$setType(ASCII_VALUE);}
					|'\\'		~('C' | 'M')	{$setType(ASCII_VALUE);}
					|('\\'	('C'|'M') '-')+	('a'..'z' | '?')	{$setType(ASCII_VALUE);}
					|{$setType(QUESTION);}	//If it does not "look like"(not depend on context!) integer, then it is QUESTION operator.
				)
		;

protected
UNDER_SCORE
		:	'_'
		;

protected
FLOAT_WITH_LEADING_DOT
		:	'.'	('0'..'9')+	(EXPONENT)?
		;

protected
NON_ZERO_DECIMAL
options{ignore=UNDER_SCORE; }
		:	('1'..'9'	('0'..'9')*)
		;

protected
OCTAL_CONTENT
options{ignore=UNDER_SCORE;}
		:	('0'..'7')+
		;

protected
HEX_CONTENT
options{ignore=UNDER_SCORE;}
		:	('0'..'9'|'A'..'F'|'a'..'f')+
		;

protected
BINARY_CONTENT
options{ignore=UNDER_SCORE;}
		:	('0'|'1')+
		;

protected
EXPONENT
		:	('e'|'E')	('+'|'-')?	('0'..'9')+
		;

COMMENT
		:	{!last_token_is_colon_with_no_following_space()}?	'#'	ANYTHING_OTHER_THAN_LINE_FEED
			{
				$setType(Token.SKIP);
			}
		|	'#'!
			{
				$setType(STRING_BEFORE_EXPRESSION_SUBSTITUTION);
				set_current_special_string_delimiter('#'/*useless*/, 0);
			}
		;

protected
WHITE_SPACE_CAHR
		:	' '
		|	'\t'
		|	'\f'
		|	'\13'
		;

WHITE_SPACE
		:	(WHITE_SPACE_CAHR)+
			{
				set_seen_whitespace();
				$setType(Token.SKIP);
			}
		;

LINE_CONTINUATION
		:	'\\'	LINE_FEED	{$setType(Token.SKIP);}
		;

//Except the pysical end of file(antlr will detect it for us),
//the following special characters are treated as end of file as well.
END_OF_FILE
		:	('\0'		// NULL
      			|'\004'	// ^D
      			|'\032'	// ^Z
      			)
      			{$setType(Token.EOF_TYPE);}
		;

