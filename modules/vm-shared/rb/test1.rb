
#public
#
#

module X
  def X.foo() 
     p "xxx"
  end
  p "xx"
  module Y
   p Module.nesting
  end
  class ::Object < nil
     p Module.nesting
  end
end

X.foo

class T
  def T.xx
    p "static xx in T"
  end
  def xx
    @voo = 1
    p "instance xx in T : #{@voo}"
  end
end

T.xx

t = T.new
t.xx

def fib(n)
    if n < 2
      n
    else
      fib(n - 2) + fib(n - 1)
    end
end

def test(n)
  before = Time.new
  fib(34)
  after = Time.new
  p "time spent: #{after-before}"
end

test(34)
test(34)
test(34)
test(34)
