p 'loading time.rb'

class Time
  def self.utc(*args)
    Time.gm(*args)
  end
  
  def self.now
    Time.new
  end
  
  def self.mktime(*args)
    Time.local(*args)
  end
  
  alias :to_s :inspect
end
