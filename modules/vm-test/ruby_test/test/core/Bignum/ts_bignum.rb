$LOAD_PATH.unshift(Dir.pwd)

Dir["*/tc*.rb"].each{ |test_case|
   require test_case[0..-4] # remove '.rb'
}
