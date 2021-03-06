######################################################################
# tc_chroot.rb
#
# Test case for the Dir.chroot class method.  Note that many tests
# are skipped on Windows and/or if the test suite is not being run
# as root.
######################################################################
require "test/unit"

class TC_Dir_Chroot_Class < Test::Unit::TestCase
   def setup
      @root = Dir.pwd
   end

   def test_chroot_basic
      assert_respond_to(Dir, :chroot)
      unless RUBY_PLATFORM.match("mswin")
         if Process.uid == 0
            assert_nothing_raised{ Dir.chroot(@root) }
            assert_equal("/", Dir.pwd)
         end
      end
   end

   def test_chroot_expected_failures
      if RUBY_PLATFORM.match("mswin")
         assert_raises(NotImplementedError){ Dir.chroot(@root) }
      else
         assert_raises(TypeError){ Dir.chroot(1) }
         assert_raises(ArgumentError){ Dir.chroot(@root, @root) }
      end
   end

   def teardown
      @root = nil
   end
end
