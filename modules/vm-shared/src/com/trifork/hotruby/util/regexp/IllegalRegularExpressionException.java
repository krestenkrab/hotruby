package com.trifork.hotruby.util.regexp;

/**
 * Used by RegularExpressionTranslator.
 * 
 * If Ruby should throw an exception during regular expression parsing, this
 * kind of exception should be thrown.
 * 
 * @author ofo
 */
public class IllegalRegularExpressionException extends IllegalArgumentException {
	public IllegalRegularExpressionException(String msg)
	{
		super(msg);
	}
}
