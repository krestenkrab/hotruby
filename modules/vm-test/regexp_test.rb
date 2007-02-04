require 'test/unit'

class RegExpTest < Test::Unit::TestCase
  def test_simple
    assert_match ['bcd'], /bcd/, 'abcde'
  end
  
  def test_specialchars
    assert_match ['%&'], /%&/, '!@%&-_=+'
  end
  
  def test_escape
    assert_match ['\[{'], /\\\[\{/, 'abc\[{def'
    assert_match ['bFc'], /b\Fc/, 'abFcd'
    assert_match ['b.c'], /b\.c/, 'ab.cd'
    assert_no_match /b\.d/, 'abcde'
  end
  
  def test_too_many_ending_parentheses
    assert_raise(SyntaxError) do
      eval "assert_match ['abcd)ef', 'cd'], /ab(cd))ef/, '12abcd)ef34'"
    end
  end
  
  def test_group
    assert_match ['abcdef', 'cd'], /ab(cd)ef/, '12abcdef34'
    assert_match ['abcdefghij', 'cd', 'gh'], /ab(cd)ef(gh)ij/, '12abcdefghij34'
    assert_match ['abcd', 'bc', 'c'], /a(b(c))d/, '12abcd34'
    assert_match ['abd', 'b', nil], /a(b(c)?)d/, '12abd34'
    assert_match ['ad', nil, nil], /a(b(c)?)?d/, '12ad34'
  end
  
  def test_alternation
    assert_match ['abc', 'b'], /a(b|3)c/, '12abc45'
    assert_match ['a3c', '3'], /a(b|3)c/, '12a3c45'
    assert_match ['abc'], /abc|a3c/, '12abc45'
    assert_match ['a3c'], /abc|a3c/, '12a3c45'
  end
  
  def test_zero_or_more
    assert_match ['ac'], /ab*c/, 'ac'
    assert_match ['abc'], /ab*c/, 'abc'
    assert_match ['abbc'], /ab*c/, 'abbc'
  end
  
  def test_optional
    assert_match ['ac'], /ab?c/, 'ac'
    assert_match ['abc'], /ab?c/, 'abc'
    assert_no_match /ab?c/, 'abbc'
  end
  
  def test_one_or_more
    assert_no_match /ab+c/, 'ac'
    assert_match ['abc'], /ab+c/, 'abc'
    assert_match ['abbc'], /ab+c/, 'abbc'
  end
  
  def test_repetition_min_max_specified
    assert_no_match /ab{2,4}c/, 'abc'
    assert_match ['abbc'], /ab{2,4}c/, 'abbc'
    assert_match ['abbbbc'], /ab{2,4}c/, 'abbbbc'
    assert_no_match /ab{2,4}c/, 'abbbbbc'

    # If any whitespace is included, it never matches
    assert_no_match /ab{2 ,4}c/, 'abbc'
    assert_no_match /ab{ 2,4}c/, 'abbc'
    assert_no_match /ab{2, 4}c/, 'abbc'
    assert_no_match /ab{2,4 }c/, 'abbc'
  end
  
  def test_repetition_exact
    assert_no_match /ab{2}c/, 'abc'
    assert_match ['abbc'], /ab{2}c/, 'abbc'
    assert_no_match /ab{2}c/, 'abbbc'
    
    # If any whitespace is included, it never matches
    assert_no_match /ab{ 2}c/, 'abbc'
    assert_no_match /ab{2 }c/, 'abbc'
  end
  
  def test_repetition_minimum
    assert_no_match /ab{2,}c/, 'abc'
    assert_match ['abbc'], /ab{2,}c/, 'abbc'
    assert_match ['abbbbc'], /ab{2,}c/, 'abbbbc'

    # If any whitespace is included, it never matches
    assert_no_match /ab{ 2,}c/, 'abbc'
    assert_no_match /ab{2 ,}c/, 'abbc'
    assert_no_match /ab{2, }c/, 'abbc'
  end
  
  def test_backreference
    assert_no_match /a(b)\1c/, 'abc'
    assert_match ['abbc', 'b'], /a(b)\1c/, 'abbc'
    assert_match ['ab&c', 'b'], /a(b)\&c/, 'ab&c'
    assert_match ['ab`c', 'b'], /a(b)\`c/, 'ab`c'
    assert_match ["ab'c", 'b'], /a(b)\'c/, "ab'c"
    assert_match ['ab+c', 'b'], /a(b)\+c/, 'ab+c'
  end
  
  def test_predefined_character_classes
    # \d, \D
    assert_match ['a3b'], /a\db/, '12a3b45'
    assert_no_match /a\Db/, '12a3b45'
    assert_match ['a b'], /a\Db/, '12a b45'

    # \s, S
    assert_match ['a b'], /a\sb/, '12a b45'
    assert_no_match /a\Sb/, '12a b45'
    assert_match ['a3b'], /a\Sb/, '12a3b45'

    # \w, \W
    assert_match ['abc'], /a\wc/, '12abc45'
    assert_no_match /a\Wc/, '12abc45'
    assert_no_match /a\Wc/, '12a3c45'
    assert_match ['a c'], /a\Wc/, '12a c45'

    assert_match ['abc'], /a.c/, '12abc45'
    assert_no_match /a.c/, "12a\nc45"
  end
  
  def test_character_classes
    assert_match ['a3c'], /a[3b]c/, '12a3c45'
    assert_match ['abc'], /a[3b]c/, '12abc45'
		assert_match ['abc'], /a[b-f]c/, '12abc45'
		assert_match ['aec'], /a[b-f]c/, '12aec45'
		assert_match ['a-c'], /a[-a-c]c/, '12a-c45'
		assert_match ['abc'], /a[-a-c]c/, '12abc45'
		assert_match ['a-c'], /a[-----a-c]c/, '12a-c45'
		assert_match ['a-c'], /a[a-c-]c/, '12a-c45'
		assert_raise(SyntaxError) do
		  eval("assert_match ['a-c'], /a[a--c]c/, '12a-c45'")
		end
		assert_match ['a]c'], /a[ab\]]c/, '12a]c45'
		assert_no_match /a[ab\]]c/, '12a\c45'
		
		# Inverse classes
		assert_match ['a3c'], /a[^bc]c/, '12a3c45'
		assert_no_match /a[^bc]c/, '12abc45'
		assert_match ['a3c'], /a[^^^^^bc]c/, '12a3c45'
		assert_no_match /a[^^^^^bc]c/, '12a^c45'
		assert_no_match /a[^-bc]c/, '12a-c45'
		
		# Escapes
		assert_match ['a3c'], /a[a-c\d]c/, '12a3c45'
		assert_no_match /a[a-c\d]c/, '12aec45'
		assert_raise(SyntaxError) do
  		eval("assert_match ['aFc'] /a[a-c\F]c/, '12aFc45'")
		end
		assert_match ['a-c'], /a[a-c\-]c/, '12a-c45'
		assert_match ['a.c'], /a[abc\.]c/, '12a.c45'
		assert_match ['a.c'], /a[abc.]c/, '12a.c45'
		assert_match ['a\\c'], /a[ab\\c]c/, '12a\\c45'
		assert_match ['abc'], /a[ab\d]c/, '12abc45'
		assert_no_match /a[ab\d]c/, '12adc45'
		
		# POSIX character classes
		assert_match ['a3c'], /a[b[:digit:]]c/, '12a3c45'
		assert_match ['abc'], /a[b[:digit:]]c/, '12abc45'
		assert_match ['abc'], /a[[:alnum:]]c/, '12abc45'
		assert_match ['a3c'], /a[[:alnum:]]c/, '12a3c45'
		assert_match ['abc'], /a[[:alpha:]]c/, '12abc45'
		assert_match ['a c'], /a[[:blank:]]c/, '12a c45'
		assert_match ["a\nc"], /a[[:cntrl:]]c/, "12a\nc45"
		assert_match ['a.c'], /a[[:graph:]]c/, "12a.c45"
		assert_match ['abc'], /a[[:lower:]]c/, "12abc45"
		assert_match ['abc'], /a[[:print:]]c/, "12abc45"
		assert_match ['a.c'], /a[[:punct:]]c/, "12a.c45"
		assert_match ['a c'], /a[[:space:]]c/, "12a c45"
		assert_match ['aBc'], /a[[:upper:]]c/, "12aBc45"
		assert_match ['aFc'], /a[[:xdigit:]]c/, "12aFc45"
		assert_raise(SyntaxError) do
		  eval("assert_no_match /a[b[:digits:]]c/, '12abc45'")
		end
  end
  
  
  def test_anchors
    # ^
    assert_no_match /^abc/, '12abc34'
    assert_match ['abc'], /^abc/, 'abc34'
		assert_no_match /a^bc/, '12abc34'

    # $
    assert_no_match /abc$/, '12abc34'
    assert_match ['abc'], /abc$/, '12abc'
    assert_no_match /ab$c/, '12abc34'
    
    # \A
    assert_no_match /\Aabc/, '12abc34'
    assert_match ['abc'], /\Aabc/, 'abc34'
		assert_no_match /a\Abc/, '12abc34'
		
		# \z
    assert_no_match /abc\z/, '12abc34'
    assert_match ['abc'], /abc\z/, '12abc'
    assert_no_match /ab\zc/, '12abc34'
    assert_no_match /abc\z/, "12abc\n"
		
		# \Z
    assert_no_match /abc\Z/, '12abc34'
    assert_match ['abc'], /abc\Z/, '12abc'
    assert_no_match /ab\Zc/, '12abc34'
    assert_match ['abc'], /abc\Z/, "12abc\n"
    
    # \b
    assert_match ['cd'], /\bcd/, 'ab cd'
    assert_no_match /\bcd/, 'abcd'
    assert_match ['ab'], /ab\b/, 'ab cd'
    assert_no_match /ab\b/, 'abcd'
    
    # \B
    assert_no_match /ab\B/, 'ab cd'
    assert_match ['ab'], /ab\B/, 'abcd'
    assert_no_match /\Bcd/, 'ab cd'
    assert_match ['cd'], /\Bcd/, 'abcd'
    
    # \G
	  # TODO: Test with repetitive matches...
    assert_no_match /\Gabc/, '12abc34'
    assert_match ['abc'], /\Gabc/, 'abc34'
		assert_no_match /a\Gbc/, '12abc34'
  end
  
  def test_extensions
    # Comment
    assert_match ['abc'], /a(?# my comment )bc/, 'abc'
    assert_match ['abc'], /a(?#my comment)bc/, 'abc'
    assert_match ['abc'], /a(?# my comment \)bc/, 'abc'
    assert_match ['abc'], /a(?# my comment ()bc/, 'abc'

    # Group without backreference
    assert_match ['abc', 'a', 'c'], /(a)(?:b)(c)/, '12abc34'
    
    # Zero-width positive lookahead
    assert_match ['a'], /[a-z]+(?=,)/, '12a,c34'
    assert_no_match /[a-z]+(?=,)/, '12ac34'
    
    # Zero-width negative lookahead
    assert_match ['a'], /a(?!c)/, '12abc34'
    assert_no_match /a(?!b)/, '12abc34'
    
    # Independent regular expression
    assert_match ['abbc'], /a(?>b*)c/, '12abbc45'
    assert_match ['abbc', 'b'], /a(?>(b)*)c/, '12abbc45'

    # Unknown extension
    assert_raise(RegexpError) do
      Regexp.new('a(?<b*)c');
    end
  end
  
  private
  def assert_match(result, regexp, input)
    assert_equal result, regexp.match(input).to_a
  end
end