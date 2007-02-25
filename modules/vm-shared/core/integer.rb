class Integer < Numeric
  def upto(limit)
    i = self
    while (i <= limit)
      yield i
      i = i + 1
    end
  end

  def downto(limit)
    i = self
    while (i >= limit)
      yield i
      i = i - 1
    end
  end
end