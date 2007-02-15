class Regexp
  # Defined "natively":
  # * ==
  # * =~
  # * initialize
  # * match
  # * options
  # * source

  IGNORECASE = 1
  EXTENDED = 2
  MULTILINE = 4

  def self.compile(expression, options=0, language=nil)
    # Ignore the language argument
    self.new(expression, options)
  end
  
  #def self.escape(s)
  #def self.last_match(index=nil)
  #def self.quote(s)
  
  #def ===(regexp)
  #def ~(rxp)
  
  def casefold?
    options & IGNORECASE == IGNORECASE
  end
  
  def inspect
    "/#{source}/#{positive_options(options)}"
  end

  def kcode
    # We don't support different character set codes
    nil
  end

  def to_s
    "(?#{positive_options(options)}#{negative_options(options)}:#{source})"
  end
  
  private
  def positive_options(flags)
    ((flags & MULTILINE != 0) ? 'm' : '') +
    ((flags & IGNORECASE != 0) ? 'i' : '') +
    ((flags & EXTENDED != 0) ? 'x' : '')
  end
  
  def negative_options(flags)
    if flags == 7
      ''
    else
      '-' + positive_options(flags ^ 7)
    end
  end
  
#irb(main):003:0> Regexp.union(/abc/.xm, /def/).to_s
#NoMethodError: undefined method `xm' for /abc/:Regexp
#        from (irb):3
#irb(main):004:0> Regexp.union(/abc/xm, /def/).to_s
#=> "(?-mix:(?mx-i:abc)|(?-mix:def))"
end
