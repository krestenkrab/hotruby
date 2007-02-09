###########################################################
# tc_intersection.rb
#
# Test suite for the Array#& instance method.
###########################################################
require "test/unit"

class TC_Array_Intersection_Instance < Test::Unit::TestCase
   def setup
      @array1 = [1, 2, 2, 3, "hello", "world", 4, 5, 6, nil, false]
      @array2 = [2, 3, "hello"]
      @array3 = [2, nil, false, true]
   end

   def test_intersection_exists
      assert_respond_to(@array1, :&)
   end

   def test_intersection_basic
      assert_nothing_raised{ @array1 & @array2 }
      assert_nothing_raised{ @array1 & @array2 & @array3}
      assert_nothing_raised{ @array1 & [1,2,3] }
      assert_equal([2, 3, "hello"], @array1 & @array2)
      assert_equal([2], @array1 & @array2 & @array3)
   end

   def test_intersection_edge_cases
      assert_equal([2, nil, false], @array1 & @array3)
      assert_equal([], [] & [])
      assert_equal([], [] & [0])
      assert_equal([], [] & [nil])
      assert_equal([nil], [nil] & [nil, 1])
   end

   def test_intersection_expected_errors
      assert_raises(TypeError){ @array1 & 1 }
      assert_raises(TypeError){ @array1 & nil }
      assert_raises(TypeError){ @array1 & true }
      assert_raises(TypeError){ @array1 & false }
      assert_raises(TypeError){ @array1 & "hello" }
   end
   
   def teardown
      @array1 = nil
      @array2 = nil
      @array3 = nil
   end
end
