##############################################################################
# tc_pack.rb
#
# Test suite for the Array#pack instance method.  Note that there is some
# extra handling to deal with big endian versus little endian architectures.
##############################################################################
require "test/unit"

class TC_Array_Pack_Instance < Test::Unit::TestCase
   BIGENDIAN = [1].pack("I") == [1].pack("N")

   def setup
      @array  = %w/alpha beta gamma/
      @bignum = 2**63
   end

   # Helper method for dealing with endian issues.  The +data+ argument can
   # be an array or a string.
   #
   def endian(data)
      data.reverse if BIGENDIAN
   end

   def test_pack_basic
      assert_respond_to(@array, :pack)
      assert_nothing_raised{ @array.pack("a") }
   end

   def test_pack_at
      assert_equal("\000", @array.pack("@"))
      assert_equal("", @array.pack("@0"))
      assert_equal("\000", @array.pack("@1"))
      assert_equal("\000\000", @array.pack("@2"))
   end

   def test_pack_A
      assert_nothing_raised{ @array.pack("A") }
      assert_nothing_raised{ @array.pack("A" * @array.length) }
      assert_equal("a", @array.pack("A"))
      assert_equal("abg", @array.pack("AAA"))
      assert_equal("alpha", @array.pack("A5"))
      assert_equal("alpha  ", @array.pack("A7"))
      assert_equal("alpbetgam", @array.pack("A3a3A3"))
      assert_equal("alpha", @array.pack("A*"))
   end

   def test_pack_a
      assert_nothing_raised{ @array.pack("a") }
      assert_nothing_raised{ @array.pack("a" * @array.length) }
      assert_equal("a", @array.pack("a"))
      assert_equal("abg", @array.pack("aaa"))
      assert_equal("alpha", @array.pack("a5"))
      assert_equal("alpha\000\000", @array.pack("a7"))
      assert_equal("alpbetgam", @array.pack("a3a3a3"))
      assert_equal("alpha", @array.pack("a*"))
   end

   def test_pack_B
      assert_equal("\200", @array.pack("B"))
      assert_equal("\200\000", @array.pack("BB"))
      assert_equal("\200\000\200", @array.pack("BBB"))
      assert_equal("\210", @array.pack("B*"))

      assert_equal("\x61", ["01100001"].pack("B8"))
      assert_equal("\x61", ["01100001"].pack("B*"))
      assert_equal("\x61", ["0110000100110111"].pack("B8"))
      assert_equal("\x61\x37", ["0110000100110111"].pack("B16"))
      assert_equal("\x61\x37", ["01100001", "00110111"].pack("B8B8"))
      assert_equal("\x60", ["01100001"].pack("B4"))
      assert_equal("\x40", ["01100001"].pack("B2")) 
   end

   def test_pack_b
      assert_equal("\001", @array.pack("b"))
      assert_equal("\001\000", @array.pack("bb"))
      assert_equal("\001\000\001", @array.pack("bbb"))
      assert_equal("\021", @array.pack("b*"))

      assert_equal "\x86",     ["01100001"].pack("b8")
      assert_equal "\x86",     ["01100001"].pack("b*")
      assert_equal "\x86",     ["0110000100110111"].pack("b8")
      assert_equal "\x86\xec", ["0110000100110111"].pack("b16")
      assert_equal "\x86\xec", ["01100001", "00110111"].pack("b8b8")
      assert_equal "\x06",     ["01100001"].pack("b4")
      assert_equal "\x02",     ["01100001"].pack("b2")
   end

   def test_pack_C
      assert_equal "ABC",      [ 65, 66, 67 ].pack("C3")
      assert_equal "\377BC",   [ -1, 66, 67 ].pack("C*")
   end

   def test_pack_C_expected_errors
      assert_raises(TypeError){ ['test'].pack("C") }
   end

   def test_pack_c
     assert_equal "ABC",      [ 65, 66, 67 ].pack("c3")
     assert_equal "\377BC",   [ -1, 66, 67 ].pack("c*")
   end

   def test_pack_c_expected_errors
      assert_raises(TypeError){ ['test'].pack("c") }
   end

   def test_pack_D
      assert_equal endian("\000\000\000\000\000\000\000\000"), [0].pack('D')
      assert_equal endian("\000\000\000\000\000\000\360?"),    [1].pack('D')
      assert_equal endian("\000\000\000\000\000\000\360\277"), [-1].pack('D')
      assert_equal endian("\000\000\000\000\000\000\340C"),    [@bignum].pack('D')
   end

   # TODO: Should this be a TypeError?
   def test_pack_D_expected_errors
      assert_raises(ArgumentError){ ['test'].pack("D") }
   end

   def test_pack_d
      assert_equal endian("\000\000\000\000\000\000\000\000"), [0].pack('d')
      assert_equal endian("\000\000\000\000\000\000\360?"),    [1].pack('d')
      assert_equal endian("\000\000\000\000\000\000\360\277"), [-1].pack('d')
      assert_equal endian("\000\000\000\000\000\000\340C"),    [@bignum].pack('d')
   end

   # TODO: Should this be a TypeError?
   def test_pack_d_expected_errors
      assert_raises(ArgumentError){ ['test'].pack("d") }
   end

   def test_pack_E
      assert_equal "\000\000\000\000\000\000\000\000", [0].pack('E')
      assert_equal "\000\000\000\000\000\000\360?",    [1].pack('E')
      assert_equal "\000\000\000\000\000\000\360\277", [-1].pack('E')
      assert_equal "\000\000\000\000\000\000\340C",    [@bignum].pack('E')
   end

   def test_pack_e
      assert_equal "\000\000\000\000", [0].pack('e')
      assert_equal "\000\000\200?",    [1].pack('e')
      assert_equal "\000\000\200\277", [-1].pack('e')
      assert_equal "\000\000\000_",    [@bignum].pack('e')
   end

   def test_pack_F
      assert_equal endian("\000\000\000\000"), [0].pack('F')
      assert_equal endian("\000\000\200?"),    [1].pack('F')
      assert_equal endian("\000\000\200\277"), [-1].pack('F')
      assert_equal endian("\000\000\000_"),    [@bignum].pack('F')
   end

   def test_pack_f
      assert_equal endian("\000\000\000\000"), [0].pack('f')
      assert_equal endian("\000\000\200?"),    [1].pack('f')
      assert_equal endian("\000\000\200\277"), [-1].pack('f')
      assert_equal endian("\000\000\000_"),    [@bignum].pack('f')
   end

   def test_pack_G
      assert_equal "\000\000\000\000\000\000\000\000", [0].pack('G')
      assert_equal "?\360\000\000\000\000\000\000",    [1].pack('G')
      assert_equal "\277\360\000\000\000\000\000\000", [-1].pack('G')
      assert_equal "C\340\000\000\000\000\000\000",    [@bignum].pack('G')
   end

   def test_pack_g
      assert_equal "\000\000\000\000", [0].pack('g')
      assert_equal "?\200\000\000",    [1].pack('g')
      assert_equal "\277\200\000\000", [-1].pack('g')
      assert_equal "_\000\000\000",    [@bignum].pack('g')
   end

   def test_pack_H
      assert_equal "\320", ["test"].pack("H")
      assert_equal "\240", @array.pack("H")
      assert_equal "\245\221\240", @array.pack("H*")
   end

   def test_pack_H_expected_errors
      assert_raises(TypeError){ [0].pack("H") }
   end

   def test_pack_h
      assert_equal "\r", ["test"].pack("h")
      assert_equal "\n", @array.pack("h")
      assert_equal "Z\031\n", @array.pack("h*")
   end

   def test_pack_h_expected_errors
      assert_raises(TypeError){ [0].pack("h") }
   end

   def test_pack_expected_errors
      assert_raises(ArgumentError){ @array.pack }
      assert_raises(ArgumentError){ @array.pack("BBBB") }
      assert_raises(ArgumentError){ @array.pack("bbbb") }
   end

   def teardown
      @array  = nil
      @bignum = nil
   end
end
