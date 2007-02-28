class Numeric

  def coerce(aNumeric)
    if (self.class == aNumeric.class)
      return [aNumeric, self]
    else
      return [aNumeric.to_f, self.to_f]
    end
  end

  def divmod(aNumeric)
    q = (self.to_f() / aNumeric.to_f()).to_i()
    r = self - q * aNumeric
    return [q, r]
  end

  def eql?(aNumeric)
    return self.class == aNumeric.class and self == aNumeric
  end
  
  def integer?
    return self.instance_of? Integer
  end
  
  def modulo(aNumeric)
    divmod(aNumeric)[1]
  end
  
  def nonzero?
    return !(self == 0)
  end

  def zero?
    return self == 0
  end
  
end
