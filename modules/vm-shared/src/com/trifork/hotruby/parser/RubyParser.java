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
package com.trifork.hotruby.parser;

import java.io.Reader;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.trifork.hotruby.ast.ConstantStringExpression;
import com.trifork.hotruby.ast.RubyCode;

public class RubyParser extends RubyParserBase implements ScopeHolder
{
	private RubyLexer lexer_;

	public String getFilename() {
		return lexer_.getFilename();
	}
	
	public RubyParser(RubyLexer lexer)
	{
		super(lexer);
		lexer_ = lexer;
		lexer_.setScopeHolder(this);
	}

	public RubyParser(Reader in, String filename, int first_line)
	{
		this(new RubyLexer(in, filename, first_line));
	}
	
	public RubyParser(Reader in, String filename)
	{
		this(new RubyLexer(in, filename, 0));
	}
	
	public int get_regexp_options() {
		return lexer_.regex_flags;
	}

	/// @return AST
	public RubyCode parse()
	{
		try
		{
			return program();
		}
		catch (RecognitionException e)
		{
			return null;
		}
		catch (TokenStreamException e)
		{
			return null;
		}
	}

	protected ConstantStringExpression nextHereDoc() { 
		ConstantStringExpression exp = new ConstantStringExpression(null, ConstantStringExpression.HEREDOC);
		lexer_.heredocs.add(exp);
		return exp;
	}

	
	protected void tell_lexer_we_have_finished_parsing_methodparameters()
	{
		lexer_.set_last_token_to_be_RPAREN_IN_METHOD_DEFINITION();
	}

	protected void tell_lexer_we_have_finished_parsing_symbol()
	{
		lexer_.set_just_finished_parsing_symbol();
	}

	protected void tell_lexer_we_have_finished_parsing_string_expression_substituation()
	{
		lexer_.set_just_finished_parsing_string_expression_substituation();
	}

	protected void tell_lexer_we_have_finished_parsing_regex_expression_substituation()
	{
		lexer_.set_just_finished_parsing_regex_expression_substituation();
	}

}

