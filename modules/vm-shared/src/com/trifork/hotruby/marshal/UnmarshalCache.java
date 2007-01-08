package com.trifork.hotruby.marshal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.runtime.RubyRuntime;

public class UnmarshalCache {
    private final RubyRuntime runtime;
    private List links = new ArrayList();
    private List symbols = new ArrayList();

    public UnmarshalCache(RubyRuntime runtime) {
        this.runtime = runtime;
    }

    public void register(IRubyObject value) {
        selectCache(value).add(value);
    }

    private List selectCache(IRubyObject value) {
        return (value instanceof IRubySymbol) ? symbols : links;
    }

    public boolean isLinkType(int c) {
        return c == ';' || c == '@';
    }

    public IRubyObject readLink(UnmarshalStream input, int type) throws IOException {
        if (type == '@') {
            return linkedByIndex(input.unmarshalInt());
        }
        assert type == ';';
        return symbolByIndex(input.unmarshalInt());
    }

    private IRubyObject linkedByIndex(int index) {
        try {
            return (IRubyObject) links.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw runtime.newArgumentError("dump format error (unlinked, index: " + index + ")");
        }
    }

    private IRubySymbol symbolByIndex(int index) {
        try {
            return (IRubySymbol) symbols.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw runtime.newTypeError("bad symbol");
        }
    }
}
