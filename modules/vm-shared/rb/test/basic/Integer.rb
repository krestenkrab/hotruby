require 'test/unit/testcase'
require 'test/unit/testresult'
#require 'test/unit'

class IntegerTest < Test::Unit::TestCase
  def test_upto
    a = []
    5.upto(9) { |i| a << i }
    assert_equal([5, 6, 7, 8, 9], a)
    a = []
    5.upto(5) { |i| a << i }
    assert_equal([5], a)
    a = []
    5.upto(4) { |i| a << i }
    assert_equal([], a)
  end
  
  def test_downto
    a = []
    5.downto(1) { |i| a << i }
    assert_equal([5, 4, 3, 2, 1], a)
    a = []
    5.downto(5) { |i| a << i }
    assert_equal([5], a)
    a = []
    5.downto(6) { |i| a << i }
    assert_equal([], a)
  end
  
  # TODO: Lots of other test cases!
end

def do_test(name)
  p name
  tc = IntegerTest.new(name)
  tr = Test::Unit::TestResult.new
  tc.run(tr) do |event,name|
    #p "test:#{event} #{name}"
  end
  p tr.to_s
end

do_test "test_upto"
do_test "test_downto"
