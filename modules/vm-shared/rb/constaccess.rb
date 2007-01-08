
module Kernel
  def assert(bool)
    if !bool
      p "AssertionFailed"
    end
  end
end


class A
 
  X = 'A'

  assert A == self  
  $top_level_A = self

  class B
    $nested_A_B = self   
  
    assert 'A' == X
    X = 'B'    
    assert 'B' == X
  end
  
  assert 'A' == X
  
  def A.getX
    X
  end
  
end

assert ::Object::A == A

$toplevel_Object = Object

class C

   assert A == $top_level_A

   class A   
     $nested_C_A = self
     $value_of_A_inside_C = A
     assert A == self
     assert self != $top_level_A
     assert self.superclass==$toplevel_Object
     assert !A.instance_of?($top_level_A)
     
     p Module.nesting
   end

   assert A == $nested_C_A

   class Object
     $nested_Object = self
     
     assert self==Object
     assert self != $toplevel_Object
   end
   
   class A
     assert A==$nested_C_A
   end
   
end

class AA < A
  p X
  X = "AA"
  assert getX == 'A'
  p X
end


