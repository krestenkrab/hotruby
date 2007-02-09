######################################################################
# tc_blockdev.rb
#
# Test case for the FileStat#blockdev instance method.
######################################################################
require 'test/unit'

class TC_FileStat_Blockdev_Instance < Test::Unit::TestCase
   def setup
      @stat = File::Stat.new(__FILE__)
      @file = '/dev/disk0'

      case RUBY_PLATFORM
         when /solaris|sunos/i
            @file = '/dev/fd0'
         when /mswin/i
            @file = 'NUL'
      end

      @blockdev = File::Stat.new(@file)
   end

   def test_blockdev_basic
      assert_respond_to(@stat, :blockdev?)
   end

   def test_blockdev
      assert_equal(false, @stat.blockdev?)

      unless RUBY_PLATFORM.match('mswin')
         assert_equal(true, File.stat('/dev/diskette').blockdev?)
      end
   end

   def test_blockdev_expected_errors
      assert_raises(ArgumentError){ @stat.blockdev?(1) }
   end

   def teardown
      @stat = nil
      @file = nil
      @blockdev = nil
   end
end
