package com.trifork.hotruby.objects;

import java.util.HashMap;
import java.util.Map;

import com.trifork.hotruby.marshal.UnmarshalStream;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.LoadedRubyRuntime;
import com.trifork.hotruby.runtime.NonLocalBreak;
import com.trifork.hotruby.runtime.NonLocalJump;
import com.trifork.hotruby.runtime.NonLocalNext;
import com.trifork.hotruby.runtime.NonLocalRedo;
import com.trifork.hotruby.runtime.RubyBlock;

public class RubyHash extends RubyBaseHash {

	HashMap<IRubyObject, IRubyObject> data = new HashMap<IRubyObject, IRubyObject>();

	RubyBlock default_maker;

	IRubyObject default_object;

	public RubyHash()
	{
		init(LoadedRubyRuntime.NIL,null);		
	}
	
	public void init(IRubyObject object, RubyBlock block) {
		this.default_object = object;
		this.default_maker = block;
	}

	
	public IRubyObject get_default(IRubyObject key) {
		if (key == null) {
			key = LoadedRubyRuntime.NIL;
		}
		if (default_maker != null) {
			try {
				return default_maker.call(this, key);
			} catch (NonLocalJump e) {
				throw getRuntime().newLocalJumpError("unhandled non-load jump",
						e);
			}
		}
		return default_object;
	}

	public static IRubyObject unmarshalFrom(UnmarshalStream stream,
			CallContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	public RubyHash set_default(IRubyObject val) {
		this.default_object = val;
		this.default_maker = null;
		return this;
	}

	public IRubyObject default_proc() {
		if (default_maker == null) {
			return LoadedRubyRuntime.NIL;
		} else {
			return new RubyProc(default_maker);
		}
	}

	public IRubyObject put(IRubyObject arg1, IRubyObject arg2) {
		data.put(arg1, arg2);
		return arg2;
	}

	public IRubyObject get(IRubyObject key) {
		IRubyObject val = data.get(key);
		if (val == null) {
			return get_default(key);
		} else {
			return val;
		}
	}

	public String inspect() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		boolean first = true;
		for (Map.Entry<IRubyObject, IRubyObject> ent : data.entrySet()) {
			if (!first) {
				sb.append(", ");
			}
			sb.append(ent.getKey().inspect());
			sb.append(" => ");
			sb.append(ent.getValue().inspect());
		}

		sb.append("}");
		return sb.toString();
	}

	public IRubyObject delete(IRubyObject key, RubyBlock block) {
		IRubyObject val = data.remove(key);
		if (val == null) {
			if (block != null) {
				try {
					return block.call(key);
				} catch (NonLocalJump e) {
					throw getRuntime().newLocalJumpError(
							"unhandled local jump", e);
				}
			} else {
				return get_default(key);
			}
		}

		return val;
	}

	public RubyHash each(RubyBlock block) {

		loop: for (Map.Entry<IRubyObject, IRubyObject> ent : data.entrySet()) {
			body: do {
				try {
					block.call(ent.getKey(), ent.getValue());
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

}
