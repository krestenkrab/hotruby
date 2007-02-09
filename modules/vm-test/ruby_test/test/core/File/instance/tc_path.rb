############################################
# tc_path.rb
#
# Test suite for the File#path method.
############################################
require "test/unit"

class TC_File_Path < Test::Unit::TestCase
   def setup
      @home = ENV["HOME"] || ENV["USERPROFILE"] || Dir.cwd

      if PLATFORM.match("mswin32")
         @file_no_path    = "test1"
         @file_with_path  = @home + "\\test2"
         @file_with_extra = @home + "\\.\\test3"
      else
         @file_no_path    = "test1"
         @file_with_path  = @home + "/test2"
         @file_with_extra = @home + "/./test3"
      end

      @fh_no_path    = File.new(@file_no_path, "wb+")
      @fh_with_path  = File.new(@file_with_path, "wb+")
      @fh_with_extra = File.new(@file_with_extra, "wb+")
   end

   def test_path_basic
      assert_respond_to(@fh_no_path, :path)
      assert_nothing_raised{ @fh_no_path.path }
      assert_kind_of(String, @fh_no_path.path)
   end

   def test_path
      if PLATFORM.match("mswin32")

      else
         assert_equal("test1", @fh_no_path.path)
         assert_equal(@home + "/test2", @fh_with_path.path)
         assert_equal(@home + "/./test3", @fh_with_extra.path)
      end
   end

   def teardown
      @fh_no_path.close
      @fh_with_path.close
      @fh_with_extra.close

      File.unlink(@file_no_path)
      File.unlink(@file_with_path)
      File.unlink(@file_with_extra)

      @fh_no_path    = nil
      @fh_with_path  = nil
      @fh_with_extra = nil
   end
end
