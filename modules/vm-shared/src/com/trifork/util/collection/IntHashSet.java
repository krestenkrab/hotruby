package com.trifork.util.collection;



public class IntHashSet implements IntCollection, IntSet {

	int size;

	int[] data = new int[7];

	public IntHashSet() {
		this(7);
	}

	public IntHashSet(int size) {
		data = new int[Math.max(size, 3)];
	}

	public IntHashSet(int[] integers) {
		this(integers, true);
	}

	public IntHashSet(int[] integers, boolean clone) {
		if (clone) {
			data = (int[]) integers.clone();
		} else {
			data = integers;
		}

		size = data.length;
	}

	public void add(int val) {
		if (val == NO_VALUE) {
			throw new IllegalArgumentException();
		}

		if (isFull()) {
			growAndRehash((data.length * 2) + 1);
		}

		int pos = hash(val);
		
		while (data[pos] != NO_VALUE) {
		
			if (data[pos] == val) {
				return;
			}
			
			pos = (pos + 1) % data.length;
		}

		data[pos] = val;
		size += 1;
	}

	public int[] toArray()
	{
		int[] result = new int[size];
		int idx = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i] != NO_VALUE) {
				result[idx++] = data[i];
			}
		}
		if (idx != result.length) {
			throw new InternalError();
		}
		return result;
	}
	
	private void growAndRehash(int new_size) {
		//System.out.print(".");
		
		int[] old = data;
		data = new int[new_size];
		size = 0;
		
		for (int i = 0; i < old.length; i++) {
			if (old[i] != NO_VALUE) {
				add(old[i]);
			}
		}
	}

	private boolean isFull() {
		return size > data.length / 2;
	}

	private int hash(int val) {
		return Math.abs(val) % data.length;
	}

	public void forEach(IntIter iter) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != NO_VALUE) {
				iter.each(data[i]);
			}
		}
	}

    public void forEach(IntExceptionIter iter) throws Exception {
        for (int i = 0; i < data.length; i++) {
            if (data[i] != NO_VALUE) {
                iter.each(data[i]);
            }
        }
    }

    public int size() {
		return size;
	}

	public boolean contains(int val) {
		int idx = hash(val);
		int cnt = 0;
		for (int i = idx; data[i] != NO_VALUE; i = (i+1)%data.length) {
			
			if (cnt == data.length) { return false; }
			
			if (data[i] == val) {
				return true;
			}
			
			cnt += 1;
		}
		return false;
	}

	
	public void addAll(IntHashSet other) {
		if ((size + other.size) * 2 > data.length) {
			growAndRehash((size + other.size) * 2 + 1);
		}
		
		int[] od = other.data;
		for (int i = 0; i < od.length; i++) {
			if (od[i] != NO_VALUE) {
				add(od[i]);
			}
		}
	}

	public void addAll(IntCollection other) {
		other.forEach(new IntIter() {
			public void each(int i) {
				IntHashSet.this.add(i);
			}
		});
	}

	public IntIterator iterator() {
		
		return new IntIterator() {

			int data[] = IntHashSet.this.data;
			int next = findNext(0);
			
			private int findNext(int i) 			{
				for (; i < data.length; i++) {
					if (data[i] != NO_VALUE) {
						break;
					}
				}
				return i;
			}
			
			public int next() {
				int result = data[next];
				next = findNext(next+1);
				return result;
			}

			public boolean hasNext() {
				return next < data.length;
			}

			public int size() {
				return IntHashSet.this.size();
			}
			
		};
	}

}
