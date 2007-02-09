###########################################################
# tc_unshift.rb
#
# Test suite for the Array#unshift instance method.
###########################################################
require "test/unit"

class TC_Array_Unshift_Instance < Test::Unit::TestCase
   def setup
      @array = ["a","b","c"]
   end

   def test_basic
      assert_respond_to(@array, :unshift)
      assert_nothing_raised{ @array.unshift }
   end

   def test_unshift
      assert_equal([1,"a","b","c"], @array.unshift(1))
      assert_equal(["d",2,1,"a","b","c"], @array.unshift("d",2))
      assert_equal([nil,[1,2],"d",2,1,"a","b","c"], @array.unshift(nil,[1,2]))
      assert_equal([nil,[1,2],"d",2,1,"a","b","c"], @array)
   end

   def teardown
      @array = nil
   end
end
