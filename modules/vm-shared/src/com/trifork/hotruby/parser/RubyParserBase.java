// $ANTLR : "ruby.g" -> "RubyParserBase.java"$

package com.trifork.hotruby.parser;

import com.trifork.hotruby.ast.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class RubyParserBase extends antlr.LLkParser       implements RubyTokenTypes
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
	   	  expr = new IdentifierExpression(scope(), fe.getName());
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
	

protected RubyParserBase(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public RubyParserBase(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected RubyParserBase(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public RubyParserBase(TokenStream lexer) {
  this(lexer,1);
}

public RubyParserBase(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final TopLevelCode  program() throws RecognitionException, TokenStreamException {
		TopLevelCode code = null;
		
		Expression expr = null;
		
		if ( inputState.guessing==0 ) {
			scope(code = new TopLevelCode(getFilename()));
		}
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=compoundStatement();
			if ( inputState.guessing==0 ) {
				code.setBody(expr);
			}
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(Token.EOF_TYPE);
		if ( inputState.guessing==0 ) {
			scope(null);
		}
		return code;
	}
	
	public final Expression  compoundStatement() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		{
			terminal();
			{
			switch ( LA(1)) {
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				expr=statements();
				break;
			}
			case EOF:
			case RCURLY:
			case RPAREN:
			case LITERAL_end:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_elsif:
			case LITERAL_when:
			{
				if ( inputState.guessing==0 ) {
					expr=NilExpression.instance;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=statements();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final RubyCode  eval_body(
		com.trifork.hotruby.runtime.EvalContext ctx
	) throws RecognitionException, TokenStreamException {
		RubyCode code = null;
		
		Expression expr = null;
		
		if ( inputState.guessing==0 ) {
			scope(code=new EvalCode(0, ctx, getFilename()));
		}
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=compoundStatement();
			if ( inputState.guessing==0 ) {
				code.setBody(expr);
			}
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(Token.EOF_TYPE);
		if ( inputState.guessing==0 ) {
			scope(null);
		}
		return code;
	}
	
	public final void terminal() throws RecognitionException, TokenStreamException {
		
		
		switch ( LA(1)) {
		case SEMI:
		{
			match(SEMI);
			break;
		}
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final Expression  statements() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		SequenceExpression seq = null; Expression stmt;
		
		expr=statement();
		{
		_loop5921:
		do {
			if ((LA(1)==SEMI||LA(1)==LINE_BREAK)) {
				terminal();
				if ( inputState.guessing==0 ) {
					
										if (	(EOF == LA(1))	||	//script end
											(RCURLY == LA(1))	||	(LITERAL_end == LA(1))	||	//block end
											(RPAREN == LA(1))	||
											(LITERAL_else == LA(1)) || (LITERAL_elsif == LA(1))	||
											(LITERAL_rescue == LA(1)) || (LITERAL_ensure == LA(1))	||
											(LITERAL_when == LA(1))
										)
										break;
									
				}
				stmt=statement();
				if ( inputState.guessing==0 ) {
					
										if (seq==null) { seq = new SequenceExpression(); seq.addExpression(expr); expr=seq; }
										seq.addExpression(stmt);
									
				}
			}
			else {
				break _loop5921;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final Expression  statement() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; int line = line(); java.util.List<RescueClause> rescues = null;
		
		expr=statementWithoutModifier();
		{
		_loop5925:
		do {
			switch ( LA(1)) {
			case IF_MODIFIER:
			{
				match(IF_MODIFIER);
				expr2=expression();
				if ( inputState.guessing==0 ) {
					expr=new IfThenElseExpression(line, expr2, expr, NilExpression.instance);
				}
				break;
			}
			case UNLESS_MODIFIER:
			{
				match(UNLESS_MODIFIER);
				expr2=expression();
				if ( inputState.guessing==0 ) {
					expr=new IfThenElseExpression(line, expr2, NilExpression.instance, expr);
				}
				break;
			}
			case WHILE_MODIFIER:
			{
				match(WHILE_MODIFIER);
				expr2=expression();
				if ( inputState.guessing==0 ) {
					expr=new WhileExpression(line, expr2, expr, true);
				}
				break;
			}
			case UNTIL_MODIFIER:
			{
				match(UNTIL_MODIFIER);
				expr2=expression();
				if ( inputState.guessing==0 ) {
					expr=new WhileExpression(line, new UnaryExpression("not", expr2), expr, true);
				}
				break;
			}
			case RESCUE_MODIFIER:
			{
				match(RESCUE_MODIFIER);
				if ( inputState.guessing==0 ) {
					line = line();
				}
				expr2=expression();
				if ( inputState.guessing==0 ) {
					rescues = new java.util.ArrayList<RescueClause>(); 
									  rescues.add (new RescueClause(line, null, null, expr2));
									  expr = new RescueExpression(expr, rescues, null, null);
				}
				break;
			}
			default:
			{
				break _loop5925;
			}
			}
		} while (true);
		}
		return expr;
	}
	
	public final Expression  statementWithoutModifier() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		String op; SequenceExpression args=null; final int line = line();
		
		switch ( LA(1)) {
		case LITERAL_alias:
		{
			keyword_alias();
			args=alias_parameter(args);
			{
			switch ( LA(1)) {
			case LINE_BREAK:
			{
				match(LINE_BREAK);
				break;
			}
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case BOR:
			case LITERAL_in:
			case LITERAL_end:
			case LOGICAL_OR:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case LBRACK_ARRAY_ACCESS:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_do:
			case LITERAL_super:
			case LITERAL_yield:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_elsif:
			case LITERAL_unless:
			case LITERAL_case:
			case LESS_THAN:
			case LEFT_SHIFT:
			case RIGHT_SHIFT:
			case EQUAL:
			case CASE_EQUAL:
			case GREATER_THAN:
			case GREATER_OR_EQUAL:
			case LESS_OR_EQUAL:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case POWER:
			case BAND:
			case BXOR:
			case MATCH:
			case COMPARE:
			case BNOT:
			case SINGLE_QUOTE:
			case LOGICAL_AND:
			case NOT:
			case LITERAL_and:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_or:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_then:
			case LITERAL_when:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			args=alias_parameter(args);
			if ( inputState.guessing==0 ) {
				expr = new AliasExpression(args);
			}
			break;
		}
		case LITERAL_undef:
		{
			keyword_undef();
			args=undef_parameter(args);
			{
			_loop5930:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					{
					switch ( LA(1)) {
					case LINE_BREAK:
					{
						match(LINE_BREAK);
						break;
					}
					case IDENTIFIER:
					case CONSTANT:
					case FUNCTION:
					case BOR:
					case LITERAL_in:
					case LITERAL_end:
					case LOGICAL_OR:
					case COLON_WITH_NO_FOLLOWING_SPACE:
					case LBRACK_ARRAY_ACCESS:
					case LBRACK:
					case LITERAL_return:
					case LITERAL_break:
					case LITERAL_next:
					case LITERAL_retry:
					case LITERAL_redo:
					case LITERAL_do:
					case LITERAL_super:
					case LITERAL_yield:
					case LITERAL_rescue:
					case LITERAL_else:
					case LITERAL_ensure:
					case LITERAL_begin:
					case LITERAL_if:
					case LITERAL_elsif:
					case LITERAL_unless:
					case LITERAL_case:
					case LESS_THAN:
					case LEFT_SHIFT:
					case RIGHT_SHIFT:
					case EQUAL:
					case CASE_EQUAL:
					case GREATER_THAN:
					case GREATER_OR_EQUAL:
					case LESS_OR_EQUAL:
					case PLUS:
					case MINUS:
					case STAR:
					case DIV:
					case MOD:
					case POWER:
					case BAND:
					case BXOR:
					case MATCH:
					case COMPARE:
					case BNOT:
					case SINGLE_QUOTE:
					case LOGICAL_AND:
					case NOT:
					case LITERAL_and:
					case LITERAL_BEGIN:
					case LITERAL_def:
					case 108:
					case LITERAL_END:
					case LITERAL_or:
					case LITERAL_module:
					case LITERAL_until:
					case LITERAL_then:
					case LITERAL_when:
					case LITERAL_for:
					case LITERAL_while:
					case LITERAL_alias:
					case LITERAL_class:
					case LITERAL_not:
					case LITERAL_undef:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					args=undef_parameter(args);
				}
				else {
					break _loop5930;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				expr = new UndefExpression(args);
			}
			break;
		}
		case LITERAL_BEGIN:
		{
			keyword_BEGIN();
			match(LCURLY_BLOCK);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				expr=compoundStatement();
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RCURLY);
			break;
		}
		case LITERAL_END:
		{
			keyword_END();
			match(LCURLY_BLOCK);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				expr=compoundStatement();
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RCURLY);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=expression();
			{
			switch ( LA(1)) {
			case COMMA:
			{
				expr=parallelAssignmentLeftOver(assignToLocal(expr));
				break;
			}
			case EOF:
			case SEMI:
			case LINE_BREAK:
			case IF_MODIFIER:
			case UNLESS_MODIFIER:
			case WHILE_MODIFIER:
			case UNTIL_MODIFIER:
			case RESCUE_MODIFIER:
			case RCURLY:
			case RPAREN:
			case LITERAL_end:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_elsif:
			case LITERAL_when:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case REST_ARG_PREFIX:
		{
			match(REST_ARG_PREFIX);
			expr=dotAccess(true);
			op=operator_ASSIGN();
			args=mrhs();
			if ( inputState.guessing==0 ) {
				SequenceExpression lhs = new SequenceExpression();
								lhs.setRestArg(assignToLocal(expr));
								expr = new MultiAssignmentExpression(lhs, args); 
							
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  expression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		
		expr=assignmentExpression();
		return expr;
	}
	
	public final void keyword_alias() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_alias);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case BOR:
		case LITERAL_in:
		case LITERAL_end:
		case LOGICAL_OR:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	protected final SequenceExpression  alias_parameter(
		SequenceExpression args
	) throws RecognitionException, TokenStreamException {
		SequenceExpression args_out;
		
		Token  gvar = null;
		args_out = args==null? new SequenceExpression() : args; final int line = line();
		
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case BOR:
		case LITERAL_in:
		case LITERAL_end:
		case LOGICAL_OR:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			args_out=undef_parameter(args_out);
			break;
		}
		case GLOBAL_VARIBLE:
		{
			gvar = LT(1);
			match(GLOBAL_VARIBLE);
			if ( inputState.guessing==0 ) {
				args_out.addExpression ( new SymbolExpression(line, gvar.getText()) );
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return args_out;
	}
	
	public final void keyword_undef() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_undef);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case BOR:
		case LITERAL_in:
		case LITERAL_end:
		case LOGICAL_OR:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	protected final SequenceExpression  undef_parameter(
		SequenceExpression args
	) throws RecognitionException, TokenStreamException {
		SequenceExpression args_out;
		
		Token  i = null;
		Token  ass1 = null;
		Token  c = null;
		Token  ass2 = null;
		Token  f = null;
		Token  ass3 = null;
		args_out = args==null? new SequenceExpression() : args; 
		String name = null; Expression expr=null; final int line = line();
		
		{
		switch ( LA(1)) {
		case IDENTIFIER:
		{
			i = LT(1);
			match(IDENTIFIER);
			{
			switch ( LA(1)) {
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				ass1 = LT(1);
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				break;
			}
			case EOF:
			case SEMI:
			case LINE_BREAK:
			case IF_MODIFIER:
			case UNLESS_MODIFIER:
			case WHILE_MODIFIER:
			case UNTIL_MODIFIER:
			case RESCUE_MODIFIER:
			case COMMA:
			case RCURLY:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case RPAREN:
			case BOR:
			case LITERAL_in:
			case LITERAL_end:
			case LOGICAL_OR:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case LBRACK_ARRAY_ACCESS:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_do:
			case LITERAL_super:
			case LITERAL_yield:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_elsif:
			case LITERAL_unless:
			case LITERAL_case:
			case LESS_THAN:
			case LEFT_SHIFT:
			case RIGHT_SHIFT:
			case EQUAL:
			case CASE_EQUAL:
			case GREATER_THAN:
			case GREATER_OR_EQUAL:
			case LESS_OR_EQUAL:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case POWER:
			case BAND:
			case BXOR:
			case MATCH:
			case COMPARE:
			case BNOT:
			case SINGLE_QUOTE:
			case LOGICAL_AND:
			case NOT:
			case LITERAL_and:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_or:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_then:
			case LITERAL_when:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				name=concat(i,ass1);
			}
			break;
		}
		case CONSTANT:
		{
			c = LT(1);
			match(CONSTANT);
			{
			switch ( LA(1)) {
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				ass2 = LT(1);
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				break;
			}
			case EOF:
			case SEMI:
			case LINE_BREAK:
			case IF_MODIFIER:
			case UNLESS_MODIFIER:
			case WHILE_MODIFIER:
			case UNTIL_MODIFIER:
			case RESCUE_MODIFIER:
			case COMMA:
			case RCURLY:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case RPAREN:
			case BOR:
			case LITERAL_in:
			case LITERAL_end:
			case LOGICAL_OR:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case LBRACK_ARRAY_ACCESS:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_do:
			case LITERAL_super:
			case LITERAL_yield:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_elsif:
			case LITERAL_unless:
			case LITERAL_case:
			case LESS_THAN:
			case LEFT_SHIFT:
			case RIGHT_SHIFT:
			case EQUAL:
			case CASE_EQUAL:
			case GREATER_THAN:
			case GREATER_OR_EQUAL:
			case LESS_OR_EQUAL:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case POWER:
			case BAND:
			case BXOR:
			case MATCH:
			case COMPARE:
			case BNOT:
			case SINGLE_QUOTE:
			case LOGICAL_AND:
			case NOT:
			case LITERAL_and:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_or:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_then:
			case LITERAL_when:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				name=concat(c,ass2);
			}
			break;
		}
		case FUNCTION:
		{
			f = LT(1);
			match(FUNCTION);
			{
			switch ( LA(1)) {
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				ass3 = LT(1);
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				break;
			}
			case EOF:
			case SEMI:
			case LINE_BREAK:
			case IF_MODIFIER:
			case UNLESS_MODIFIER:
			case WHILE_MODIFIER:
			case UNTIL_MODIFIER:
			case RESCUE_MODIFIER:
			case COMMA:
			case RCURLY:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case RPAREN:
			case BOR:
			case LITERAL_in:
			case LITERAL_end:
			case LOGICAL_OR:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case LBRACK_ARRAY_ACCESS:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_do:
			case LITERAL_super:
			case LITERAL_yield:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_elsif:
			case LITERAL_unless:
			case LITERAL_case:
			case LESS_THAN:
			case LEFT_SHIFT:
			case RIGHT_SHIFT:
			case EQUAL:
			case CASE_EQUAL:
			case GREATER_THAN:
			case GREATER_OR_EQUAL:
			case LESS_OR_EQUAL:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case POWER:
			case BAND:
			case BXOR:
			case MATCH:
			case COMPARE:
			case BNOT:
			case SINGLE_QUOTE:
			case LOGICAL_AND:
			case NOT:
			case LITERAL_and:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_or:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_then:
			case LITERAL_when:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				name=concat(f,ass3);
			}
			break;
		}
		case COLON_WITH_NO_FOLLOWING_SPACE:
		{
			expr=symbol(false);
			break;
		}
		case LITERAL_in:
		case LITERAL_end:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			name=keywordAsMethodName();
			break;
		}
		case BOR:
		case LOGICAL_OR:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		{
			name=operatorAsMethodname();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
						if (expr==null) { expr=new SymbolExpression(line, name); } 
						args_out.addExpression(expr);
					
		}
		return args_out;
	}
	
	public final void keyword_BEGIN() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_BEGIN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case LCURLY_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_END() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_END);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case LCURLY_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final Expression  parallelAssignmentLeftOver(
		Expression first
	) throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		
			Expression expr2; 
			SequenceExpression lhs = new SequenceExpression(); 
			lhs.addExpression(first); 
			SequenceExpression rhs = null;
			String op; 
			boolean done=false;
		
		
		{
		int _cnt5951=0;
		_loop5951:
		do {
			if (((LA(1)==COMMA))&&(!done)) {
				match(COMMA);
				{
				if (((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==RCURLY||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==LITERAL_end||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LITERAL_when))&&(LA(1)==ASSIGN||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE)) {
					done=empty_true();
				}
				else if ((LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class)) {
					done=mlhs_item(lhs);
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
			}
			else {
				if ( _cnt5951>=1 ) { break _loop5951; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt5951++;
		} while (true);
		}
		{
		switch ( LA(1)) {
		case ASSIGN_WITH_NO_LEADING_SPACE:
		case ASSIGN:
		{
			op=operator_ASSIGN();
			rhs=mrhs();
			if ( inputState.guessing==0 ) {
				expr = new MultiAssignmentExpression(lhs, rhs);
			}
			break;
		}
		case EOF:
		case SEMI:
		case LINE_BREAK:
		case IF_MODIFIER:
		case UNLESS_MODIFIER:
		case WHILE_MODIFIER:
		case UNTIL_MODIFIER:
		case RESCUE_MODIFIER:
		case RCURLY:
		case RPAREN:
		case LITERAL_end:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_elsif:
		case LITERAL_when:
		{
			if ( inputState.guessing==0 ) {
				expr = lhs;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return expr;
	}
	
	public final Expression  dotAccess(
		boolean lhs
	) throws RecognitionException, TokenStreamException {
		Expression expr;
		
		
		expr=command(lhs);
		return expr;
	}
	
	public final String  operator_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			match(ASSIGN);
			break;
		}
		case ASSIGN_WITH_NO_LEADING_SPACE:
		{
			match(ASSIGN_WITH_NO_LEADING_SPACE);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final SequenceExpression  mrhs() throws RecognitionException, TokenStreamException {
		SequenceExpression rhs=new SequenceExpression();
		
		Expression expr; boolean done=false; final int line = line();
		
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=expression();
			if ( inputState.guessing==0 ) {
				rhs.addExpression(expr);
			}
			{
			_loop5956:
			do {
				if (((LA(1)==COMMA))&&(!done)) {
					match(COMMA);
					{
					switch ( LA(1)) {
					case REST_ARG_PREFIX:
					{
						match(REST_ARG_PREFIX);
						expr=expression();
						if ( inputState.guessing==0 ) {
							rhs.setRestArg(expr); done=true;
						}
						break;
					}
					case IDENTIFIER:
					case CONSTANT:
					case FUNCTION:
					case GLOBAL_VARIBLE:
					case LPAREN:
					case COLON_WITH_NO_FOLLOWING_SPACE:
					case INSTANCE_VARIBLE:
					case CLASS_VARIBLE:
					case LBRACK:
					case LITERAL_return:
					case LITERAL_break:
					case LITERAL_next:
					case LITERAL_retry:
					case LITERAL_redo:
					case LITERAL_nil:
					case LITERAL_self:
					case LITERAL_true:
					case LITERAL_false:
					case LITERAL___FILE__:
					case LITERAL___LINE__:
					case DOUBLE_QUOTE_STRING:
					case SINGLE_QUOTE_STRING:
					case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
					case DOUBLE_QUOTE_WARRAY:
					case SINGLE_QUOTE_WARRAY:
					case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
					case REGEX:
					case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
					case COMMAND_OUTPUT:
					case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
					case HERE_DOC_BEGIN:
					case INTEGER:
					case HEX:
					case BINARY:
					case OCTAL:
					case FLOAT:
					case ASCII_VALUE:
					case LEADING_COLON2:
					case LITERAL_super:
					case LITERAL_yield:
					case LCURLY_HASH:
					case LITERAL_begin:
					case LITERAL_if:
					case LITERAL_unless:
					case LITERAL_case:
					case BNOT:
					case NOT:
					case LITERAL_def:
					case 108:
					case LITERAL_module:
					case LITERAL_until:
					case LITERAL_for:
					case LITERAL_while:
					case LITERAL_class:
					case LITERAL_not:
					case UNARY_PLUS:
					case UNARY_MINUS:
					{
						expr=expression();
						if ( inputState.guessing==0 ) {
							rhs.addExpression(expr);
						}
						break;
					}
					case EOF:
					case SEMI:
					case LINE_BREAK:
					case IF_MODIFIER:
					case UNLESS_MODIFIER:
					case WHILE_MODIFIER:
					case UNTIL_MODIFIER:
					case RESCUE_MODIFIER:
					case COMMA:
					case RCURLY:
					case RPAREN:
					case LITERAL_end:
					case LITERAL_rescue:
					case LITERAL_else:
					case LITERAL_ensure:
					case LITERAL_elsif:
					case LITERAL_then:
					case LITERAL_when:
					case COLON:
					{
						done=empty_true();
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					break _loop5956;
				}
				
			} while (true);
			}
			break;
		}
		case REST_ARG_PREFIX:
		{
			match(REST_ARG_PREFIX);
			expr=expression();
			if ( inputState.guessing==0 ) {
				rhs.addExpression(new RestArgExpression(expr));
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return rhs;
	}
	
	public final Expression  symbol(
		boolean string_allowed
	) throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  iden = null;
		Token  ass1 = null;
		Token  fun = null;
		Token  ass2 = null;
		Token  con = null;
		Token  ass3 = null;
		Token  gvar = null;
		Token  ivar = null;
		Token  cvar = null;
		Token  unary = null;
		String name = null; final int line = line();
		
		match(COLON_WITH_NO_FOLLOWING_SPACE);
		{
		switch ( LA(1)) {
		case IDENTIFIER:
		{
			iden = LT(1);
			match(IDENTIFIER);
			{
			if ((LA(1)==ASSIGN_WITH_NO_LEADING_SPACE)) {
				ass1 = LT(1);
				match(ASSIGN_WITH_NO_LEADING_SPACE);
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==SINGLE_QUOTE||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				name=concat(iden,ass1);
			}
			break;
		}
		case FUNCTION:
		{
			fun = LT(1);
			match(FUNCTION);
			{
			if ((LA(1)==ASSIGN_WITH_NO_LEADING_SPACE)) {
				ass2 = LT(1);
				match(ASSIGN_WITH_NO_LEADING_SPACE);
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==SINGLE_QUOTE||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				name=concat(fun,ass2);
			}
			break;
		}
		case CONSTANT:
		{
			con = LT(1);
			match(CONSTANT);
			{
			if ((LA(1)==ASSIGN_WITH_NO_LEADING_SPACE)) {
				ass3 = LT(1);
				match(ASSIGN_WITH_NO_LEADING_SPACE);
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==SINGLE_QUOTE||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				name=concat(con,ass3);
			}
			break;
		}
		case GLOBAL_VARIBLE:
		{
			gvar = LT(1);
			match(GLOBAL_VARIBLE);
			if ( inputState.guessing==0 ) {
				name = gvar.getText();
			}
			break;
		}
		case INSTANCE_VARIBLE:
		{
			ivar = LT(1);
			match(INSTANCE_VARIBLE);
			if ( inputState.guessing==0 ) {
				name = ivar.getText();
			}
			break;
		}
		case CLASS_VARIBLE:
		{
			cvar = LT(1);
			match(CLASS_VARIBLE);
			if ( inputState.guessing==0 ) {
				name = cvar.getText();
			}
			break;
		}
		case UNARY_PLUS_MINUS_METHOD_NAME:
		{
			unary = LT(1);
			match(UNARY_PLUS_MINUS_METHOD_NAME);
			if ( inputState.guessing==0 ) {
				name = unary.getText();
			}
			break;
		}
		case BOR:
		case LOGICAL_OR:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		{
			name=operatorAsMethodname();
			break;
		}
		case LITERAL_in:
		case LITERAL_end:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			name=keyword();
			break;
		}
		default:
			if ((((LA(1) >= DOUBLE_QUOTE_STRING && LA(1) <= STRING_BEFORE_EXPRESSION_SUBSTITUTION)))&&(string_allowed)) {
				expr=string();
				if ( inputState.guessing==0 ) {
					expr=SymbolExpression.make(line, expr);
				}
			}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
							// System.out.println("read symbol "+name);
							tell_lexer_we_have_finished_parsing_symbol();
						 	if (expr==null) { expr = new SymbolExpression(line, name); }
						
		}
		return expr;
	}
	
	public final String  keywordAsMethodName() throws RecognitionException, TokenStreamException {
		String name;
		
		name = LT(1).getText();
		
		switch ( LA(1)) {
		case LITERAL_and:
		{
			match(LITERAL_and);
			break;
		}
		case LITERAL_begin:
		{
			match(LITERAL_begin);
			break;
		}
		case LITERAL_BEGIN:
		{
			match(LITERAL_BEGIN);
			break;
		}
		case LITERAL_def:
		{
			match(LITERAL_def);
			break;
		}
		case 108:
		{
			match(108);
			break;
		}
		case LITERAL_end:
		{
			match(LITERAL_end);
			break;
		}
		case LITERAL_END:
		{
			match(LITERAL_END);
			break;
		}
		case LITERAL_ensure:
		{
			match(LITERAL_ensure);
			break;
		}
		case LITERAL_if:
		{
			match(LITERAL_if);
			break;
		}
		case LITERAL_in:
		{
			match(LITERAL_in);
			break;
		}
		case LITERAL_or:
		{
			match(LITERAL_or);
			break;
		}
		case LITERAL_module:
		{
			match(LITERAL_module);
			break;
		}
		case LITERAL_redo:
		{
			match(LITERAL_redo);
			break;
		}
		case LITERAL_super:
		{
			match(LITERAL_super);
			break;
		}
		case LITERAL_unless:
		{
			match(LITERAL_unless);
			break;
		}
		case LITERAL_until:
		{
			match(LITERAL_until);
			break;
		}
		case LITERAL_break:
		{
			match(LITERAL_break);
			break;
		}
		case LITERAL_do:
		{
			match(LITERAL_do);
			break;
		}
		case LITERAL_next:
		{
			match(LITERAL_next);
			break;
		}
		case LITERAL_rescue:
		{
			match(LITERAL_rescue);
			break;
		}
		case LITERAL_then:
		{
			match(LITERAL_then);
			break;
		}
		case LITERAL_when:
		{
			match(LITERAL_when);
			break;
		}
		case LITERAL_case:
		{
			match(LITERAL_case);
			break;
		}
		case LITERAL_else:
		{
			match(LITERAL_else);
			break;
		}
		case LITERAL_for:
		{
			match(LITERAL_for);
			break;
		}
		case LITERAL_retry:
		{
			match(LITERAL_retry);
			break;
		}
		case LITERAL_while:
		{
			match(LITERAL_while);
			break;
		}
		case LITERAL_alias:
		{
			match(LITERAL_alias);
			break;
		}
		case LITERAL_class:
		{
			match(LITERAL_class);
			break;
		}
		case LITERAL_elsif:
		{
			match(LITERAL_elsif);
			break;
		}
		case LITERAL_not:
		{
			match(LITERAL_not);
			break;
		}
		case LITERAL_return:
		{
			match(LITERAL_return);
			break;
		}
		case LITERAL_undef:
		{
			match(LITERAL_undef);
			break;
		}
		case LITERAL_yield:
		{
			match(LITERAL_yield);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return name;
	}
	
	public final String  operatorAsMethodname() throws RecognitionException, TokenStreamException {
		String name;
		
		name = LT(1).getText();
		
		switch ( LA(1)) {
		case LEFT_SHIFT:
		{
			match(LEFT_SHIFT);
			break;
		}
		case RIGHT_SHIFT:
		{
			match(RIGHT_SHIFT);
			break;
		}
		case EQUAL:
		{
			match(EQUAL);
			break;
		}
		case CASE_EQUAL:
		{
			match(CASE_EQUAL);
			break;
		}
		case GREATER_THAN:
		{
			match(GREATER_THAN);
			break;
		}
		case GREATER_OR_EQUAL:
		{
			match(GREATER_OR_EQUAL);
			break;
		}
		case LESS_THAN:
		{
			match(LESS_THAN);
			break;
		}
		case LESS_OR_EQUAL:
		{
			match(LESS_OR_EQUAL);
			break;
		}
		case PLUS:
		{
			match(PLUS);
			break;
		}
		case MINUS:
		{
			match(MINUS);
			break;
		}
		case STAR:
		{
			match(STAR);
			break;
		}
		case DIV:
		{
			match(DIV);
			break;
		}
		case MOD:
		{
			match(MOD);
			break;
		}
		case POWER:
		{
			match(POWER);
			break;
		}
		case BAND:
		{
			match(BAND);
			break;
		}
		case BOR:
		{
			match(BOR);
			break;
		}
		case BXOR:
		{
			match(BXOR);
			break;
		}
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		{
			{
			switch ( LA(1)) {
			case LBRACK:
			{
				match(LBRACK);
				break;
			}
			case LBRACK_ARRAY_ACCESS:
			{
				match(LBRACK_ARRAY_ACCESS);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RBRACK);
			if ( inputState.guessing==0 ) {
				name="[]";
			}
			{
			if ((LA(1)==ASSIGN_WITH_NO_LEADING_SPACE)) {
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				if ( inputState.guessing==0 ) {
					name="[]=";
				}
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==SINGLE_QUOTE||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			break;
		}
		case MATCH:
		{
			match(MATCH);
			break;
		}
		case COMPARE:
		{
			match(COMPARE);
			break;
		}
		case BNOT:
		{
			match(BNOT);
			break;
		}
		case SINGLE_QUOTE:
		{
			match(SINGLE_QUOTE);
			break;
		}
		case LOGICAL_OR:
		{
			match(LOGICAL_OR);
			break;
		}
		case LOGICAL_AND:
		{
			match(LOGICAL_AND);
			break;
		}
		case NOT:
		{
			match(NOT);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return name;
	}
	
	public final boolean  mlhs_item(
		SequenceExpression lhs
	) throws RecognitionException, TokenStreamException {
		boolean last=false;
		
		Expression expr = null; SequenceExpression nested = null; boolean done;
		
		boolean synPredMatched5942 = false;
		if (((LA(1)==LPAREN))) {
			int _m5942 = mark();
			synPredMatched5942 = true;
			inputState.guessing++;
			try {
				{
				match(LPAREN);
				mlhs_item(null);
				match(COMMA);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched5942 = false;
			}
			rewind(_m5942);
inputState.guessing--;
		}
		if ( synPredMatched5942 ) {
			match(LPAREN);
			if ( inputState.guessing==0 ) {
				nested = new SequenceExpression();
			}
			done=mlhs_item(nested);
			{
			_loop5944:
			do {
				if (((LA(1)==COMMA))&&(!done)) {
					match(COMMA);
					done=mlhs_item(nested);
				}
				else {
					break _loop5944;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				lhs.addExpression(nested);
			}
			match(RPAREN);
		}
		else if ((LA(1)==REST_ARG_PREFIX)) {
			match(REST_ARG_PREFIX);
			{
			switch ( LA(1)) {
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case LITERAL_def:
			case 108:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_class:
			{
				expr=dotAccess(true);
				if ( inputState.guessing==0 ) {
					expr=assignToLocal(expr);
				}
				break;
			}
			case EOF:
			case SEMI:
			case LINE_BREAK:
			case IF_MODIFIER:
			case UNLESS_MODIFIER:
			case WHILE_MODIFIER:
			case UNTIL_MODIFIER:
			case RESCUE_MODIFIER:
			case COMMA:
			case RCURLY:
			case ASSIGN_WITH_NO_LEADING_SPACE:
			case RPAREN:
			case BOR:
			case LITERAL_in:
			case LITERAL_end:
			case ASSIGN:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_elsif:
			case LITERAL_when:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				lhs.setRestArg(expr); last=true;
			}
		}
		else if ((LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class)) {
			expr=dotAccess(true);
			if ( inputState.guessing==0 ) {
				expr=assignToLocal(expr); lhs.addExpression(expr);
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		return last;
	}
	
	protected final boolean  empty_true() throws RecognitionException, TokenStreamException {
		boolean result=true;
		
		
		return result;
	}
	
	protected final boolean  empty_false() throws RecognitionException, TokenStreamException {
		boolean result=false;
		
		
		return result;
	}
	
	public final void block_vars(
		BlockCode block
	) throws RecognitionException, TokenStreamException {
		
		boolean done = false; SequenceExpression parms = block.parms();
		
		done=mlhs_item(parms);
		{
		_loop5962:
		do {
			if (((LA(1)==COMMA))&&(!done)) {
				match(COMMA);
				{
				switch ( LA(1)) {
				case COMMA:
				case BOR:
				case LITERAL_in:
				{
					done=empty_true();
					break;
				}
				case REST_ARG_PREFIX:
				case IDENTIFIER:
				case CONSTANT:
				case FUNCTION:
				case GLOBAL_VARIBLE:
				case LPAREN:
				case COLON_WITH_NO_FOLLOWING_SPACE:
				case INSTANCE_VARIBLE:
				case CLASS_VARIBLE:
				case LBRACK:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_next:
				case LITERAL_retry:
				case LITERAL_redo:
				case LITERAL_nil:
				case LITERAL_self:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL___FILE__:
				case LITERAL___LINE__:
				case DOUBLE_QUOTE_STRING:
				case SINGLE_QUOTE_STRING:
				case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
				case DOUBLE_QUOTE_WARRAY:
				case SINGLE_QUOTE_WARRAY:
				case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
				case REGEX:
				case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
				case COMMAND_OUTPUT:
				case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
				case HERE_DOC_BEGIN:
				case INTEGER:
				case HEX:
				case BINARY:
				case OCTAL:
				case FLOAT:
				case ASCII_VALUE:
				case LEADING_COLON2:
				case LITERAL_super:
				case LITERAL_yield:
				case LCURLY_HASH:
				case LITERAL_begin:
				case LITERAL_if:
				case LITERAL_unless:
				case LITERAL_case:
				case LITERAL_def:
				case 108:
				case LITERAL_module:
				case LITERAL_until:
				case LITERAL_for:
				case LITERAL_while:
				case LITERAL_class:
				{
					done=mlhs_item(parms);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				break _loop5962;
			}
			
		} while (true);
		}
	}
	
	public final BlockCode  codeBlock() throws RecognitionException, TokenStreamException {
		BlockCode block=null;
		
		RubyCode context = scope(); int line = line();
		
		if ( inputState.guessing==0 ) {
			scope(block = new BlockCode(line, context, getFilename()));
		}
		{
		switch ( LA(1)) {
		case LITERAL_do:
		{
			keyword_do();
			blockContent(block);
			match(LITERAL_end);
			break;
		}
		case LCURLY_BLOCK:
		{
			match(LCURLY_BLOCK);
			blockContent(block);
			match(RCURLY);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			scope(context);
		}
		return block;
	}
	
	public final void keyword_do() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_do);
		{
		if ((LA(1)==LINE_BREAK)) {
			match(LINE_BREAK);
		}
		else if ((LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==BOR||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==BNOT||LA(1)==NOT||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
	}
	
	public final void blockContent(
		BlockCode block
	) throws RecognitionException, TokenStreamException {
		
		
			Expression body = NilExpression.instance;
		
		
		{
		switch ( LA(1)) {
		case BOR:
		{
			match(BOR);
			{
			switch ( LA(1)) {
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case LITERAL_def:
			case 108:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_class:
			{
				block_vars(block);
				break;
			}
			case BOR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(BOR);
			break;
		}
		case LOGICAL_OR:
		{
			match(LOGICAL_OR);
			break;
		}
		case SEMI:
		case LINE_BREAK:
		case RCURLY:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case LITERAL_end:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			body=compoundStatement();
			if ( inputState.guessing==0 ) {
				block.setBody(body);
			}
			break;
		}
		case RCURLY:
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final String  keyword() throws RecognitionException, TokenStreamException {
		String name;
		
		name = LT(1).getText();
		
		switch ( LA(1)) {
		case LITERAL_in:
		case LITERAL_end:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			name=keywordAsMethodName();
			break;
		}
		case LITERAL_nil:
		{
			match(LITERAL_nil);
			break;
		}
		case LITERAL_self:
		{
			match(LITERAL_self);
			break;
		}
		case LITERAL_true:
		{
			match(LITERAL_true);
			break;
		}
		case LITERAL_false:
		{
			match(LITERAL_false);
			break;
		}
		case LITERAL___FILE__:
		{
			match(LITERAL___FILE__);
			break;
		}
		case LITERAL___LINE__:
		{
			match(LITERAL___LINE__);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return name;
	}
	
	public final Expression  string() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  double_quote_string_value = null;
		Token  single_quote_string_value = null;
		Token  s1 = null;
		Token  s3 = null;
		Token  s2 = null;
		StringConcatExpression exc = null; Expression exp2;
		
		switch ( LA(1)) {
		case DOUBLE_QUOTE_STRING:
		{
			double_quote_string_value = LT(1);
			match(DOUBLE_QUOTE_STRING);
			if ( inputState.guessing==0 ) {
				expr = new ConstantStringExpression(double_quote_string_value.getText(), ConstantStringExpression.DOUBLE_QUOTE);
			}
			break;
		}
		case SINGLE_QUOTE_STRING:
		{
			single_quote_string_value = LT(1);
			match(SINGLE_QUOTE_STRING);
			if ( inputState.guessing==0 ) {
				expr = new ConstantStringExpression(single_quote_string_value.getText(), ConstantStringExpression.SINGLE_QUOTE);
			}
			break;
		}
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			s1 = LT(1);
			match(STRING_BEFORE_EXPRESSION_SUBSTITUTION);
			exp2=expression_substituation();
			if ( inputState.guessing==0 ) {
				expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.DOUBLE_QUOTE); exc.add(s1.getText()); exc.add(exp2);
			}
			{
			_loop6076:
			do {
				if ((LA(1)==STRING_BETWEEN_EXPRESSION_SUBSTITUTION)) {
					s3 = LT(1);
					match(STRING_BETWEEN_EXPRESSION_SUBSTITUTION);
					exp2=expression_substituation();
					if ( inputState.guessing==0 ) {
						exc.add(s3.getText()); exc.add(exp2);
					}
				}
				else {
					break _loop6076;
				}
				
			} while (true);
			}
			s2 = LT(1);
			match(STRING_AFTER_EXPRESSION_SUBSTITUTION);
			if ( inputState.guessing==0 ) {
				exc.add(s2.getText());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  assignmentExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		String op=null; Expression expr2;
		
		expr=andorExpression();
		{
		_loop5979:
		do {
			if ((LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==ASSIGN||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN)) {
				{
				switch ( LA(1)) {
				case ASSIGN_WITH_NO_LEADING_SPACE:
				case ASSIGN:
				{
					op=operator_ASSIGN();
					if ( inputState.guessing==0 ) {
						
												expr = assignToLocal(expr);
											
					}
					break;
				}
				case PLUS_ASSIGN:
				{
					op=operator_PLUS_ASSIGN();
					break;
				}
				case MINUS_ASSIGN:
				{
					op=operator_MINUS_ASSIGN();
					break;
				}
				case STAR_ASSIGN:
				{
					op=operator_STAR_ASSIGN();
					break;
				}
				case DIV_ASSIGN:
				{
					op=operator_DIV_ASSIGN();
					break;
				}
				case MOD_ASSIGN:
				{
					op=operator_MOD_ASSIGN();
					break;
				}
				case POWER_ASSIGN:
				{
					op=operator_POWER_ASSIGN();
					break;
				}
				case BAND_ASSIGN:
				{
					op=operator_BAND_ASSIGN();
					break;
				}
				case BXOR_ASSIGN:
				{
					op=operator_BXOR_ASSIGN();
					break;
				}
				case BOR_ASSIGN:
				{
					op=operator_BOR_ASSIGN();
					break;
				}
				case LEFT_SHIFT_ASSIGN:
				{
					op=operator_LEFT_SHIFT_ASSIGN();
					break;
				}
				case RIGHT_SHIFT_ASSIGN:
				{
					op=operator_RIGHT_SHIFT_ASSIGN();
					break;
				}
				case LOGICAL_AND_ASSIGN:
				{
					op=operator_LOGICAL_AND_ASSIGN();
					break;
				}
				case LOGICAL_OR_ASSIGN:
				{
					op=operator_LOGICAL_OR_ASSIGN();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case REST_ARG_PREFIX:
				{
					match(REST_ARG_PREFIX);
					expr2=rangeExpression();
					if ( inputState.guessing==0 ) {
						expr2=new RestArgExpression(expr2);
					}
					break;
				}
				case IDENTIFIER:
				case CONSTANT:
				case FUNCTION:
				case GLOBAL_VARIBLE:
				case LPAREN:
				case COLON_WITH_NO_FOLLOWING_SPACE:
				case INSTANCE_VARIBLE:
				case CLASS_VARIBLE:
				case LBRACK:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_next:
				case LITERAL_retry:
				case LITERAL_redo:
				case LITERAL_nil:
				case LITERAL_self:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL___FILE__:
				case LITERAL___LINE__:
				case DOUBLE_QUOTE_STRING:
				case SINGLE_QUOTE_STRING:
				case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
				case DOUBLE_QUOTE_WARRAY:
				case SINGLE_QUOTE_WARRAY:
				case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
				case REGEX:
				case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
				case COMMAND_OUTPUT:
				case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
				case HERE_DOC_BEGIN:
				case INTEGER:
				case HEX:
				case BINARY:
				case OCTAL:
				case FLOAT:
				case ASCII_VALUE:
				case LEADING_COLON2:
				case LITERAL_super:
				case LITERAL_yield:
				case LCURLY_HASH:
				case LITERAL_begin:
				case LITERAL_if:
				case LITERAL_unless:
				case LITERAL_case:
				case BNOT:
				case NOT:
				case LITERAL_def:
				case 108:
				case LITERAL_module:
				case LITERAL_until:
				case LITERAL_for:
				case LITERAL_while:
				case LITERAL_class:
				case LITERAL_not:
				case UNARY_PLUS:
				case UNARY_MINUS:
				{
					expr2=assignmentExpression();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					expr= AssignmentExpression.build (expr, op, expr2);
				}
			}
			else {
				break _loop5979;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final Expression  andorExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		String op=null; Expression expr2;
		
		expr=notExpression();
		{
		_loop5983:
		do {
			if ((LA(1)==LITERAL_and||LA(1)==LITERAL_or)) {
				{
				switch ( LA(1)) {
				case LITERAL_and:
				{
					keyword_and();
					if ( inputState.guessing==0 ) {
						op="and";
					}
					break;
				}
				case LITERAL_or:
				{
					keyword_or();
					if ( inputState.guessing==0 ) {
						op="or";
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=notExpression();
				if ( inputState.guessing==0 ) {
					expr= new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop5983;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_PLUS_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(PLUS_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_MINUS_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(MINUS_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_STAR_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(STAR_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_DIV_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(DIV_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_MOD_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(MOD_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_POWER_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(POWER_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_BAND_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BAND_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_BXOR_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BXOR_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_BOR_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BOR_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_LEFT_SHIFT_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LEFT_SHIFT_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_RIGHT_SHIFT_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(RIGHT_SHIFT_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_LOGICAL_AND_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LOGICAL_AND_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_LOGICAL_OR_ASSIGN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LOGICAL_OR_ASSIGN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  rangeExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		String op=null; Expression expr2;
		
		expr=logicalOrExpression();
		{
		_loop5990:
		do {
			if ((LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE)) {
				{
				switch ( LA(1)) {
				case INCLUSIVE_RANGE:
				{
					op=operator_INCLUSIVE_RANGE();
					break;
				}
				case EXCLUSIVE_RANGE:
				{
					op=operator_EXCLUSIVE_RANGE();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=logicalOrExpression();
				if ( inputState.guessing==0 ) {
					expr=new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop5990;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final Expression  notExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		
		switch ( LA(1)) {
		case LITERAL_not:
		{
			keyword_not();
			expr=notExpression();
			if ( inputState.guessing==0 ) {
				expr = new UnaryExpression("not", expr);
			}
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=ternaryIfThenElseExpression();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final void keyword_and() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_and);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_or() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_or);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_not() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_not);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final Expression  ternaryIfThenElseExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2, expr3; String op; int line=0;
		
		expr=rangeExpression();
		{
		if ((LA(1)==QUESTION)) {
			if ( inputState.guessing==0 ) {
				line = line();
			}
			op=operator_QUESTION();
			expr2=ternaryIfThenElseExpression();
			op=operator_COLON();
			expr3=ternaryIfThenElseExpression();
			if ( inputState.guessing==0 ) {
				expr = new IfThenElseExpression(line, expr, expr2, expr3);
			}
		}
		else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		return expr;
	}
	
	public final String  operator_QUESTION() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(QUESTION);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_COLON() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		{
		switch ( LA(1)) {
		case COLON:
		{
			match(COLON);
			break;
		}
		case COLON_WITH_NO_FOLLOWING_SPACE:
		{
			match(COLON_WITH_NO_FOLLOWING_SPACE);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  logicalOrExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		String op=null; Expression expr2;
		
		expr=logicalAndExpression();
		{
		_loop5993:
		do {
			if ((LA(1)==LOGICAL_OR)) {
				op=operator_LOGICAL_OR();
				expr2=logicalAndExpression();
				if ( inputState.guessing==0 ) {
					expr = new LogicalOrExpression(expr, expr2);
				}
			}
			else {
				break _loop5993;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_INCLUSIVE_RANGE() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(INCLUSIVE_RANGE);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_EXCLUSIVE_RANGE() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(EXCLUSIVE_RANGE);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  logicalAndExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		String op=null; Expression expr2;
		
		expr=equalityExpression();
		{
		_loop5996:
		do {
			if ((LA(1)==LOGICAL_AND)) {
				op=operator_LOGICAL_AND();
				expr2=equalityExpression();
				if ( inputState.guessing==0 ) {
					expr = new LogicalAndExpression(expr, expr2);
				}
			}
			else {
				break _loop5996;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_LOGICAL_OR() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LOGICAL_OR);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  equalityExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=relationalExpression();
		{
		_loop6000:
		do {
			if ((LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
				{
				switch ( LA(1)) {
				case COMPARE:
				{
					op=operator_COMPARE();
					break;
				}
				case EQUAL:
				{
					op=operator_EQUAL();
					break;
				}
				case CASE_EQUAL:
				{
					op=operator_CASE_EQUAL();
					break;
				}
				case NOT_EQUAL:
				{
					op=operator_NOT_EQUAL();
					break;
				}
				case MATCH:
				{
					op=operator_MATCH();
					break;
				}
				case NOT_MATCH:
				{
					op=operator_NOT_MATCH();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=relationalExpression();
				if ( inputState.guessing==0 ) {
					
									if ("!=".equals(op)) {
										expr=new UnaryExpression ("not", new BinaryExpression(expr, "==", expr2)); 
									} else if ("!~".equals(op)) {
										expr=new UnaryExpression ("not", new BinaryExpression(expr, "=~", expr2)); 
									} else {
										expr=new BinaryExpression(expr, op, expr2); 
									}
								
				}
			}
			else {
				break _loop6000;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_LOGICAL_AND() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LOGICAL_AND);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  relationalExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=orExpression();
		{
		_loop6004:
		do {
			if ((LA(1)==LESS_THAN||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL)) {
				{
				switch ( LA(1)) {
				case LESS_THAN:
				{
					op=operator_LESS_THAN();
					break;
				}
				case GREATER_THAN:
				{
					op=operator_GREATER_THAN();
					break;
				}
				case LESS_OR_EQUAL:
				{
					op=operator_LESS_OR_EQUAL();
					break;
				}
				case GREATER_OR_EQUAL:
				{
					op=operator_GREATER_OR_EQUAL();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=orExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6004;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_COMPARE() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(COMPARE);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_EQUAL() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(EQUAL);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_CASE_EQUAL() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(CASE_EQUAL);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_NOT_EQUAL() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(NOT_EQUAL);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_MATCH() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(MATCH);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_NOT_MATCH() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(NOT_MATCH);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  orExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=andExpression();
		{
		_loop6008:
		do {
			if ((LA(1)==BOR||LA(1)==BXOR)) {
				{
				switch ( LA(1)) {
				case BXOR:
				{
					op=operator_BXOR();
					break;
				}
				case BOR:
				{
					op=operator_BOR();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=andExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6008;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_LESS_THAN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LESS_THAN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_GREATER_THAN() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(GREATER_THAN);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_LESS_OR_EQUAL() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LESS_OR_EQUAL);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_GREATER_OR_EQUAL() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(GREATER_OR_EQUAL);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  andExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=shiftExpression();
		{
		_loop6011:
		do {
			if ((LA(1)==BAND)) {
				op=operator_BAND();
				expr2=shiftExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6011;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_BXOR() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BXOR);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_BOR() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BOR);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  shiftExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=additiveExpression();
		{
		_loop6015:
		do {
			if ((LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT)) {
				{
				switch ( LA(1)) {
				case LEFT_SHIFT:
				{
					op=operator_LEFT_SHIFT();
					break;
				}
				case RIGHT_SHIFT:
				{
					op=operator_RIGHT_SHIFT();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=additiveExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6015;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_BAND() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BAND);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  additiveExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=multiplicativeExpression();
		{
		_loop6019:
		do {
			if ((LA(1)==PLUS||LA(1)==MINUS)) {
				{
				switch ( LA(1)) {
				case PLUS:
				{
					op=operator_PLUS();
					break;
				}
				case MINUS:
				{
					op=operator_MINUS();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=multiplicativeExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6019;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_LEFT_SHIFT() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(LEFT_SHIFT);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_RIGHT_SHIFT() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(RIGHT_SHIFT);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  multiplicativeExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=powerExpression();
		{
		_loop6023:
		do {
			if (((LA(1) >= STAR && LA(1) <= MOD))) {
				{
				switch ( LA(1)) {
				case STAR:
				{
					op=operator_STAR();
					break;
				}
				case DIV:
				{
					op=operator_DIV();
					break;
				}
				case MOD:
				{
					op=operator_MOD();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				expr2=powerExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6023;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_PLUS() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(PLUS);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_MINUS() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(MINUS);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  powerExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null;
		
		expr=unaryExpression();
		{
		_loop6026:
		do {
			if ((LA(1)==POWER)) {
				op=operator_POWER();
				expr2=unaryExpression();
				if ( inputState.guessing==0 ) {
					expr = new BinaryExpression(expr, op, expr2);
				}
			}
			else {
				break _loop6026;
			}
			
		} while (true);
		}
		return expr;
	}
	
	public final String  operator_STAR() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(STAR);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_DIV() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(DIV);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_MOD() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(MOD);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  unaryExpression() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		Expression expr2; String op=null; java.util.ArrayList<String> ops=null;
		
		{
		_loop6029:
		do {
			switch ( LA(1)) {
			case UNARY_PLUS:
			{
				op=operator_UNARY_PLUS();
				if ( inputState.guessing==0 ) {
					ops=append(ops,op);
				}
				break;
			}
			case UNARY_MINUS:
			{
				op=operator_UNARY_MINUS();
				if ( inputState.guessing==0 ) {
					ops=append(ops,op);
				}
				break;
			}
			case BNOT:
			{
				op=operator_BNOT();
				if ( inputState.guessing==0 ) {
					ops=append(ops,op);
				}
				break;
			}
			case NOT:
			{
				op=operator_NOT();
				if ( inputState.guessing==0 ) {
					ops=append(ops,"not");
				}
				break;
			}
			default:
			{
				break _loop6029;
			}
			}
		} while (true);
		}
		expr=dotAccess(false /*also allow non-lhs*/);
		if ( inputState.guessing==0 ) {
			
							if (ops!=null) {
								for(int i = ops.size()-1; i>=0; i--) {
									expr=new UnaryExpression(ops.get(i), expr);	
								}	
							}	
						
		}
		return expr;
	}
	
	public final String  operator_POWER() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(POWER);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_UNARY_PLUS() throws RecognitionException, TokenStreamException {
		String op="+@";
		
		
		match(UNARY_PLUS);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_UNARY_MINUS() throws RecognitionException, TokenStreamException {
		String op="-@";
		
		
		match(UNARY_MINUS);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_BNOT() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(BNOT);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final String  operator_NOT() throws RecognitionException, TokenStreamException {
		String op=LT(1).getText();
		
		
		match(NOT);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return op;
	}
	
	public final Expression  command(
		boolean lhs_only
	) throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		String method = null; 
		SequenceExpression args = null;  
		BlockCode block = null; 
		is_lhs_ = false;
		seen_rparen_ = false;
		MethodCallExpression mc = null;
		boolean maybe_command = false;
		int line = 0;
		
		
		if ((LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class)) {
			{
			switch ( LA(1)) {
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case LITERAL_def:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_class:
			{
				expr=primaryExpressionCannotBeCommand();
				seen_rparen_=empty_false();
				maybe_command=empty_false();
				break;
			}
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case 108:
			{
				expr=primaryExpressionCanBeCommand();
				if ( inputState.guessing==0 ) {
					
										MethodDenominator denom = (MethodDenominator)expr;
										method = denom.getMethodName();
									
				}
				expr=restOfMethodCall(null, expr, method, false);
				maybe_command=empty_true();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop6037:
			do {
				if (((LA(1) >= DOT && LA(1) <= LBRACK))) {
					expr=commandPart(expr);
					maybe_command=empty_true();
					is_lhs_=empty_true();
				}
				else {
					break _loop6037;
				}
				
			} while (true);
			}
			{
			if (((LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==BLOCK_ARG_PREFIX||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==BNOT||LA(1)==NOT||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS))&&(!lhs_only && maybe_command && !seen_rparen_)) {
				if ( inputState.guessing==0 ) {
					mc = MethodCallExpression.make(scope(), expr);
				}
				args=methodInvocationArgumentWithoutParen(new SequenceExpression(),false);
				{
				if (((LA(1)==LCURLY_BLOCK||LA(1)==LITERAL_do))&&(!args.hasBlockArg())) {
					block=codeBlock();
				}
				else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				if ( inputState.guessing==0 ) {
					mc.setArgs(args); mc.setBlock(block);  expr = mc;
				}
				is_lhs_=empty_false();
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			if (((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH))&&(is_lhs_ || !lhs_only)) {
				if ( inputState.guessing==0 ) {
					/*OK*/
				}
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
				if ( inputState.guessing==0 ) {
					throw new NoViableAltException(LT(1), getFilename());
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		else if ((((LA(1) >= LITERAL_return && LA(1) <= LITERAL_redo)))&&(!lhs_only)) {
			if ( inputState.guessing==0 ) {
				line = line(); method = LT(1).getText();
			}
			{
			switch ( LA(1)) {
			case LITERAL_return:
			{
				match(LITERAL_return);
				break;
			}
			case LITERAL_break:
			{
				match(LITERAL_break);
				break;
			}
			case LITERAL_next:
			{
				match(LITERAL_next);
				break;
			}
			case LITERAL_retry:
			{
				match(LITERAL_retry);
				break;
			}
			case LITERAL_redo:
			{
				match(LITERAL_redo);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			if ((LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==BLOCK_ARG_PREFIX||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==BNOT||LA(1)==NOT||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
				args=methodInvocationArgumentWithoutParen(args,false);
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				expr = new MethodCallExpression(scope(), line, null, method, args, null, false);
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		return expr;
	}
	
	public final Expression  commandPart(
		Expression base
	) throws RecognitionException, TokenStreamException {
		Expression expr;
		
		
			expr = base; 
			SequenceExpression args = null;  
			int line = line();
		
		
		switch ( LA(1)) {
		case DOT:
		{
			match(DOT);
			expr=methodCallAfterDot(expr, false);
			break;
		}
		case COLON2:
		{
			match(COLON2);
			expr=methodCallAfterDot(expr, true);
			break;
		}
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		{
			{
			switch ( LA(1)) {
			case LBRACK_ARRAY_ACCESS:
			{
				match(LBRACK_ARRAY_ACCESS);
				break;
			}
			case LBRACK:
			{
				match(LBRACK);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_def:
			case 108:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_class:
			case LITERAL_not:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				args=arrayReferenceArgument();
				break;
			}
			case RBRACK:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RBRACK);
			if ( inputState.guessing==0 ) {
				expr = new MethodCallExpression(scope(), line, expr, "[]", args, null, false);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  methodCallAfterDot(
		Expression base, boolean base_must_be_class_or_module
	) throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		SequenceExpression args=null; String method=null; is_lhs_ = true; seen_rparen_=false;
		
		{
		switch ( LA(1)) {
		case BOR:
		case LOGICAL_OR:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		{
			method=operatorAsMethodname();
			break;
		}
		case LITERAL_in:
		case LITERAL_end:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			method=keywordAsMethodName();
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		{
			expr=variableCanBeCommand();
			if ( inputState.guessing==0 ) {
				
									MethodDenominator denom = (MethodDenominator)expr;
									method = denom.getMethodName();
								
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		expr=restOfMethodCall(base,expr,method,base_must_be_class_or_module);
		return expr;
	}
	
	public final SequenceExpression  arrayReferenceArgument() throws RecognitionException, TokenStreamException {
		SequenceExpression args = new SequenceExpression();
		
		Expression expr; boolean done=false;
		
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			keyValuePair(args);
			{
			_loop6104:
			do {
				if (((LA(1)==COMMA))&&(!done)) {
					match(COMMA);
					{
					switch ( LA(1)) {
					case COMMA:
					case RBRACK:
					{
						done=empty_true();
						break;
					}
					case REST_ARG_PREFIX:
					{
						match(REST_ARG_PREFIX);
						expr=expression();
						{
						switch ( LA(1)) {
						case LINE_BREAK:
						{
							match(LINE_BREAK);
							break;
						}
						case COMMA:
						case RBRACK:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						done=empty_true();
						if ( inputState.guessing==0 ) {
							args.setRestArg(expr);
						}
						break;
					}
					case IDENTIFIER:
					case CONSTANT:
					case FUNCTION:
					case GLOBAL_VARIBLE:
					case LPAREN:
					case COLON_WITH_NO_FOLLOWING_SPACE:
					case INSTANCE_VARIBLE:
					case CLASS_VARIBLE:
					case LBRACK:
					case LITERAL_return:
					case LITERAL_break:
					case LITERAL_next:
					case LITERAL_retry:
					case LITERAL_redo:
					case LITERAL_nil:
					case LITERAL_self:
					case LITERAL_true:
					case LITERAL_false:
					case LITERAL___FILE__:
					case LITERAL___LINE__:
					case DOUBLE_QUOTE_STRING:
					case SINGLE_QUOTE_STRING:
					case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
					case DOUBLE_QUOTE_WARRAY:
					case SINGLE_QUOTE_WARRAY:
					case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
					case REGEX:
					case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
					case COMMAND_OUTPUT:
					case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
					case HERE_DOC_BEGIN:
					case INTEGER:
					case HEX:
					case BINARY:
					case OCTAL:
					case FLOAT:
					case ASCII_VALUE:
					case LEADING_COLON2:
					case LITERAL_super:
					case LITERAL_yield:
					case LCURLY_HASH:
					case LITERAL_begin:
					case LITERAL_if:
					case LITERAL_unless:
					case LITERAL_case:
					case BNOT:
					case NOT:
					case LITERAL_def:
					case 108:
					case LITERAL_module:
					case LITERAL_until:
					case LITERAL_for:
					case LITERAL_while:
					case LITERAL_class:
					case LITERAL_not:
					case UNARY_PLUS:
					case UNARY_MINUS:
					{
						keyValuePair(args);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					break _loop6104;
				}
				
			} while (true);
			}
			break;
		}
		case REST_ARG_PREFIX:
		{
			match(REST_ARG_PREFIX);
			expr=expression();
			if ( inputState.guessing==0 ) {
				args.setRestArg(expr);
			}
			{
			switch ( LA(1)) {
			case LINE_BREAK:
			{
				match(LINE_BREAK);
				break;
			}
			case RBRACK:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return args;
	}
	
	public final Expression  primaryExpressionCannotBeCommand() throws RecognitionException, TokenStreamException {
		Expression expr;
		
		
		{
		switch ( LA(1)) {
		case LCURLY_HASH:
		{
			expr=hashExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;
			}
			break;
		}
		case LPAREN:
		{
			match(LPAREN);
			expr=compoundStatement();
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_if:
		{
			expr=ifExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_unless:
		{
			expr=unlessExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_while:
		{
			expr=whileExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_until:
		{
			expr=untilExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_case:
		{
			expr=caseExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_for:
		{
			expr=forExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_begin:
		{
			expr=exceptionHandlingExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_module:
		{
			expr=moduleDefinition();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_class:
		{
			expr=classDefinition();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case LITERAL_def:
		{
			expr=methodDefinition();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;seen_rparen_ = false;
			}
			break;
		}
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		{
			expr=numeric();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;
			}
			break;
		}
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		{
			expr=literal();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;
			}
			break;
		}
		case GLOBAL_VARIBLE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		{
			expr=syntaxedVariable();
			is_lhs_=empty_true();
			break;
		}
		case LBRACK:
		{
			expr=arrayExpression();
			if ( inputState.guessing==0 ) {
				can_be_command_ = false;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return expr;
	}
	
	public final Expression  primaryExpressionCanBeCommand() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  fun = null;
		
		switch ( LA(1)) {
		case LEADING_COLON2:
		{
			match(LEADING_COLON2);
			fun = LT(1);
			match(FUNCTION);
			if ( inputState.guessing==0 ) {
				expr = new FunctionExpression(scope(), true, fun.getText());
			}
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		{
			expr=variableCanBeCommand();
			break;
		}
		case LITERAL_super:
		{
			match(LITERAL_super);
			if ( inputState.guessing==0 ) {
				expr = SuperExpression.instance;
			}
			break;
		}
		case LITERAL_yield:
		{
			match(LITERAL_yield);
			if ( inputState.guessing==0 ) {
				expr = YieldExpression.instance;
			}
			break;
		}
		case 108:
		{
			keyword_defined();
			if ( inputState.guessing==0 ) {
				expr = DefinedExpression.instance;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  restOfMethodCall(
		Expression base, Expression expr_in, String method, boolean base_must_be_class_or_module
	) throws RecognitionException, TokenStreamException {
		Expression expr=expr_in;
		
		SequenceExpression args = null; BlockCode block = null; int line = line();;
		
		boolean synPredMatched6048 = false;
		if (((LA(1)==LPAREN))) {
			int _m6048 = mark();
			synPredMatched6048 = true;
			inputState.guessing++;
			try {
				{
				match(LPAREN);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched6048 = false;
			}
			rewind(_m6048);
inputState.guessing--;
		}
		if ( synPredMatched6048 ) {
			args=methodInvocationArgumentWithParen();
			is_lhs_=empty_false();
			seen_rparen_=empty_true();
			{
			if ((LA(1)==LCURLY_BLOCK||LA(1)==LITERAL_do)) {
				block=codeBlock();
				is_lhs_=empty_false();
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				expr = new MethodCallExpression(scope(), line, base, method, args, block, base_must_be_class_or_module);
			}
		}
		else {
			boolean synPredMatched6051 = false;
			if (((LA(1)==LCURLY_BLOCK||LA(1)==LITERAL_do))) {
				int _m6051 = mark();
				synPredMatched6051 = true;
				inputState.guessing++;
				try {
					{
					switch ( LA(1)) {
					case LCURLY_BLOCK:
					{
						match(LCURLY_BLOCK);
						break;
					}
					case LITERAL_do:
					{
						match(LITERAL_do);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				catch (RecognitionException pe) {
					synPredMatched6051 = false;
				}
				rewind(_m6051);
inputState.guessing--;
			}
			if ( synPredMatched6051 ) {
				block=codeBlock();
				is_lhs_=empty_false();
				if ( inputState.guessing==0 ) {
					expr = new MethodCallExpression(scope(), line, base, method, null, block, base_must_be_class_or_module);
				}
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
				if ( inputState.guessing==0 ) {
					if (base != null) expr = new MethodCallExpression(scope(), line, base, method, null, block, base_must_be_class_or_module);
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			return expr;
		}
		
	public final SequenceExpression  methodInvocationArgumentWithoutParen(
		SequenceExpression args, boolean should_ignore_line_break
	) throws RecognitionException, TokenStreamException {
		SequenceExpression out_args;
		
		boolean seen_star_or_band = false; args = out_args = (args==null)?new SequenceExpression():args;
		
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			normalMethodInvocationArgument(args, should_ignore_line_break);
			{
			_loop6061:
			do {
				if (((LA(1)==COMMA))&&(!seen_star_or_band)) {
					match(COMMA);
					{
					if ((LA(1)==LINE_BREAK)) {
						match(LINE_BREAK);
					}
					else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					{
					boolean synPredMatched6060 = false;
					if (((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH))) {
						int _m6060 = mark();
						synPredMatched6060 = true;
						inputState.guessing++;
						try {
							{
							switch ( LA(1)) {
							case REST_ARG_PREFIX:
							{
								match(REST_ARG_PREFIX);
								break;
							}
							case BLOCK_ARG_PREFIX:
							{
								match(BLOCK_ARG_PREFIX);
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
						}
						catch (RecognitionException pe) {
							synPredMatched6060 = false;
						}
						rewind(_m6060);
inputState.guessing--;
					}
					if ( synPredMatched6060 ) {
						seen_star_or_band=empty_true();
					}
					else if ((LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==BNOT||LA(1)==NOT||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
						normalMethodInvocationArgument(args, should_ignore_line_break);
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
				}
				else {
					break _loop6061;
				}
				
			} while (true);
			}
			{
			if (((LA(1)==REST_ARG_PREFIX))&&(seen_star_or_band)) {
				restMethodInvocationArgument(args);
			}
			else if (((LA(1)==BLOCK_ARG_PREFIX))&&(seen_star_or_band)) {
				blockMethodInvocationArgument(args);
			}
			else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			break;
		}
		case REST_ARG_PREFIX:
		{
			restMethodInvocationArgument(args);
			break;
		}
		case BLOCK_ARG_PREFIX:
		{
			blockMethodInvocationArgument(args);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return out_args;
	}
	
	public final Expression  methodCallCanBeCommand(
		Expression base, boolean base_must_be_class_or_module
	) throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		SequenceExpression args=null; String method=null; is_lhs_ = true; seen_rparen_=false;
		
		expr=primaryExpressionCanBeCommand();
		if ( inputState.guessing==0 ) {
			
								MethodDenominator denom = (MethodDenominator)expr;
								method = denom.getMethodName();
							
		}
		expr=restOfMethodCall(base, expr, method, base_must_be_class_or_module);
		return expr;
	}
	
	public final Expression  variableCanBeCommand() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		can_be_command_ = true;
		
		switch ( LA(1)) {
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		{
			expr=predefinedValue();
			is_lhs_=empty_false();
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		{
			expr=normalVariable();
			is_lhs_=empty_true();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final SequenceExpression  methodInvocationArgumentWithParen() throws RecognitionException, TokenStreamException {
		SequenceExpression args=null;
		
		
		match(LPAREN);
		{
		switch ( LA(1)) {
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case BLOCK_ARG_PREFIX:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			args=methodInvocationArgumentWithoutParen(null, true);
			break;
		}
		case LINE_BREAK:
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RPAREN);
		return args;
	}
	
	public final void normalMethodInvocationArgument(
		SequenceExpression args, boolean should_ignore_line_break
	) throws RecognitionException, TokenStreamException {
		
		Expression expr, expr2=null;
		
		expr=expression();
		{
		if ((LA(1)==ASSOC)) {
			match(ASSOC);
			expr2=expression();
		}
		else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			args.addAssoc(expr, expr2);
		}
		{
		if (((LA(1)==LINE_BREAK))&&(should_ignore_line_break)) {
			match(LINE_BREAK);
		}
		else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
	}
	
	public final void restMethodInvocationArgument(
		SequenceExpression args
	) throws RecognitionException, TokenStreamException {
		
		Expression expr;
		
		match(REST_ARG_PREFIX);
		expr=expression();
		if ( inputState.guessing==0 ) {
			args.setRestArg(expr);
		}
		{
		if ((LA(1)==COMMA)) {
			match(COMMA);
			blockMethodInvocationArgument(args);
		}
		else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==RBRACK||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==ASSIGN||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_elsif||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==LOGICAL_AND||LA(1)==LITERAL_and||LA(1)==LITERAL_or||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
	}
	
	public final void blockMethodInvocationArgument(
		SequenceExpression args
	) throws RecognitionException, TokenStreamException {
		
		Expression expr;
		
		match(BLOCK_ARG_PREFIX);
		expr=expression();
		if ( inputState.guessing==0 ) {
			args.setBlockArg(expr);
		}
	}
	
	protected final Expression  predefinedValue() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  file = null;
		Token  line = null;
		
		switch ( LA(1)) {
		case LITERAL_nil:
		{
			match(LITERAL_nil);
			if ( inputState.guessing==0 ) {
				expr=NilExpression.instance;
			}
			break;
		}
		case LITERAL_self:
		{
			match(LITERAL_self);
			if ( inputState.guessing==0 ) {
				expr= SelfExpression.instance;
			}
			break;
		}
		case LITERAL_true:
		{
			match(LITERAL_true);
			if ( inputState.guessing==0 ) {
				expr=TrueExpression.instance;
			}
			break;
		}
		case LITERAL_false:
		{
			match(LITERAL_false);
			if ( inputState.guessing==0 ) {
				expr=FalseExpression.instance;
			}
			break;
		}
		case LITERAL___FILE__:
		{
			file = LT(1);
			match(LITERAL___FILE__);
			if ( inputState.guessing==0 ) {
				expr=new FileExpression(getFilename());
			}
			break;
		}
		case LITERAL___LINE__:
		{
			line = LT(1);
			match(LITERAL___LINE__);
			if ( inputState.guessing==0 ) {
				expr=new LineExpression(line.getLine());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  variable() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		can_be_command_ = false;
		
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		{
			expr=variableCanBeCommand();
			break;
		}
		case GLOBAL_VARIBLE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		{
			expr=syntaxedVariable();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  syntaxedVariable() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  id6 = null;
		Token  id7 = null;
		Token  id8 = null;
		can_be_command_ = false;
		
		switch ( LA(1)) {
		case INSTANCE_VARIBLE:
		{
			id6 = LT(1);
			match(INSTANCE_VARIBLE);
			if ( inputState.guessing==0 ) {
				expr=new InstanceVarExpression(id6.getText());
			}
			break;
		}
		case GLOBAL_VARIBLE:
		{
			id7 = LT(1);
			match(GLOBAL_VARIBLE);
			if ( inputState.guessing==0 ) {
				expr=new GlobalVarExpression(id7.getText());
			}
			break;
		}
		case CLASS_VARIBLE:
		{
			id8 = LT(1);
			match(CLASS_VARIBLE);
			if ( inputState.guessing==0 ) {
				expr=new ClassVarExpression(id8.getText());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  normalVariable() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  id1 = null;
		Token  id4 = null;
		Token  id5 = null;
		
		switch ( LA(1)) {
		case FUNCTION:
		{
			id1 = LT(1);
			match(FUNCTION);
			if ( inputState.guessing==0 ) {
				expr=new FunctionExpression(scope(), false, id1.getText());
			}
			break;
		}
		case IDENTIFIER:
		{
			id4 = LT(1);
			match(IDENTIFIER);
			if ( inputState.guessing==0 ) {
				expr=new IdentifierExpression(scope(), id4.getText());
			}
			break;
		}
		case CONSTANT:
		{
			id5 = LT(1);
			match(CONSTANT);
			if ( inputState.guessing==0 ) {
				expr=new ConstVarExpression(id5.getText());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	protected final Expression  expression_substituation() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		
		{
		switch ( LA(1)) {
		case LCURLY_BLOCK:
		{
			match(LCURLY_BLOCK);
			expr=compoundStatement();
			match(RCURLY);
			break;
		}
		case GLOBAL_VARIBLE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		{
			expr=syntaxedVariable();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			tell_lexer_we_have_finished_parsing_string_expression_substituation();
		}
		return expr;
	}
	
	public final Expression  w_array() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  double_quote_string_value = null;
		Token  single_quote_string_value = null;
		Token  s1 = null;
		Token  s3 = null;
		Token  s2 = null;
		StringConcatExpression exc = null; Expression exp2;
		
		{
		switch ( LA(1)) {
		case DOUBLE_QUOTE_WARRAY:
		{
			double_quote_string_value = LT(1);
			match(DOUBLE_QUOTE_WARRAY);
			if ( inputState.guessing==0 ) {
				expr = new ConstantStringExpression(double_quote_string_value.getText(), ConstantStringExpression.DOUBLE_QUOTE);
			}
			break;
		}
		case SINGLE_QUOTE_WARRAY:
		{
			single_quote_string_value = LT(1);
			match(SINGLE_QUOTE_WARRAY);
			if ( inputState.guessing==0 ) {
				expr = new ConstantStringExpression(single_quote_string_value.getText(), ConstantStringExpression.SINGLE_QUOTE);
			}
			break;
		}
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			s1 = LT(1);
			match(WARRAY_BEFORE_EXPRESSION_SUBSTITUTION);
			exp2=expression_substituation();
			if ( inputState.guessing==0 ) {
				expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.DOUBLE_QUOTE); exc.add(s1.getText()); exc.add(exp2);
			}
			{
			_loop6082:
			do {
				if ((LA(1)==STRING_BETWEEN_EXPRESSION_SUBSTITUTION)) {
					s3 = LT(1);
					match(STRING_BETWEEN_EXPRESSION_SUBSTITUTION);
					exp2=expression_substituation();
					if ( inputState.guessing==0 ) {
						exc.add(s3.getText()); exc.add(exp2);
					}
				}
				else {
					break _loop6082;
				}
				
			} while (true);
			}
			s2 = LT(1);
			match(STRING_AFTER_EXPRESSION_SUBSTITUTION);
			if ( inputState.guessing==0 ) {
				exc.add(s2.getText());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			expr = new WArrayExpression(expr);
		}
		return expr;
	}
	
	public final Expression  regex() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  reg = null;
		Token  s1 = null;
		Token  s3 = null;
		Token  s2 = null;
		StringConcatExpression exc = null; Expression exp2;
		
		{
		switch ( LA(1)) {
		case REGEX:
		{
			reg = LT(1);
			match(REGEX);
			if ( inputState.guessing==0 ) {
				expr = new ConstantStringExpression(reg.getText(), ConstantStringExpression.REGEX);
			}
			break;
		}
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			s1 = LT(1);
			match(REGEX_BEFORE_EXPRESSION_SUBSTITUTION);
			exp2=expression_substituation();
			if ( inputState.guessing==0 ) {
				expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.REGEX); exc.add(s1.getText()); exc.add(exp2);
			}
			if ( inputState.guessing==0 ) {
				tell_lexer_we_have_finished_parsing_regex_expression_substituation();
			}
			{
			_loop6086:
			do {
				if ((LA(1)==STRING_BETWEEN_EXPRESSION_SUBSTITUTION)) {
					s3 = LT(1);
					match(STRING_BETWEEN_EXPRESSION_SUBSTITUTION);
					exp2=expression_substituation();
					if ( inputState.guessing==0 ) {
						exc.add(s3.getText()); exc.add(exp2);
					}
					if ( inputState.guessing==0 ) {
						tell_lexer_we_have_finished_parsing_regex_expression_substituation();
					}
				}
				else {
					break _loop6086;
				}
				
			} while (true);
			}
			s2 = LT(1);
			match(STRING_AFTER_EXPRESSION_SUBSTITUTION);
			if ( inputState.guessing==0 ) {
				exc.add(s2.getText());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			expr = new RegularExpression(expr, get_regexp_options());
		}
		return expr;
	}
	
	public final Expression  command_output() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  cmd = null;
		Token  s1 = null;
		Token  s3 = null;
		Token  s2 = null;
		StringConcatExpression exc = null; Expression exp2;
		
		{
		switch ( LA(1)) {
		case COMMAND_OUTPUT:
		{
			cmd = LT(1);
			match(COMMAND_OUTPUT);
			if ( inputState.guessing==0 ) {
				expr = new ConstantStringExpression(cmd.getText(), ConstantStringExpression.COMMAND_OUTPUT);
			}
			break;
		}
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			s1 = LT(1);
			match(COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION);
			exp2=expression_substituation();
			if ( inputState.guessing==0 ) {
				expr = exc = new StringConcatExpression(s1.getLine(), ConstantStringExpression.COMMAND_OUTPUT); exc.add(s1.getText()); exc.add(exp2);
			}
			{
			_loop6090:
			do {
				if ((LA(1)==STRING_BETWEEN_EXPRESSION_SUBSTITUTION)) {
					s3 = LT(1);
					match(STRING_BETWEEN_EXPRESSION_SUBSTITUTION);
					exp2=expression_substituation();
					if ( inputState.guessing==0 ) {
						exc.add(s3.getText()); exc.add(exp2);
					}
				}
				else {
					break _loop6090;
				}
				
			} while (true);
			}
			s2 = LT(1);
			match(STRING_AFTER_EXPRESSION_SUBSTITUTION);
			if ( inputState.guessing==0 ) {
				exc.add(s2.getText());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			expr = new CommandOutputExpression(expr);
		}
		return expr;
	}
	
	public final Expression  literal() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Expression expr2; StringConcatExpression exc = null;
		
		switch ( LA(1)) {
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			expr=regex();
			break;
		}
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			expr=string();
			{
			_loop6093:
			do {
				if (((LA(1) >= DOUBLE_QUOTE_STRING && LA(1) <= STRING_BEFORE_EXPRESSION_SUBSTITUTION))) {
					expr2=string();
					if ( inputState.guessing==0 ) {
						
											if (exc == null) { exc = new StringConcatExpression(expr.line(), 0, expr); expr = exc; }
											exc.add(expr2);
										
					}
				}
				else {
					break _loop6093;
				}
				
			} while (true);
			}
			break;
		}
		case HERE_DOC_BEGIN:
		{
			match(HERE_DOC_BEGIN);
			if ( inputState.guessing==0 ) {
				expr=nextHereDoc();
			}
			break;
		}
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			expr=command_output();
			break;
		}
		case COLON_WITH_NO_FOLLOWING_SPACE:
		{
			expr=symbol(true);
			break;
		}
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		{
			expr=w_array();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final Expression  numeric() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Token  integer_value = null;
		Token  hex_value = null;
		Token  binary_value = null;
		Token  octal_value = null;
		Token  float_value = null;
		Token  ascii_value = null;
		
		switch ( LA(1)) {
		case INTEGER:
		{
			integer_value = LT(1);
			match(INTEGER);
			if ( inputState.guessing==0 ) {
				expr = new IntegerExpression(integer_value.getText(), 10);
			}
			break;
		}
		case HEX:
		{
			hex_value = LT(1);
			match(HEX);
			if ( inputState.guessing==0 ) {
				expr = new IntegerExpression(hex_value.getText(), 16);
			}
			break;
		}
		case BINARY:
		{
			binary_value = LT(1);
			match(BINARY);
			if ( inputState.guessing==0 ) {
				expr = new IntegerExpression(binary_value.getText(), 2);
			}
			break;
		}
		case OCTAL:
		{
			octal_value = LT(1);
			match(OCTAL);
			if ( inputState.guessing==0 ) {
				expr = new IntegerExpression(octal_value.getText(), 8);
			}
			break;
		}
		case FLOAT:
		{
			float_value = LT(1);
			match(FLOAT);
			if ( inputState.guessing==0 ) {
				expr = new FloatExpression(float_value.getText());
			}
			break;
		}
		case ASCII_VALUE:
		{
			ascii_value = LT(1);
			match(ASCII_VALUE);
			if ( inputState.guessing==0 ) {
				expr=new IntegerExpression(String.valueOf((int)ascii_value.getText().charAt(1)), 10);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return expr;
	}
	
	public final HashExpression  hashExpression() throws RecognitionException, TokenStreamException {
		HashExpression expr = new HashExpression();
		
		
		match(LCURLY_HASH);
		{
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			keyValuePair(expr);
			{
			_loop6116:
			do {
				if (((LA(1)==COMMA))&&(RCURLY != LA(2))) {
					match(COMMA);
					keyValuePair(expr);
				}
				else {
					break _loop6116;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case RCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RCURLY);
		return expr;
	}
	
	public final Expression  ifExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Expression test = null, thenExpr=null, elseExpr=null, elsifExpr = null; int line=line(); 
		java.util.Stack<IfThenElseExpression> elses = new java.util.Stack<IfThenElseExpression>(); 
		
		
		match(LITERAL_if);
		test=expression();
		thenOrTermialOrColon();
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			thenExpr=compoundStatement();
			break;
		}
		case LITERAL_end:
		case LITERAL_else:
		case LITERAL_elsif:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop6136:
		do {
			if ((LA(1)==LITERAL_elsif)) {
				match(LITERAL_elsif);
				{
				switch ( LA(1)) {
				case IDENTIFIER:
				case CONSTANT:
				case FUNCTION:
				case GLOBAL_VARIBLE:
				case LPAREN:
				case COLON_WITH_NO_FOLLOWING_SPACE:
				case INSTANCE_VARIBLE:
				case CLASS_VARIBLE:
				case LBRACK:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_next:
				case LITERAL_retry:
				case LITERAL_redo:
				case LITERAL_nil:
				case LITERAL_self:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL___FILE__:
				case LITERAL___LINE__:
				case DOUBLE_QUOTE_STRING:
				case SINGLE_QUOTE_STRING:
				case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
				case DOUBLE_QUOTE_WARRAY:
				case SINGLE_QUOTE_WARRAY:
				case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
				case REGEX:
				case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
				case COMMAND_OUTPUT:
				case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
				case HERE_DOC_BEGIN:
				case INTEGER:
				case HEX:
				case BINARY:
				case OCTAL:
				case FLOAT:
				case ASCII_VALUE:
				case LEADING_COLON2:
				case LITERAL_super:
				case LITERAL_yield:
				case LCURLY_HASH:
				case LITERAL_begin:
				case LITERAL_if:
				case LITERAL_unless:
				case LITERAL_case:
				case BNOT:
				case NOT:
				case LITERAL_def:
				case 108:
				case LITERAL_module:
				case LITERAL_until:
				case LITERAL_for:
				case LITERAL_while:
				case LITERAL_class:
				case LITERAL_not:
				case UNARY_PLUS:
				case UNARY_MINUS:
				{
					expr=expression();
					break;
				}
				case SEMI:
				case LINE_BREAK:
				case LITERAL_then:
				case COLON:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				thenOrTermialOrColon();
				{
				switch ( LA(1)) {
				case SEMI:
				case LINE_BREAK:
				case REST_ARG_PREFIX:
				case IDENTIFIER:
				case CONSTANT:
				case FUNCTION:
				case GLOBAL_VARIBLE:
				case LPAREN:
				case COLON_WITH_NO_FOLLOWING_SPACE:
				case INSTANCE_VARIBLE:
				case CLASS_VARIBLE:
				case LBRACK:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_next:
				case LITERAL_retry:
				case LITERAL_redo:
				case LITERAL_nil:
				case LITERAL_self:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL___FILE__:
				case LITERAL___LINE__:
				case DOUBLE_QUOTE_STRING:
				case SINGLE_QUOTE_STRING:
				case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
				case DOUBLE_QUOTE_WARRAY:
				case SINGLE_QUOTE_WARRAY:
				case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
				case REGEX:
				case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
				case COMMAND_OUTPUT:
				case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
				case HERE_DOC_BEGIN:
				case INTEGER:
				case HEX:
				case BINARY:
				case OCTAL:
				case FLOAT:
				case ASCII_VALUE:
				case LEADING_COLON2:
				case LITERAL_super:
				case LITERAL_yield:
				case LCURLY_HASH:
				case LITERAL_begin:
				case LITERAL_if:
				case LITERAL_unless:
				case LITERAL_case:
				case BNOT:
				case NOT:
				case LITERAL_BEGIN:
				case LITERAL_def:
				case 108:
				case LITERAL_END:
				case LITERAL_module:
				case LITERAL_until:
				case LITERAL_for:
				case LITERAL_while:
				case LITERAL_alias:
				case LITERAL_class:
				case LITERAL_not:
				case LITERAL_undef:
				case UNARY_PLUS:
				case UNARY_MINUS:
				{
					elsifExpr=compoundStatement();
					break;
				}
				case LITERAL_end:
				case LITERAL_else:
				case LITERAL_elsif:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					elses.push(new IfThenElseExpression(line, expr, elsifExpr, NilExpression.instance));
				}
			}
			else {
				break _loop6136;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case LITERAL_else:
		{
			match(LITERAL_else);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				elseExpr=compoundStatement();
				break;
			}
			case LITERAL_end:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
							while (!elses.isEmpty()) {
								IfThenElseExpression ite = elses.pop();
								ite.setElseExpression(elseExpr);
								elseExpr = ite;
							}
							
							expr = new IfThenElseExpression(line, test, thenExpr, elseExpr); 
						
		}
		match(LITERAL_end);
		return expr;
	}
	
	public final Expression  unlessExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Expression test = null; Expression unlessExpr = null; Expression elseExpr = null; int line=line();
		
		match(LITERAL_unless);
		test=expression();
		thenOrTermialOrColon();
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			unlessExpr=compoundStatement();
			break;
		}
		case LITERAL_end:
		case LITERAL_else:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_else:
		{
			match(LITERAL_else);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				elseExpr=compoundStatement();
				break;
			}
			case LITERAL_end:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			expr = new IfThenElseExpression(line, test, elseExpr, unlessExpr);
		}
		return expr;
	}
	
	public final Expression  whileExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Expression test=null, body=null; int line=line();
		
		keyword_while();
		test=expression();
		doOrTermialOrColon();
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			body=compoundStatement();
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			expr = new WhileExpression(line,test,body, false);
		}
		return expr;
	}
	
	public final Expression  untilExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		Expression test=null, body=null; int line=line();
		
		keyword_until();
		test=expression();
		doOrTermialOrColon();
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			body=compoundStatement();
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			expr = new WhileExpression(line, new UnaryExpression("!", test),body, false);
		}
		return expr;
	}
	
	public final Expression  caseExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		int line = line(); 
			Expression test = null; 
			java.util.List<SequenceExpression> conds = new java.util.ArrayList<SequenceExpression>();
			java.util.List<Expression> codes = new java.util.ArrayList<Expression>();
			SequenceExpression expr1 = null;
			Expression expr2 = null;
			Expression elseExpr = null;
		
		
		match(LITERAL_case);
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			test=compoundStatement();
			break;
		}
		case LITERAL_when:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		int _cnt6147=0;
		_loop6147:
		do {
			if ((LA(1)==LITERAL_when)) {
				keyword_when();
				expr1=mrhs();
				thenOrTermialOrColon();
				{
				switch ( LA(1)) {
				case SEMI:
				case LINE_BREAK:
				case REST_ARG_PREFIX:
				case IDENTIFIER:
				case CONSTANT:
				case FUNCTION:
				case GLOBAL_VARIBLE:
				case LPAREN:
				case COLON_WITH_NO_FOLLOWING_SPACE:
				case INSTANCE_VARIBLE:
				case CLASS_VARIBLE:
				case LBRACK:
				case LITERAL_return:
				case LITERAL_break:
				case LITERAL_next:
				case LITERAL_retry:
				case LITERAL_redo:
				case LITERAL_nil:
				case LITERAL_self:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL___FILE__:
				case LITERAL___LINE__:
				case DOUBLE_QUOTE_STRING:
				case SINGLE_QUOTE_STRING:
				case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
				case DOUBLE_QUOTE_WARRAY:
				case SINGLE_QUOTE_WARRAY:
				case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
				case REGEX:
				case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
				case COMMAND_OUTPUT:
				case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
				case HERE_DOC_BEGIN:
				case INTEGER:
				case HEX:
				case BINARY:
				case OCTAL:
				case FLOAT:
				case ASCII_VALUE:
				case LEADING_COLON2:
				case LITERAL_super:
				case LITERAL_yield:
				case LCURLY_HASH:
				case LITERAL_begin:
				case LITERAL_if:
				case LITERAL_unless:
				case LITERAL_case:
				case BNOT:
				case NOT:
				case LITERAL_BEGIN:
				case LITERAL_def:
				case 108:
				case LITERAL_END:
				case LITERAL_module:
				case LITERAL_until:
				case LITERAL_for:
				case LITERAL_while:
				case LITERAL_alias:
				case LITERAL_class:
				case LITERAL_not:
				case LITERAL_undef:
				case UNARY_PLUS:
				case UNARY_MINUS:
				{
					expr2=compoundStatement();
					if ( inputState.guessing==0 ) {
						conds.add(expr1); codes.add(expr2);
					}
					break;
				}
				case LITERAL_end:
				case LITERAL_else:
				case LITERAL_when:
				{
					if ( inputState.guessing==0 ) {
						conds.add(expr1); codes.add(NilExpression.instance);
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				if ( _cnt6147>=1 ) { break _loop6147; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt6147++;
		} while (true);
		}
		{
		switch ( LA(1)) {
		case LITERAL_else:
		{
			match(LITERAL_else);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				elseExpr=compoundStatement();
				break;
			}
			case LITERAL_end:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			expr = new CaseExpression(line, test, conds, codes, elseExpr);
		}
		match(LITERAL_end);
		return expr;
	}
	
	public final Expression  forExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		String op; SequenceExpression vars = null; Expression values = null; Expression body=null; BlockCode block = null; RubyCode context = scope(); int line=line();
		
		keyword_for();
		if ( inputState.guessing==0 ) {
			scope(block = new BlockCode(line(), context, getFilename()));
		}
		block_vars(block);
		keyword_in();
		if ( inputState.guessing==0 ) {
			scope(context); /*pop context*/
		}
		values=expression();
		doOrTermialOrColon();
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			if ( inputState.guessing==0 ) {
				scope(block);
			}
			body=compoundStatement();
			if ( inputState.guessing==0 ) {
				scope(context); block.setBody(body);
			}
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			
							expr = new MethodCallExpression(scope(), line, values, "each", null, block, false);
						
		}
		return expr;
	}
	
	public final Expression  exceptionHandlingExpression() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		
		match(LITERAL_begin);
		expr=bodyStatement();
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			expr = new BeginEndExpression(expr);
		}
		return expr;
	}
	
	public final Expression  moduleDefinition() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		ModuleCode code = null; RubyCode context = scope(); Expression body=null; String name; int line=line();
		
		keyword_module();
		name=moduleName();
		{
		if ((LA(1)==SEMI||LA(1)==LINE_BREAK)) {
			terminal();
		}
		else if ((LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==LITERAL_end||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==BNOT||LA(1)==NOT||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			scope(code=new ModuleCode(line, null, getFilename()));
		}
		body=bodyStatement();
		if ( inputState.guessing==0 ) {
			code.setBody(body);
		}
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			
							scope(context);
							expr=new ModuleExpression(name, code); 
						
		}
		return expr;
	}
	
	public final Expression  classDefinition() throws RecognitionException, TokenStreamException {
		Expression expr=null;
		
		ClassCode code = null; RubyCode context = scope(); Expression body=null; String name; Expression superExpression=null; int line=line();
		
		keyword_class();
		{
		switch ( LA(1)) {
		case CONSTANT:
		case FUNCTION:
		case LEADING_COLON2:
		{
			name=className();
			{
			switch ( LA(1)) {
			case LESS_THAN:
			{
				match(LESS_THAN);
				superExpression=expression();
				break;
			}
			case SEMI:
			case LINE_BREAK:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			terminal();
			if ( inputState.guessing==0 ) {
				scope(code = new ClassCode(line, null, getFilename()));
			}
			body=bodyStatement();
			match(LITERAL_end);
			if ( inputState.guessing==0 ) {
				
									code.setBody(body);
									scope(context); 
								  	expr = new ClassExpression(name, superExpression, code); 
								
			}
			break;
		}
		case LEFT_SHIFT:
		{
			match(LEFT_SHIFT);
			superExpression=expression();
			terminal();
			if ( inputState.guessing==0 ) {
				scope(code = new ClassCode(line, context, getFilename()));
			}
			body=bodyStatement();
			match(LITERAL_end);
			if ( inputState.guessing==0 ) {
				
									code.setBody(body);
									scope(context); 
								  	expr = new SingletonClassExpression(superExpression, code); 
								
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return expr;
	}
	
	public final Expression  methodDefinition() throws RecognitionException, TokenStreamException {
		Expression defexpr=null;
		
		Token  tok = null;
		Expression expr = null; String name = null; boolean colon2=false; MethodCode code = null; RubyCode context = scope(); int line=line();
		
		keyword_def();
		{
		switch ( LA(1)) {
		case BOR:
		case LOGICAL_OR:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		{
			name=operatorAsMethodname();
			break;
		}
		case LITERAL_in:
		case LITERAL_end:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			name=keywordAsMethodName();
			{
			switch ( LA(1)) {
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				if ( inputState.guessing==0 ) {
					name=name+"=";
				}
				break;
			}
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case FUNCTION:
			case LPAREN:
			case BLOCK_ARG_PREFIX:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case UNARY_PLUS_MINUS_METHOD_NAME:
		{
			tok = LT(1);
			match(UNARY_PLUS_MINUS_METHOD_NAME);
			if ( inputState.guessing==0 ) {
				name = tok.getText();
			}
			break;
		}
		case GLOBAL_VARIBLE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		{
			expr=syntaxedVariable();
			colon2=colon2orDot();
			name=methodNameSupplement();
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		{
			expr=variableCanBeCommand();
			{
			switch ( LA(1)) {
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				if ( inputState.guessing==0 ) {
					MethodDenominator den = (MethodDenominator)expr;  name = den.getMethodName()+"="; expr = null;
				}
				break;
			}
			case DOT:
			case COLON2:
			{
				colon2=colon2orDot();
				name=methodNameSupplement();
				break;
			}
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case FUNCTION:
			case LPAREN:
			case BLOCK_ARG_PREFIX:
			{
				if ( inputState.guessing==0 ) {
					MethodDenominator den = (MethodDenominator)expr;  name = den.getMethodName(); expr = null;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
							scope(code = new MethodCode(line, null, expr, name, getFilename()));
						
		}
		methodDefinitionArgument(code);
		expr=bodyStatement();
		if ( inputState.guessing==0 ) {
			code.setBody(expr);
		}
		match(LITERAL_end);
		if ( inputState.guessing==0 ) {
			scope(context); defexpr = new DefExpression(line, name, code);
		}
		return defexpr;
	}
	
	public final ArrayExpression  arrayExpression() throws RecognitionException, TokenStreamException {
		ArrayExpression expr = null;
		
		SequenceExpression args = null;
		
		match(LBRACK);
		{
		switch ( LA(1)) {
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			args=arrayReferenceArgument();
			break;
		}
		case RBRACK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RBRACK);
		if ( inputState.guessing==0 ) {
			expr = new ArrayExpression(args);
		}
		return expr;
	}
	
	public final void keyword_defined() throws RecognitionException, TokenStreamException {
		
		
		match(108);
		{
		if ((LA(1)==LINE_BREAK)) {
			match(LINE_BREAK);
		}
		else if ((LA(1)==EOF||LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==IF_MODIFIER||LA(1)==UNLESS_MODIFIER||LA(1)==WHILE_MODIFIER||LA(1)==UNTIL_MODIFIER||LA(1)==RESCUE_MODIFIER||LA(1)==COMMA||LA(1)==LCURLY_BLOCK||LA(1)==RCURLY||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==ASSIGN_WITH_NO_LEADING_SPACE||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==RPAREN||LA(1)==BOR||LA(1)==LITERAL_in||LA(1)==LITERAL_end||LA(1)==LOGICAL_OR||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==DOT||LA(1)==COLON2||LA(1)==LBRACK_ARRAY_ACCESS||LA(1)==LBRACK||LA(1)==RBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_do||LA(1)==BLOCK_ARG_PREFIX||LA(1)==ASSOC||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==ASSIGN||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_elsif||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==LESS_THAN||LA(1)==LEFT_SHIFT||LA(1)==RIGHT_SHIFT||LA(1)==EQUAL||LA(1)==CASE_EQUAL||LA(1)==GREATER_THAN||LA(1)==GREATER_OR_EQUAL||LA(1)==LESS_OR_EQUAL||LA(1)==PLUS||LA(1)==MINUS||LA(1)==STAR||LA(1)==DIV||LA(1)==MOD||LA(1)==POWER||LA(1)==BAND||LA(1)==BXOR||LA(1)==MATCH||LA(1)==COMPARE||LA(1)==BNOT||LA(1)==LOGICAL_AND||LA(1)==NOT||LA(1)==LITERAL_and||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_or||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_then||LA(1)==LITERAL_when||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==COLON||LA(1)==DO_IN_CONDITION||LA(1)==QUESTION||LA(1)==PLUS_ASSIGN||LA(1)==MINUS_ASSIGN||LA(1)==STAR_ASSIGN||LA(1)==DIV_ASSIGN||LA(1)==MOD_ASSIGN||LA(1)==POWER_ASSIGN||LA(1)==BAND_ASSIGN||LA(1)==BXOR_ASSIGN||LA(1)==BOR_ASSIGN||LA(1)==LEFT_SHIFT_ASSIGN||LA(1)==RIGHT_SHIFT_ASSIGN||LA(1)==LOGICAL_AND_ASSIGN||LA(1)==LOGICAL_OR_ASSIGN||LA(1)==INCLUSIVE_RANGE||LA(1)==EXCLUSIVE_RANGE||LA(1)==NOT_EQUAL||LA(1)==NOT_MATCH||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
	}
	
	public final void keyValuePair(
		AssocHolder assoc
	) throws RecognitionException, TokenStreamException {
		
		Expression key = null, value = null;
		
		key=expression();
		{
		switch ( LA(1)) {
		case ASSOC:
		{
			match(ASSOC);
			value=expression();
			break;
		}
		case LINE_BREAK:
		case COMMA:
		case RCURLY:
		case RBRACK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			assoc.addAssoc(key,value);
		}
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case COMMA:
		case RCURLY:
		case RBRACK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final SequenceExpression  arrayAccess() throws RecognitionException, TokenStreamException {
		SequenceExpression args = null;
		
		
		match(LBRACK_ARRAY_ACCESS);
		{
		switch ( LA(1)) {
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			args=arrayReferenceArgument();
			break;
		}
		case RBRACK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RBRACK);
		return args;
	}
	
	protected final Expression  bodyStatement() throws RecognitionException, TokenStreamException {
		Expression expr=SelfExpression.instance;
		
		Expression body = null, elseStmt=null, ensureStmt=null;  
		RescueClause resc = null;
		java.util.List<RescueClause> rescues = new java.util.ArrayList<RescueClause>();
		SequenceExpression args = null;
		int line = 0;
		boolean hasExceptions = false;
		String name=null;
		
		
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			expr=compoundStatement();
			break;
		}
		case LITERAL_end:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop6121:
		do {
			if ((LA(1)==LITERAL_rescue)) {
				match(LITERAL_rescue);
				rescueBody(rescues);
				if ( inputState.guessing==0 ) {
					hasExceptions=true;
				}
			}
			else {
				break _loop6121;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case LITERAL_else:
		{
			match(LITERAL_else);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				elseStmt=compoundStatement();
				break;
			}
			case LITERAL_end:
			case LITERAL_ensure:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				hasExceptions=true;
			}
			break;
		}
		case LITERAL_end:
		case LITERAL_ensure:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_ensure:
		{
			match(LITERAL_ensure);
			{
			switch ( LA(1)) {
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				ensureStmt=compoundStatement();
				break;
			}
			case LITERAL_end:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				hasExceptions=true;
			}
			break;
		}
		case LITERAL_end:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
							if (hasExceptions) {
								expr = new RescueExpression(expr, rescues, elseStmt, ensureStmt); 
							}
						
		}
		return expr;
	}
	
	public final void rescueBody(
		java.util.List<RescueClause> rescues
	) throws RecognitionException, TokenStreamException {
		
		int line = line(); Expression body=NilExpression.instance; SequenceExpression args=new SequenceExpression(); String name = null;
		
		{
		switch ( LA(1)) {
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case BLOCK_ARG_PREFIX:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			args=methodInvocationArgumentWithoutParen(args,false);
			break;
		}
		case SEMI:
		case LINE_BREAK:
		case ASSOC:
		case LITERAL_then:
		case COLON:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case ASSOC:
		{
			match(ASSOC);
			name=idOrFun();
			if ( inputState.guessing==0 ) {
				scope().assignToLocal(name, false);
			}
			break;
		}
		case SEMI:
		case LINE_BREAK:
		case LITERAL_then:
		case COLON:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		thenOrTermialOrColon();
		{
		switch ( LA(1)) {
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			body=compoundStatement();
			break;
		}
		case LITERAL_end:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			rescues.add(new RescueClause(line, args, name, body));
		}
	}
	
	public final String  idOrFun() throws RecognitionException, TokenStreamException {
		String name=null;
		
		Token  id = null;
		Token  fu = null;
		
		switch ( LA(1)) {
		case IDENTIFIER:
		{
			id = LT(1);
			match(IDENTIFIER);
			if ( inputState.guessing==0 ) {
				return id.getText();
			}
			break;
		}
		case FUNCTION:
		{
			fu = LT(1);
			match(FUNCTION);
			if ( inputState.guessing==0 ) {
				return fu.getText();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return name;
	}
	
	public final void thenOrTermialOrColon() throws RecognitionException, TokenStreamException {
		
		
		switch ( LA(1)) {
		case LITERAL_then:
		{
			match(LITERAL_then);
			break;
		}
		case SEMI:
		case LINE_BREAK:
		{
			terminal();
			{
			switch ( LA(1)) {
			case LITERAL_then:
			{
				match(LITERAL_then);
				break;
			}
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case CONSTANT:
			case FUNCTION:
			case GLOBAL_VARIBLE:
			case LPAREN:
			case LITERAL_end:
			case COLON_WITH_NO_FOLLOWING_SPACE:
			case INSTANCE_VARIBLE:
			case CLASS_VARIBLE:
			case LBRACK:
			case LITERAL_return:
			case LITERAL_break:
			case LITERAL_next:
			case LITERAL_retry:
			case LITERAL_redo:
			case LITERAL_nil:
			case LITERAL_self:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL___FILE__:
			case LITERAL___LINE__:
			case DOUBLE_QUOTE_STRING:
			case SINGLE_QUOTE_STRING:
			case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
			case DOUBLE_QUOTE_WARRAY:
			case SINGLE_QUOTE_WARRAY:
			case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
			case REGEX:
			case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
			case COMMAND_OUTPUT:
			case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
			case HERE_DOC_BEGIN:
			case INTEGER:
			case HEX:
			case BINARY:
			case OCTAL:
			case FLOAT:
			case ASCII_VALUE:
			case LEADING_COLON2:
			case LITERAL_super:
			case LITERAL_yield:
			case LCURLY_HASH:
			case LITERAL_rescue:
			case LITERAL_else:
			case LITERAL_ensure:
			case LITERAL_begin:
			case LITERAL_if:
			case LITERAL_elsif:
			case LITERAL_unless:
			case LITERAL_case:
			case BNOT:
			case NOT:
			case LITERAL_BEGIN:
			case LITERAL_def:
			case 108:
			case LITERAL_END:
			case LITERAL_module:
			case LITERAL_until:
			case LITERAL_when:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_alias:
			case LITERAL_class:
			case LITERAL_not:
			case LITERAL_undef:
			case UNARY_PLUS:
			case UNARY_MINUS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case COLON:
		{
			match(COLON);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void keyword_when() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_when);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_for() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_for);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_in() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_in);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void doOrTermialOrColon() throws RecognitionException, TokenStreamException {
		
		
		switch ( LA(1)) {
		case DO_IN_CONDITION:
		{
			match(DO_IN_CONDITION);
			break;
		}
		case SEMI:
		case LINE_BREAK:
		{
			terminal();
			break;
		}
		case COLON:
		{
			match(COLON);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void keyword_while() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_while);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_until() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_until);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case LPAREN:
		case COLON_WITH_NO_FOLLOWING_SPACE:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case DOUBLE_QUOTE_STRING:
		case SINGLE_QUOTE_STRING:
		case STRING_BEFORE_EXPRESSION_SUBSTITUTION:
		case DOUBLE_QUOTE_WARRAY:
		case SINGLE_QUOTE_WARRAY:
		case WARRAY_BEFORE_EXPRESSION_SUBSTITUTION:
		case REGEX:
		case REGEX_BEFORE_EXPRESSION_SUBSTITUTION:
		case COMMAND_OUTPUT:
		case COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION:
		case HERE_DOC_BEGIN:
		case INTEGER:
		case HEX:
		case BINARY:
		case OCTAL:
		case FLOAT:
		case ASCII_VALUE:
		case LEADING_COLON2:
		case LITERAL_super:
		case LITERAL_yield:
		case LCURLY_HASH:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_unless:
		case LITERAL_case:
		case BNOT:
		case NOT:
		case LITERAL_def:
		case 108:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_class:
		case LITERAL_not:
		case UNARY_PLUS:
		case UNARY_MINUS:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_module() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_module);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case CONSTANT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final String  moduleName() throws RecognitionException, TokenStreamException {
		String name = null;
		
		Token  c = null;
		Token  f = null;
		StringBuilder sb = new StringBuilder();
		
		c = LT(1);
		match(CONSTANT);
		if ( inputState.guessing==0 ) {
			sb.append(c.getText());
		}
		{
		_loop6160:
		do {
			if ((LA(1)==COLON2)) {
				match(COLON2);
				f = LT(1);
				match(FUNCTION);
				if ( inputState.guessing==0 ) {
					sb.append("::"); sb.append(f.getText());
				}
			}
			else {
				break _loop6160;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			name = sb.toString();
		}
		return name;
	}
	
	public final void keyword_class() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_class);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case CONSTANT:
		case FUNCTION:
		case LEADING_COLON2:
		case LEFT_SHIFT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final String  className() throws RecognitionException, TokenStreamException {
		String name=null;
		
		Token  c = null;
		Token  f1 = null;
		Token  f2 = null;
		Token  f3 = null;
		StringBuilder sb = new StringBuilder();
		
		{
		switch ( LA(1)) {
		case CONSTANT:
		case FUNCTION:
		{
			{
			switch ( LA(1)) {
			case CONSTANT:
			{
				c = LT(1);
				match(CONSTANT);
				if ( inputState.guessing==0 ) {
					sb.append(c.getText());
				}
				break;
			}
			case FUNCTION:
			{
				f1 = LT(1);
				match(FUNCTION);
				if ( inputState.guessing==0 ) {
					sb.append(f1.getText());
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case LEADING_COLON2:
		{
			{
			match(LEADING_COLON2);
			f2 = LT(1);
			match(FUNCTION);
			if ( inputState.guessing==0 ) {
				sb.append("::").append(f2.getText());
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop6169:
		do {
			if ((LA(1)==COLON2)) {
				match(COLON2);
				f3 = LT(1);
				match(FUNCTION);
				if ( inputState.guessing==0 ) {
					sb.append("::").append(f3.getText());
				}
			}
			else {
				break _loop6169;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			name = sb.toString();
		}
		return name;
	}
	
	public final void keyword_def() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_def);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		case GLOBAL_VARIBLE:
		case BOR:
		case LITERAL_in:
		case LITERAL_end:
		case LOGICAL_OR:
		case INSTANCE_VARIBLE:
		case CLASS_VARIBLE:
		case UNARY_PLUS_MINUS_METHOD_NAME:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	protected final boolean  colon2orDot() throws RecognitionException, TokenStreamException {
		boolean isColon2 = false;
		
		
		switch ( LA(1)) {
		case COLON2:
		{
			match(COLON2);
			isColon2=empty_true();
			break;
		}
		case DOT:
		{
			match(DOT);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return isColon2;
	}
	
	protected final String  methodNameSupplement() throws RecognitionException, TokenStreamException {
		String name=null;
		
		Token  id = null;
		Token  fu = null;
		Token  co = null;
		
		{
		switch ( LA(1)) {
		case IDENTIFIER:
		case CONSTANT:
		case FUNCTION:
		{
			{
			switch ( LA(1)) {
			case IDENTIFIER:
			{
				id = LT(1);
				match(IDENTIFIER);
				if ( inputState.guessing==0 ) {
					name = (id.getText());
				}
				break;
			}
			case FUNCTION:
			{
				fu = LT(1);
				match(FUNCTION);
				if ( inputState.guessing==0 ) {
					name = (fu.getText());
				}
				break;
			}
			case CONSTANT:
			{
				co = LT(1);
				match(CONSTANT);
				if ( inputState.guessing==0 ) {
					name = (co.getText());
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				if ( inputState.guessing==0 ) {
					name += '=';
				}
				break;
			}
			case SEMI:
			case LINE_BREAK:
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case FUNCTION:
			case LPAREN:
			case BLOCK_ARG_PREFIX:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case BOR:
		case LOGICAL_OR:
		case LBRACK_ARRAY_ACCESS:
		case LBRACK:
		case LESS_THAN:
		case LEFT_SHIFT:
		case RIGHT_SHIFT:
		case EQUAL:
		case CASE_EQUAL:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_OR_EQUAL:
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
		case MOD:
		case POWER:
		case BAND:
		case BXOR:
		case MATCH:
		case COMPARE:
		case BNOT:
		case SINGLE_QUOTE:
		case LOGICAL_AND:
		case NOT:
		{
			name=operatorAsMethodname();
			break;
		}
		case LITERAL_in:
		case LITERAL_end:
		case LITERAL_return:
		case LITERAL_break:
		case LITERAL_next:
		case LITERAL_retry:
		case LITERAL_redo:
		case LITERAL_do:
		case LITERAL_nil:
		case LITERAL_self:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL___FILE__:
		case LITERAL___LINE__:
		case LITERAL_super:
		case LITERAL_yield:
		case LITERAL_rescue:
		case LITERAL_else:
		case LITERAL_ensure:
		case LITERAL_begin:
		case LITERAL_if:
		case LITERAL_elsif:
		case LITERAL_unless:
		case LITERAL_case:
		case LITERAL_and:
		case LITERAL_BEGIN:
		case LITERAL_def:
		case 108:
		case LITERAL_END:
		case LITERAL_or:
		case LITERAL_module:
		case LITERAL_until:
		case LITERAL_then:
		case LITERAL_when:
		case LITERAL_for:
		case LITERAL_while:
		case LITERAL_alias:
		case LITERAL_class:
		case LITERAL_not:
		case LITERAL_undef:
		{
			name=keyword();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return name;
	}
	
	public final void methodDefinitionArgument(
		MethodCode def
	) throws RecognitionException, TokenStreamException {
		
		
		switch ( LA(1)) {
		case LPAREN:
		{
			match(LPAREN);
			{
			switch ( LA(1)) {
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case FUNCTION:
			case BLOCK_ARG_PREFIX:
			{
				methodDefinitionArgumentWithoutParen(def);
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				tell_lexer_we_have_finished_parsing_methodparameters();
			}
			{
			if ((LA(1)==SEMI||LA(1)==LINE_BREAK)) {
				terminal();
			}
			else if ((LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==REST_ARG_PREFIX||LA(1)==IDENTIFIER||LA(1)==CONSTANT||LA(1)==FUNCTION||LA(1)==GLOBAL_VARIBLE||LA(1)==LPAREN||LA(1)==LITERAL_end||LA(1)==COLON_WITH_NO_FOLLOWING_SPACE||LA(1)==INSTANCE_VARIBLE||LA(1)==CLASS_VARIBLE||LA(1)==LBRACK||LA(1)==LITERAL_return||LA(1)==LITERAL_break||LA(1)==LITERAL_next||LA(1)==LITERAL_retry||LA(1)==LITERAL_redo||LA(1)==LITERAL_nil||LA(1)==LITERAL_self||LA(1)==LITERAL_true||LA(1)==LITERAL_false||LA(1)==LITERAL___FILE__||LA(1)==LITERAL___LINE__||LA(1)==DOUBLE_QUOTE_STRING||LA(1)==SINGLE_QUOTE_STRING||LA(1)==STRING_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==DOUBLE_QUOTE_WARRAY||LA(1)==SINGLE_QUOTE_WARRAY||LA(1)==WARRAY_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==REGEX||LA(1)==REGEX_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==COMMAND_OUTPUT||LA(1)==COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION||LA(1)==HERE_DOC_BEGIN||LA(1)==INTEGER||LA(1)==HEX||LA(1)==BINARY||LA(1)==OCTAL||LA(1)==FLOAT||LA(1)==ASCII_VALUE||LA(1)==LEADING_COLON2||LA(1)==LITERAL_super||LA(1)==LITERAL_yield||LA(1)==LCURLY_HASH||LA(1)==LITERAL_rescue||LA(1)==LITERAL_else||LA(1)==LITERAL_ensure||LA(1)==LITERAL_begin||LA(1)==LITERAL_if||LA(1)==LITERAL_unless||LA(1)==LITERAL_case||LA(1)==BNOT||LA(1)==NOT||LA(1)==LITERAL_BEGIN||LA(1)==LITERAL_def||LA(1)==108||LA(1)==LITERAL_END||LA(1)==LITERAL_module||LA(1)==LITERAL_until||LA(1)==LITERAL_for||LA(1)==LITERAL_while||LA(1)==LITERAL_alias||LA(1)==LITERAL_class||LA(1)==LITERAL_not||LA(1)==LITERAL_undef||LA(1)==UNARY_PLUS||LA(1)==UNARY_MINUS)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			break;
		}
		case SEMI:
		case LINE_BREAK:
		case REST_ARG_PREFIX:
		case IDENTIFIER:
		case FUNCTION:
		case BLOCK_ARG_PREFIX:
		{
			{
			switch ( LA(1)) {
			case REST_ARG_PREFIX:
			case IDENTIFIER:
			case FUNCTION:
			case BLOCK_ARG_PREFIX:
			{
				methodDefinitionArgumentWithoutParen(def);
				break;
			}
			case SEMI:
			case LINE_BREAK:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			terminal();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void methodDefinitionArgumentWithoutParen(
		MethodCode def
	) throws RecognitionException, TokenStreamException {
		
		boolean seen_star_or_band = false;
		
		switch ( LA(1)) {
		case IDENTIFIER:
		case FUNCTION:
		{
			normalMethodDefinitionArgument(def);
			{
			_loop6194:
			do {
				if (((LA(1)==COMMA))&&(!seen_star_or_band)) {
					match(COMMA);
					{
					switch ( LA(1)) {
					case SEMI:
					case LINE_BREAK:
					case COMMA:
					case REST_ARG_PREFIX:
					case RPAREN:
					case BLOCK_ARG_PREFIX:
					{
						seen_star_or_band=empty_true();
						break;
					}
					case IDENTIFIER:
					case FUNCTION:
					{
						normalMethodDefinitionArgument(def);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					break _loop6194;
				}
				
			} while (true);
			}
			{
			if (((LA(1)==REST_ARG_PREFIX))&&(seen_star_or_band)) {
				restMethodDefinitionArgument(def);
			}
			else if (((LA(1)==BLOCK_ARG_PREFIX))&&(seen_star_or_band)) {
				blockMethodDefinitionArgument(def);
			}
			else if ((LA(1)==SEMI||LA(1)==LINE_BREAK||LA(1)==RPAREN)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			break;
		}
		case REST_ARG_PREFIX:
		{
			restMethodDefinitionArgument(def);
			break;
		}
		case BLOCK_ARG_PREFIX:
		{
			blockMethodDefinitionArgument(def);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void normalMethodDefinitionArgument(
		MethodCode def
	) throws RecognitionException, TokenStreamException {
		
		Expression expr = null; String name=null;
		
		name=idOrFun();
		if ( inputState.guessing==0 ) {
			/*todo*/
		}
		{
		switch ( LA(1)) {
		case ASSIGN_WITH_NO_LEADING_SPACE:
		case ASSIGN:
		{
			{
			switch ( LA(1)) {
			case ASSIGN:
			{
				match(ASSIGN);
				break;
			}
			case ASSIGN_WITH_NO_LEADING_SPACE:
			{
				match(ASSIGN_WITH_NO_LEADING_SPACE);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expr=expression();
			break;
		}
		case SEMI:
		case LINE_BREAK:
		case COMMA:
		case REST_ARG_PREFIX:
		case RPAREN:
		case BLOCK_ARG_PREFIX:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			def.addParameter(name, expr);
		}
	}
	
	public final void restMethodDefinitionArgument(
		MethodCode def
	) throws RecognitionException, TokenStreamException {
		
		String name=null;
		
		match(REST_ARG_PREFIX);
		{
		switch ( LA(1)) {
		case IDENTIFIER:
		case FUNCTION:
		{
			name=idOrFun();
			if ( inputState.guessing==0 ) {
				def.addRestParameter(name);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				blockMethodDefinitionArgument(def);
				break;
			}
			case SEMI:
			case LINE_BREAK:
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case SEMI:
		case LINE_BREAK:
		case RPAREN:
		{
			if ( inputState.guessing==0 ) {
				def.addRestParameter(null); /*anonymous rest-arg*/
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void blockMethodDefinitionArgument(
		MethodCode def
	) throws RecognitionException, TokenStreamException {
		
		String name = null;
		
		match(BLOCK_ARG_PREFIX);
		name=idOrFun();
		if ( inputState.guessing==0 ) {
			def.addBlockParameter(name);
		}
	}
	
	public final void keyword___FILE__() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL___FILE__);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword___LINE__() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL___LINE__);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_begin() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_begin);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_break() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_break);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_case() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_case);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_else() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_else);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_elsif() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_elsif);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_end() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_end);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_ensure() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_ensure);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_false() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_false);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_if() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_if);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_next() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_next);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_nil() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_nil);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_redo() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_redo);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_rescue() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_rescue);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_retry() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_retry);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_return() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_return);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_self() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_self);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_super() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_super);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_then() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_then);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_true() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_true);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_unless() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_unless);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void keyword_yield() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_yield);
		{
		switch ( LA(1)) {
		case LINE_BREAK:
		{
			match(LINE_BREAK);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"RPAREN_IN_METHOD_DEFINITION",
		"SYMBOL",
		"SEMI",
		"LINE_BREAK",
		"IF_MODIFIER",
		"UNLESS_MODIFIER",
		"WHILE_MODIFIER",
		"UNTIL_MODIFIER",
		"RESCUE_MODIFIER",
		"COMMA",
		"LCURLY_BLOCK",
		"RCURLY",
		"REST_ARG_PREFIX",
		"IDENTIFIER",
		"ASSIGN_WITH_NO_LEADING_SPACE",
		"CONSTANT",
		"FUNCTION",
		"GLOBAL_VARIBLE",
		"LPAREN",
		"RPAREN",
		"BOR",
		"\"in\"",
		"\"end\"",
		"LOGICAL_OR",
		"COLON_WITH_NO_FOLLOWING_SPACE",
		"INSTANCE_VARIBLE",
		"CLASS_VARIBLE",
		"UNARY_PLUS_MINUS_METHOD_NAME",
		"DOT",
		"COLON2",
		"LBRACK_ARRAY_ACCESS",
		"LBRACK",
		"RBRACK",
		"\"return\"",
		"\"break\"",
		"\"next\"",
		"\"retry\"",
		"\"redo\"",
		"\"do\"",
		"BLOCK_ARG_PREFIX",
		"ASSOC",
		"\"nil\"",
		"\"self\"",
		"\"true\"",
		"\"false\"",
		"\"__FILE__\"",
		"\"__LINE__\"",
		"DOUBLE_QUOTE_STRING",
		"SINGLE_QUOTE_STRING",
		"STRING_BEFORE_EXPRESSION_SUBSTITUTION",
		"STRING_BETWEEN_EXPRESSION_SUBSTITUTION",
		"STRING_AFTER_EXPRESSION_SUBSTITUTION",
		"DOUBLE_QUOTE_WARRAY",
		"SINGLE_QUOTE_WARRAY",
		"WARRAY_BEFORE_EXPRESSION_SUBSTITUTION",
		"REGEX",
		"REGEX_BEFORE_EXPRESSION_SUBSTITUTION",
		"COMMAND_OUTPUT",
		"COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION",
		"HERE_DOC_BEGIN",
		"INTEGER",
		"HEX",
		"BINARY",
		"OCTAL",
		"FLOAT",
		"ASCII_VALUE",
		"LEADING_COLON2",
		"\"super\"",
		"\"yield\"",
		"ASSIGN",
		"LCURLY_HASH",
		"\"rescue\"",
		"\"else\"",
		"\"ensure\"",
		"\"begin\"",
		"\"if\"",
		"\"elsif\"",
		"\"unless\"",
		"\"case\"",
		"LESS_THAN",
		"LEFT_SHIFT",
		"RIGHT_SHIFT",
		"EQUAL",
		"CASE_EQUAL",
		"GREATER_THAN",
		"GREATER_OR_EQUAL",
		"LESS_OR_EQUAL",
		"PLUS",
		"MINUS",
		"STAR",
		"DIV",
		"MOD",
		"POWER",
		"BAND",
		"BXOR",
		"MATCH",
		"COMPARE",
		"BNOT",
		"SINGLE_QUOTE",
		"LOGICAL_AND",
		"NOT",
		"\"and\"",
		"\"BEGIN\"",
		"\"def\"",
		"\"defined?\"",
		"\"END\"",
		"\"or\"",
		"\"module\"",
		"\"until\"",
		"\"then\"",
		"\"when\"",
		"\"for\"",
		"\"while\"",
		"\"alias\"",
		"\"class\"",
		"\"not\"",
		"\"undef\"",
		"COLON",
		"DO_IN_CONDITION",
		"QUESTION",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"POWER_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"LEFT_SHIFT_ASSIGN",
		"RIGHT_SHIFT_ASSIGN",
		"LOGICAL_AND_ASSIGN",
		"LOGICAL_OR_ASSIGN",
		"INCLUSIVE_RANGE",
		"EXCLUSIVE_RANGE",
		"NOT_EQUAL",
		"NOT_MATCH",
		"UNARY_PLUS",
		"UNARY_MINUS",
		"PURE_LINE_BREAK",
		"LINE_FEED",
		"REGEX_MODIFIER",
		"SPECIAL_STRING",
		"STRING_CHAR",
		"HERE_DOC_CONTENT",
		"HERE_DOC_DELIMITER",
		"RDOC",
		"ANYTHING_OTHER_THAN_LINE_FEED",
		"LINE",
		"ESC",
		"IDENTIFIER_CONSTANT_AND_KEYWORD",
		"UNDER_SCORE",
		"FLOAT_WITH_LEADING_DOT",
		"NON_ZERO_DECIMAL",
		"OCTAL_CONTENT",
		"HEX_CONTENT",
		"BINARY_CONTENT",
		"EXPONENT",
		"COMMENT",
		"WHITE_SPACE_CAHR",
		"WHITE_SPACE",
		"LINE_CONTINUATION",
		"END_OF_FILE"
	};
	
	
	}
