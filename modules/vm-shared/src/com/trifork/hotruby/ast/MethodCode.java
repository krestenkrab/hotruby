package com.trifork.hotruby.ast;

import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.interp.CompileContext;
import com.trifork.hotruby.interp.Instructions;

public class MethodCode extends RubyCode {
	
	@Override
	public int code_type() {
		return Instructions.ISEQ_TYPE_METHOD;
	}
	
	@Override
	public int getCodeArgs() {
		if (hasRestParm() || parms.size() > 2 || hasDefaultArgs()) {
			return -1;
		} else {
			return parms.size() - (hasBlockParm() ? 1 : 0);
		}
	}
	
	private boolean hasDefaultArgs() {
		return !optionals.isEmpty();
	}

	public String[] getAllParmNames() {
		return parm_names.toArray(new String[parm_names.size()]);
	}

	List<Expression> parms = new ArrayList<Expression>();
	final Expression singleton;
	private final String name;
	
	int min_arg_count  = 0;
	boolean has_rest_parm = false;
	boolean has_block_parm = false;
	
	List<Expression> optionals = new ArrayList<Expression>();
	List<String> parm_names = new ArrayList<String>();
	
	public MethodCode(int line, RubyCode context, Expression singleton, String name, String fileName) {
		super(line, context, fileName);
		this.singleton = singleton;
		this.name = name;
	}

	public String getMethodName() {
		return name;
	}

	public void addBlockParameter(String name) {
		super.assignToLocal(name, true);
		parms.add(new BlockArgExpression(new IdentifierExpression(this, name)));
		parm_names.add(name);
		has_block_parm = true;
	}

	public boolean hasBlockParm() {
		return has_block_parm;
	}

	static int counter;
	
	public void addRestParameter(String name) {
		if (name == null) {
			name = "anon$"+counter++;
		}

			parm_names.add(name);
			super.assignToLocal(name, true);
			parms.add(new RestArgExpression(new IdentifierExpression(this, name)));

		has_rest_parm = true;
	}

	public void addParameter(String name, Expression expr) {
		if (name == null) {
			name = "anon$"+counter++;
		}

		super.assignToLocal(name, true);
		if (expr == null) {
			parm_names.add(name);
			parms.add(new IdentifierExpression(this, name));
			min_arg_count += 1;
		} else {
			parm_names.add(name);
			parms.add(new IdentifierExpression(this, name));
			optionals.add(expr);
		}
	}

	int[] optional_pos;
	
	public void compile(CompileContext ctx) {

		assign_local_indexes();
		
		
		List<Expression> save = ctx.set_method_parms(this.parms);
		
		
		// generate code for optional parameters (default args)
		
		// this keeps the PC of the code to initialize this optional
		optional_pos = new int[optionals.size()+1];
		
		for (int i = 0; i < optionals.size(); i++) {
			Expression e = optionals.get(i);;
			
			optional_pos[i] = ctx.get_code_position();
			
			IdentifierExpression id = (IdentifierExpression)parms.get(min_arg_count + i);
			
			ctx.set_is_compiling_default_value(i);
			e.compile(ctx, true);
			id.compile_assignment(ctx, false);
		}
		ctx.set_is_compiling_default_value(-1);
		
		// if all optional args are given, goto here...
		optional_pos[optionals.size()] = ctx.get_code_position();
		
		super.compile(ctx);
		
		ctx.set_method_parms(save);
	}

	
	public int[] getDefaultArgEvaluation() {
		return optional_pos;
	}
	
	public int getMinParmCount() {
		return min_arg_count;
	}
	
	public boolean hasRestParm() {
		return has_rest_parm;
	}

	
	public int[] getDynamicsFromArgs() {
		int[] result = new int[getNumDynamics()];
		for (int i = 0; i < getNumDynamics(); i++) {
			String name = null;
			for (LocalVariable loc : locals) {
				if (loc.getDynamicIndex() == i) {
					name = loc.name;
					break;
				}
			}
			
			assert name != null;
			
			for (int nam = 0; nam < parm_names.size(); nam++) {
				if (name.equals(parm_names.get(nam))) {
					result[i] = nam+1;
					break;
				}
			}
		}
		return result;
	}
	

}
