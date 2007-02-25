package com.trifork.hotruby.objects;

import com.trifork.hotruby.classes.RubyClassRange;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.MetaModule;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.NonLocalReturn;
import com.trifork.hotruby.runtime.RubyBlock;
import com.trifork.hotruby.runtime.RubyRuntime;
import com.trifork.hotruby.runtime.Selector;

public class RubyRange extends RubyBaseRange {

	static final RubyRuntime runtime = LoadedRubyRuntime.instance;

	static final MetaModule ctx = RubyClassRange.instance.get_meta_module();

	private static final Selector SEL_GE = runtime.getSelector(ctx, ">=");

	private static final Selector SEL_LE = runtime.getSelector(ctx, "<=");

	private static final Selector SEL_LT = runtime.getSelector(ctx, "<");

	private static final Selector SEL_EQ2 = runtime.getSelector(ctx, "==");

	private static final Selector SEL_SUCC = runtime.getSelector(ctx, "succ");

	private static final Selector SEL_CMP = runtime.getSelector(ctx, "<=>");

	private static final IRubyFixnum FIX0 = runtime.newFixnum(0);

	IRubyObject first;

	private IRubyObject last;

	private boolean include_last;

	public RubyRange init(IRubyObject first, IRubyObject last,
			IRubyObject include_last) {
		this.first = first;
		this.last = last;
		this.include_last = include_last.isTrue();
		return this;
	}

	public IRubyObject first() {
		return first;
	}

	public IRubyObject includes(IRubyObject other) {
		if (other instanceof IRubyNumeric) {
			IRubyNumeric num = (IRubyNumeric) other;

			return bool(num.fast_ge(first, SEL_GE).isTrue()
					&& (include_last ? num.fast_le(last, SEL_LE) : num.fast_lt(
							last, SEL_LT)).isTrue());

		}
		return bool(false);
	}

	public IRubyObject last() {
		return last;
	}

	public IRubyObject fast_eq2(IRubyObject arg, CallContext ctx) {
		return eq2(arg);
	}

	public IRubyObject eq2(IRubyObject other) {
		if (other == this) {
			return bool(true);
		}

		if (other instanceof RubyRange) {
			RubyRange or = (RubyRange) other;
			IRubyObject test1 = first.fast_eq2(or.first, SEL_EQ2);
			if (test1.isFalse())
				return bool(false);

			IRubyObject test2 = last.fast_eq2(or.last, SEL_EQ2);
			if (test2.isFalse())
				return bool(false);

			return bool(include_last == or.include_last);
		}

		return bool(false);
	}

	public IRubyObject each(RubyBlock block) {

		all: do {

			IRubyObject curr = this.first;
			IRubyObject last = this.last;

			while (true) {
				boolean this_is_last = range_equals(curr, last);
				if ((!include_last) & this_is_last) {
					return this;
				}

				redo_label: do {
					try {
						block.call(curr);
					} catch (NonLocalBreak e) {
						return this;
					} catch (NonLocalNext e) {
						// do nothing //
					} catch (NonLocalRedo e) {
						continue redo_label;
					}
				} while (false);

				if (include_last & this_is_last) {
					return this;
				}

				curr = curr.do_select(SEL_SUCC.get()).call(curr, (RubyBlock) null);
			}

		} while (false);

	}

	private boolean range_equals(IRubyObject o1, IRubyObject o2) {
		IRubyObject cmp_result = o1.fast_cmp(o2, SEL_CMP);
		return RubyInteger.induced_from(cmp_result).intValue() == 0;
	}

	public boolean include_last() {
		return include_last;
	}

	public boolean exclude_end_p() {
		return !include_last;
	}
}
