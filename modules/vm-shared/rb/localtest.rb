

p "OK"

def v(a,b,c,d)
 p "a=#{a}; b=#{b}"
 yield (a,b,c,d)
end

v(1,2,3,4) { |c,d| 
  p "c=#{c}; d=#{d}" }


v(1,2,3,4) { |c,d,e,f| 
  p "c=#{c}; d=#{d}" }


require 'test/unit'

