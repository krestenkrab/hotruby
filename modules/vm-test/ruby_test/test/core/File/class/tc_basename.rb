#################################################
# tc_basename.rb
#
# Test suite for the File.basename method.
#################################################
require "test/unit"

class TC_File_Basename < Test::Unit::TestCase
   def test_basic
      assert_respond_to(File, :basename)
      assert_nothing_raised{ File.basename("foo") }
      assert_kind_of(String, File.basename("foo"))
   end

   # Except for two cases, these will work on Windows as well
   def test_basename_unix
      assert_equal("bar", File.basename("/foo/bar"))
      assert_equal("bar.txt", File.basename("/foo/bar.txt"))
      assert_equal("bar.c", File.basename("bar.c"))
      assert_equal("bar", File.basename("/bar"))
      assert_equal("bar", File.basename("/bar/"))
      
      # Considered UNC paths on Windows
      unless PLATFORM.match("mswin32")
         assert_equal("foo", File.basename("//foo"))
         assert_equal("baz", File.basename("//foo/bar/baz"))
      end
   end

   def test_basename_edge_cases
      assert_equal("", File.basename(""))
      assert_equal(".", File.basename("."))
      assert_equal("..", File.basename(".."))
      assert_equal("foo", File.basename("//foo/"))
      assert_equal("foo", File.basename("//foo//"))
   end
      
   def test_basename_unix_suffix
      assert_equal("bar", File.basename("bar.c", ".c"))
      assert_equal("bar", File.basename("bar.txt", ".txt"))
      assert_equal("bar", File.basename("/bar.txt", ".txt"))
      assert_equal("bar", File.basename("/foo/bar.txt", ".txt"))
      assert_equal("bar.txt", File.basename("bar.txt", ".exe"))
      assert_equal("bar.txt", File.basename("bar.txt.exe", ".exe"))
      assert_equal("bar.txt.exe", File.basename("bar.txt.exe", ".txt"))
      assert_equal("bar", File.basename("bar.txt", ".*"))
      assert_equal("bar.txt", File.basename("bar.txt.exe", ".*"))
   end

   def test_basename_expected_errors
      assert_raises(TypeError){ File.basename(nil) }
      assert_raises(TypeError){ File.basename(1) }
      assert_raises(TypeError){ File.basename("bar.txt", 1) }
      assert_raises(TypeError){ File.basename(true) }
   end

   # Windows specific tests
   if PLATFORM.match("mswin32")
      require "win32/file"
      def test_basename_windows
         assert_equal("baz.txt", File.basename("C:\\foo\\bar\\baz.txt"))
         assert_equal("bar", File.basename("C:\\foo\\bar"))
         assert_equal("bar", File.basename("C:\\foo\\bar\\"))
         assert_equal("foo", File.basename("C:\\foo"))
         assert_equal("C:\\", File.basename("C:\\"))
      end

      def test_basename_windows_unc
         assert_equal("baz.txt", File.basename("\\\\foo\\bar\\baz.txt"))
         assert_equal("baz", File.basename("\\\\foo\\bar\\baz"))
         assert_equal("\\\\foo", File.basename("\\\\foo"))
         assert_equal("\\\\foo\\bar", File.basename("\\\\foo\\bar"))
      end
         
      def test_basename_windows_forward_slash
         assert_equal("C:/", File.basename("C:/"))
         assert_equal("foo", File.basename("C:/foo"))
         assert_equal("bar", File.basename("C:/foo/bar"))
         assert_equal("bar", File.basename("C:/foo/bar/"))
         assert_equal("bar", File.basename("C:/foo/bar//"))
      end

      def test_basename_windows_suffix
         assert_equal("bar", File.basename("c:\\bar.txt", ".txt"))
         assert_equal("bar", File.basename("c:\\foo\\bar.txt", ".txt"))
         assert_equal("bar.txt", File.basename("c:\\bar.txt", ".exe"))
         assert_equal("bar.txt", File.basename("c:\\bar.txt.exe", ".exe"))
         assert_equal("bar.txt.exe", File.basename("c:\\bar.txt.exe", ".txt"))
         assert_equal("bar", File.basename("c:\\bar.txt", ".*"))
         assert_equal("bar.txt", File.basename("c:\\bar.txt.exe", ".*"))
      end

      def test_basename_expected_windows_errors
         assert_raises(ArgumentError){ File.basename("Hello" * 1000) }
      end
   end
          
end
