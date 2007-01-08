package com.trifork.util.collection;

import java.util.Arrays;


public class ArraySortedIntSet extends ImmutableSortedIntSet {

	private int[] rep;
	
	public ArraySortedIntSet(IntCollection coll)
	{
		this(coll.iterator());
	}
	
	public ArraySortedIntSet(IntIterator iterator) {
		init(iterator);
	}

	protected void sort() {
		Arrays.sort(rep);
	}
	
	protected int get(int idx) {
		return rep[idx];
	}

	protected void setRep(int idx, int val) {
		rep[idx] = val;
	}

	protected void allocRep(int size) {
		rep = new int[size];
	}

	public int size() {
		return rep.length;
	}

}
