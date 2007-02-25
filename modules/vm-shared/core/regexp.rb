class Regexp
  # Defined "natively":
  # self.escape
  # * ==
  # * =~
  # * initialize
  # * match
  # * options
  # * source

  def self.quote(s)
    self.escape(s)
  end

  IGNORECASE = 1
  EXTENDED = 2
  MULTILINE = 4

  def self.compile(expression, options=0, language=nil)
    # Ignore the language argument
    self.new(expression, options)
  end
  
  def self.union(*expressions)
    return Regexp.new('(?!)') if expressions.size == 0
    return expressions[0] if expressions.size == 1 && expressions[0].class == Regexp
    i, first, result = [0, true, String.new("")]
    while (i < expressions.size)
      result << '|' if !first
      result << part_of_union(expressions[i])
      first, i = [false, i + 1]
    end
    Regexp.new(result)
  end

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
  def self.part_of_union(part)
    if part.class == Regexp
      part.to_s
    else
      return self.escape('' + part)
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
end
