#
# YASM: YARV Assembler
#
# Usage: See examples on the end of this file.
#

require 'yasmdata'

module YARVCore
  class Assembler
    def self.simpledata_to_iseq sd
      YARVCore::InstructionSequence.load sd
    end

    def self.toplevel vars = [], &b
      simpledata_to_iseq  SimpleDataBuilder.toplevel(vars, &b)
    end

    def self.method name, args = [], vars = [], &b
      simpledata_to_iseq SimpleDataBuilder.method(name, args, vars, &b)
    end

    def self.class name, vars = [], &b
      simpledata_to_iseq SimpleDataBuilder.class(name, vars = [], &b)
    end
    
    class SimpleDataBuilder

      def self.toplevel vars, &b
        sdb = SimpleDataBuilder.new(
          :toplevel, "<toplevel@yasm>", "<yasm>",
          [], vars, [nil], nil
        )
        sdb.build(b)
      end
      
      def self.method name, args = [], vars = [], &b
        sdb = SimpleDataBuilder.new(
          :method, name, "TODO: filename",
          args, vars, [nil], nil
        )
        sdb.build(b)
      end

      def self.class name, vars = [], &b
        sdb = SimpleDataBuilder.new(
          :method, name, "TODO: filename",
          args, vars, [nil], nil
        )
        sdb.build(b)
      end
      
      def initialize type, name, filename, args, vars, lopt, parent
        @type = type
        @name = name
        @filename = filename
        @vars = (args || []) + (vars || [])
        @args = args.size
        @locals = args + vars + lopt
        @line = []
        @body = []
        @exception = []
        @parent_sdb = parent
        @local_sdb = parent ? parent.local_sdb : self
      end

      attr_reader :body
      attr_reader :locals
      attr_reader :local_sdb, :parent_sdb

      def build b
        raise LocalJumpError, "No block given" unless b
        
        if b.arity == 1
          b.call self
        else
          self.instance_eval(&b)
        end
        to_a
      end
      
      def to_a
        # [magic, major_version, minor_version, format_type, misc,
        #  name, filename, line,
        #  type, args, vars, exception_table, body]
        ['YARVInstructionSimpledataFormat', 1, 1, 1, nil,
         @name, @filename, @line,
         @type, @vars, @args, @exception, @body]
      end

      def method name, args=[], vars=[], &b
        YASM.method(name, args, vars, &b).to_a
      end
      
      def block args=[], vars=[], &b
        sdb = SimpleDataBuilder.new(
          :block, "block", "TODO: filename",
          args, vars, [], self
        )
        sdb.instance_eval(&b)
        sdb.to_a
      end
      
      # label
      
      def label sym
        raise "Label must be Symbol, but #{sym.class}" unless Symbol === sym
        @body << sym
      end
      
      alias _ label
      alias l label
      
      YARVCore::InstructionSequence::Instruction::InsnID2NO.each_key{|insn|
        define_method(insn){|*ops|
          @body << [insn, *ops]
        }
      }
      
      # support instructions
      
      def send id, argc, block = nil, flag = 0
        @body << [:send, id, argc, block, flag, nil]
      end
      
      def call id, argc, block = nil
        @body << [:send, id, argc, block, 0x04, nil]
      end
      
      def sym2lidx sym
        raise unless @locals.index(sym)
        @local_sdb.locals.size - @local_sdb.locals.index(sym)
      end
      
      def setlocal sym
        @body << [:setlocal, sym2lidx(sym)]
      end
      
      def getlocal sym
        @body << [:getlocal, sym2lidx(sym)]
      end

      def sym2didx sym
        sdb = self
        level = 0
        while sdb
          if idx = sdb.locals.index(sym)
            return [sdb.locals.size - idx, level]
          end
          level += 1
          sdb = sdb.parent_sdb
        end
        raise "unknown local dynamic variable: #{sym}"
      end
      
      def getdynamic sym
        @body << [:getdynamic, *sym2didx(sym)]
      end

      def setdynamic sym
        @body << [:setdynamic, *sym2didx(sym)]
      end
      
      def definemethod name, method
        @body << [:definemethod, name, method, 0]
      end
      
      def definesingletonmethod name, method
        @body << [:definemethod, name, method, 1]
      end
    end
  end
end

YASM = YARVCore::Assembler

if __FILE__ == $0

#
# samples
#

######################################################

iseq = YASM.toplevel([:a, :b]){|ib|
  # return :ok
  ib.putobject :ok
  ib.leave
}
p iseq.eval

iseq = YASM.toplevel([:a, :b]){
  #
  # a = 10
  # b = 20
  # p(a+b)
  # 3.times{|i| p i}
  #
  putobject 10
  setlocal :a
  putobject 20
  setlocal :b
  putself
  getlocal :a
  getlocal :b
  send :+, 1
  call :p, 1 # call means send with flags 4
  pop
  putobject 3
  send :times, 0, block([:i]){
    putself
    getdynamic :i
    call :p, 1
    leave
  }
  leave
}
p iseq
iseq.eval #=> 30, 0, 1, 2

###########################################################

#
# def show_args(a, b)
#   p [a, b]
# end
# =>
m_show_args = YASM.method(:show_args, [:a, :b]){
  putself
  getlocal :a
  getlocal :b
  newarray 2
  call :p, 1
  leave
}

#
# def abs(a)
#   if a > 0
#     a
#   else
#     -a
#   end
# end
m_abs = YASM.method(:abs, [:a]){
  getlocal :a
  putobject 0
  send :>, 1
  branchunless :label_else
  getlocal :a
  jump :label_end
_ :label_else
  getlocal :a
  send :-@, 0
_ :label_end
  leave
}

#
# def show_args(...) ... end
# def abs(a) ... end
# show_args(1, -1)
# =>
top = YASM.toplevel{
  putnil
  definemethod(:show_args, m_show_args)
  putnil
  definemethod(:abs, m_abs)
  putself
    # abs(1)
    putself
    putobject 1
    call :abs, 1
    # abs(-1)
    putself
    putobject -1
    call :abs, 1
  # show_args(...)
  call :show_args, 2
  leave
}

#=> run
top.eval

###########################################################
end
