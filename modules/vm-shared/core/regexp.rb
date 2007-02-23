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
  
  def self.union(*expressions)
    Regexp.new(do_union expressions)
  end

  #def self.escape(s)
  #def self.quote(s)

  def self.last_match(idx=nil)
    if $~ != nil && idx != nil
      $~[idx]
    else
      $~
    end
  end
  
  def ===(other)
    self =~ other
  end

  def ~
    self =~ $_
  end
  
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
  def self.do_union(*expressions)
    case expressions.size
      when 0: '(?!)'
      when 1: part_of_union(expressions[0])
      else part_of_union(expressions[0]) + '|'+ do_union(expressions[1,-1])
    end
  end
  
  def self.part_of_union(part)
    if part.class == Regexp
      part.to_s
    else
      '' + part
    end
  end
  
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
  
#irb(main):004:0> Regexp.union(/abc/xm, /def/).to_s
#=> "(?-mix:(?mx-i:abc)|(?-mix:def))"
end
