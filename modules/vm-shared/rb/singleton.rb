


x = Object.new

class Object
  def m() 
    p "mm"
  end
end

x.m

p "aa"

def x.y()
  
  p "inside y"
  
end


p "bb"
x.y


def tt
  begin
    y = Object.new
    y.m
    y.y
  rescue NoMethodError 
    p "OK"
  else
    p "Fail"
    
  end
end

tt()


