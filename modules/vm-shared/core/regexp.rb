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
  
  #def to_s
  #  "(?#{optionstring}:#{source})"
  #end
  
  #def inspect
  #  return source
  #end

  #private
  #def optionstring
  #  s = ''
  #  s << 'm' if flags & MULTILINE
  #  if (flags != 0)
  #end
  
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
