#################################################
# tc_difference.rb
#
# Test suite for the Array#- instance method.
#################################################
require "test/unit"

class TC_Array_Difference_Instance < Test::Unit::TestCase
   def setup
      @array1 = [1, 2, 2, 3, 3, 3]
      @array2 = [2, 3]
      @array3 = [1, "hello", "world", nil, true, false]
      @array4 = [nil, true, false]
   end

   def test_difference_method_exists
      assert_respond_to(@array1, :-)
   end

   # Test simple arrays of numbers
   def test_difference_basic
      assert_nothing_raised{ @array1 - @array2 }
      assert_nothing_raised{ @array2 - @array1 }
      assert_equal([1], @array1 - @array2)
      assert_equal([], @array2 - @array1)
   end

   # Test mixed types
   def test_difference_advanced
      assert_nothing_raised{ @array3 - @array4 }
      assert_nothing_raised{ @array4 - @array3 }
      assert_equal([1, "hello", "world"], @array3 - @array4)
      assert_equal([], @array4 - @array3)
   end

   # Ensure that a new array is created when diff'ing two arrays, and
   # that the original arrays are unchanged.
   def test_difference_new_array
      assert_nothing_raised{ @array1 - @array2 }
      assert_equal([1, 2, 2, 3, 3, 3], @array1)
      assert_equal([2, 3], @array2)
   end

   def test_difference_edge_cases
      assert_nothing_raised{ @array1 - [1, 1, 1, 1, 1] }
      assert_nothing_raised{ @array1 - [nil] }
   end

   def test_difference_expected_errors
      assert_raises(TypeError){ @array1 - nil }
      assert_raises(TypeError){ @array1 - 1 }
      assert_raises(TypeError){ @array1 - "hello" }
   end

   def teardown
      @array1 = nil
      @array2 = nil
   end
end
