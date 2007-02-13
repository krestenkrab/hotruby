class Regexp
  # Defined "natively":
  # * initialize
  # * =~
  # * match
  # * options
  # * source

  IGNORECASE = 1
  EXTENDED = 2
  MULTILINE = 4
  
  def ==(other)
    #p "Other: #{other.source}"
    #p "This: #{source}"
    return other.source == source && other.options == options
  end
  
  def to_s
    "(?#{positive_options(options)}#{negative_options(options)}:#{source})"
  end
  
  def inspect
    return "/#{source}/#{positive_options(options)}"
  end

  private
  def positive_options(flags)
    m = (flags & MULTILINE != 0) ? 'm' : ''
    i = (flags & IGNORECASE != 0) ? 'i' : ''
    x =  (flags & EXTENDED != 0) ? 'x' : ''
    return m + i + x
  end
  
  def negative_options(flags)
    return '' if flags == 7
    return '-' + positive_options(flags ^ 7)
  end
  
  #def abc(flags, flag, value)
  #  if flags & flag
  #    value
  #  else
  #    ''
  #  end
  #end
#irb(main):001:0> /abc/xm.inspect
#=> "/abc/mx"
#irb(main):002:0> /abc/xm.to_s
#=> "(?mx-i:abc)"
#irb(main):003:0> Regexp.union(/abc/.xm, /def/).to_s
#NoMethodError: undefined method `xm' for /abc/:Regexp
#        from (irb):3
#irb(main):004:0> Regexp.union(/abc/xm, /def/).to_s
#=> "(?-mix:(?mx-i:abc)|(?-mix:def))"
end
