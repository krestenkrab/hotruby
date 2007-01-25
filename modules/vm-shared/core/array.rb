

module Enumerable

   def detect
      each { |e| return e if yield(e) }
      nil
   end

   def collect 
     result = []
     each { |e| result << yield(e) }
     result
   end

   def find_all
      result = Array.new
      each { |e| 
        if yield(e) 
          result << e
        end 
      }
      result
   end

end

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
        range = (val)...(val+length)        
     when ::Range
        range = val
     else
        val = Integer.induced_from(val)
        return self.at(val) if length==nil
        range = val...(val+length)
     end 
     
     size = self.size
     first = range.first
     last = range.last

     if first<0
        first = size-first
        first = 0 if first < 0
     end
    
     if last<0
        last = size-last
        last = 0 if last < 0
     end
    
  #   p "[] #{val},#{length} => #{range}"

     if !range.exclude_end?
        last += 1;
     end
     
     result = []
     
     pos = first
     while (pos += 1) < last
          result << self.at(pos)
     end
     
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
   
   def dup
     result = []
     each { |e| result << e }
     result
   end
   
   def join(sep = $,)
      result = ""
      first = true
      each { |e| result += sep unless first; first = true; result += e }
      result
   end
   

end