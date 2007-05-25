
  def test
    begin
      p "XX"
      raise "foo"
    rescue Exception
      p "YY"
    end
  end
  
  test
  
  
