######################################################################
# tc_mkdir.rb
#
# Test case for the Dir.mkdir class method.
######################################################################
require "test/unit"

class TC_Dir_Mkdir_Class < Test::Unit::TestCase
   def setup
      @dir1 = "bogus1"
      @dir2 = "bogus2"
   end

   def test_mkdir_basic
      assert_respond_to(Dir, :mkdir)
      assert_nothing_raised{ Dir.mkdir(@dir1) }
      assert_nothing_raised{ Dir.mkdir(@dir2, 0777) }
   end

   def test_mkdir
      assert_equal(0, Dir.mkdir(@dir1))
      assert_equal(0, Dir.mkdir(@dir2, 0777))
      unless RUBY_PLATFORM.match("mswin")
         assert_equal(0, File.umask & File.stat(@dir1).mode)
      end
   end

   def test_mkdir_expected_errors
      assert_raises(TypeError){ Dir.mkdir(1) }
      assert_nothing_raised{ Dir.mkdir(@dir1) }
      assert_raises(Errno::EEXIST){ Dir.mkdir(@dir1) }
   end

   def teardown
      if RUBY_PLATFORM.match("mswin")
         system("rmdir #{@dir1}")
         system("rmdir #{@dir2}")
      else
         system("rm -rf #{@dir1}")
         system("rm -rf #{@dir2}")
      end
      @dir = nil
   end
end
