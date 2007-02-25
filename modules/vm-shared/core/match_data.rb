class MatchData
  # Defined "natively":
  # * []
  # * to_a
  # * begin
  # * end
  # * string
  
  def to_s
    self[0]
  end

  def select
    # TODO
  end

  def pre_match
    string()[0, self.begin(1)]
  end

  def post_match
    s = string()
    e = self.end(0)
    s[e, s.length - e]
  end

  def length
    # TODO
  end

  def offset
    # TODO
  end

  def size
    # TODO
  end

  def captures
    # TODO
  end

  def values_at(*indices)
    indices.collect { |index| self[index] }
  end
end