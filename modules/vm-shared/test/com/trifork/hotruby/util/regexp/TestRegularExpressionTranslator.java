package com.trifork.hotruby.util.regexp;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.trifork.hotruby.util.regexp.RegularExpressionTranslator;
import com.trifork.hotruby.util.regexp.IllegalRegularExpressionException;

public class TestRegularExpressionTranslator {
	
	@Test
	public void simple()
	{
		assertMatch("bcd", "abcde").withResult("bcd");
	}
	
	@Test
	public void specialCharacters()
	{
		assertMatch("%&", "!@%&-_=+").withResult("%&");
		
		// TODO: Hvaba?
		assertMatch("((.*)\\.)?([^\\.]*)", "").withResult("", null, null, "");
	}
	
	@Test
	public void escape()
	{
		// Hard to read... '\\\[\{' and 'abc\[{def'
		assertMatch("\\\\\\[\\{", "abc\\[{def").withResult("\\[{");
		assertMatch("b\\Fc", "abFcd").withResult("bFc");
		assertMatch("b\\.c", "ab.cd").withResult("b.c");
		assertNoMatch("b\\.d", "abcde");
	}
	
	@Test(expected=IllegalRegularExpressionException.class)
	public void tooManyEndingParentheses()
	{
		assertMatch("ab(cd))ef", "12abcdef34");
	}
	
	@Test
	public void group()
	{
		assertMatch("ab(cd)ef", "12abcdef34").withResult("abcdef", "cd");
		assertMatch("ab(cd)ef(gh)ij", "12abcdefghij34").withResult("abcdefghij", "cd", "gh");
		assertMatch("a(b(c))d", "12abcd34").withResult("abcd", "bc", "c");
		assertMatch("a(b(c)?)d", "12abd34").withResult("abd", "b", null);
		assertMatch("a(b(c)?)?d", "12ad34").withResult("ad", null, null);
	}
	
	@Test
	public void alternation()
	{
		assertMatch("a(b|3)c", "12abc45").withResult("abc", "b");
		assertMatch("a(b|3)c", "12a3c45").withResult("a3c", "3");
		assertMatch("abc|a3c", "12abc45").withResult("abc");
		assertMatch("abc|a3c", "12a3c45").withResult("a3c");
	}
	
	@Test
	public void zeroOrMore()
	{
		assertMatch("ab*c", "ac").withResult("ac");
	    assertMatch("ab*c", "abc").withResult("abc");
	    assertMatch("ab*c", "abbc").withResult("abbc");
	}
	
	@Test
	public void optional()
	{
		assertMatch("ab?c", "ac").withResult("ac");
	    assertMatch("ab?c", "abc").withResult("abc");
	    assertNoMatch("ab?c", "abbc");
	}
	
	@Test
	public void oneOrMore()
	{
		assertNoMatch("ab+c", "ac");
		assertMatch("ab+c", "abc").withResult("abc");
	    assertMatch("ab+c", "abbc").withResult("abbc");
	}
	
	@Test
	public void repetitionSpecified()
	{
		assertNoMatch("ab{2,4}c", "abc");
		assertMatch("ab{2,4}c", "abbc").withResult("abbc");
		assertMatch("ab{2,4}c", "abbbbc").withResult("abbbbc");
		assertNoMatch("ab{2,4}c", "abbbbbc");

		// If any whitespace is included, it never matches
		assertNoMatch("ab{2 ,4}c", "abbc");
		assertNoMatch("ab{ 2,4}c", "abbc");
		assertNoMatch("ab{2, 4}c", "abbc");
		assertNoMatch("ab{2,4 }c", "abbc");
	}
	
	@Test
	public void repetitionExact()
	{
		assertNoMatch("ab{2}c", "abc");
		assertMatch("ab{2}c", "abbc").withResult("abbc");
		assertNoMatch("ab{2}c", "abbbc");

		// If any whitespace is included, it never matches
		assertNoMatch("ab{ 2}c", "abbc");
		assertNoMatch("ab{2 }c", "abbc");
	}
	
