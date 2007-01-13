##
## core.rb is loaded on start up
##

p "loading core..."

Kernel.eval_file 'object.rb', Object

##
## the very first thing we need to define is "load" and "require".  from there, we can
## define the rest in separate files
##

class Exception
  
  def initialize(message=nil)
    @message = message
    @stacktrace = caller(0)
  end
  
  def exception(msg=self)
    if msg==self
      self
    else
      self.clone.initialize(msg.to_str)
    end
  end
  
  def backtrace
    @stacktrace
  end
  
  def to_s
    @message == nil ? self.class.inspect : @message
  end
  
  def to_str
    to_s
  end
  
  alias :message :to_str
  
end

class File
 # JavaIOFile = include_class('java.io.File') 

  def File.absolute?(string)
    JavaIOFile.new(string).absolute? 
#    string == expand_path(string)
  end
  
  def File.expand_path(file, dir_string=cwd)
     if file[0..3] =~ /[a-zA-Z]:\\/ # windows-style
      file
     elsif file[0..1] == '\\\\'
      file
     elsif file[0] == '/'
       file
     else
       dir_string + SEPARATOR + file
     end
  end
end


module Kernel
  
  def load(filename, wrap=false)
    
    if (File.absolute? filename)
      Kernel.eval_file filename, (wrap ? Module.new() : Object::MAIN)
      return true
    end
    
     ($:).each {|path_elem| 
      full_name = File.join(path_elem, filename)
      
      if (File.file? full_name) 
        Kernel.eval_file full_name, (wrap ? Module.new() : Object::MAIN)
        return true
      end
    }
    
    throw LoadError.new "No such file to load -- #{filename}"
    
  end
  
  def require(filename)
    return true if $".contains? filename
    
    if (File.absolute? filename)
      $" << filename
      Kernel.eval_file filename, (wrap ? Module.new() : Object::MAIN)
      return true
    end
  
   ($:).each {|path_elem| 
    full_name = File.join(path_elem, filename)
    
    if (File.file? full_name) 
      $" << filename
      Kernel.eval_file full_name, (wrap ? Module.new() : Object::MAIN)
      return true
    end
  }

  if (/.rb$/ !~ file) 
    require(filename + ".rb")
  end
  
  throw LoadError.new "No such file to load -- #{filename}"
    
    
  end
  
end  

class Array
   def contains?(elm)
     each { |e| if e==elm then return true; end }
     return false
   end
end

class String

  def =~(anObject)
     if anObject.class == String
        Regexp.new(anObject) =~ self
     else
        anObject =~ self
     end
  end

end

class Object

  def =~(anObject)
     false
  end

end



class Fixnum
  
  def +@
    self
  end
  
  def -@
    0 - self
  end
  
  def zero?
    self==0
  end
  
  def >>(howmuch)
    res=self
    while howmuch>0
      res /= 2
      howmuch -= 1
    end
    res
  end
end

class Array
  def collect!
    pos = -1
    size= self.size
    while (pos += 1) < size
      elem = self[pos]
      value = yield elem
      self[pos]= value
    end
    self
  end
end

class FalseClass
  
  def not()
    true
  end
  
  def or(other)
    other
  end
end

class TrueClass
  def or(other)
    true
  end
  
  def not
    false
  end
end

module Kernel
  def Integer(val)
    if (val.class == Fixnum)
      val
    elsif (val.class == Bignum) 
      val
    elsif (val.class == String && val =~ /^ *(0[bBxX])?[0-9]+ *$/) 
      eval val,nil
    end
  end
end

class Module
  
  def attr_reader (*names)
    names.each {|name|
      sname = name.to_s
      script = "def #{sname}() @#{sname} end"
      module_eval script;
    }
    nil
  end
  
  def attr_accessor (*names)
    names.each {|name|
      sname = name.to_s
      script = "def #{sname}() @#{sname} end; def #{sname}=(val) @#{sname}=val end"
      p "#{self}.module_eval #{script}"
      module_eval script;
    }
    nil
  end
end

class NilClass
  def or(other)
    other
  end
end
