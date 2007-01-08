package com.trifork.util.collection;


public abstract class ImmutableSortedIntSet implements IntSet {

	protected abstract int get(int idx);
	
	public void add(int val) {
		throw new UnsupportedOperationException();
	}

	protected abstract void setRep(int idx, int val);
	protected abstract void allocRep(int size);
	
	protected void init(IntIterator coll)
	{
		int max = coll.size();
		allocRep(max);
		
		int pos = 0;
		while(coll.hasNext()) {
			if (pos == max) { throw new IllegalArgumentException(); }
			setRep(pos++, coll.next());
		}
		
		if (pos != max) {
			throw new IllegalArgumentException();
		}
		
		sort();
	}
	
	protected void sort() {
		sortRep(0, size()-1);
	}

	private void sortRep(int low, int high) {
		if (high-low < 2) return;
		if (high <= low) throw new InternalError("XXX");
		
		int pivotValue = get(low + (high-low)/2);
		
		int high2 = high;
		int low2 = low;
		
		while (high2 > low2) {
			int val = get(low2);
			if (val > pivotValue) {
				swap(low2, high2--);
			} else {
				low2++;
			}
		}
		
		if (high2 != low2) {
			throw new InternalError("YYY");
		}
		
		sortRep(low, low2);
		sortRep(high2, high);
	}

	private void swap(int idx0, int idx1) {
		int val = get(idx1);
		setRep(idx1, get(idx0));
		setRep(idx0, val);
	}
	
	public int[] toArray() {
		int[] arr = new int[size()];
		for (int i = 0; i < size(); i++) {
			arr[i] = get(i);
		}
		return arr;
	}

    public void forEach(IntIter iter) {
        for (int i = 0; i < size(); i++) {
            iter.each(get(i));
        }
    }

    public void forEach(IntExceptionIter iter) throws Exception {
        for (int i = 0; i < size(); i++) {
            iter.each(get(i));
        }
    }

	public abstract int size();

	public boolean contains(int val) {
		int low = 0;
		int high = size();
		
		while(high>low) {
			int pivot = low + (high-low)/2;
			int elm = get(pivot);
			if (elm == val) return true;
			if (elm > val) { high = pivot; }
			else { low = pivot+1; }
		}
		
		return false;
	}

	public void addAll(IntCollection other) {
		throw new UnsupportedOperationException();
	}

	public IntIterator iterator() {
		return new IntIterator() {
			
			int pos = 0;

			public int next() {
				return ImmutableSortedIntSet.this.get(pos++);
			}

			public boolean hasNext() {
				return pos < size();
			}

			public int size() {
				return ImmutableSortedIntSet.this.size();
			}
			
		};
	}


	public String toString() {
		final StringBuffer sb = new StringBuffer("[");
		
		for (int i = 0; i < size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(get(i));
		}
		
		sb.append("]");
		return sb.toString();
	}
	
}