	@Test
	public void repetitionMinimum()
	{
		assertNoMatch("ab{2,}c", "abc");
		assertMatch("ab{2,}c", "abbc").withResult("abbc");
		assertMatch("ab{2,}c", "abbbbc").withResult("abbbbc");

	    // If any whitespace is included, it never matches
		assertNoMatch("ab{ 2,}c", "abbc");
		assertNoMatch("ab{2 ,}c", "abbc");
		assertNoMatch("ab{2, }c", "abbc");
	}
	
	@Test
	public void comment()
	{
		assertMatch("a(?# my comment )bc", "abc").withResult("abc");
		assertMatch("a(?#my comment)bc", "abc").withResult("abc");
		assertMatch("a(?# my comment \\)bc", "abc").withResult("abc");
		assertMatch("a(?# my comment ()bc", "abc").withResult("abc");
	}
	
	@Test
	public void backreference()
	{
		assertNoMatch("a(b)\\1c", "abc");
		assertMatch("a(b)\\1c", "abbc").withResult("abbc", "b");

		// \&, \`, \', \+ cannot be used for anything special when matching
		assertMatch("a(b)\\&c", "ab&c").withResult("ab&c", "b");
		assertMatch("a(b)\\`c", "ab`c").withResult("ab`c", "b");
		assertMatch("a(b)\\'c", "ab'c").withResult("ab'c", "b");
		assertMatch("a(b)\\+c", "ab+c").withResult("ab+c", "b");
	}
	
	@Test
	public void predefinedCharacterClasses()
	{
		// \d, \D
		assertMatch("a\\db", "12a3b45").withResult("a3b");
		assertNoMatch("a\\Db", "12a3b45");
		assertMatch("a\\Db", "12a b45").withResult("a b");

		// \s, \S
		assertMatch("a\\sb", "12a b45").withResult("a b");
		assertNoMatch("a\\Sb", "12a b45");
		assertMatch("a\\Sb", "12a3b45").withResult("a3b");

		// \w, \W
		assertMatch("a\\wc", "12abc45").withResult("abc");
		assertNoMatch("a\\Wc", "12abc45");
		assertNoMatch("a\\Wc", "12a3c45");
		assertMatch("a\\Wc", "12a c45").withResult("a c");

		// .
		assertMatch("a.c", "12abc45").withResult("abc");
		assertNoMatch("a.c", "12a\nc45");
	}
	
	@Test
	public void characterClasses()
	{
		assertMatch("a[3b]c", "12a3c45").withResult("a3c");
		assertMatch("a[3b]c", "12abc45").withResult("abc");
		assertMatch("a[b-f]c", "12abc45").withResult("abc");
		assertMatch("a[b-f]c", "12aec45").withResult("aec");
		assertMatch("a[-a-c]c", "12a-c45").withResult("a-c");
		assertMatch("a[-a-c]c", "12abc45").withResult("abc");
		assertMatch("a[-----a-c]c", "12a-c45").withResult("a-c");
		assertMatch("a[a-c-]c", "12a-c45").withResult("a-c");
		try {
			assertMatch("a[a--c]c", "12a-c45").withResult("a-c");
			fail();
		} catch (IllegalRegularExpressionException e) {
			// Should go here
		}
		assertMatch("a[ab\\]]c", "12a]c45").withResult("a]c");
		assertNoMatch("a[ab\\]]c", "12a\\c45");
		
		// Inverse classes
		assertMatch("a[^bc]c", "12a3c45").withResult("a3c");
		assertNoMatch("a[^bc]c", "12abc45");
		assertMatch("a[^^^^^bc]c", "12a3c45").withResult("a3c");
		assertNoMatch("a[^^^^^bc]c", "12a^c45");
		assertNoMatch("a[^-bc]c", "12a-c45");
		
		// Escapes
		assertMatch("a[a-c\\d]c", "12a3c45").withResult("a3c");
		assertNoMatch("a[a-c\\d]c", "12aec45");
		try {
			assertMatch("a[a-c\\F]c", "12aFc45").withResult("aFc");
		} catch (IllegalRegularExpressionException e) {
			// Should go here
		}
		assertMatch("a[a-c\\-]c", "12a-c45").withResult("a-c");
		assertMatch("a[abc\\.]c", "12a.c45").withResult("a.c");
		assertMatch("a[abc.]c", "12a.c45").withResult("a.c");
		assertMatch("a[ab\\\\c]c", "12a\\c45").withResult("a\\c");
		assertMatch("a[ab\\\\d]c", "12abc45").withResult("abc");
		assertNoMatch("a[ab\\d]c", "12adc45");

		// POSIX character classes
		assertMatch("a[b[:digit:]]c", "12a3c45").withResult("a3c");
		assertMatch("a[b[:digit:]]c", "12abc45").withResult("abc");
		assertMatch("a[[:alnum:]]c", "12abc45").withResult("abc");
		assertMatch("a[[:alnum:]]c", "12a3c45").withResult("a3c");
		assertMatch("a[[:alpha:]]c", "12abc45").withResult("abc");
		assertMatch("a[[:blank:]]c", "12a c45").withResult("a c");
		assertMatch("a[[:cntrl:]]c", "12a\nc45").withResult("a\nc");
		assertMatch("a[[:graph:]]c", "12a.c45").withResult("a.c");
		assertMatch("a[[:lower:]]c", "12abc45").withResult("abc");
		assertMatch("a[[:print:]]c", "12abc45").withResult("abc");
		assertMatch("a[[:punct:]]c", "12a.c45").withResult("a.c");
		assertMatch("a[[:space:]]c", "12a c45").withResult("a c");
		assertMatch("a[[:upper:]]c", "12aBc45").withResult("aBc");
		assertMatch("a[[:xdigit:]]c", "12aFc45").withResult("aFc");
		try {
			assertNoMatch("a[b[:digits:]]c", "12abc45");
		} catch (IllegalRegularExpressionException e)
		{
			// Should go here
		}
	}
	
