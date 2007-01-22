#
#   irb/ws-for-case-2.rb - 
#   	$Release Version: 0.9.5$
#   	$Revision: 1.3 $
#   	$Date: 2005/04/13 15:27:07 $
#   	by Keiju ISHITSUKA(keiju@ruby-lang.org)
#
# --
#
#   
#

while true
  IRB::BINDING_QUEUE.push b = binding
end
