package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyRuntime;

public class CompiledCodeUtil {

	public static void setDVar
		(IRubyObject value, CompiledEvalContext ctx, int idx) {
		ctx.set(idx, value);
	}
	
	public static void checkBlock(RubyBlock block, RubyRuntime runtime)
	{
		if (block == null) {
			throw runtime.newArgumentError("no block");
		}
	}
	
	public static IRubyObject[] java_array_put_rest(IRubyObject[] rest, IRubyArray[] into)
	{
		System.arraycopy(rest, 0, into, into.length-rest.length, rest.length);
		return into;
	}
	
	public static IRubyArray java_array_rest(IRubyObject[] arr, int idx, RubyRuntime runtime) {
		int alen = arr.length;
		if (alen > idx) {
			int rlen = alen - idx;
			IRubyObject[] result = new IRubyObject[rlen];
			System.arraycopy(arr, idx, result, 0, rlen);
			return runtime.newArray(result);
		} else {
			return runtime.newArray();
		}
	}
	
	public static IRubyObject java_array_at(IRubyObject[] arr, int idx, RubyRuntime runtime) {
		if (arr.length > idx) {
			return arr[idx];
		} else {
			return runtime.getNil();
		}
	}


}