	@Test
	public void anchors()
	{
		// ^
		assertNoMatch("^abc", "12abc34");
	    assertMatch("^abc", "abc34").withResult("abc");
	    assertNoMatch("a^bc", "12abc34");
	    
	    // $
	    assertNoMatch("abc$", "12abc34");
	    assertMatch("abc$", "12abc").withResult("abc");
	    assertNoMatch("ab$c", "12abc34");
	    
	    // \A
	    assertNoMatch("\\Aabc", "12abc34");
	    assertMatch("\\Aabc", "abc34").withResult("abc");
	    assertNoMatch("a\\Abc", "12abc34");
	    
	    // \z
	    assertNoMatch("abc\\z", "12abc34");
	    assertMatch("abc\\z", "12abc").withResult("abc");
	    assertNoMatch("ab\\zc", "12abc34");
	    assertNoMatch("abc\\z", "12abc\n");
	    
	    // \Z
	    assertNoMatch("abc\\Z", "12abc34");
	    assertMatch("abc\\Z", "12abc").withResult("abc");
	    assertNoMatch("ab\\Zc", "12abc34");
	    assertMatch("abc\\Z", "12abc\n").withResult("abc");
	    
	    // \b
	    assertMatch("\\bcd", "ab cd").withResult("cd");
	    assertNoMatch("\\bcd", "abcd");
	    assertMatch("ab\\b", "ab cd").withResult("ab");
	    assertNoMatch("ab\\b", "abcd");
	    
	    // \B
	    assertNoMatch("ab\\B", "ab cd");
	    assertMatch("ab\\B", "abcd").withResult("ab");
	    assertNoMatch("\\Bcd", "ab cd");
	    assertMatch("\\Bcd", "abcd").withResult("cd");
	    
	    // \G
	    // TODO: Test with repetitive matches...
	    assertNoMatch("\\Gabc", "12abc34");
	    assertMatch("\\Gabc", "abc34").withResult("abc");
	    assertNoMatch("a\\Gbc", "12abc34");
	}
	
	private void assertNoMatch(String regex, String s)
	{
		assertMatch(regex, s).withResult();
	}
	
	private MatchChecker assertMatch(String regex, String s)
	{
		return new MatchChecker(regex, s);
	}
	
    private static class MatchChecker
    {
    	List<String> result;

    	public MatchChecker(String regex, String s)
    	{
    		result = RegularExpressionTranslator.match(regex, s);
    	}

    	public void withResult(String... expected)
    	{
    		assertEquals(expected.length, result.size());
    		for (int i=0; i<expected.length; i++)
    		{
    			assertEquals("Value " + (i+1) + " wrong", expected[i], result.get(i));
    		}
    	}
    }
}
