
begin
  #t = Time.utc 2005
  #assert_equals("Sat Jan 01 00:00:00 UTC 2005", t.to_s)
  
  #t = Time.utc 2007, 1, "1", 23, 59, 1, "100000000"
  #assert_equals("Fri Feb 02 00:00:41 UTC 2007", t.to_s)
  
  t = Time.new
  p t.inspect
end