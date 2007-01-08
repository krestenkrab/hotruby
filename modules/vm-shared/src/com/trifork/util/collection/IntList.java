package com.trifork.util.collection;


public class IntList implements IntCollection {

	int size;

	int[] data = new int[3];

	public IntList() {
		super();
	}

	public IntList(int[] instances) {
		this.data = instances;
		this.size = data.length;
	}

	public int size() {
		return size;
	}

	public void add(int value) {
		if (size == data.length) {
			int[] new_data = new int[size * 2];
			System.arraycopy(data, 0, new_data, 0, size);
			data = new_data;
		}

		data[size++] = value;
	}

	public int[] toArray() {
		int[] result = new int[size];
		System.arraycopy(data, 0, result, 0, size);
		return result;
	}

    public void forEach(IntIter iter) {
        for (int i = 0; i < size; i++) {
            iter.each(data[i]);
        }
    }

    public void forEach(IntExceptionIter iter) throws Exception {
        for (int i = 0; i < size; i++) {
            iter.each(data[i]);
        }
    }

	public int get(int i) {
		return data[i];
	}
	
	public void set(int idx, int val) {
		data[idx] = val;
	}

	public IntIterator iterator() {
		return new IntIterator() {

			int pos = 0;
			
			public int next() {
				return get(pos++);
			}

			public boolean hasNext() {
				return pos < size();
			}

			public int size() {
				return IntList.this.size();
			}
			
		};
	}

	public boolean contains(int value) {
		for (int i = 0; i < size; i++) {
			if (data[i] == value) { return true; }
		}
		return false;
	}

}
