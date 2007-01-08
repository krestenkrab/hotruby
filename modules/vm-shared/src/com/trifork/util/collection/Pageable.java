package com.trifork.util.collection;

import java.nio.ByteBuffer;

public interface Pageable {
	void init(ByteBuffer bb, int pos);
}
