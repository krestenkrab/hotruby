package com.trifork.util.collection;

import com.trifork.util.collection.IntegerToObjectMap.EntryIterator;


public interface IntMapToSet {

	int valuesTotalSize();
	
	void addAllToValue(int key, IntSet values);
	
	void put (int key, IntSet value);
	
	IntSet getSet (int key);
	
	IntIterator keyIterator();
	
	int keyCount();

	EntryIterator entryIterator();

	boolean containsKey(int target);

	IntIterator allValuesIterator();
	
}
