
x,*y=7,8,9
p y

x, (y, *z) = [2,3,4]

x= 3;

x = 5

p String
p Regexp

def  String.[]= (idx, val)
   p "String[#{idx}] = #{val}"
end

String[7] = 4





# BAR
CONST = 4
x = CONST
p "after const_set: #{x}"

a,b,i=[4,5,6]
vv= [1,2,*y]
#p vv

a=34
b="a#{a}c" "!"
p b
p a
f="b"

xx=true
val = 1 + (while xx : xx=false; 3 end; 3)
p val

for val in [1,2,3]
 p val
end

reg = /^a#{f}.*/
p reg

if reg =~ 'abc'
 p "match!"
else
 p "no match"
end

i = 1
while (i < 3) 
  p i
  i = i + 1
end

p "file: #{__FILE__}:#{__LINE__}"



$a = 4
p $a

foo = 345
elm = 123
["a", "b", "c", "d"].each { |elm| p elm }
p "elm after loop:#{elm}"

["a", "b", "c", "d"].each { || ss=nil; p ss }

a,b,c=111,222,333
p a 
p b
p c

a=( xx,(yy,*zz)=[1,[2,3]] )
p xx
p yy
p zz

xx
a
yy

a,v=nil





