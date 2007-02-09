######################################################################
# tc_length.rb
#
# Test case for the String#length instance method.
######################################################################
require "test/unit"

class TC_String_Length_Instance < Test::Unit::TestCase
   def setup
      @string = "hello" * 1000
   end

   def test_length_basic
      assert_respond_to(@string, :length)
      assert_nothing_raised{ @string.length }
   end

   def test_length
      assert_equal(5000, @string.length)
      assert_equal(0, ''.length)
      assert_equal(1, '/'.length)
      assert_equal(1, "\\".length)
   end

   def test_length_expected_errors
      assert_raises(ArgumentError){ @string.length(1) }
   end

   def teardown
      @string = nil
   end
end
