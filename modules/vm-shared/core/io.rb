


class IO

   def puts(*vals)
      if (vals.size==1) && (Array === vals[0])
        vals = vals[0]
      end
      vals.each { |v| print v }
      print '\n'
   end

   def flush
   
   end

end