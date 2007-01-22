#
#   nop.rb - 
#   	$Release Version: 0.9.5$
#   	$Revision: 1.2 $
#   	$Date: 2005/04/13 15:27:08 $
#   	by Keiju ISHITSUKA(keiju@ruby-lang.org)
#
# --
#
#   
#
module IRB
  module ExtendCommand
    class Nop
      
      @RCS_ID='-$Id: nop.rb,v 1.2 2005/04/13 15:27:08 keiju Exp $-'

      def self.execute(conf, *opts)
	command = new(conf)
	command.execute(*opts)
      end

      def initialize(conf)
	@irb_context = conf
      end

      attr_reader :irb_context

      def irb
	@irb_context.irb
      end

      def execute(*opts)
	#nop
      end
    end
  end
end

