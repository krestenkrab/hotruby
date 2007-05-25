
# Kernel.eval_file "enumerable.rb", Object

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

   def sort_by
     val = []
     each { |v| val << Array[yield(v), v] }
     val.sort! { |k1,k2| k1[0] <=> k2[0] }
     val.collect { |v| v[1] }     
   end

   def each_with_index
     index = 0
     each { |val| yield(val, index); index += 1 }
   end

end

##
## Defines classes in Array that can be written in plain Ruby
##
class Array

private
   NOARG = Object.new

public

   include Enumerable

   def Array.[] (*args) 
      args
   end

   def empty?
      0 == size
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

   def ===(val)
      each { |e| return true if e===val }
      false
   end
   
   def [] (val, length=nil)
     case val.class
     when ::Fixnum
        if length==nil
          return self.at(val) 
        end
        range = (val)..(val+length)        
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
        last = size+last
        last = 0 if last < 0
     end
    
  #   p "[] #{val},#{length} => #{range}"

     if !range.exclude_end?
        last += 1;
     end
     
     result = []
     
     pos = first-1
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
   
   def reverse_each
      idx = size-1;
      while (idx >= 0)
         yield self[idx]
         idx -= 1
      end
   end
   
   def delete_if
      idx = 0;
      while (idx < size)
         if yield(self[idx])
           delete_at(idx)
         else
           idx += 1
         end
      end
      self
   end
   
   def push(*vals)
      vals.each { |v| self << v }
      self
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
   
   def fetch(idx, ifnone=NOARG)
      if idx < 0
         idx += size
      end
      if idx < 0 || size <= idx
        return yield(idx) if block_given?
        raise IndexError, "index out of range" if ifnone==NOARG
        ifnone
      else
        self.at(idx)
      end
   end
   
   def sort
     result = dup
     result.sort!
     result
   end
   
end