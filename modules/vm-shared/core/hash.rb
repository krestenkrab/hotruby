


class Hash

  alias :each_pair :each

  NO_KEY = Object.new

  def fetch (key, default=NO_KEY)
     
     if has_key? key
        self[key]
     elsif default==NO_KEY
        
        if block_given?
           yield(key)
        else
           raise IndexError, "key not found"
        end
        
     else
        default             
     end
  end
  
  def invert
     r = Hash.new
     each { |key,val| r[val]=key }
     r
  end
  
  def update(hsh)
     hsh.each {|key,val| self[key]=val }
  end 

  def collect
    result = []
    each {|key,value| 
      result << yield(key,value)
    }
    result
  end
  
  def inspect
    val = "{"
    each {|key,value| 
      val += key.inspect 
      val += " => "
      val += value.inspect
      val += ", "
    }
    val += "}"
    val
  end
  
  def keys
     result = []
     each { |k,v| result << k }
     result
  end
  
  def values
     result = []
     each { |k,v| result << v }
     result
  end
  
end