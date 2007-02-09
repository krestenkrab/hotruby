##
##

p 'loading object.rb'

class Object
   
  def =~(anObject)
     false
  end
  
  def extend(klass)
     self
  end

  def not
    nil
  end

  MAIN = Object.new
  def MAIN.inspect
    "main"
  end

   
   def include_class(class_name)
     classes = class_name.to_a
     
     classes.each { |name| 
        include_single_class(name, &Proc.new)
     }
     
   end
   
   private
   
   def include_single_class(name)
     package_name, class_name = name.match(/((.*)\.)?([^\.]*)/)[2,3]
    
      if block_given?
        constant = yield(package_name, class_name)
      else
        constant = class_name
      end

       result = Class.new(JavaClass)
       
       result.initialize(package_name, class_name)      
   end
   
   
   def to_a
      [self]
   end

end

class String
  def match(pattern) 
     if pattern.class == Regexp
       pattern.match (self)
     else
       Regexp.new(pattern).match(self)
     end
  end
end



