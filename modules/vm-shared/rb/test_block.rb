

def test

map = {1=>11, 2=>22, 3=>33}

a1 = map.collect {|e| e}
a2 = map.collect {|e, ee| e}
a3 = map.collect {|e, *ee| e}

p "#{a1}"
p "#{a2}"
p "#{a3}"


end


test