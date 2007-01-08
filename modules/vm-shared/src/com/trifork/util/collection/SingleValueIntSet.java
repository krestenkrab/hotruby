package com.trifork.util.collection;


public class SingleValueIntSet implements IntSet {

	final int val;
	
	public boolean equals(Object other)
	{
		if (other instanceof IntSet) {
			IntSet otherSet = (IntSet) other;
			return otherSet.size() == 1 && otherSet.contains(val);
		} else {
			return false;
		}
	}
	
	public SingleValueIntSet(int val) { this.val = val; }
	
	public void add(int val) {
		if (val != this.val) {
			throw new UnsupportedOperationException();
		}
	}

	public int[] toArray() {
		return new int[] { val };
	}

    public void forEach(IntIter iter) {
        iter.each(val);
    }

    public void forEach(IntExceptionIter iter) throws Exception {
        iter.each(val);
    }

	public int size() {
		return 1;
	}

	public boolean contains(int val) {
		return val == this.val;
	}

	public void addAll(IntCollection other) {
		throw new UnsupportedOperationException();
	}

	public IntIterator iterator() {
		return new IntIterator() {

			boolean hasNext = true;
			
			public int next() {
				hasNext = false;
				return val;
			}

			public boolean hasNext() {
				return hasNext;
			}

			public int size() {
				return 1;
			}
			
		};
	}

}
