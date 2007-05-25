

class A
   def self.m1
      m2
   end
   
   def self.m2
      p "A::m2"
   end
   
   def mm1
    mm2
   end
   
   def mm2
     p "A::mm2"
   end
   
end

class B < A   
   def self.m2
      p "B::m2"
   end
   
   def mm2
      p "B::mm2"
   end
end

def test

  c = A
  c = B
  
  B.m1
  B.m2
  
  cc = c.new
  cc.mm1
  cc.mm2
  
end

test
