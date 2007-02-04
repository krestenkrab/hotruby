package com.trifork.hotruby.util.regexp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translates from a Ruby regular expression to a Java regular expression.
 * 
 * @author ofo
 */
public class RegularExpressionTranslator {
	private static final String[][] POSIX_CLASSES =
	{
		{ "[:alnum:]", "\\p{Alnum}" },
		{ "[:alpha:]", "\\p{Alpha}" },
		{ "[:blank:]", "\\p{Blank}" },
		{ "[:cntrl:]", "\\p{Cntrl}" },
		{ "[:digit:]", "\\p{Digit}" },
		{ "[:graph:]", "\\p{Graph}" },
		{ "[:lower:]", "\\p{Lower}" },
		{ "[:print:]", "\\p{Print}" },
		{ "[:punct:]", "\\p{Punct}" },
		{ "[:space:]", "\\p{Space}" },
		{ "[:upper:]", "\\p{Upper}" },
		{ "[:xdigit:]", "\\p{XDigit}" }
	};
	
	private final StringBuilder newExp;
	private final String originalExp;
	private final Pattern pattern;
	private int pos;
	private boolean valid;

	public static List<String> match(String regex, String s)
	{
		List<String> result = new ArrayList<String>();
		RegularExpressionTranslator myMatcher = new RegularExpressionTranslator(regex);
		if (myMatcher.isValid())
		{
			String newExp = myMatcher.getParsedExpression();
			Pattern pattern = Pattern.compile(newExp);
			Matcher matcher = pattern.matcher(s);
			if (matcher.find())
			{
				for (int i=0; i<matcher.groupCount() + 1; i++ )
				{
					result.add(matcher.group(i));
				}
			}
		}
		return result;
	}

	public RegularExpressionTranslator(String regex)
	{
		try {
			originalExp = regex;
			newExp = new StringBuilder();
			valid = parseRegularExpression(false);
			if (valid)
			{
				pattern = Pattern.compile(newExp.toString());
			}
			else
			{
				pattern = null;
			}
		}
		catch (IllegalRegularExpressionException e)
		{
			System.out.println("Error: " + e.getMessage());
			throw e;
		}
	}

	public boolean isValid()
	{
		return valid;
	}
	
	public Pattern getPattern()
	{
		return pattern;
	}

	private String getParsedExpression()
	{
		return newExp.toString();
	}

	private boolean parseRegularExpression(boolean inGroup)
	{
		while(!finished())
		{
			switch(current())
			{
			case '[':
				if (!characterClass())
				{
					return false;
				}
				break;
			case '{':
				if (!repetition())
				{
					return false;
				}
				break;
			case '(':
				if (!group())
				{
					return false;
				}
				break;
			case ')':
				if (inGroup)
				{
					return true;
				}
				throw new IllegalRegularExpressionException("Unmatched )");
			case '\\':
				escape();
				break;
			default:
				// |, ., $, ^, +, ?, and * are also appended as-is
				append(current());
			}
			advance();
		}
		return true;
	}

	/**
	 * [abc]
	 */
	private boolean characterClass()
	{
		assert current() == '[';
		append('[');
		advanceAndExpectMore();
		
		for (; current() == '^'; advanceAndExpectMore())
		{
			append('^');
		}
		
		startingCharacters('-');
		
		for (; current() != ']'; advance())
		{
			if (!hasMore())
			{
				return false; // Fail silently
			}
			if (current() == '[')
			{
				posixClass();
				break;
			}
			else if (current() == '\\')
			{
				append('\\');
				advanceAndExpectMore();
				switch(current())
				{
					// Character classes
				case 'd':
				case 'D':
				case 's':
				case 'S':
				case 'w':
				case 'W':
					
					// Closing bracket, minus sign
				case ']':
				case '-':
				case '.':
					break;
					
					// perhaps this should be the default rule?
				case '\\':
				case '?':
					//append(current());
					break;
				default:
					throw new IllegalRegularExpressionException("Unknown escape sequence: \\" + current() + " (0x" + Integer.toHexString(current()) + ")");
				}
			}
			else if (current() == '-')
			{
				advanceAndExpectMore();
				if (current() == '-')
				{
					throw new IllegalRegularExpressionException("Invalid regular expression");
				}
				else if (current() == ']')
				{
					append("\\-]");
					return true;
				}
				append('-');
			}
			append(current());
		}
		append(']');
		return true;
	}

	private void startingCharacters(char c) {
		boolean startingChar = false;
		for (; current() == c; advanceAndExpectMore())
		{
			if (!startingChar)
			{
				append(c);
				startingChar = true;
			}
		}
	}

