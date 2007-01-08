package com.trifork.util.collection;

public interface IntCollection {
	int[] toArray();
    void forEach(IntIter iter);
    void forEach(IntExceptionIter iter) throws Exception;
	void add(int value);
	int size();
	IntIterator iterator();
	boolean contains(int value);
}
