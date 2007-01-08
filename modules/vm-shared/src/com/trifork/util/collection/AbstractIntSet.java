package com.trifork.util.collection;

public abstract class AbstractIntSet implements IntSet {

	public void add(int val) {
		throw new UnsupportedOperationException();
	}

	public int[] toArray() {
		IntIterator it = iterator();
		int[] result = new int[it.size()];
		int idx = 0;
		while(it.hasNext()) {
			result[idx++] = it.next();
		}
		return result;
	}

    public void forEach(IntIter iter) {
        IntIterator it = iterator();
        while(it.hasNext()) {
            iter.each(it.next());
        }
    }

    public void forEachException(IntExceptionIter iter) throws Exception {
        IntIterator it = iterator();
        while(it.hasNext()) {
            iter.each(it.next());
        }
    }

	public int size() {
		return iterator().size();
	}

	public boolean contains(int val) {
		IntIterator it = iterator();
		while(it.hasNext()) {
			if(it.next() == val) return true;
		}
		return false;
	}

	public void addAll(IntCollection other) {
		other.forEach(new IntIter() {
			public void each(int i) {
				add(i);
			}});
	}


}
