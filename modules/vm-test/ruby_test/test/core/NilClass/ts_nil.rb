$LOAD_PATH.unshift(Dir.pwd)

Dir["*/tc*.rb"].each{ |file|
   load file
}