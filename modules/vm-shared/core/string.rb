class String

#  def rindex(arg, fixnum = -1)
#    # TODO
#  end

#"map"

#"split"
#"any?"
#"sort"
#"strip"
#"size"
#"downcase"
#"min"
#"gsub!"
#"count"
#"include?"
#"succ!"
#"downcase!"
#"intern"
#"squeeze!"
#"*"
#"next"
#"find_all"
#"each"
#"rstrip!"
#"each_line"
#"+"
#"sub"
#"slice!"
#"tr"
#"replace"
#"inject"
#"reverse"
#"sort_by"
#"lstrip"
#"capitalize"
#"max"
#"chop!"
#"capitalize!"
#"scan"
#"select"
#"each_byte"
#"casecmp"
#"gsub"
#"empty?"
#"to_str"
#"partition"
#"tr_s"
#"tr!"
#"match"
#"grep"
#"rstrip"
#"to_sym"
#"next!"
#"swapcase"
#"chomp!"
#"swapcase!"
#"ljust"
#"between?"
#"reject"
#"upto"
#"hex"
#"sum"
#"reverse!"
#"chop"
#"<=>"
#"insert"
#"<"
#"delete"
#"dump"
#"member?"
#"tr_s!"
#"unpack"
#">"

  def concat(arg)
    self << arg
  end

#"succ"
#"find"
#"strip!"
#"each_with_index"
#">="
#"to_i"
#"rjust"
#"<="
  def index(arg, fixnum = -1)
    ignore_ending = (fixnum > 0) ? fixnum : 0
    ignore_beginning = (fixnum < 0) ? -fixnum - 1: 0

    if (arg.class == String)
      return nil if arg.length > self.length
      ignore_beginning.upto(self.length - arg.length - ignore_ending) do |i|
        return i if (self[i,arg.length] == arg)
      end
    elsif (arg.class == Fixnum)
      return index("" << arg, fixnum)
    elsif (arg.class == Regexp)
      string_to_match = self[ignore_beginning, self.length - ignore_ending - ignore_beginning]
      match_res = arg.match(string_to_match)
      return match_res.begin(0) + ignore_beginning if match_res != nil
    else
      raise TypeError.new("type mismatch: " + arg.class + " given")
    end
    nil
  end
#"collect"
#"slice"
#"oct"
#"all?"
#"length"
#"entries"
#"chomp"
#"upcase"
#"sub!"
#"squeeze"
#"upcase!"
#"crypt"
#"delete!"
#"detect"
#"zip"
#"[]"
#"lstrip!"
#"center"
#"[]="
#"to_f"
end

#p "hello".index(/[aeiou]/, -3)
#p "hello".index(/[x]/, -3)

#p "hello".index(101) 
