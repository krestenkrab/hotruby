


class X

   def a(arr)
      local_var = @val
      arr.each { |e|  e.do_something; return local_var }
   end
   
   def do_something
     if (@val == 2) 
        a( [ X.new 3] )
     end 
   end
   
   def initialize(val)
      @val = val
   end 
   
end

x = X.new 0
p x.a ( [X.new(1), X.new(2)] )


