require 'benchmark'

class ZZZ

def self.foo
self
end

def self.invoking
a = [];
i = 0;
while i < 100000
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  foo; foo; foo; foo; foo; foo; foo; foo; foo; foo;
  i += 1;
end
end

def self.run
puts "Test interpreted: 100k loops calling self's foo 100 times"
100.times {
puts Benchmark.measure {
  invoking
}
}
end

end

ZZZ.run

