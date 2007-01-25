package com.trifork.hotruby.compiler;

import com.trifork.hotruby.objects.IRubyArray;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubyRange;
import com.trifork.hotruby.objects.IRubyRegexp;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyRuntime;

public class CompiledCodeUtil {

	public static void setDVar(IRubyObject value, CompiledEvalContext ctx,
			int idx) {
		ctx.set(idx, value);
	}

	public static void checkBlock(RubyBlock block, RubyRuntime runtime) {
		if (block == null) {
			throw runtime.newLocalJumpError("no block given", null);
		}
	}

	public static IRubyObject[] java_array_put_rest(IRubyObject[] rest,
			IRubyArray[] into) {
		System.arraycopy(rest, 0, into, into.length - rest.length, rest.length);
		return into;
	}

	public static IRubyArray java_array_rest(IRubyObject[] arr, int idx,
			RubyRuntime runtime) {
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

	public static IRubyObject java_array_at(IRubyObject[] arr, int idx,
			RubyRuntime runtime) {
		if (arr.length > idx) {
			return arr[idx];
		} else {
			return runtime.getNil();
		}
	}

	public static IRubyObject make_return_value(IRubyObject val, RubyRuntime runtime) {
		if (val instanceof IRubyArray) {
			IRubyArray arr = (IRubyArray) val;
			if (arr.int_size() == 1) {
				val = arr.int_at(0);
			} else if (arr.int_size() == 0) {
				val = runtime.getNil();
			}
		}
		return val;
	}

	public static IRubyObject[] convert_rest_arg_to_array(IRubyObject object,
			int pre_n_size, RubyRuntime runtime) {
		
		IRubyArray arr;
		if (object instanceof IRubyArray) {
			arr = (IRubyArray) object;
		} else if (object == runtime.getNil()) {
			arr = runtime.newArray(1);
		} else {
			arr = runtime.newArray(new IRubyObject[] { object });
		}

		IRubyObject[] result = new IRubyObject[pre_n_size + arr.int_size()];
		for (int i = 0; i < arr.int_size(); i++) {
			result[pre_n_size+i] = arr.int_at(i);
		}
		
		return result;
	}

	public static IRubyRange newRange(IRubyObject first, IRubyObject last,
			boolean inclusive, RubyRuntime runtime) {
		return runtime.newRange(first, last, inclusive);
	}

	public static IRubyRegexp newRegexp(IRubyObject string, int flags,
			RubyRuntime runtime) {
		return runtime.newRegexp(string, flags);
	}

}
