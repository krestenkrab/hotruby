require 'test/unit/testcase'
require 'test/unit/testresult'
#require 'test/unit'

class RegexpTest < Test::Unit::TestCase
  def test_array_access
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal("HX1138", m[0])
    assert_equal(["H", "X"], m[1, 2])
    assert_equal(["H", "X", "113"], m[1..3])
    assert_equal(["X", "113"], m[-3, 2])
    # TODO assert_raises(ArgumentError) { m[1, 2, 3] }
    # TODO assert_raises(ArgumentError) { m[] }
  end
  
  def test_to_a
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal(["HX1138", "H", "X", "113", "8"], m.to_a)

#   TODO: Kresten, på arbejde :-) "to_a" skal automatisk kaldes på højresiden,
#   så man får denne nifty, kompakte ting:
#    all,f1,f2,f3 = *(/(.)(.)(\d+)(\d)/.match("THX1138."))
#    assert_equal("HX1138", all)
#    assert_equal("H", f1)
#    assert_equal("X", f2)
#    assert_equal("113", f3)
  end
  
  def test_begin
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal 1, m.begin(0)
    assert_equal 2, m.begin(2)
  end
  
  def test_end
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal 7, m.end(0)
    assert_equal 3, m.end(2)
  end
  
  def test_to_s
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal("HX1138", m.to_s)
  end

  def test_select
    # TODO: The RubyDoc description is wrong
  end

  def test_pre_match
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal "T", m.pre_match
  end

  def test_post_match
    m = /(.)(.)(\d+)(\d)/.match("THX1138: The Movie")
    assert_equal ": The Movie", m.post_match
  end

  def test_length
    # TODO
  end

  def test_offset
    # TODO
  end

  def test_size
    # TODO
  end

  def test_string
    m = /(.)(.)(\d+)(\d)/.match("THX1138.")
    assert_equal("THX1138.", m.string)
    # TODO assert m.string.frozen?
  end

  def test_captures
    # TODO
  end

  def test_values_at
    m = /(.)(.)(\d+)(\d)/.match("THX1138: The Movie")
    assert_equal(["HX1138", "H", "X", "113", "8"], m.to_a)
    assert_equal(["HX1138", "X", "113"], m.values_at(0, 2, -2))
  end
end

def do_test(name)
  p name
  tc = RegexpTest.new(name)
  tr = Test::Unit::TestResult.new
  tc.run(tr) do |event,name|
    #p "test:#{event} #{name}"
  end
  p tr.to_s
end

do_test "test_array_access"
do_test "test_to_a"
do_test "test_begin"
do_test "test_end"
do_test "test_to_s"
do_test "test_select"
do_test "test_pre_match"
do_test "test_length"
do_test "test_offset"
do_test "test_post_match"
do_test "test_size"
do_test "test_string"
do_test "test_captures"
do_test "test_values_at" 