
begin
  t = Time.utc 2005
  #assert_equals("Sat Jan 01 00:00:00 UTC 2005", t.to_s)
  
  #t = Time.utc 2007, 1, "1", 23, 59, 1, "100000000"
  #assert_equals("Fri Feb 02 00:00:41 UTC 2007", t.to_s)
  
  t1 = Time.new
  
  #t = Time.at 0, 0
  #assert_equals("Thu Jan 01 01:00:00 +0100 1970", t.to_s) 
  
  #t = Time.at 1000, 10000000
  #"Thu Jan 01 01:16:50 +0100 1970"
  #t = Time.mktime 2005, 6, 1
  p t.inspect
end