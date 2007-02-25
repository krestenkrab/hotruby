class MatchData
  # Defined "natively":
  # * []
  # * begin
  # * end
  # * length
  # * string
  # * to_a
  
  def to_s
    self[0]
  end

  def select
    to_a.each { |e| yield e }
  end

  def pre_match
    string()[0, self.begin(1)]
  end

  def post_match
    s = string()
    e = self.end(0)
    s[e, s.length - e]
  end

  def offset(index)
    [self.begin(index), self.end(index)]
  end

  def size
    length
  end

  def captures
    a = to_a
    a[1, a.size - 1]
  end

  def values_at(*indices)
    indices.collect { |index| self[index] }
  end
end