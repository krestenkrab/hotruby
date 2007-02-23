##
## core.rb is loaded on start up
##

p "loading core..."

Kernel.eval_file 'object.rb', Object
Kernel.eval_file 'array.rb', Object
Kernel.eval_file 'regexp.rb', Object

##
## the very first thing we need to define is "load" and "require".  from there, we can
## define the rest in separate files
##

class Class
   def use_pp
      false
   end
end

class Exception

  def Exception.exception( msg=nil )
    self.new(msg)
  end
  
  def initialize(message=nil)
    @message = message
    @stacktrace = caller(3)
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

class StandardError < Exception
end

class ArgumentError < StandardError
end

class NameError < StandardError
end

class NoMethodError < NameError
end

class TypeError < StandardError
end

class ScriptError < Exception
end

class LoadError < ScriptError
end

class RuntimeError < StandardError
end

class ThreadError < StandardError
end

class LocalJumpError < StandardError
end

class File
 # JavaIOFile = include_class('java.io.File') 

  def File.absolute?(string)
    /^\// =~ string
  end
  
  def File.join(dir,file) 
     "#{dir}/#{file}"
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
    
    ($:).each do |path_elem| 
      full_name = File.join(path_elem, filename)
      
      if (File.file? full_name) 
        Kernel.eval_file full_name, (wrap ? Module.new() : Object::MAIN)
        return true
      end
    end
    
    raise LoadError.new "No such file to load -- #{filename}"
  end
  
  def require(filename, wrap=false)
    return true if $".contains?(filename)
    
    if (File.absolute? filename)
      $" << filename
      Kernel.eval_file filename, (wrap ? Module.new() : Object)
      return true
    end
  
    ($:).each do |path_elem| 
      full_name = File.join(path_elem, filename)

      if (File.file? full_name)
        $" << filename
        p "loading #{full_name}..."  
        Kernel.eval_file full_name, (wrap ? Module.new() : Object)
        p "loading #{full_name}... done"  
        return true
      end
    end

    if (/.rb$/ !~ filename) 
      require(filename + ".rb")
      return
    end
  
    raise LoadError.new "No such file to load -- #{filename}"
  end
end  

class Array
   def contains?(elm)
     each { |e| return true if e==elm }
     return false
   end
end

module Comparable

end

class String

  def to_i
     Kernel::Integer(self)
  end

  def empty?
     length == 0
  end

  def chomp(aString=$/) 
    if /#{aString}$/ =~ self
       self[0,length-aString.length]
    else
       self
    end
  end

  def split(pattern=$;, limit=0)
     if (nil == pattern) 
        pattern = ' '
     end
     
     case pattern.class
     when String
       split_by_string(pattern, limit)
     when Regexp
       split_by_regexp(pattern, limit)
     else
        raise ArgumentError, 'pattern must be string or regexp'
     end
  end
  
  def scan(pattern)
     case pattern.class
     when String
       scan_by_string(pattern)
     when Regexp
       scan_by_regexp(pattern)
     else
        raise ArgumentError, 'pattern must be string or regexp'
     end
     
  end
  
  private  

  def scan_by_regexp(pattern)
  #  p "scanning #{self} for #{pattern}"
    pos= 0
    lim= self.length
    result= []
    while (md = pattern.match(self[pos,lim-pos])) 
      result << md[0]
      pos += md.end
    end
  #  p "returning #{result}"
    result
  end
  
  def split_by_regexp(pattern, limit)
   # p "splitting #{self.to_s} by #{pattern.inspect}"
    pos= 0
    lim= self.length
    result= []
    str = ""
    while (md = pattern.match(str=self[pos,lim-pos])) 
      app = str[0, md.begin]
   #   p "str[#{pos},#{lim-pos}]=#{str} ... + #{app}"
      result << app
      pos += md.end
    end
   #   p "str[#{pos},#{lim-pos}]=#{str}"
   #   p "result => #{result}"
    result
  end
  
  public
  
  def =~(anObject)
     if anObject.class == String
        Regexp.new(anObject) =~ self
     else
        anObject =~ self
     end
  end

  def +(other)
    "#{self}#{other}"
  end

end



class Fixnum
  
  def +@
    self
  end
  
  def -@
    0 - self
  end
  
  def abs
    res = self
    if res < 0
      res = -res
    end
    res
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
  
  def and(other)
    false
  end
  
end

class TrueClass
  def not
    false
  end

  def or(other)
    true
  end
  
  def and(other)  
    other
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

  def Kernel.const_missing(sym)
     raise StandardError, "missing #{sym}"
  end
  
  def proc (&val)
    val
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
  
  def attr_writer (*names)
    names.each {|name|
      sname = name.to_s
      script = "def #{sname}=(val) @#{sname}=val end"
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
  
  def not
    true
  end
end

class Float
  def +@
    self
  end
  
#  def -@
#    0 - self
#  end
end

require 'numeric.rb'
require 'string.rb'
