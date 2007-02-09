##############################################
# tc_new.rb
#
# Test suite for the Dir.new class method.
##############################################
require "test/unit"

class TC_Dir_New_Class < Test::Unit::TestCase
   def setup
      @dir = "test"
      Dir.mkdir(@dir)
   end
   
   def test_new_basic
      assert_respond_to(Dir, :new)
      assert_nothing_raised{ Dir.new(@dir) }
      assert_kind_of(Dir, Dir.new(@dir))
   end

   # Dir.open with no block is a synonym for Dir.new
   def test_new_alias
      assert_respond_to(Dir, :open)
      assert_nothing_raised{ Dir.open(@dir) }
      assert_kind_of(Dir, Dir.open(@dir))
   end
   
   def test_new_expected_errors
      assert_raises(ArgumentError){ Dir.new }
      assert_raises(TypeError){ Dir.new(nil) }
      assert_raises(TypeError){ Dir.new(true) }
      assert_raises(Errno::ENOENT){ Dir.new("bogus") }
   end
   
   def teardown
      Dir.rmdir(@dir) if File.exists?(@dir)
   end
end
