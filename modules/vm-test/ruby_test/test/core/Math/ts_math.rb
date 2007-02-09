$LOAD_PATH.unshift(Dir.pwd)

Dir["*/*.rb"].each{ |test_case|
   require test_case[0..-4] # remove '.rb'
}