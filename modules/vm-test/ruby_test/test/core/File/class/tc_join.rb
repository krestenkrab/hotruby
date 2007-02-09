##########################################################
# tc_join.rb
#
# Test suite for the File.join method
##########################################################
require "test/unit"

class TC_File_Join < Test::Unit::TestCase
   if File::ALT_SEPARATOR
      def test_edge_cases_windows
      end
   else
      def test_edge_cases_unix
         assert_equal("", File.join(""))
         assert_equal("/foo", File.join("", "foo"))
      end
   end

   def test_expected_errors
      assert_raises(TypeError){ File.join(nil, nil) }
   end
end
