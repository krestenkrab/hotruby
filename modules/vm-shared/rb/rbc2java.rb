

require 'insninfo'
require 'pp'

module HotRuby
  
  include InsnInfo
  
  INSN_SYM_TO_FIX = Hash.new
  
  INSN_NAME_INFO.each_index { |idx| 
    INSN_SYM_TO_FIX[INSN_NAME_INFO[idx].to_sym]= idx
  }
  
  
  
  class RubyModule
    def initialize(name, context=nil, superclass=nil)
      @name = name
      @context = (context == nil ? self : context)
    end
    
    def context
      @context
    end
  end
  
  class RubyClass < RubyModule
    def initialize(name, context=nil, superclass=nil)
      super(name, context, superclass)
      @superclass = superclass
    end
  end
  
  CLASS_OBJECT = RubyClass.new("Object", nil, nil);
  CLASS_MODULE = RubyClass.new("Module", CLASS_OBJECT, CLASS_OBJECT);
  CLASS_CLASS  = RubyClass.new("Class", CLASS_OBJECT, CLASS_MODULE);
  
  
  class RubyBlock
    
    def visibility= (value) 
      @visibility = value
    end
    
    
    def initialize(ctx)
      @context = ctx
    end
  end
  
  class CompileData
    
    def initialize(option)
      @catch_table_ary = Array.new
      @option = option
      @code = Array.new
    end
    
    def add_exception_handler(type, lstart, lend, eiseq, lcont)
      @catch_table_ary << [type, lstart, lend, eiseq, lcont]
    end
    
    def code
      @code
    end
    
  end
  
  
  class CompileOption
    def initialize(opt)
      @option = opt
    end
  end
  
  
  class Label
    def pos= (pos) 
      @pos = pos
    end  
    def name= (pos) 
      @name = pos
    end  
  end
  
  class IC
  end
  
  class InstructionSequence
    
    def InstructionSequence.load(array,parent=nil,opt=nil)
      
      iseq = InstructionSequence.new
      
      magic =		array[0]
      version1 =	array[1]
      version2 =	array[2]
      format_type =	array[3]
      misc =		array[4]
      name =		array[5]
      filename =	array[6]
      line =		array[7]
      type =		array[8]
      locals =		array[9]
      args =		array[10]     
      exception =	array[11]
      body =		array[12]
      
      option = CompileOption.new(opt)
      
      iseq.prepare_build_from_ary(name, filename, parent, type, nil, option)
      iseq.build_from_ary(line, locals, args, exception, body)
      
      iseq
    end
    
    attr_accessor :name, :file_name, :type, :arg_rest, :arg_block, :special_block_builder, :cref_stack, :compile_data, :local_iseq, :parent_iseq
    
    #
    # see yarv/compile.c
    #    
    def prepare_build_from_ary(name, filename, parent, type, block_opt, option)
      
      @name = name;
      @defined_method_id = 0
      @file_name = filename
      @type = type
      @arg_rest = 0
      @arg_block = 0
      @special_block_builder = block_opt
      @cached_special_block_builder = nil
      @cached_special_block = nil
      
      if (type == :toplevel) 
        @cref_stack = RubyBlock.new(CLASS_OBJECT)       
        @cref_stack.visibility= 1
      elsif (type == :method || type == :class) 
        @cref_stack = RubyBlock.new(parent)
      elsif (parent)
        @cref_stack = parent.cref_stack
      end
      
      @compile_data = CompileData.new(option)
      
      if (type == :toplevel || type == :method || type == :class) 
        @local_iseq = self
      else
        @local_iseq = parent.local_iseq
      end
      
      if (parent) 
        @parent_iseq = parent
      end
      
      true
    end
    
    def build_from_ary(line, locals, args, exception, body)
      opt = 0
      labels_table = Hash.new
      
      if (type == :toplevel || type == :method || type == :class)
        opt = 1
      end
      
      @local_size = opt + locals.length
      @local_tbl = Array.new(@local_size, nil)
      
      for i in 0..(locals.length)
        @local_tbl[i + opt] = locals[i]
      end
      
      if opt == 1 
        @local_tbl[0] = :self
      end
      
      if (args.class == Fixnum)
        @argc = args
        @args_simple = true
      else
        @argc = args[0]
        @arg_opts = args[1]
        @arg_opt_labels = args[2]
        @arg_rest = args[3]
        @arg_block = args[4]
        
        @arg_opt_tbl = Array.new(@arg_opts)
        
        @arg_opt_labels.each_index { |idx| 
          @arg_opt_tbl[idx] = self.register_label(labels_table, @arg_opt_labels[idx])
        }
      end
      
      build_exception(labels_table, exception)
      build_body(body, line, labels_table)
      
    end
    
    def register_label(labels_table, label)
      bug("register_label: not a label") unless label.class == Symbol      
      result = labels_table[label]
      if result == nil
        result = (labels_table[label] = Label.new)
      end 
      result
    end
    
    def build_exception(labels_table, exception)
      
      exception.each { |exc|
        type=exc[0]
        eiseq = nil
        if exc[1] == nil
          eiseq = nil
        else
          eiseq = InstructionSequence.load(exc[1], self, nil)
        end
        
        lstart = register_label(labels_table, exc[2]);
        lend =   register_label(labels_table, exc[3]);
        lcont =  register_label(labels_table, exc[4]);
        sp = exc[5]
        
        @compile_data.add_exception_handler(type, lstart, lend, eiseq, lcont)
      }
      
    end
    
    
    
    def build_body(body, line, labels_table)
      
      ptr = body
      len = body.length
      
      insn_table = INSN_SYM_TO_FIX
      
      code = @compile_data.code
      last = nil
      
      @body = body

      idx = -1    
      body.each {|obj|
        idx = idx+1  
        
        if obj.class==Symbol
          label = register_label(labels_table, obj)
          label.pos= idx+1
          label.name= obj
          body[idx] = label
        else
          argc = obj.length-1
          line_no = 0
           (insn_id = insn_table[obj[0]]) || HotRuby.bug("unknown instruction #{obj[0]}")      
          
          if (argc != InsnInfo.insn_len(insn_id)-1) 
            rb_bug("operand size mismatch");
          end

#          code << [obj[0], insn_id, last]
#          last = nil
          
          for j in 0...argc
            
            # fetch operand
            op = obj[j+1]
            
            case InsnInfo.insn_op_type(insn_id, j)
              
            when InsnInfo::TS_OFFSET:
              op = register_label(labels_table, op)
              
#            when InsnInfo::TS_LINDEX
#              op = @local_tbl.length-op
#              
#            when InsnInfo::TS_IC
#              op = IC.new  
              
            when InsnInfo::TS_ISEQ:

              if op == nil
                # ignore
                            
              elsif op.class == Array
                op = InstructionSequence.load(op, self, nil)
                
              elsif op.class == InstructionSequence              
                # ignore
                
              else
                p op.class
                p obj
                bug("not an instruction sequence")
                
              end
              
            end
 
            obj[j+1] = op           
            
          end
          
        end
        
      }
      
      
      
      
    end
    
  end
  
  def HotRuby.load(file) 
    
    open(file) do |f|
      exit "Not really an RBCM!" unless f.read(4) == "RBCM" 
      iseq = InstructionSequence.load(Marshal.load(f))
      
      pp(iseq)
    end
    
  end
  
end



HotRuby.load("richards.rbc")
