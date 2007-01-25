package com.trifork.hotruby.objects;

import java.io.IOException;

import com.trifork.hotruby.classes.RubyClassArray;
import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyBlock1;
import com.trifork.hotruby.runtime.Selector;

public class RubyArray extends RubyBaseArray {

	// private static final CallContext META = RubyClassArray.instance
	// .get_meta_class();

	public String inspect() {
		if (size == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");

		for (int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(value[i].inspect());
		}

		sb.append("]");
		return sb.toString();
	}

	private static final IRubyObject[] EMPTY_ARRAY = new IRubyObject[0];

	private static final Selector call0_call = LoadedRubyRuntime.instance
			.getSelector(RubyClassArray.instance.get_meta_module(), "call");

	private IRubyObject[] value;

	private int size;

	public RubyArray() {
		value = EMPTY_ARRAY;
		size = 0;
	}

	public RubyArray(IRubyObject[] args) {
		this.value = args;
		this.size = args.length;
	}

	public RubyArray initialize() {
		return initialize(0);
	}

	public RubyArray initialize(int size) {
		return initialize(size, LoadedRubyRuntime.NIL);
	}

	public RubyArray initialize(int size, IRubyObject val) {
		this.value = new IRubyObject[size];
		for (int i = 0; i < size; i++) {
			this.value[i] = val;
		}
		this.size = size;
		return this;
	}

	public int int_size() {
		return size;
	}

	public RubyFixnum size() {
		return new RubyFixnum(size);
	}

	public RubyArray reverse() {
		IRubyObject[] out = new IRubyObject[size];
		for (int i = 0; i < size; i++) {
			out[i] = value[size - 1 - i];
		}
		return new RubyArray(out);
	}

	public IRubyObject at(IRubyObject index) {

		int val = RubyInteger.mm_induced_from(index).intValue();
		if (val < 0) {
			val = size - val;
		}

		if (val >= size || val < 0) {
			return LoadedRubyRuntime.NIL;
		} else {
			return value[val];
		}
	}

	public IRubyArray add(IRubyObject elem) {
		ensure_capacity(size + 1);
		value[size++] = elem;
		return this;
	}

	public RubyArray push(IRubyObject obj, IRubyObject[] rest) {
		ensure_capacity(size + 1 + rest.length);
		value[size++] = obj;
		for (int i = 0; i < rest.length; i++) {
			value[size++] = rest[i];
		}
		return this;
	}

	public RubyArray each(RubyBlock block) {
		loop: for (int i = 0; i < size; i++) {
			body: do {
				try {
					block.call(value[i]);
				} catch (NonLocalBreak e) {
					break loop;
				} catch (NonLocalNext e) {
					continue loop;
				} catch (NonLocalRedo e) {
					continue body;
				}
			} while (false);
		}
		return this;
	}

	public RubyArray map(final RubyBlock block) {
		final RubyArray result = new RubyArray();

		each(new RubyBlock1() {
			public IRubyObject call(IRubyObject arg1) throws NonLocalBreak,
					NonLocalNext, NonLocalRedo, NonLocalReturn {
				result.add(block.call(arg1));
				return LoadedRubyRuntime.NIL;
			}
		});

		return result;
	}

	public IRubyObject detect(IRubyObject[] ifnoneProc, RubyBlock block) {

		loop: for (int i = 0; i < size; i++) {
			body: do {
				try {
					IRubyObject result = block.call(value[i]);

					if (result.isTrue()) {
						return value[i];
					}
				} catch (NonLocalBreak e) {
					break loop;
				} catch (NonLocalNext e) {
					continue loop;
				} catch (NonLocalRedo e) {
					continue body;
				}
			} while (false);
		}

		if (ifnoneProc != null && ifnoneProc.length > 1) {
			ifnoneProc[0].do_select(call0_call).call(ifnoneProc[0]);
		}

		return LoadedRubyRuntime.NIL;

	}

	public Object all_p(RubyBlock block) {
		if (block == null) {
			for (int i = 0; i < size; i++) {
				if (value[i].isFalse()) {
					return LoadedRubyRuntime.FALSE;
				}
			}
		} else {
			loop: for (int i = 0; i < size; i++) {
				IRubyObject result = LoadedRubyRuntime.NIL;
				body: do {
					try {
						result = block.call(value[i]);
					} catch (NonLocalBreak e) {
						break loop;
					} catch (NonLocalNext e) {
						continue loop;
					} catch (NonLocalRedo e) {
						continue body;
					}
				} while (false);
				if (result.isFalse()) {
					return LoadedRubyRuntime.FALSE;
				}
			}
		}
		return LoadedRubyRuntime.TRUE;
	}

	public IRubyObject any_p(RubyBlock block) {
		if (block == null) {
			for (int i = 0; i < size; i++) {
				if (value[i].isTrue()) {
					return LoadedRubyRuntime.TRUE;
				}
			}
		} else {

			loop: for (int i = 0; i < size; i++) {
				IRubyObject result = LoadedRubyRuntime.NIL;
				body: do {
					try {
						result = block.call(value[i]);
					} catch (NonLocalBreak e) {
						break loop;
					} catch (NonLocalNext e) {
						continue loop;
					} catch (NonLocalRedo e) {
						continue body;
					}
				} while (false);
				if (result.isTrue()) {
					return LoadedRubyRuntime.TRUE;
				}
			}
		}
		return LoadedRubyRuntime.FALSE;
	}

	public IRubyArray mapx(RubyBlock block) {
		if (block != null) {
			loop: for (int i = 0; i < size; i++) {
				body: do {
					try {
						int size_before = size;
						IRubyObject val = block.call(value[i]);

						// if the block modified the contents, then
						// we need to call the expensive version of at_put
						if (size == size_before) {
							value[i] = val;
						} else {
							at_put_x(new RubyFixnum(i), val);
						}
					} catch (NonLocalBreak e) {
						break loop;
					} catch (NonLocalNext e) {
						continue loop;
					} catch (NonLocalRedo e) {
						continue body;
					}
				} while (false);
			}
		}
		return this;
	}

	private void ensure_capacity(int i) {
		int len = value.length;
		if (len >= i)
			return;

		int new_length = Math.max(4, Math.min(i, len * 2));
		IRubyObject[] new_value = new IRubyObject[new_length];
		System.arraycopy(value, 0, new_value, 0, len);
		for (int p = value.length; p < new_length; p++) {
			new_value[p] = LoadedRubyRuntime.NIL;
		}
		value = new_value;
	}

	public IRubyObject subseq(int beg, int len) {
		if (beg > size || beg < 0 || len < 0) {
			return LoadedRubyRuntime.NIL;
		}

		if (beg + len > size) {
			len = size - beg;
		}

		if (len <= 0) {
			return new RubyArray();
		}

		IRubyObject[] out = new IRubyObject[len];
		System.arraycopy(value, beg, out, 0, len);
		return new RubyArray(out);
	}

	// implementation of []=
	public IRubyObject at_put_x(IRubyObject idx, IRubyObject object) {

		if (idx instanceof IRubyInteger) {
			IRubyInteger ival = (IRubyInteger) idx;
			int val = ival.intValue();

			if (val < 0) {
				val = size - val;
			}

			if (val < 0) {
				return LoadedRubyRuntime.NIL;
			} else if (val < size) {
				return value[val] = object;
			} else {
				ensure_capacity(val + 1);
				return value[val] = object;
			}

		}

		System.out.println(LoadedRubyRuntime.instance.caller(0));

		throw new InternalError("not implemented: idx=" + idx.get_class());
	}

	// implementation of []
	public IRubyObject at_x(IRubyObject idx) {
		if (idx instanceof IRubyInteger) {
			return at((IRubyInteger) idx);
		}

		if (idx instanceof RubyRange) {
			final RubyArray result = new RubyArray();
			RubyRange range = (RubyRange) idx;
			int first = RubyInteger.mm_induced_from(range.first()).intValue();
			int last = RubyInteger.mm_induced_from(range.last()).intValue();
			if (range.include_last()) {
				last += 1;
			}

			int count = 0;
			for (int i = first; (last<0 ? count==last : i==last); i++) {
				result.add(int_at(i));
				count ++;
			}
			
			return result;
			
		} else {
			throw  getRuntime().newRuntimeError(new RubyString("not implemented, array[" + idx.inspect() + "]"));
		}
		
		
//		throw new InternalError();
	}

	public static IRubyObject unmarshalFrom(UnmarshalStream input,
			CallContext ctx) throws IOException {
		RubyArray result = new RubyArray();
		input.registerLinkTarget(result);
		int size = input.unmarshalInt();
		for (int i = 0; i < size; i++) {
			result.add(input.unmarshalObject(ctx));
		}
		return result;
	}

	public IRubyObject int_at(int i) {
		if (i >= size) {
			return LoadedRubyRuntime.NIL;
		} else {
			return value[i];
		}
	}

	public RubyArray int_rest(int idx) {
		if (idx >= size) {
			return new RubyArray(EMPTY_ARRAY);
		} else {
			IRubyObject[] arr = new IRubyObject[size - idx];
			System.arraycopy(value, idx, arr, 0, arr.length);
			return new RubyArray(arr);
		}
	}

	public void int_at_put(int i, IRubyObject object) {
		assert i >= 0;
		ensure_capacity(i);
		if (i >= size) {
			size = i + 1;
		}
		value[i] = object;
	}

	public IRubyObject shift() {
		if (size == 0) {
			return getRuntime().getNil();
		}
		IRubyObject result = value[0];
		size -= 1;
		System.arraycopy(value, 1, value, 0, size);
		return result;
	}

	@Override
	public IRubyObject fast_eq2(IRubyObject arg, Selector selector) {
		return op_eq2(arg, selector);
	}

	final public IRubyObject op_eq2(IRubyObject arg, Selector selector) {
		if (arg instanceof RubyArray) {
			RubyArray ra = (RubyArray) arg;
			if (ra.size != size)
				return bool(false);
			for (int i = 0; i < size; i++) {
				if (int_at(i).fast_eq2(ra.int_at(i), selector).isFalse()) {
					return bool(false);
				}
			}
			return bool(true);
		}
		return bool(false);
	}
}
