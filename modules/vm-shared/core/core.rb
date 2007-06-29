##
## core.rb is loaded on start up
##

p "BOOTING HOTRUBY"

p "loading core..."

Kernel.eval_file 'object.rb', Object
Kernel.eval_file 'objectspace.rb', Object
Kernel.eval_file 'array.rb', Object
Kernel.eval_file 'regexp.rb', Object
Kernel.eval_file 'hash.rb', Object
Kernel.eval_file 'time.rb', Object
Kernel.eval_file 'match_data.rb', Object
Kernel.eval_file 'io.rb', Object

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
  
  def set_backtrace(val)
    @stacktrace = val
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
    # Doesn't work - using regexps screws up $~and other globals:
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
    # TODO: Must not use regular expressions, as it messes up $~ and other globals 
    #if (aString.size >= size) && (index(aString) == size - aString.size)
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
  
  def sub(pattern, replacement=nil)
     case pattern.class
     when String
       sub_by_string(pattern, replacement) { |s| yield(s) }
     when Regexp
       sub_by_regexp(pattern, replacement) { |s| yield(s) }
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
      pos += md.end(0)
    end
  #  p "returning #{result}"
    result
  end
  
  def sub_by_regexp(pattern, replacement)
     if (md = pattern.match(self))
        return self[0, md.begin] + replacement + self[md.end, self.length-md.end]
     end
     
     self
  end
  
  def split_by_regexp(pattern, limit)
    pos= 0
    lim= self.length
    result= []
    str = ""
    while (md = pattern.match(str=self[pos,lim-pos])) 
      app = str[0, md.begin(0)]
      result << app
      pos += md.end(0)
    end
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
  
  class ThrowNameError < NameError
   
     def sym; @sym; end
     def value; @value; end
  
     def initialize(sym, value)
        super("missing catch for #{sym}")
        @sym = sym
        @value = value
     end
  end
  
  class ThrowMatcher 
     def initialize(sym)
        @sym = sym
     end

     def ===(ex)
        (ThrowNameError==ex) && (@sym == ex.sym)
     end
  end
  
  def catch(sym)
    begin
       yield
    rescue (ThrowMatcher.new(sym)) => e
       e.value
    end
  end
  
  def throw(sym, value=nil)
    raise ThrowNameError.new (sym, value)
  end
  
end

class Proc
  alias :[] :call
end
  
class Module
  
  def module_function (*names)
    anon = nil
    if const_defined? :MODULE_FUNS
       anon = self.const_get(:MODULE_FUNS)
    else
       const_set (:MODULE_FUNS, anon = Class.new(Object))
    end
    
    anon.extend(self)
    
    names.each {|name|
      sname = name.to_s
      script = "def self.#{sname}(*args, &block) MODULE_FUNS.#{sname}(*args, &block) end"
      module_eval script;
    }
    nil
  end
  

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
      module_eval script;
    }
    nil
  end
  
  def attr_writer (*names)
    names.each {|name|
      sname = name.to_s
      script = "def #{sname}=(val) @#{sname}=val end"
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
require 'integer.rb'
require 'string.rb'


class Process
  class Tms
    def utime 
      0.0
    end
    def stime 
      0.0
    end
    def cutime 
      0.0
    end
    def cstime 
      0.0
    end
  end

  def self.times
    Tms.new
  end
end