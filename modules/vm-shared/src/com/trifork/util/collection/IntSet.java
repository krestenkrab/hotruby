
package com.trifork.util.collection;





public interface IntSet extends IntCollection {

	public static final int NO_VALUE = 0;

	public abstract void add(int val);

	public abstract int[] toArray();

    public abstract void forEach(IntIter iter);

    public abstract void forEach(IntExceptionIter iter) throws Exception;

	public abstract int size();

	public abstract boolean contains(int val);

	public abstract void addAll(IntCollection other);

	public abstract IntIterator iterator();

}