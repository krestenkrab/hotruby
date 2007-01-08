
##
## Defines classes in Array that can be written in plain Ruby
##
class Array

   include Enumerable

   def Array.[] (*args) 
      args
   end

   def initialize( anInteger=0, anObject=nil ) 
      idx=0
      while (idx<anInteger)
        self << anObject
        idx += 1
      end
   end
   
   def & (other)
     self.select {|elm| 
        other.include? elm
     }
   end

   def * (other) 
     self.join(other) if other.class==String
     
     result=Array.new
     (1..other).each { 
        self.each {|elm| result << elm }
     }
     result
   end
   
   def + other
      result = self.dup
      other.each { |elm| result << elm }
      result
   end
   
   def - other
      result = self.dup
      result.delete_if {|elm| other.includes? elm }
      result
   end
   
   def << (elm)
      self[size]= elm
   end
   
   def <=> (other)
     idx=0
     max = Math.max(self.size, other.size)
     while (idx<max)
        cmp = (self.at(idx) <=> other.at(idx))
        if (cmp == 0)
          idx += 1
          next
        else
          return cmp
        end
     end
     return self.size <=> other.size
   end
   
   def == (other)
     (self <=> other) == 0
   end

   alias :=== :==
   
   def [] (val, length=nil)
     case val.class
     when ::Fixnum
        return self.at(val) if length==nil
        range = val...(val+length)
     when ::Range
        range = val
     else
        val = Integer.induced_from(val)
        return self.at(val) if length==nil
        range = val...(val+length)
     end 
     
     size = self.size
     result = Array.new
     range.each {|idx| 
        if idx<0
          idx = size + idx
        end
        
        if idx >= 0 && idx < size
          result << self.at(idx)
        end
     }     
     
     # empty array returns nil
     return *result     
   end

   ##
   ## []= is defined natively
   ##

   def | (other)
     result = self.dup
     other.each{|val| 
        result << val unless result.includes? val
     }
     result
   end

    def assoc (elem)
      idx = 0
      while (idx < size)
         ae = this.at(idx)
         if ae.class == Array && ae.size > 0 && ae.at(0) == elem
           return ae
         end
      end
      nil
    end
    
    
    ##
    ## at is defined natively
    ##
   
    
   

   def map!
      idx = 0;
      while (idx < size)
         self[idx]= yield self[idx]
         idx += 1
      end
   end
   
   

end