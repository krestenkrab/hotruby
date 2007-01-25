

class A
  def m(x=4, tmp=nil)
    x
  end
  def n()
    yield
  end
end


class B < A
  def m(n)
    super(6) 
  end
  def n
    p "aa"
    super
  end
  
end

bb = B.new
p bb.m 1
p bb.n { p "aaa" }


## this special case is tricky
class C < A
  def m(xx, pp=super)
    pp
  end
end

cc = C.new
p cc.m 4

class C < A
  def m(pp=super, xx=nil)
    pp
  end
end

cc = C.new
p cc.m 4


class AA
   def pp(x)
     p x
   end
end


class BB < AA
  def pp(list=[1,2,3])
     list.each { |e| super(e) }
  end
end


bb= BB.new
p bb.pp(['a', 'b', 'c'])

