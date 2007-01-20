// $ANTLR : "ruby.g" -> "RubyLexerBase.java"$

package com.trifork.hotruby.parser;

import com.trifork.hotruby.ast.*;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class RubyLexerBase extends antlr.CharScanner implements RubyTokenTypes, TokenStream
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
public RubyLexerBase(InputStream in) {
	this(new ByteBuffer(in));
}
public RubyLexerBase(Reader in) {
	this(new CharBuffer(in));
}
public RubyLexerBase(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public RubyLexerBase(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
	literals.put(new ANTLRHashString("END", this), new Integer(109));
	literals.put(new ANTLRHashString("case", this), new Integer(82));
	literals.put(new ANTLRHashString("next", this), new Integer(39));
	literals.put(new ANTLRHashString("break", this), new Integer(38));
	literals.put(new ANTLRHashString("while", this), new Integer(116));
	literals.put(new ANTLRHashString("end", this), new Integer(26));
	literals.put(new ANTLRHashString("then", this), new Integer(113));
	literals.put(new ANTLRHashString("until", this), new Integer(112));
	literals.put(new ANTLRHashString("and", this), new Integer(105));
	literals.put(new ANTLRHashString("module", this), new Integer(111));
	literals.put(new ANTLRHashString("not", this), new Integer(119));
	literals.put(new ANTLRHashString("return", this), new Integer(37));
	literals.put(new ANTLRHashString("undef", this), new Integer(120));
	literals.put(new ANTLRHashString("def", this), new Integer(107));
	literals.put(new ANTLRHashString("retry", this), new Integer(40));
	literals.put(new ANTLRHashString("when", this), new Integer(114));
	literals.put(new ANTLRHashString("class", this), new Integer(118));
	literals.put(new ANTLRHashString("do", this), new Integer(42));
	literals.put(new ANTLRHashString("unless", this), new Integer(81));
	literals.put(new ANTLRHashString("super", this), new Integer(71));
	literals.put(new ANTLRHashString("yield", this), new Integer(72));
	literals.put(new ANTLRHashString("or", this), new Integer(110));
	literals.put(new ANTLRHashString("redo", this), new Integer(41));
	literals.put(new ANTLRHashString("if", this), new Integer(79));
	literals.put(new ANTLRHashString("__FILE__", this), new Integer(49));
	literals.put(new ANTLRHashString("BEGIN", this), new Integer(106));
	literals.put(new ANTLRHashString("for", this), new Integer(115));
	literals.put(new ANTLRHashString("alias", this), new Integer(117));
	literals.put(new ANTLRHashString("false", this), new Integer(48));
	literals.put(new ANTLRHashString("__LINE__", this), new Integer(50));
	literals.put(new ANTLRHashString("rescue", this), new Integer(75));
	literals.put(new ANTLRHashString("defined?", this), new Integer(108));
	literals.put(new ANTLRHashString("begin", this), new Integer(78));
	literals.put(new ANTLRHashString("else", this), new Integer(76));
	literals.put(new ANTLRHashString("in", this), new Integer(25));
	literals.put(new ANTLRHashString("self", this), new Integer(46));
	literals.put(new ANTLRHashString("elsif", this), new Integer(80));
	literals.put(new ANTLRHashString("ensure", this), new Integer(77));
	literals.put(new ANTLRHashString("true", this), new Integer(47));
	literals.put(new ANTLRHashString("nil", this), new Integer(45));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '(':
				{
					mLPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '[':
				{
					mLBRACK(true);
					theRetToken=_returnToken;
					break;
				}
				case ']':
				{
					mRBRACK(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLCURLY_HASH(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case '~':
				{
					mBNOT(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMI(true);
					theRetToken=_returnToken;
					break;
				}
				case '\n':  case '\r':
				{
					mLINE_BREAK(true);
					theRetToken=_returnToken;
					break;
				}
				case '`':
				{
					mCOMMAND_OUTPUT(true);
					theRetToken=_returnToken;
					break;
				}
				case '\'':
				{
					mSINGLE_QUOTE_STRING(true);
					theRetToken=_returnToken;
					break;
				}
				case '/':
				{
					mREGEX(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':
				{
					mDOUBLE_QUOTE_STRING(true);
					theRetToken=_returnToken;
					break;
				}
				case '%':
				{
					mSPECIAL_STRING(true);
					theRetToken=_returnToken;
					break;
				}
				case '_':  case 'a':  case 'b':  case 'c':
				case 'd':  case 'e':  case 'f':  case 'g':
				case 'h':  case 'i':  case 'j':  case 'k':
				case 'l':  case 'm':  case 'n':  case 'o':
				case 'p':  case 'q':  case 'r':  case 's':
				case 't':  case 'u':  case 'v':  case 'w':
				case 'x':  case 'y':  case 'z':
				{
					mIDENTIFIER(true);
					theRetToken=_returnToken;
					break;
				}
				case '$':
				{
					mGLOBAL_VARIBLE(true);
					theRetToken=_returnToken;
					break;
				}
				case 'A':  case 'B':  case 'C':  case 'D':
				case 'E':  case 'F':  case 'G':  case 'H':
				case 'I':  case 'J':  case 'K':  case 'L':
				case 'M':  case 'N':  case 'O':  case 'P':
				case 'Q':  case 'R':  case 'S':  case 'T':
				case 'U':  case 'V':  case 'W':  case 'X':
				case 'Y':  case 'Z':
				{
					mCONSTANT(true);
					theRetToken=_returnToken;
					break;
				}
				case '.':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':  case '?':
				{
					mINTEGER(true);
					theRetToken=_returnToken;
					break;
				}
				case '#':
				{
					mCOMMENT(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\u000b':  case '\u000c':  case ' ':
				{
					mWHITE_SPACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '\\':
				{
					mLINE_CONTINUATION(true);
					theRetToken=_returnToken;
					break;
				}
				case '\u0000':  case '\u0004':  case '\u001a':
				{
					mEND_OF_FILE(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='<') && (LA(2)=='=') && (LA(3)=='>')) {
						mCOMPARE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='=') && (LA(3)=='=')) {
						mCASE_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (LA(2)=='*') && (LA(3)=='=')) {
						mPOWER_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='=')) {
						mRIGHT_SHIFT_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='&') && (LA(3)=='=')) {
						mLOGICAL_AND_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='|') && (LA(3)=='=')) {
						mLOGICAL_OR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)==':') && (LA(2)==':')) {
						mCOLON2(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (LA(2)=='*') && (true)) {
						mPOWER(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='=')) {
						mGREATER_OR_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='=') && (true)) {
						mLESS_OR_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='=') && (true)) {
						mEQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (LA(2)=='=')) {
						mNOT_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='~')) {
						mMATCH(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (LA(2)=='~')) {
						mNOT_MATCH(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (true)) {
						mRIGHT_SHIFT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='>')) {
						mASSOC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='&') && (true)) {
						mLOGICAL_AND(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='|') && (true)) {
						mLOGICAL_OR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (LA(2)=='=')) {
						mPLUS_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (LA(2)=='=')) {
						mMINUS_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (LA(2)=='=')) {
						mSTAR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='=')) {
						mBAND_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='^') && (LA(2)=='=')) {
						mBXOR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='=')) {
						mBOR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if (((LA(1)=='+'||LA(1)=='-') && (LA(2)=='@'))&&(last_token_is_keyword_def_or_colon())) {
						mUNARY_PLUS_MINUS_METHOD_NAME(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='<')) {
						mHERE_DOC_BEGIN(true);
						theRetToken=_returnToken;
					}
					else if (((LA(1)=='=') && (LA(2)=='b'))&&(getColumn()==1)) {
						mRDOC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='@') && (_tokenSet_0.member(LA(2)))) {
						mINSTANCE_VARIBLE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='@') && (LA(2)=='@')) {
						mCLASS_VARIBLE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)==':') && (true)) {
						mCOLON(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (true)) {
						mNOT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (true)) {
						mPLUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (true)) {
						mMINUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (true)) {
						mSTAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (true)) {
						mLESS_THAN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (true)) {
						mGREATER_THAN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='^') && (true)) {
						mBXOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (true)) {
						mBOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (true)) {
						mBAND(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (true)) {
						mASSIGN(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		match('(');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		match(')');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LBRACK;
		int _saveIndex;
		
		match('[');
		if (expect_array_access()) {_ttype = LBRACK_ARRAY_ACCESS;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RBRACK;
		int _saveIndex;
		
		match(']');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLCURLY_HASH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LCURLY_HASH;
		int _saveIndex;
		
		match('{');
		if (!expect_hash()) {_ttype = LCURLY_BLOCK;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RCURLY;
		int _saveIndex;
		
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		_saveIndex=text.length();
		match(',');
		text.setLength(_saveIndex);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLON;
		int _saveIndex;
		
		match(':');
		if (!space_is_next())	{_ttype = COLON_WITH_NO_FOLLOWING_SPACE;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLON2(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLON2;
		int _saveIndex;
		
		match("::");
		if (expect_leading_colon2())	{_ttype = LEADING_COLON2;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT;
		int _saveIndex;
		
		match('!');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BNOT;
		int _saveIndex;
		
		match('~');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS;
		int _saveIndex;
		
		match('+');
		if (expect_unary())	{_ttype = UNARY_PLUS;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS;
		int _saveIndex;
		
		match('-');
		if (expect_unary())	{_ttype = UNARY_MINUS;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR;
		int _saveIndex;
		
		match('*');
		if (!expect_operator(1)) {_ttype = REST_ARG_PREFIX;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLESS_THAN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LESS_THAN;
		int _saveIndex;
		
		match('<');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGREATER_THAN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GREATER_THAN;
		int _saveIndex;
		
		match('>');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBXOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BXOR;
		int _saveIndex;
		
		match('^');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOR;
		int _saveIndex;
		
		match('|');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAND;
		int _saveIndex;
		
		match('&');
		if (!expect_operator(1)) {_ttype = BLOCK_ARG_PREFIX;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPOWER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = POWER;
		int _saveIndex;
		
		match("**");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMPARE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMPARE;
		int _saveIndex;
		
		match("<=>");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGREATER_OR_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GREATER_OR_EQUAL;
		int _saveIndex;
		
		match(">=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLESS_OR_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LESS_OR_EQUAL;
		int _saveIndex;
		
		match("<=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EQUAL;
		int _saveIndex;
		
		match("==");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCASE_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CASE_EQUAL;
		int _saveIndex;
		
		match("===");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNOT_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT_EQUAL;
		int _saveIndex;
		
		match("!=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMATCH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MATCH;
		int _saveIndex;
		
		match("=~");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNOT_MATCH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT_MATCH;
		int _saveIndex;
		
		match("!~");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRIGHT_SHIFT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHT_SHIFT;
		int _saveIndex;
		
		match(">>");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASSOC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASSOC;
		int _saveIndex;
		
		match("=>");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_AND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_AND;
		int _saveIndex;
		
		match("&&");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_OR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_OR;
		int _saveIndex;
		
		match("||");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASSIGN;
		int _saveIndex;
		
		match('=');
		if (!just_seen_whitespace()) {_ttype = ASSIGN_WITH_NO_LEADING_SPACE;}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS_ASSIGN;
		int _saveIndex;
		
		match("+=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS_ASSIGN;
		int _saveIndex;
		
		match("-=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR_ASSIGN;
		int _saveIndex;
		
		match("*=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPOWER_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = POWER_ASSIGN;
		int _saveIndex;
		
		match("**=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAND_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAND_ASSIGN;
		int _saveIndex;
		
		match("&=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBXOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BXOR_ASSIGN;
		int _saveIndex;
		
		match("^=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOR_ASSIGN;
		int _saveIndex;
		
		match("|=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRIGHT_SHIFT_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHT_SHIFT_ASSIGN;
		int _saveIndex;
		
		match(">>=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_AND_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_AND_ASSIGN;
		int _saveIndex;
		
		match("&&=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_OR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_OR_ASSIGN;
		int _saveIndex;
		
		match("||=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mUNARY_PLUS_MINUS_METHOD_NAME(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UNARY_PLUS_MINUS_METHOD_NAME;
		int _saveIndex;
		
		if (!(last_token_is_keyword_def_or_colon()))
		  throw new SemanticException("last_token_is_keyword_def_or_colon()");
		{
		switch ( LA(1)) {
		case '+':
		{
			match("+@");
			break;
		}
		case '-':
		{
			match("-@");
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMI;
		int _saveIndex;
		
		_saveIndex=text.length();
		match(';');
		text.setLength(_saveIndex);
		{
		_loop513:
		do {
			switch ( LA(1)) {
			case '\t':  case '\u000b':  case '\u000c':  case ' ':
			{
				_saveIndex=text.length();
				mWHITE_SPACE_CAHR(false);
				text.setLength(_saveIndex);
				break;
			}
			case '\n':  case '\r':
			{
				_saveIndex=text.length();
				mLINE_FEED(false);
				text.setLength(_saveIndex);
				break;
			}
			case ';':
			{
				_saveIndex=text.length();
				match(';');
				text.setLength(_saveIndex);
				break;
			}
			default:
			{
				break _loop513;
			}
			}
		} while (true);
		}
		
						if (last_token_is_semi())
						{
							_ttype = Token.SKIP;
						}
					
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mWHITE_SPACE_CAHR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WHITE_SPACE_CAHR;
		int _saveIndex;
		
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '\t':
		{
			match('\t');
			break;
		}
		case '\u000c':
		{
			match('\f');
			break;
		}
		case '\u000b':
		{
			match('\13');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLINE_FEED(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE_FEED;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '\n':
		{
			match('\n');
			break;
		}
		case '\r':
		{
			match('\r');
			{
			if ((LA(1)=='\n') && (true) && (true)) {
				match('\n');
			}
			else {
			}
			
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		newline();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLINE_BREAK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE_BREAK;
		int _saveIndex;
		
		if (((LA(1)=='\n'||LA(1)=='\r') && (true) && (true))&&(expect_heredoc_content())) {
			_saveIndex=text.length();
			mLINE_FEED(false);
			text.setLength(_saveIndex);
		}
		else if ((LA(1)=='\n'||LA(1)=='\r') && (true) && (true)) {
			_saveIndex=text.length();
			mPURE_LINE_BREAK(false);
			text.setLength(_saveIndex);
			{
			if ((LA(1)==';')) {
				_saveIndex=text.length();
				mSEMI(false);
				text.setLength(_saveIndex);
				_ttype = SEMI;
			}
			else {
			}
			
			}
			
							if ((LINE_BREAK == _ttype) && should_ignore_linebreak())
							{
								_ttype = Token.SKIP;
							}
						
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mPURE_LINE_BREAK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PURE_LINE_BREAK;
		int _saveIndex;
		
		mLINE_FEED(false);
		{
		_loop518:
		do {
			switch ( LA(1)) {
			case '\n':  case '\r':
			{
				_saveIndex=text.length();
				mLINE_FEED(false);
				text.setLength(_saveIndex);
				break;
			}
			case '\t':  case '\u000b':  case '\u000c':  case ' ':
			{
				_saveIndex=text.length();
				mWHITE_SPACE_CAHR(false);
				text.setLength(_saveIndex);
				break;
			}
			default:
			{
				break _loop518;
			}
			}
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mREGEX_MODIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = REGEX_MODIFIER;
		int _saveIndex;
		regex_flags = 0;
		
		{
		_loop524:
		do {
			switch ( LA(1)) {
			case 'i':
			{
				_saveIndex=text.length();
				match('i');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_I;
				break;
			}
			case 'o':
			{
				_saveIndex=text.length();
				match('o');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_O;
				break;
			}
			case 'm':
			{
				_saveIndex=text.length();
				match('m');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_M;
				break;
			}
			case 'x':
			{
				_saveIndex=text.length();
				match('x');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_X;
				break;
			}
			case 'n':
			{
				_saveIndex=text.length();
				match('n');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_N;
				break;
			}
			case 'e':
			{
				_saveIndex=text.length();
				match('e');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_E;
				break;
			}
			case 'u':
			{
				_saveIndex=text.length();
				match('u');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_U;
				break;
			}
			case 's':
			{
				_saveIndex=text.length();
				match('s');
				text.setLength(_saveIndex);
				regex_flags |= RegularExpression.REGEX_OPTION_S;
				break;
			}
			default:
			{
				break _loop524;
			}
			}
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMAND_OUTPUT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMAND_OUTPUT;
		int _saveIndex;
		char  delimiter = '\0';
		char  end = '\0';
		
		if (((LA(1)=='`') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')))&&(!last_token_is_keyword_def_or_colon())) {
			delimiter = LA(1);
			_saveIndex=text.length();
			match('`');
			text.setLength(_saveIndex);
			{
			_loop527:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')))&&(LA(1) != delimiter && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop527;
				}
				
			} while (true);
			}
			end = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			
							if (end != delimiter)
							{
								_ttype = COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter, 1);
							}
						
		}
		else if ((LA(1)=='`') && (true)) {
			match('`');
			_ttype = SINGLE_QUOTE;
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRING_CHAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_CHAR;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '\n':  case '\r':
		{
			mLINE_FEED(false);
			break;
		}
		case '\\':
		{
			mESC(false);
			break;
		}
		default:
			if ((_tokenSet_1.member(LA(1)))) {
				{
				match(_tokenSet_1);
				}
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSINGLE_QUOTE_STRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SINGLE_QUOTE_STRING;
		int _saveIndex;
		
		_saveIndex=text.length();
		match('\'');
		text.setLength(_saveIndex);
		{
		_loop531:
		do {
			switch ( LA(1)) {
			case '\\':
			{
				mESC(false);
				break;
			}
			case '\n':  case '\r':
			{
				mLINE_FEED(false);
				break;
			}
			default:
				if ((_tokenSet_2.member(LA(1)))) {
					{
					match(_tokenSet_2);
					}
				}
			else {
				break _loop531;
			}
			}
		} while (true);
		}
		_saveIndex=text.length();
		match('\'');
		text.setLength(_saveIndex);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		match('\\');
		matchNot(EOF_CHAR);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mREGEX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = REGEX;
		int _saveIndex;
		char  delimiter = '\0';
		char  end = '\0';
		
		if (((LA(1)=='/') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && (true))&&(!expect_operator(2))) {
			delimiter = LA(1);
			_saveIndex=text.length();
			match('/');
			text.setLength(_saveIndex);
			{
			_loop534:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')))&&(LA(1) != delimiter && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop534;
				}
				
			} while (true);
			}
			end = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			
							if (end != delimiter)
							{
								_ttype = REGEX_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter, 1);
							}
							else
							{
								mREGEX_MODIFIER(false);
							}
						
		}
		else if ((LA(1)=='/') && (LA(2)=='=') && (true)) {
			match("/=");
			_ttype = DIV_ASSIGN;
		}
		else if ((LA(1)=='/') && (true)) {
			match('/');
			_ttype = DIV;
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDOUBLE_QUOTE_STRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DOUBLE_QUOTE_STRING;
		int _saveIndex;
		char  delimiter = '\0';
		char  end = '\0';
		
		delimiter = LA(1);
		_saveIndex=text.length();
		match('\"');
		text.setLength(_saveIndex);
		{
		_loop537:
		do {
			if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')))&&(LA(1) != delimiter && !expression_substitution_is_next())) {
				mSTRING_CHAR(false);
			}
			else {
				break _loop537;
			}
			
		} while (true);
		}
		end = LA(1);
		_saveIndex=text.length();
		matchNot(EOF_CHAR);
		text.setLength(_saveIndex);
		
						if (end != delimiter)
						{
							_ttype = STRING_BEFORE_EXPRESSION_SUBSTITUTION;
							set_current_special_string_delimiter(delimiter, 1);
						}
					
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRING_BETWEEN_EXPRESSION_SUBSTITUTION(boolean _createToken,
		char delimiter, int delimiter_count
	) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_BETWEEN_EXPRESSION_SUBSTITUTION;
		int _saveIndex;
		
		{
		_loop540:
		do {
			if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')))&&((delimiter_count > 0) && (delimiter_count = track_delimiter_count(LA(1), delimiter, delimiter_count)) != 0&& !expression_substitution_is_next())) {
				mSTRING_CHAR(false);
			}
			else {
				break _loop540;
			}
			
		} while (true);
		}
		
						//match and skip delimiter, there maybe no delimiter, e.g. ':#{cmd_name}'
						if (LA(1) != EOF_CHAR)
						{
							_saveIndex=text.length();
							matchNot(EOF_CHAR);
							text.setLength(_saveIndex);
						}
		
						if (0 == delimiter_count)
						{
							_ttype = STRING_AFTER_EXPRESSION_SUBSTITUTION;
						}
						else
						{
							update_current_special_string_delimiter_count(delimiter_count);
						}
					
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSPECIAL_STRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SPECIAL_STRING;
		int _saveIndex;
		char  delimiter1 = '\0';
		char  delimiter2 = '\0';
		char  delimiter3 = '\0';
		char  delimiter4 = '\0';
		char  delimiter5 = '\0';
		char  delimiter7 = '\0';
		char  delimiter6 = '\0';
		
			int delimiter_count = 1;
		
		
		if ((LA(1)=='%') && (LA(2)=='q')) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			match('q');
			text.setLength(_saveIndex);
			delimiter1 = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			{
			_loop543:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter1, delimiter_count)) != 0)) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop543;
				}
				
			} while (true);
			}
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			_ttype = SINGLE_QUOTE_STRING;
		}
		else if ((LA(1)=='%') && (LA(2)=='Q')) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			match('Q');
			text.setLength(_saveIndex);
			delimiter2 = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			{
			_loop545:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter2, delimiter_count)) != 0 && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop545;
				}
				
			} while (true);
			}
			
							//match and skip delimiter
							_saveIndex=text.length();
							matchNot(EOF_CHAR);
							text.setLength(_saveIndex);
						
							if (0 == delimiter_count)
							{
								_ttype = DOUBLE_QUOTE_STRING;
							}
							else
							{
								_ttype = STRING_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter2, delimiter_count);
							}
						
		}
		else if ((LA(1)=='%') && (LA(2)=='r')) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			match('r');
			text.setLength(_saveIndex);
			delimiter3 = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			{
			_loop547:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter3, delimiter_count)) != 0 && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop547;
				}
				
			} while (true);
			}
			
							//match and skip delimiter
							_saveIndex=text.length();
							matchNot(EOF_CHAR);
							text.setLength(_saveIndex);
			
							if (0 == delimiter_count)
							{
								mREGEX_MODIFIER(false);
								_ttype = REGEX;
							}
							else
							{
								_ttype = REGEX_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter3, delimiter_count);
							}
						
		}
		else if ((LA(1)=='%') && (LA(2)=='x')) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			match('x');
			text.setLength(_saveIndex);
			delimiter4 = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			{
			_loop549:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter4, delimiter_count)) != 0 && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop549;
				}
				
			} while (true);
			}
			
							//match and skip delimiter
							_saveIndex=text.length();
							matchNot(EOF_CHAR);
							text.setLength(_saveIndex);
			
							if (0 == delimiter_count)
							{
								_ttype = COMMAND_OUTPUT;
							}
							else
							{
								_ttype = COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter4, delimiter_count);
							}
						
		}
		else if ((LA(1)=='%') && (LA(2)=='w')) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			match('w');
			text.setLength(_saveIndex);
			delimiter5 = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			{
			_loop551:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter5, delimiter_count)) != 0)) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop551;
				}
				
			} while (true);
			}
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			_ttype = SINGLE_QUOTE_WARRAY;
		}
		else if ((LA(1)=='%') && (LA(2)=='W')) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			match('W');
			text.setLength(_saveIndex);
			delimiter7 = LA(1);
			_saveIndex=text.length();
			matchNot(EOF_CHAR);
			text.setLength(_saveIndex);
			{
			_loop553:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter7, delimiter_count)) != 0 && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop553;
				}
				
			} while (true);
			}
			
							//match and skip delimiter
							_saveIndex=text.length();
							matchNot(EOF_CHAR);
							text.setLength(_saveIndex);
						
							if (0 == delimiter_count)
							{
								_ttype = DOUBLE_QUOTE_WARRAY;
							}
							else
							{
								_ttype = WARRAY_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter7, delimiter_count);
							}
						
		}
		else if (((LA(1)=='%') && (_tokenSet_3.member(LA(2))))&&(!expect_operator(2))) {
			_saveIndex=text.length();
			match('%');
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			{
			delimiter6 = LA(1);
			match(_tokenSet_3);
			}
			text.setLength(_saveIndex);
			{
			_loop556:
			do {
				if ((((LA(1) >= '\u0000' && LA(1) <= '\ufffe')))&&((delimiter_count = track_delimiter_count(LA(1), delimiter6, delimiter_count)) != 0 && !expression_substitution_is_next())) {
					mSTRING_CHAR(false);
				}
				else {
					break _loop556;
				}
				
			} while (true);
			}
			
							//match and skip delimiter
							_saveIndex=text.length();
							matchNot(EOF_CHAR);
							text.setLength(_saveIndex);
			
							if (0 == delimiter_count)
							{
								_ttype = DOUBLE_QUOTE_STRING;
							}
							else
							{
								_ttype = STRING_BEFORE_EXPRESSION_SUBSTITUTION;
								set_current_special_string_delimiter(delimiter6, delimiter_count);
							}
						
		}
		else if ((LA(1)=='%') && (LA(2)=='=')) {
			match("%=");
			_ttype = MOD_ASSIGN;
		}
		else if ((LA(1)=='%') && (true)) {
			match("%");
			_ttype = MOD;
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mHERE_DOC_BEGIN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HERE_DOC_BEGIN;
		int _saveIndex;
		Token delimiter=null;
		
		if (((LA(1)=='<') && (LA(2)=='<') && (_tokenSet_4.member(LA(3))))&&(expect_heredoc())) {
			_saveIndex=text.length();
			match("<<");
			text.setLength(_saveIndex);
			mHERE_DOC_DELIMITER(true);
			delimiter=_returnToken;
		}
		else if ((LA(1)=='<') && (LA(2)=='<') && (LA(3)=='=')) {
			match("<<=");
			_ttype = LEFT_SHIFT_ASSIGN;
		}
		else if ((LA(1)=='<') && (LA(2)=='<') && (true)) {
			match("<<");
			_ttype = LEFT_SHIFT;
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHERE_DOC_DELIMITER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HERE_DOC_DELIMITER;
		int _saveIndex;
		
		{
		if ((LA(1)=='-') && (_tokenSet_4.member(LA(2))) && (true)) {
			match('-');
		}
		else if ((_tokenSet_4.member(LA(1))) && (true) && (true)) {
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		{
		switch ( LA(1)) {
		case '\'':
		{
			_saveIndex=text.length();
			match('\'');
			text.setLength(_saveIndex);
			{
			int _cnt571=0;
			_loop571:
			do {
				if ((_tokenSet_5.member(LA(1)))) {
					{
					match(_tokenSet_5);
					}
				}
				else {
					if ( _cnt571>=1 ) { break _loop571; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt571++;
			} while (true);
			}
			/* TODO:SingleQuote*/
			_saveIndex=text.length();
			match('\'');
			text.setLength(_saveIndex);
			break;
		}
		case '"':
		{
			_saveIndex=text.length();
			match('"');
			text.setLength(_saveIndex);
			{
			int _cnt574=0;
			_loop574:
			do {
				if ((_tokenSet_6.member(LA(1)))) {
					{
					match(_tokenSet_6);
					}
				}
				else {
					if ( _cnt574>=1 ) { break _loop574; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt574++;
			} while (true);
			}
			/* TODO:DoubleQuote */
			_saveIndex=text.length();
			match('"');
			text.setLength(_saveIndex);
			break;
		}
		default:
			if ((_tokenSet_7.member(LA(1)))) {
				{
				int _cnt568=0;
				_loop568:
				do {
					if ((_tokenSet_7.member(LA(1)))) {
						{
						match(_tokenSet_7);
						}
					}
					else {
						if ( _cnt568>=1 ) { break _loop568; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt568++;
				} while (true);
				}
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHERE_DOC_CONTENT(boolean _createToken,
		String delimiter
	) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HERE_DOC_CONTENT;
		int _saveIndex;
		Token next_line=null;
		
		{
		int _cnt562=0;
		_loop562:
		do {
			if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe'))) {
				mANYTHING_OTHER_THAN_LINE_FEED(true);
				next_line=_returnToken;
				mLINE_FEED(false);
				if (is_delimiter(next_line.getText(), delimiter)) break;
			}
			else {
				if ( _cnt562>=1 ) { break _loop562; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt562++;
		} while (true);
		}
		
						//skip delimiter
						text.setLength(text.length() - next_line.getText().length() - 1);
					
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mANYTHING_OTHER_THAN_LINE_FEED(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ANYTHING_OTHER_THAN_LINE_FEED;
		int _saveIndex;
		
		{
		_loop581:
		do {
			if ((_tokenSet_8.member(LA(1)))) {
				{
				match(_tokenSet_8);
				}
			}
			else {
				break _loop581;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRDOC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RDOC;
		int _saveIndex;
		
		if (!(getColumn()==1))
		  throw new SemanticException("getColumn()==1");
		match("=begin");
		{
		_loop577:
		do {
			// nongreedy exit test
			if ((LA(1)=='=') && (LA(2)=='e') && (LA(3)=='n')) break _loop577;
			if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe'))) {
				mLINE(false);
			}
			else {
				break _loop577;
			}
			
		} while (true);
		}
		if (!(getColumn()==1))
		  throw new SemanticException("getColumn()==1");
		match("=end");
		
						_ttype = Token.SKIP;
					
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLINE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE;
		int _saveIndex;
		
		mANYTHING_OTHER_THAN_LINE_FEED(false);
		mLINE_FEED(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENTIFIER;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('a','z');
			break;
		}
		case '_':
		{
			match('_');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		_loop587:
		do {
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			default:
			{
				break _loop587;
			}
			}
		} while (true);
		}
		{
		switch ( LA(1)) {
		case '?':
		{
			match('?');
			_ttype = FUNCTION;
			break;
		}
		case '!':
		{
			match('!');
			_ttype = FUNCTION;
			break;
		}
		default:
			{
				if (last_token_is_dot_or_colon2()) {_ttype = FUNCTION;}
			}
		}
		}
		_ttype = testLiteralsTable(_ttype);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGLOBAL_VARIBLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GLOBAL_VARIBLE;
		int _saveIndex;
		
		if ((LA(1)=='$') && (_tokenSet_9.member(LA(2)))) {
			match('$');
			{
			switch ( LA(1)) {
			case '-':
			{
				match('-');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':  case '_':  case 'a':
			case 'b':  case 'c':  case 'd':  case 'e':
			case 'f':  case 'g':  case 'h':  case 'i':
			case 'j':  case 'k':  case 'l':  case 'm':
			case 'n':  case 'o':  case 'p':  case 'q':
			case 'r':  case 's':  case 't':  case 'u':
			case 'v':  case 'w':  case 'x':  case 'y':
			case 'z':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			mIDENTIFIER_CONSTANT_AND_KEYWORD(false);
		}
		else if ((LA(1)=='$') && ((LA(2) >= '0' && LA(2) <= '9'))) {
			match('$');
			{
			int _cnt592=0;
			_loop592:
			do {
				if (((LA(1) >= '0' && LA(1) <= '9'))) {
					matchRange('0','9');
				}
				else {
					if ( _cnt592>=1 ) { break _loop592; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt592++;
			} while (true);
			}
		}
		else if ((LA(1)=='$') && (_tokenSet_10.member(LA(2)))) {
			match('$');
			{
			switch ( LA(1)) {
			case '!':
			{
				match('!');
				break;
			}
			case '@':
			{
				match('@');
				break;
			}
			case '&':
			{
				match('&');
				break;
			}
			case '`':
			{
				match('`');
				break;
			}
			case '\'':
			{
				match('\'');
				break;
			}
			case '+':
			{
				match('+');
				break;
			}
			case '~':
			{
				match('~');
				break;
			}
			case '=':
			{
				match('=');
				break;
			}
			case '/':
			{
				match('/');
				break;
			}
			case '\\':
			{
				match('\\');
				break;
			}
			case ',':
			{
				match(',');
				break;
			}
			case ';':
			{
				match(';');
				break;
			}
			case '.':
			{
				match('.');
				break;
			}
			case '<':
			{
				match('<');
				break;
			}
			case '>':
			{
				match('>');
				break;
			}
			case '*':
			{
				match('*');
				break;
			}
			case '$':
			{
				match('$');
				break;
			}
			case '?':
			{
				match('?');
				break;
			}
			case ':':
			{
				match(':');
				break;
			}
			case '"':
			{
				match('\"');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mIDENTIFIER_CONSTANT_AND_KEYWORD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENTIFIER_CONSTANT_AND_KEYWORD;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('a','z');
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			matchRange('A','Z');
			break;
		}
		case '_':
		{
			match('_');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		_loop597:
		do {
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			default:
			{
				break _loop597;
			}
			}
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINSTANCE_VARIBLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INSTANCE_VARIBLE;
		int _saveIndex;
		
		match('@');
		mIDENTIFIER_CONSTANT_AND_KEYWORD(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCLASS_VARIBLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CLASS_VARIBLE;
		int _saveIndex;
		
		match('@');
		mINSTANCE_VARIBLE(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCONSTANT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CONSTANT;
		int _saveIndex;
		
		{
		matchRange('A','Z');
		}
		{
		_loop603:
		do {
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			default:
			{
				break _loop603;
			}
			}
		} while (true);
		}
		{
		switch ( LA(1)) {
		case '?':
		{
			match('?');
			_ttype = FUNCTION;
			break;
		}
		case '!':
		{
			match('!');
			_ttype = FUNCTION;
			break;
		}
		default:
			{
				if (last_token_is_dot_or_colon2()) {_ttype = FUNCTION;}
			}
		}
		}
		_ttype = testLiteralsTable(_ttype);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINTEGER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INTEGER;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '1':  case '2':  case '3':  case '4':
		case '5':  case '6':  case '7':  case '8':
		case '9':
		{
			mNON_ZERO_DECIMAL(false);
			{
			if (((LA(1)=='.'))&&((LA(2)>='0')&&(LA(2)<='9'))) {
				mFLOAT_WITH_LEADING_DOT(false);
				_ttype = FLOAT;
			}
			else if ((LA(1)=='E'||LA(1)=='e')) {
				mEXPONENT(false);
				_ttype = FLOAT;
			}
			else {
			}
			
			}
			break;
		}
		case '?':
		{
			match('?');
			{
			if ((LA(1)=='\\') && (_tokenSet_11.member(LA(2)))) {
				match('\\');
				{
				match(_tokenSet_11);
				}
				_ttype = ASCII_VALUE;
			}
			else if ((LA(1)=='\\') && (LA(2)=='C'||LA(2)=='M')) {
				{
				int _cnt616=0;
				_loop616:
				do {
					if ((LA(1)=='\\')) {
						match('\\');
						{
						switch ( LA(1)) {
						case 'C':
						{
							match('C');
							break;
						}
						case 'M':
						{
							match('M');
							break;
						}
						default:
						{
							throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
						}
						}
						}
						match('-');
					}
					else {
						if ( _cnt616>=1 ) { break _loop616; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt616++;
				} while (true);
				}
				{
				switch ( LA(1)) {
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':
				{
					matchRange('a','z');
					break;
				}
				case '?':
				{
					match('?');
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				_ttype = ASCII_VALUE;
			}
			else if (((_tokenSet_12.member(LA(1))))&&(is_ascii_value_terminator(LA(2)))) {
				{
				{
				match(_tokenSet_12);
				}
				}
				_ttype = ASCII_VALUE;
			}
			else {
				_ttype = QUESTION;
			}
			
			}
			break;
		}
		default:
			if ((LA(1)=='.') && (LA(2)=='.') && (LA(3)=='.')) {
				match("...");
				_ttype = EXCLUSIVE_RANGE;
			}
			else if ((LA(1)=='0') && ((LA(2) >= '0' && LA(2) <= '7'))) {
				_saveIndex=text.length();
				match('0');
				text.setLength(_saveIndex);
				mOCTAL_CONTENT(false);
				_ttype = OCTAL;
			}
			else if ((LA(1)=='0') && (LA(2)=='B'||LA(2)=='b')) {
				_saveIndex=text.length();
				match('0');
				text.setLength(_saveIndex);
				{
				switch ( LA(1)) {
				case 'b':
				{
					_saveIndex=text.length();
					match('b');
					text.setLength(_saveIndex);
					break;
				}
				case 'B':
				{
					_saveIndex=text.length();
					match('B');
					text.setLength(_saveIndex);
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				mBINARY_CONTENT(false);
				_ttype = BINARY;
			}
			else if ((LA(1)=='0') && (LA(2)=='X'||LA(2)=='x')) {
				_saveIndex=text.length();
				match('0');
				text.setLength(_saveIndex);
				{
				switch ( LA(1)) {
				case 'x':
				{
					_saveIndex=text.length();
					match('x');
					text.setLength(_saveIndex);
					break;
				}
				case 'X':
				{
					_saveIndex=text.length();
					match('X');
					text.setLength(_saveIndex);
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				mHEX_CONTENT(false);
				_ttype = HEX;
			}
			else if ((LA(1)=='.') && ((LA(2) >= '0' && LA(2) <= '9'))) {
				mFLOAT_WITH_LEADING_DOT(false);
				_ttype = FLOAT;
			}
			else if ((LA(1)=='.') && (LA(2)=='.') && (true)) {
				match("..");
				_ttype = INCLUSIVE_RANGE;
			}
			else if ((LA(1)=='0') && (true)) {
				match('0');
				{
				if (((LA(1)=='.'))&&((LA(2)>='0')&&(LA(2)<='9'))) {
					mFLOAT_WITH_LEADING_DOT(false);
					_ttype = FLOAT;
				}
				else {
				}
				
				}
			}
			else if ((LA(1)=='.') && (true)) {
				match('.');
				_ttype = DOT;
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mOCTAL_CONTENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCTAL_CONTENT;
		int _saveIndex;
		
		{
		int _cnt632=0;
		_loop632:
		do {
			if (((LA(1) >= '0' && LA(1) <= '7'))) {
				matchRange('0','7');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
			}
			else {
				if ( _cnt632>=1 ) { break _loop632; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt632++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mBINARY_CONTENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BINARY_CONTENT;
		int _saveIndex;
		
		{
		int _cnt643=0;
		_loop643:
		do {
			switch ( LA(1)) {
			case '0':
			{
				match('0');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			case '1':
			{
				match('1');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			default:
			{
				if ( _cnt643>=1 ) { break _loop643; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			}
			_cnt643++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX_CONTENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX_CONTENT;
		int _saveIndex;
		
		{
		int _cnt638=0;
		_loop638:
		do {
			switch ( LA(1)) {
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':
			{
				matchRange('A','F');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':
			{
				matchRange('a','f');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			default:
			{
				if ( _cnt638>=1 ) { break _loop638; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			}
			_cnt638++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mFLOAT_WITH_LEADING_DOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = FLOAT_WITH_LEADING_DOT;
		int _saveIndex;
		
		match('.');
		{
		int _cnt621=0;
		_loop621:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				matchRange('0','9');
			}
			else {
				if ( _cnt621>=1 ) { break _loop621; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt621++;
		} while (true);
		}
		{
		if ((LA(1)=='E'||LA(1)=='e')) {
			mEXPONENT(false);
		}
		else {
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mNON_ZERO_DECIMAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NON_ZERO_DECIMAL;
		int _saveIndex;
		
		{
		matchRange('1','9');
		{
		if ((LA(1)=='_')) {
			_saveIndex=text.length();
			mUNDER_SCORE(false);
			text.setLength(_saveIndex);
		}
		else {
		}
		
		}
		{
		_loop628:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				matchRange('0','9');
				{
				if ((LA(1)=='_')) {
					_saveIndex=text.length();
					mUNDER_SCORE(false);
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
			}
			else {
				break _loop628;
			}
			
		} while (true);
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mEXPONENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EXPONENT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'e':
		{
			match('e');
			break;
		}
		case 'E':
		{
			match('E');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		switch ( LA(1)) {
		case '+':
		{
			match('+');
			break;
		}
		case '-':
		{
			match('-');
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		int _cnt648=0;
		_loop648:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				matchRange('0','9');
			}
			else {
				if ( _cnt648>=1 ) { break _loop648; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt648++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUNDER_SCORE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UNDER_SCORE;
		int _saveIndex;
		
		match('_');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMENT;
		int _saveIndex;
		
		if (((LA(1)=='#') && (true) && (true))&&(!last_token_is_colon_with_no_following_space())) {
			match('#');
			mANYTHING_OTHER_THAN_LINE_FEED(false);
			
							_ttype = Token.SKIP;
						
		}
		else if ((LA(1)=='#') && (true) && (true)) {
			_saveIndex=text.length();
			match('#');
			text.setLength(_saveIndex);
			
							_ttype = STRING_BEFORE_EXPRESSION_SUBSTITUTION;
							set_current_special_string_delimiter('#'/*useless*/, 0);
						
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWHITE_SPACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WHITE_SPACE;
		int _saveIndex;
		
		{
		int _cnt653=0;
		_loop653:
		do {
			if ((LA(1)=='\t'||LA(1)=='\u000b'||LA(1)=='\u000c'||LA(1)==' ')) {
				mWHITE_SPACE_CAHR(false);
			}
			else {
				if ( _cnt653>=1 ) { break _loop653; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt653++;
		} while (true);
		}
		
						set_seen_whitespace();
						_ttype = Token.SKIP;
					
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLINE_CONTINUATION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE_CONTINUATION;
		int _saveIndex;
		
		match('\\');
		mLINE_FEED(false);
		_ttype = Token.SKIP;
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEND_OF_FILE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = END_OF_FILE;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '\u0000':
		{
			match('\0');
			break;
		}
		case '\u0004':
		{
			match('\004');
			break;
		}
		case '\u001a':
		{
			match('\032');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		_ttype = Token.EOF_TYPE;
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[1025];
		data[1]=576460745995190270L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[2048];
		data[0]=-9217L;
		data[1]=-268435457L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[2048];
		data[0]=-549755823105L;
		data[1]=-268435457L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2048];
		data[0]=-2593791910388695041L;
		data[1]=-576460743847706623L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[2048];
		data[0]=-2882393925765571585L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[2048];
		data[0]=-549755823105L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[2048];
		data[0]=-17179878401L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[2048];
		data[0]=-2882394492701254657L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[2048];
		data[0]=-9217L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = new long[1025];
		data[0]=35184372088832L;
		data[1]=576460745995190270L;
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = new long[1025];
		data[0]=-287987564470599680L;
		data[1]=4611686022990790657L;
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = new long[2048];
		data[0]=-1L;
		data[1]=-8201L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = new long[2048];
		data[0]=-4294976513L;
		data[1]=-268435457L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	
	}
