package com.trifork.hotruby.objects;
import com.trifork.hotruby.callable.*;
import com.trifork.hotruby.classes.*;
import com.trifork.hotruby.objects.*;
import com.trifork.hotruby.runtime.*;
public abstract class RubyBaseBignum
	extends RubyInteger
	implements IRubyBignum
{
	interface SelectBignum { RubyMethod get_RubyBignum(); }
	public RubyMethod select(Selector sel) {
		if(sel instanceof SelectBignum) { return ((SelectBignum)sel).get_RubyBignum(); }
		else { return LoadedRubyRuntime.resolve_method((RubyObject)this,sel,SelectBignum.class); }
	}
	public RubyClass get_class() { return RubyClassBignum.instance; }
}
