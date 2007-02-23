require 'test/unit/testcase'
require 'test/unit/testresult'
#require 'test/unit'

class RegexpTest < Test::Unit::TestCase
  def test_simple
    assert_match(['bcd'], /bcd/, 'abcde')
  end
  
  def test_constructors
    assert_match(['bcd'], Regexp.new('bcd'), 'abcde')
    assert_match(['bcd'], Regexp.new('bcd', Regexp::MULTILINE), 'abcde')
    assert_match(['bcd'], Regexp.new('bcd', Regexp::MULTILINE, 'n'), 'abcde')

    assert_match(['bcd'], Regexp.compile('bcd'), 'abcde')
    assert_match(['bcd'], Regexp.compile('bcd', Regexp::MULTILINE), 'abcde')
    assert_match(['bcd'], Regexp.compile('bcd', Regexp::MULTILINE, 'n'), 'abcde')
  end
  
  def test_specialchars
    assert_match(['%&'], /%&/, '!@%&-_=+')
  end
  
  def test_escape
    assert_match(['\[{'], /\\\[\{/, 'abc\[{def')
    assert_match(['bFc'], /b\Fc/, 'abFcd')
    assert_match(['b.c'], /b\.c/, 'ab.cd')
    assert_no_match(/b\.d/, 'abcde')
  end
  
  def test_too_many_ending_parentheses
    # TODO assert_raise(RegexpError) { Regexp.new('ab(cd))ef') }
    # TODO assert_raise(SyntaxError) do
    #  eval "assert_match(['abcd)ef', 'cd'], /ab(cd))ef/, '12abcd)ef34')"
    #end
  end
  
  def test_group
    assert_match(['abcdef', 'cd'], /ab(cd)ef/, '12abcdef34')
    assert_match(['abcdefghij', 'cd', 'gh'], /ab(cd)ef(gh)ij/, '12abcdefghij34')
    assert_match(['abcd', 'bc', 'c'], /a(b(c))d/, '12abcd34')
    assert_match(['abd', 'b', nil], /a(b(c)?)d/, '12abd34')
    assert_match(['ad', nil, nil], /a(b(c)?)?d/, '12ad34')
  end
  
  def test_alternation
    assert_match(['abc', 'b'], /a(b|3)c/, '12abc45')
    assert_match(['a3c', '3'], /a(b|3)c/, '12a3c45')
    assert_match(['abc'], /abc|a3c/, '12abc45')
    assert_match(['a3c'], /abc|a3c/, '12a3c45')
  end
  
  def test_zero_or_more
    assert_match(['ac'], /ab*c/, 'ac')
    assert_match(['abc'], /ab*c/, 'abc')
    assert_match(['abbc'], /ab*c/, 'abbc')
  end
  
  def test_optional
    assert_match(['ac'], /ab?c/, 'ac')
    assert_match(['abc'], /ab?c/, 'abc')
    assert_no_match(/ab?c/, 'abbc')
  end
  
  def test_one_or_more
    assert_no_match(/ab+c/, 'ac')
    assert_match(['abc'], /ab+c/, 'abc')
    assert_match(['abbc'], /ab+c/, 'abbc')
  end
  
  def test_repetition_min_max_specified
    assert_no_match(/ab{2,4}c/, 'abc')
    assert_match(['abbc'], /ab{2,4}c/, 'abbc')
    assert_match(['abbbbc'], /ab{2,4}c/, 'abbbbc')
    assert_no_match(/ab{2,4}c/, 'abbbbbc')

    # If any whitespace is included, it never matches
    assert_no_match(/ab{2 ,4}c/, 'abbc')
    assert_no_match(/ab{ 2,4}c/, 'abbc')
    assert_no_match(/ab{2, 4}c/, 'abbc')
    assert_no_match(/ab{2,4 }c/, 'abbc')
  end
  
  def test_repetition_exact
    assert_no_match(/ab{2}c/, 'abc')
    assert_match(['abbc'], /ab{2}c/, 'abbc')
    assert_no_match(/ab{2}c/, 'abbbc')
    
    # If any whitespace is included, it never matches
    assert_no_match(/ab{ 2}c/, 'abbc')
    assert_no_match(/ab{2 }c/, 'abbc')
  end
  
  def test_repetition_minimum
    assert_no_match(/ab{2,}c/, 'abc')
    assert_match(['abbc'], /ab{2,}c/, 'abbc')
    assert_match(['abbbbc'], /ab{2,}c/, 'abbbbc')

    # If any whitespace is included, it never matches
    assert_no_match(/ab{ 2,}c/, 'abbc')
    assert_no_match(/ab{2 ,}c/, 'abbc')
    assert_no_match(/ab{2, }c/, 'abbc')
  end
  
  def test_backreference
    assert_no_match(/a(b)\1c/, 'abc')
    assert_match(['abbc', 'b'], /a(b)\1c/, 'abbc')
    assert_match(['ab&c', 'b'], /a(b)\&c/, 'ab&c')
    assert_match(['ab`c', 'b'], /a(b)\`c/, 'ab`c')
    assert_match(["ab'c", 'b'], /a(b)\'c/, "ab'c")
    assert_match(['ab+c', 'b'], /a(b)\+c/, 'ab+c')
  end
  
  def test_predefined_character_classes
    # \d, \D
    assert_match(['a3b'], /a\db/, '12a3b45')
    assert_no_match(/a\Db/, '12a3b45')
    assert_match(['a b'], /a\Db/, '12a b45')

    # \s, S
    assert_match(['a b'], /a\sb/, '12a b45')
    assert_no_match(/a\Sb/, '12a b45')
    assert_match(['a3b'], /a\Sb/, '12a3b45')

    # \w, \W
    assert_match(['abc'], /a\wc/, '12abc45')
    assert_no_match(/a\Wc/, '12abc45')
    assert_no_match(/a\Wc/, '12a3c45')
    assert_match(['a c'], /a\Wc/, '12a c45')

    assert_match(['abc'], /a.c/, '12abc45')
    assert_no_match(/a.c/, "12a\nc45")
  end
  
  def test_character_classes
    assert_match(['a3c'], /a[3b]c/, '12a3c45')
    assert_match(['abc'], /a[3b]c/, '12abc45')
    assert_match(['abc'], /a[b-f]c/, '12abc45')
    assert_match(['aec'], /a[b-f]c/, '12aec45')
    assert_match(['a-c'], /a[-a-c]c/, '12a-c45')
    assert_match(['abc'], /a[-a-c]c/, '12abc45')
    assert_match(['a-c'], /a[-----a-c]c/, '12a-c45')
    assert_match(['a-c'], /a[a-c-]c/, '12a-c45')
    # TODO assert_raise(SyntaxError) do
    #  eval("assert_match(['a-c'], /a[a--c]c/, '12a-c45')")
    #end
    assert_match(['a]c'], /a[ab\]]c/, '12a]c45')
    assert_no_match(/a[ab\]]c/, '12a\c45')

    # Inverse classes
    assert_match(['a3c'], /a[^bc]c/, '12a3c45')
    assert_no_match(/a[^bc]c/, '12abc45')
    assert_match(['a3c'], /a[^^^^^bc]c/, '12a3c45')
    assert_no_match(/a[^^^^^bc]c/, '12a^c45')
    assert_no_match(/a[^-bc]c/, '12a-c45')

    # Escapes
    assert_match(['a3c'], /a[a-c\d]c/, '12a3c45')
    assert_no_match(/a[a-c\d]c/, '12aec45')
    # TODO assert_raise(SyntaxError) do
  	#	eval("assert_match(['aFc'] /a[a-c\F]c/, '12aFc45')")
    #end
    assert_match(['a-c'], /a[a-c\-]c/, '12a-c45')
    assert_match(['a.c'], /a[abc\.]c/, '12a.c45')
    assert_match(['a.c'], /a[abc.]c/, '12a.c45')
    # TODO assert_match(['a\\c'], /a[ab\\c]c/, '12a\\c45')
    assert_match(['abc'], /a[ab\d]c/, '12abc45')
    assert_no_match(/a[ab\d]c/, '12adc45')
    assert_match(['abc'], /a[^\[\]]c/, '12abc45')
		
    # POSIX character classes
    assert_match(['a3c'], /a[b[:digit:]]c/, '12a3c45')
    assert_match(['abc'], /a[b[:digit:]]c/, '12abc45')
    assert_match(['abc'], /a[[:alnum:]]c/, '12abc45')
    assert_match(['a3c'], /a[[:alnum:]]c/, '12a3c45')
    assert_match(['abc'], /a[[:alpha:]]c/, '12abc45')
    assert_match(['a c'], /a[[:blank:]]c/, '12a c45')
    # Doesn't work currently because the parser does not convert \n to a newline
    # TODO assert_match(["a\nc"], /a[[:cntrl:]]c/, "12a\nc45")
    assert_match(['a.c'], /a[[:graph:]]c/, "12a.c45")
    assert_match(['abc'], /a[[:lower:]]c/, "12abc45")
    assert_match(['abc'], /a[[:print:]]c/, "12abc45")
    assert_match(['a.c'], /a[[:punct:]]c/, "12a.c45")
    assert_match(['a c'], /a[[:space:]]c/, "12a c45")
    assert_match(['aBc'], /a[[:upper:]]c/, "12aBc45")
    assert_match(['aFc'], /a[[:xdigit:]]c/, "12aFc45")
    # TODO assert_raise(SyntaxError) do
    #  eval("assert_no_match(/a[b[:digits:]]c/, '12abc45')")
    #end
  end
  
  def test_anchors
    # ^
    assert_no_match(/^abc/, '12abc34')
    assert_match(['abc'], /^abc/, 'abc34')
    assert_no_match(/a^bc/, '12abc34')

    # $
    assert_no_match(/abc$/, '12abc34')
    assert_match(['abc'], /abc$/, '12abc')
    assert_no_match(/ab$c/, '12abc34')
    
    # \A
    assert_no_match(/\Aabc/, '12abc34')
    assert_match(['abc'], /\Aabc/, 'abc34')
    assert_no_match(/a\Abc/, '12abc34')
		
    # \z
    assert_no_match(/abc\z/, '12abc34')
    assert_match(['abc'], /abc\z/, '12abc')
    assert_no_match(/ab\zc/, '12abc34')
    assert_no_match(/abc\z/, "12abc\n")
		
    # \Z
    assert_no_match(/abc\Z/, '12abc34')
    assert_match(['abc'], /abc\Z/, '12abc')
    assert_no_match(/ab\Zc/, '12abc34')
    # Doesn't work currently because the parser does not convert \n to a newline
    # TODO assert_match(['abc'], /abc\Z/, "12abc\n")
    
    # \b
    assert_match(['cd'], /\bcd/, 'ab cd')
    assert_no_match(/\bcd/, 'abcd')
    assert_match(['ab'], /ab\b/, 'ab cd')
    assert_no_match(/ab\b/, 'abcd')
    
    # \B
    assert_no_match(/ab\B/, 'ab cd')
    assert_match(['ab'], /ab\B/, 'abcd')
    assert_no_match(/\Bcd/, 'ab cd')
    assert_match(['cd'], /\Bcd/, 'abcd')
    
    # \G
    # TODO: Test with repetitive matches...
    assert_no_match(/\Gabc/, '12abc34')
    assert_match(['abc'], /\Gabc/, 'abc34')
    assert_no_match(/a\Gbc/, '12abc34')
  end
  
  def test_extensions
    # Comment
    assert_match(['abc'], /a(?# my comment )bc/, 'abc')
    assert_match(['abc'], /a(?#my comment)bc/, 'abc')
    assert_match(['abc'], /a(?# my comment \)bc/, 'abc')
    assert_match(['abc'], /a(?# my comment ()bc/, 'abc')

    # Group without backreference
    assert_match(['abc', 'a', 'c'], /(a)(?:b)(c)/, '12abc34')
    
    # Zero-width positive lookahead
    assert_match(['a'], /[a-z]+(?=,)/, '12a,c34')
    assert_no_match(/[a-z]+(?=,)/, '12ac34')
    
    # Zero-width negative lookahead
    assert_match(['a'], /a(?!c)/, '12abc34')
    assert_no_match(/a(?!b)/, '12abc34')
    
    # Independent regular expression
    assert_match(['abbc'], /a(?>b*)c/, '12abbc45')
    assert_match(['abbc', 'b'], /a(?>(b)*)c/, '12abbc45')

    # Unknown extension
    # TODO assert_raise(RegexpError) do
    #  Regexp.new('a(?<b*)c')
    #end
  end

  def test_options
    # TODO: Cannot fully test "MULTILINE" option, since escape sequences are not
    #       interpreted in hotruby
    # TODO: Cannot test options given in explicit regular expressions, since
    #       they are not given to the created Regexp class.
  
    assert_equal 1, Regexp::IGNORECASE
    assert_equal 2, Regexp::EXTENDED
    assert_equal 4, Regexp::MULTILINE
    
    # Ignorecase
    assert_no_match(Regexp.new('abc', 0), '12aBc45')
    assert_match(['aBc'], Regexp.new('abc', Regexp::IGNORECASE), '12aBc45')
    # TODO assert_match(['aBc'], /abc/i, '12aBc45')
    assert_match(['abcaBc'], /abc(?i)abc/, '12abcaBc45')
    assert_match(['abcaBc'], /abc(?i:abc)/, '12abcaBc45')
    assert_no_match(/abc(?i)abc(?-i)abc/, '12abcaBcaBc45')
    assert_no_match(Regexp.new('abc(?-i)abc', Regexp::IGNORECASE), '12aBcaBc45')
    # casefold?
    assert !(Regexp.new('abc', 0).casefold?)
    assert Regexp.new('abc', Regexp::IGNORECASE).casefold?
		
    # Extended
# TODO    s = <<HERE
#      a #A comment
#      # Another comment
#      bc
#HERE
#    assert_match(['abc'], Regexp.new(s, Regexp::EXTENDED), '12abc45')
    # TODO assert_match(['abc'], /a #A comment
    #  # Another comment
    #  bc/x, '12abc45')
#    t = <<HERE
#      a #A comment
#      (?-x) # Another comment
#      bc
#HERE
#    assert_no_match(Regexp.new(t, Regexp::EXTENDED), '12abc45')

    # Multiline
    assert_no_match(/12a\nc45/, 'a.b')
#    assert_match(['abc'], /^abc/, "12\nabc45")
# TODO: Convert these to Ruby when character esccaping works
#	    assertMatch("^abc", "12\nabc45", RegularExpressionTranslator.MULTILINE).withResult("abc");
#	    assertMatch("abc(?m)a\nc", "12abca\nc45").withResult("abca\nc");
#	    assert_match(['abc'], /abc$/, "12abc\n45")
#	    assertMatch("abc$", "12abc\n45", RegularExpressionTranslator.MULTILINE).withResult("abc");
#	    assertMatch("abc(?m)abc$", "12abcabc\n45").withResult("abcabc");
#	    assertNoMatch("a.c", "12a\nc45");
#	    assertMatch("a.c", "12a\nc45", RegularExpressionTranslator.MULTILINE).withResult("a\nc");
#	    assertMatch("a.c(?m)a.c", "12abca\nc45").withResult("abca\nc");
#	    assertNoMatch("a.c(?-m)a.c", "12a\nca\nc45", RegularExpressionTranslator.MULTILINE);
	    
    # Total confusion of option switching
    assert_no_match(/(?i)a(?i-i)bc/, '12aBc45')
    assert_no_match(/a(?i-i)bc/, '12aBc45')
    #assert_raise(RegexpError) { Regexp.new('b(?ih)c') }
  end
  
  def test_equals
    assert !(/abd/ == /abc/), 1
    assert /abd/ != /abc/, 2
    assert /abc/ == /abc/, 3
    assert /abc/ == Regexp.new('abc'), 4
    assert Regexp.new('abc') == /abc/, 5
    assert !(/abc/ == /abc/x), 6
    assert /abc/ != /abc/x, 7
    assert !(/abc/ == /abc/i), 8
    assert /abc/ != /abc/i, 9
    assert /abc/ != 'abc'
  end
  
  def test_case_equality
    assert /abc/ === '12abc45'
    assert !(/abd/ === '12abc45')
    case '12abc45'
      when /abd/: res = 1
      when /abc/: res = 2
      when /abe/: res = 3
      else res = 4
    end
    assert_equal 2, res
  end
  
  def test_eqtilde
    assert '12abc45' =~ /abc/
    assert !('12abd45' =~ /abc/)
  end
  
  def test_to_s
    assert_equal '(?-mix:abc)', /abc/.to_s
    assert_equal '(?mix:abc)', Regexp.new('abc',
      Regexp::MULTILINE | Regexp::IGNORECASE | Regexp::EXTENDED).to_s
  end
  
  def test_inspect
    assert_equal '/ab/', /ab/.inspect
    assert_equal '/ab/mx', Regexp.new('ab', Regexp::MULTILINE | Regexp::EXTENDED).inspect
    assert_equal '/ab/mix', Regexp.new('ab', Regexp::MULTILINE | Regexp::IGNORECASE | Regexp::EXTENDED).inspect
  end
  
  def test_match_constant
    $_ = '12abc45'
    assert ~ /abc/
    assert !(~ /abd/)
  end
  
  def test_union
    assert_equal '(?!)', Regexp.union().source
    assert_equal 'abc', Regexp.union('abc').source
    assert_equal 'a\(c', Regexp.union('a(c').source
    assert_equal 'abc', Regexp.union(/abc/).source
    assert_equal 'abc|def', Regexp.union('abc', 'def').source
    assert_equal 'abc|def|ghi', Regexp.union('abc', 'def', 'ghi').source
    assert_equal '(?-mix:abc)|(?-mix:def)', Regexp.union(/abc/, /def/).source
    assert_equal '(?-mix:abc)|(?-mix:def)|(?-mix:ghi)', Regexp.union(/abc/, /def/, /ghi/).source
    assert_equal 'abc|(?-mix:def)', Regexp.union('abc', /def/).source
  end
  
  def test_globals
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    match = $~
    assert_equal 'cdefghijklmnop', $&
    assert_equal 'cdefghijklmnop', match[0]
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    assert_equal 'ab', $`
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    assert_equal 'qr', $'
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    assert_equal 'c', $1
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    assert_equal 'd', $2
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    assert_equal 'l', $10
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    assert_equal 'p', $14
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/

    /(a)(b)(c)/.match('12abc45')
    match = $~
    assert_equal 'abc', $&
    assert_equal 'abc', match[0]
    /(a)(b)(c)/.match('12abc45')
    assert_equal '12', $`
    /(a)(b)(c)/.match('12abc45')
    assert_equal '45', $'
    /(a)(b)(c)/.match('12abc45')
    assert_equal 'a', $1
    /(a)(b)(c)/.match('12abc45')
    assert_equal nil, $14
    
    /abc/.match('hello')
    match = $~
    assert_equal nil, $&
    assert_equal nil, match
    /abc/.match('hello')
    assert_equal nil, $`
    /abc/.match('hello')
    assert_equal nil, $'
    /abc/.match('hello')
    assert_equal nil, $1
  end

  def test_last_match
    'abcdefghijklmnopqr' =~ /(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)(n)(o)(p)/
    last_match = Regexp.last_match
    last_match_zero = Regexp.last_match 0
    last_match_one = Regexp.last_match 1
    assert_equal 'cdefghijklmnop', last_match[0]
    assert_equal 'cdefghijklmnop', last_match_zero
    assert_equal('c', last_match_one)

    /abc/.match('hello')
    last_match = Regexp.last_match
    last_match_zero = Regexp.last_match 0
    last_match_one = Regexp.last_match 1
    assert_equal nil, last_match
    assert_equal nil, last_match_zero
    assert_equal nil, last_match_one
  end
  
  private
#  def assert(val, msg)
#    p "Fejl: #{msg}" if !val
#    fail if !val
#  end

#  def assert_equal(val1, val2)
#    p "Fejl: #{val1} != #{val2}" if val1 != val2
#    
#  end
#  
  def assert_match(result, regexp, input)
    match = regexp.match(input).to_a
    if (match != result)
      p "No match #{regexp}: Expected #{result}, was #{match}"
    end
    assert_equal result, regexp.match(input).to_a
  end
  
  def assert_no_match2(regexp, input)
    p regexp
    assert_no_match(regexp, input)
  end
end

def do_test(name)
  p name
  tc = RegexpTest.new(name)
  tr = Test::Unit::TestResult.new
  tc.run(tr) do |event,name|
    #p "test:#{event} #{name}"
  end
  p tr.to_s
end

do_test "test_simple"
do_test "test_constructors"
do_test "test_specialchars"
do_test "test_escape"
do_test "test_too_many_ending_parentheses"
do_test "test_group"
do_test "test_alternation"
do_test "test_zero_or_more"
do_test "test_optional"
do_test "test_one_or_more"
do_test "test_repetition_min_max_specified"
do_test "test_repetition_exact"
do_test "test_repetition_minimum"
do_test "test_backreference"
do_test "test_predefined_character_classes"
do_test "test_character_classes"
do_test "test_anchors"
do_test "test_extensions"
do_test "test_options"
do_test "test_equals"
do_test "test_case_equality"
do_test "test_eqtilde"
do_test "test_to_s"
do_test "test_inspect"
do_test "test_match_constant"
#do_test "test_union"
do_test "test_globals"
do_test "test_last_match"