p 'loading time.rb'

class Time
  def self.utc(*args)
    Time.gm(args)
  end
  
  def self.now
    Time.new
  end
  
  alias :to_s :inspect
end
