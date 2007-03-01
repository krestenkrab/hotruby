require 'test/unit/testcase'
require 'test/unit/testresult'

#
# Tests Numeric operators for fixnums, bignums and floats
#
class NumericTest < Test::Unit::TestCase

  def test_fixnum_bignum_ranges
    n = 1073741823 # 2^30-1
    assert_equal(Fixnum, n.class);

    n = 1073741824 # 2^30
    assert_equal(Bignum, n.class)

    n = -1073741824 # -2^30
    assert_equal(Fixnum, n.class);

    n = -1073741825 # -2^30-1
    assert_equal(Bignum, n.class)
  end


  def test_unaryplus
    # Fixnums
    assert_equal(10, +10)
    assert_equal(0, +0)

    # Bignums
    # TODO: Giver exception
    #assert_equal(9999999999, +9999999999)

    # Floats
    assert_equal(10.1, +10.1)
  end
  
  def test_unaryminus
    assert_equal(10-20, -10)
    assert_equal(9.9-20, -10.1)
  end
     
  def test_abs
    assert_equal(12, 12.abs)
    assert_equal(34.56, (-34.56).abs)
    assert_equal(34.56, -34.56.abs) # JIRA HR-6
  end
  
  def test_coerce
    assert_equal([2.5, 1.0], 1.coerce(2.5))
    assert_equal([3.0, 1.2], 1.2.coerce(3))
    assert_equal([2, 1], 1.coerce(2))
    assert_equal([2.5, 1.5], 1.5.coerce(2.5))
  end
  
  def test_divmod
    assert_equal([3, 2], 11.divmod(3))
    assert_equal([-4, -1], 11.divmod(-3))
    assert_equal([3.0, 0.5], 11.divmod(3.5))
    assert_equal([-4.0, 3.0], (-11).divmod(3.5))
    assert_equal([3.0, 1.0], (11.5).divmod(3.5))
  end
  
  def test_eql?
    assert_equal(false, 1.eql?(1.0))
    assert_equal(true, (1.0).eql?(1.0))
    assert_equal(false, 1.eql?(2))
    assert_equal(false, (1.0).eql?(2.0))
  end

  def test_integer?
    assert_equal(true, 1.integer?)
    # TODO: can currently not compile
    #assert_equal(true, 9999999999.integer?)
    assert_equal(false, 1.0.integer?)
  end

  def test_modulo
    assert_equal(1, 13.modulo(4))
    assert_equal(-3, 13.modulo(-4))
    assert_equal(3, (-13).modulo(4))
    assert_equal(-1, (-13).modulo(-4))

    assert_equal(3.5, 11.5.modulo(4))
    assert_equal(-0.5, 11.5.modulo(-4))
    assert_equal(0.5, (-11.5).modulo(4))
    assert_equal(-3.5, (-11.5).modulo(-4))
  end

  def test_nonzero?
    assert_equal(false, 0.nonzero?)
    assert_equal(false, 0.0.nonzero?)
    assert_equal(true, 1.nonzero?)
    assert_equal(true, 1.0.nonzero?)
    # TODO: can currently not compile
    #assert_equal(true, 9999999999.zero?)
  end

  def test_remainder
    assert_equal(1, 13.remainder(4))
    assert_equal(1, 13.remainder(-4))
    assert_equal(-1, (-13).remainder(4))
    assert_equal(-1, (-13).remainder(-4))

    assert_equal(3.5, (11.5).remainder(4))
    assert_equal(3.5, (11.5).remainder(-4))
    assert_equal(-3.5, (-11.5).remainder(4))
    assert_equal(-3.5, (-11.5).remainder(-4))
  end

  def test_zero?
    assert_equal(true, 0.zero?)
    assert_equal(true, 0.0.zero?)
    assert_equal(false, 1.zero?)
    assert_equal(false, 1.0.zero?)
    # TODO: can currently not compile
    #assert_equal(false, 9999999999.zero?)
  end
end

def do_test(name)
  p name
  tc = NumericTest.new(name)
  tr = Test::Unit::TestResult.new
  tc.run(tr) do |event,name|
    #p "test:#{event} #{name}"
  end
  p tr.to_s
end

#do_test "test_fixnum_bignum_ranges"
#do_test "test_unaryplus"
#do_test "test_unaryminus"
#do_test "test_abs"
#do_test "test_coerce"
#do_test "test_divmod"
#do_test "test_eql?"
#do_test "test_integer?"
#do_test "test_modulo"
#do_test "test_nonzero?"
#do_test "test_remainder"
#do_test "test_zero?"
