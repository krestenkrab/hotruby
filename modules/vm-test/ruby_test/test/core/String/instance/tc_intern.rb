######################################################################
# tc_intern.rb
#
# Test case for the String#intern instance method.
######################################################################
require "test/unit"

class TC_String_Intern_Instance < Test::Unit::TestCase
   def setup
      @string = "<html><b>Hello</b></html>"
   end

   def test_intern_basic
      assert_respond_to(@string, :intern)
      assert_nothing_raised{ @string.intern }
      assert_kind_of(Symbol, @string.intern)
   end

   def test_intern
      assert_equal(@string, @string.intern.to_s)
      assert_equal(:foo, "foo".intern)
      assert_equal(:Foo, "Foo".intern)

      assert_nothing_raised{ ' '.intern }
      assert_nothing_raised{ '0'.intern }
      assert_nothing_raised{ 'nil'.intern }
      assert_nothing_raised{ 'true'.intern }
      assert_nothing_raised{ 'false'.intern }
   end

   def test_intern_expected_errors
      assert_raises(ArgumentError){ ''.intern } # Can't intern empty string
      assert_raises(ArgumentError){ @string.intern(1) }
   end

   def teardown
      @string = nil
   end
end
