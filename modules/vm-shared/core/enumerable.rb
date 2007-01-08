

module Enumerable
 
   ##
   ## receiver is assumed to implement each
   ##
   
   ## enumObj.collect {| obj | block } -> anArray
   def collect
     result = Array.new
     each { |elm|
        result << yield(elm)
     }
     result
   end
   
   ## enumObj.detect {| obj | block } -> anObject or nil
   def detect
     each{|elm|
       if yield(elm)
         return elm
       end
     }
     nil
   end
   
   def :find :detect
   
   def each_with_index
      idx = 0
      each{ |elm|
        yield (elm, idx)
        idx += 1
      }
      nil
   end

   def entries
     result = Array.new
     each {|elm| result << elm}
     result
   end
   
   alias :to_a :entries
   
   def find_all
     result = Array.new
     each {|elm| 
        result << elm if yield(elm)
     }
     result     
   end

   def grep (pattern)
      result = Array.new
      each { |elm| 
        if (pattern===elm)
           if block_given?
              result << yield(elm)
           else
              result << elm
           end
        end
      end
      result
   end

   def include? (elm)
     each{|e| 
       return true if e==elm
     }
     false
   end
   
   alias :map :collect
   
   def max
     result = nil
     if block_given?
       each { |elm| 
         if result==nil
            result=elm
         else
            if yield(result,elm) > 0
              result=elm
            end
         end
       }
     else
       each { |elm| 
         if result=nil
            result=elm
         else
            if (result <=> elm) > 0
              result=elm
            end
         end
       }
     end
     result
   end

   alias :member? :include?
   
   def min
     result = nil
     if block_given?
       each { |elm| 
         if result==nil
            result=elm
         else
            if yield(result,elm) < 0
              result=elm
            end
         end
       }
     else
       each { |elm| 
         if result=nil
            result=elm
         else
            if (result <=> elm) < 0
              result=elm
            end
         end
       }
     end
     result
   end

   def reject
     result = Array.new
     each { |elm|
       result << elm unless yield(elm)
     }
     result
   end

   alias :select :find_all

   def sort 
     if block_given?
       self.to_a.sort{|a,b| yield(a,b) }
     else
       self.to_a.sort{|a,b| a <=> b }
     end
   end

end