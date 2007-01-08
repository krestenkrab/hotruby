
def moo(dd,ee,ff=4, &block)
  a,b,c=1,2,3
  p "inside method! a=#{dd}, b=#{ee}, c=#{ff}"
  block.call(77)
end

moo 1,2,3 { |a| p "hello #{a}" }



val=0
def x (val=66, &proc)
  a,*b=1,2,3
  result=proc
  p "#{a}+#{b}+#{proc}:#{val}"
  [1,2,3].each{ p b; p val }
  result
end

x 4{||}
x 5{||}

var=3

proc = x{p "hello:#{var}"; 4}
proc.call