	/**
	 * [:digit:], ...
	 */
	private void posixClass()
	{
		for (String[] pclass : POSIX_CLASSES)
		{
			if (continues(pclass[0]))
			{
				append(pclass[1]);
				return;
			}
		}
		throw new IllegalRegularExpressionException("Unknown character class");
	}
	
	/**
	 * {min,max}
	 */
	private boolean repetition()
	{
		assert current() == '{';
		append('{');
		advanceAndExpectMore();
		if (!number())
		{
			return false;
		}
		if(current() == '}')
		{
			append('}');
			return true;
		}
		if (current() != ',')
		{
			return false;
		}
		append(',');
		advanceAndExpectMore();
		if (current() == '}')
		{
			append('}');
			return true;
		}
		if (!number())
		{
			return false;
		}
		if (current() != '}')
		{
			return false;
		}
		append('}');
		return true;
	}

	private boolean number()
	{
		if (!Character.isDigit(current()))
		{
			return false;
		}
		while(Character.isDigit(current()))
		{
			append(current());
			advanceAndExpectMore();
		}
		return true;
	}

	/**
	 * (regexp), (?extension)
	 */
	private boolean group()
	{
		assert current() == '(';
		advanceAndExpectMore();
		if (current() == '?')
		{
			return extension();
		}
		append('(');
		if (!parseRegularExpression(true))
		{
			return false;
		}
		append(')');
		return true;
	}
	
	/**
	 * \A, ...
	 */
	private void escape()
	{
		assert current() == '\\';
		advanceAndExpectMore();
		switch(current())
		{
			// Anchors
		case 'A':
		case 'z':
		case 'Z':
		case 'b':
		case 'B':
		case 'G':

			// Escapable characters
		case '.':
		case '|':
		case '[':
		case '{':
		case '$':
		case '^':
		case '+':
		case '?':
		case '*':
		case '(':
		case ')':
		case '\\':
			
			// Backreferences
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			
			// Character classes
		case 'd':
		case 'D':
		case 's':
		case 'S':
		case 'w':
		case 'W':
			append('\\');
		}
		append(current());
	}

	/**
	 * (? extension)
	 */
	private boolean extension()
	{
		assert current() == '?';
		advanceAndExpectMore();
		switch(current())
		{
		case ':': 
			return extension("(?:"); // Group without backreference
		case '=': 
			return extension("(?="); // Zero-width positive lookahead
		case '!': 
			return extension("(?!"); // Zero-width negative lookahead
		case '>': 
			return extension("(?>"); // Independent regular expresseion
		case '#':
			return comment(); // Comment
		default:
			throw new IllegalRegularExpressionException("Undefined (?...) sequence: " + originalExp);
		}
	}

	private boolean extension(String javaExtension)
	{
		assert current() == ':';
		advanceAndExpectMore();
		append(javaExtension);
		if (!parseRegularExpression(true))
		{
			return false;
		}
		append(')');
		return true;
	}

	/**
	 * (?# comment )
	 *
	 */
	private boolean comment()
	{
		assert current() == '#';
		while (current() != ')')
		{
			advanceAndExpectMore();
		}
		return true;
	}
	
	//
	// Helper methods
	//
	
	/**
	 * True if the input continues with the given string. Also, if that is the
	 * case, the input position is advanced to just after the given string.
	 */
	private boolean continues(String s)
	{
		int length = s.length();
		if (!originalExp.regionMatches(pos, s, 0, length))
		{
			return false;
		}
		advance(length);
		return true;
	}
	
	/**
	 * Character at current position in input.
	 */
	private char current()
	{
		return originalExp.charAt(pos);
	}

	/**
	 * Advances 1 character in input.
	 */
	private void advance()
	{
		advance(1);
	}
	
	/**
	 * Advances "length" characters in input.
	 */
	private void advance(int length)
	{
		pos += length;
	}

	/**
	 * Errs if at the end of input, advances 1 character in input otherwise.
	 */
	private void advanceAndExpectMore()
	{
		if (!hasMore())
		{
			throw new IllegalRegularExpressionException("Unexpected end of regular expression");
		}
		advance();
	}
	
	/**
	 * True if there is at least one more character besides current().
	 */
	private boolean hasMore()
	{
		return pos + 1 < originalExp.length();
	}
	
	/**
	 * True if we have passed the last character in input.
	 */
	private boolean finished()
	{
		return pos >= originalExp.length();
	}

	/**
	 * Appends given char to output.
	 */
	private void append(char c)
	{
		newExp.append(c);
	}
	
	/**
	 * Appends given string to output.
	 */
	private void append(String s)
	{
		newExp.append(s);
	}
}
