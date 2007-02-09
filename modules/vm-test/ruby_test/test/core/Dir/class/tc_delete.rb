######################################################################
# tc_delete.rb
#
# Test case for the Dir.delete class method.  This also covers the
# Dir.rmdir and Dir.unlink aliases.
######################################################################
require "test/unit"

class TC_Dir_Delete_Class < Test::Unit::TestCase
   def setup
      @cur_dir = Dir.pwd
      @new_dir = "bogus"
      Dir.mkdir(@new_dir)
   end

   def test_delete_basic
      assert_respond_to(Dir, :delete)
      assert_nothing_raised{ Dir.delete(@new_dir) }
   end

   def test_rmdir_basic
      assert_respond_to(Dir, :rmdir)
      assert_nothing_raised{ Dir.rmdir(@new_dir) }
   end

   def test_unlink_basic
      assert_respond_to(Dir, :unlink)
      assert_nothing_raised{ Dir.unlink(@new_dir) }
   end

   def test_delete
      assert_equal(0, Dir.delete(@new_dir))
   end

   def test_rmdir
      assert_equal(0, Dir.rmdir(@new_dir))
   end

   def test_unlink
      assert_equal(0, Dir.rmdir(@new_dir))
   end

   def test_delete_expected_errors
      assert_raises(TypeError){ Dir.delete(1) }
      assert_raises(ArgumentError){ Dir.delete(@new_dir, @new_dir) }
      assert_raises(Errno::EACCES, Errno::ENOTEMPTY){ Dir.delete(@cur_dir) }
   end

   def test_rmdir_expected_errors
      assert_raises(TypeError){ Dir.rmdir(1) }
      assert_raises(ArgumentError){ Dir.rmdir(@new_dir, @new_dir) }
      assert_raises(Errno::EACCES, Errno::ENOTEMPTY){ Dir.rmdir(@cur_dir) }
   end

   def test_unlink_expected_errors
      assert_raises(TypeError){ Dir.unlink(1) }
      assert_raises(ArgumentError){ Dir.unlink(@new_dir, @new_dir) }
      assert_raises(Errno::EACCES, Errno::ENOTEMPTY){ Dir.unlink(@cur_dir) }
   end

   def teardown
      if File.exists?(@new_dir)
         if PLATFORM.match("mswin")
            system("rmdir #{@new_dir}")
         else
            system("rm -rf #{@new_dir}")
         end
      end

      @cur_dir = nil
      @new_dir = nil
   end
end
