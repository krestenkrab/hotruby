######################################################################
# tc_entries.rb
#
# Test case for the Dir.entries class method.
######################################################################
require "test/unit"

class TC_Dir_Entries_Class < Test::Unit::TestCase
   def setup
      @pwd = Dir.pwd
      if PLATFORM.match("mswin")
         @entries = `dir /A /B`.split("\n").push('.', '..')
      else
         @entries = `ls -1`.split("\n")
         @entries.push('.') unless @entries.include?('.')
         @entries.push('..') unless @entries.include?('..')
      end
   end

   def test_entries_basic
      assert_respond_to(Dir, :entries)
      assert_nothing_raised{ Dir.entries(@pwd) }
      assert_kind_of(Array, Dir.entries(@pwd))
   end

   def test_entries
      assert_equal(@entries.sort, Dir.entries(@pwd).sort)
   end

   def test_entries_expected_errors
      assert_raises(ArgumentError){ Dir.entries }
      assert_raises(ArgumentError){ Dir.entries(@pwd, @pwd) }
      assert_raises(TypeError){ Dir.entries(1) }
      assert_raises(Errno::ENOENT){ Dir.entries("bogus") }
   end

   def teardown
      @pwd     = nil
      @entries = nil
   end
end
