

class XX

def test

begin
  p "x"
  raise Exception.new("hello")
rescue Exception
  p "rescue (good)"
else
  p "no error (bad)"
ensure 
  p "ensure (good)"
end

(1..3) . each { |i|  p "xxx"; p "ccc"; p "ddd" }

for i in 1..3
# p i +
# begin
#   break
# ensure
   p "ens"
#   4
# end
end

p 1 + (5 rescue 4)

p 3

end

end

xx = XX.new

xx.test
