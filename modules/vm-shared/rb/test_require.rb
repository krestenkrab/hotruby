


require 'test/unit'
#require 'test/unit/testresult'
#require 'test/unit/testcase'

class TC_MyTest < Test::Unit::TestCase
  # def setup
  # end
  
  # def teardown
  # end
  
  def test_fail
    assert(false, 'Assertion was false.')
  end

  def test_success
    assert(true, 'Assertion was true.')
  end
end



tc = TC_MyTest.new("test_fail")
tr = Test::Unit::TestResult.new
tc.run(tr) { |event,name| p "test:#{event} #{name}" }
p tr.to_s

tc = TC_MyTest.new("test_success")
tr = Test::Unit::TestResult.new
tc.run(tr) { |event,name| p "test:#{event} #{name}" }
p tr.to_s







