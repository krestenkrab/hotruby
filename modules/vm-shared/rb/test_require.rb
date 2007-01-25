


require 'test/unit/testcase'
require 'test/unit/testresult'

class TC_MyTest < Test::Unit::TestCase
  # def setup
  # end
  
  # def teardown
  # end
  
  def test_fail
    assert(false, 'Assertion was false.')
  end
end



tc = TC_MyTest.new("test_fail")
tr = Test::Unit::TestResult.new
tc.run(tr) { |event,name| p "test:#{event} #{name}" }
p tr.to_s


