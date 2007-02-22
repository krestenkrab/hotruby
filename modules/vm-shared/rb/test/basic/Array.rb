require 'test/unit/testcase'
require 'test/unit/testresult'
#require 'test/unit'

class ArrayTest < Test::Unit::TestCase

   def test_empty
      assert_equal([], Array.new)
   end

   def test_size
      assert_equal([], Array.new(0))
      assert_equal([nil], Array.new(1))
      assert_equal([nil,nil], Array.new(2, nil))
      assert_equal([false,false], Array.new(2, false))
   end
   
   def test_size_errors     
      p "A"
      assert_raises(ArgumentError){ Array.new(-1) }
      p "A2"
      assert_raises(ArgumentError){ Array.new(-999999999) }
      p "A3"
      assert_raises(TypeError){ Array.new(nil) }
      p "A4"
   end
   
   def test_size_plus_object
      assert_equal([], Array.new(0, "test"))
      assert_equal(["foo"], Array.new(1, "foo"))
      assert_equal([2], Array.new(1,2))
      assert_equal(["bar", "bar", "bar"], Array.new(3, "bar"))
   end
   
   def test_size_plus_object_errors   
      assert_raises(ArgumentError){ Array.new(-1, "baz") }
   end
   
   def test_array
      assert_equal([], Array.new([]))
      assert_equal([1,2,3], Array.new([1,2,3]))
   end
   
   def test_size_with_block
      assert_equal([], Array.new{ Hash.new })
      assert_equal([{}, {}], Array.new(2){ Hash.new })
      assert_equal([0,1,4,9], Array.new(4){ |i| i * i })
   end
   
   def test_size_with_block_errors   
      assert_raises(ArgumentError){ Array.new(-1){ Hash.new } }
   end


end

def do_test(name)
  p name
  tc = ArrayTest.new(name)
  tr = Test::Unit::TestResult.new
  tc.run(tr) do |event,name|
    #p "test:#{event} #{name}"
  end
  p tr.to_s
end

##do_test "test_empty"
##do_test "test_size"
do_test "test_size_errors"
##do_test "test_size_plus_object"
#do_test "test_size_plus_object_errors"
#do_test "test_array"
#do_test "test_size_with_block"
#do_test "test_size_with_block_errors"
