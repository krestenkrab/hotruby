package com.trifork.hotruby.interp;

public interface ConstantPool {

	/** returns index of new value in constant pool */
	int add_integer(String text, int radix);
	
	/** same for small integer */
	int add_integer(int intvalue);

	/** add symbol to constant table */
	int add_symbol(String sym1);

	/** add regex to constant table 
	 * @param flags */
	int add_regex(String text, int flags);

	int add_string(String text);

	int add_float(double val);

}
