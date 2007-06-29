


class Object
  def puts(*vals)
     STDOUT.puts(vals)
  end
end

class IO

   def puts(*vals)
      if (vals.size==1) && (Array === vals[0])
        vals = vals[0]
      end
      vals.each { |v| print v }
      print "\n"
   end

   def write(*vals)
      if (vals.size==1) && (Array === vals[0])
        vals = vals[0]
      end
      vals.each { |v| print v }
   end

   def flush
   
   end

end


class Object
  def puts(*vals) 
    STDOUT.puts(vals)
  end
end