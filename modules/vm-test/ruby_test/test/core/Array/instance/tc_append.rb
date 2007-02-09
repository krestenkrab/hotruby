############################################################
# tc_append.rb
#
# Test suite for the Array#<< instance method.
############################################################
require "test/unit"

class TC_Array_Append_Instance < Test::Unit::TestCase
   def setup
      @array1 = [1,2,3]
      @array2 = ["hello", "world"]
   end

   def test_append_method_exists
      assert_respond_to(@array1, :<<)
   end

   def test_append_basic
      assert_nothing_raised{ @array1 << 4 }
      assert_equal([1, 2, 3, 4], @array1)

      assert_nothing_raised{ @array1 << [1, 2] }
      assert_equal([1, 2, 3, 4, [1, 2]], @array1)
   end

   def test_append_chained
      assert_nothing_raised{ @array1 << 1 << "hello" << 4.0 }
      assert_equal([1, 2, 3, 1, "hello", 4.0], @array1)
   end

   def test_append_edge_cases
      assert_nothing_raised{ @array2 << nil }
      assert_equal(["hello", "world", nil], @array2)

      assert_nothing_raised{ @array2 << false }
      assert_equal(["hello", "world", nil, false], @array2)

      assert_nothing_raised{ @array2 << true }
      assert_equal(["hello", "world", nil, false, true], @array2)
   end

   def teardown
      @array1 = nil
      @array2 = nil
   end
end
