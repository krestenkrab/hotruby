package com.trifork.util.collection;


public class IdentityToIntHashMap {

	static final int NO_VALUE = Integer.MIN_VALUE;
	static final int BUCKET_SIZE = 32;

	static class Bucket {
		Object[] data1;
		int[] data2;

		final int order;

		int size;

		public Bucket(int order) {
			this.order = order;
			this.data1 = new Object[BUCKET_SIZE];
			this.data2 = new int[BUCKET_SIZE];
		}

		public boolean isFull() {
			return size == BUCKET_SIZE;
		}

		public boolean add(Object key, int num) {
			for (int i = 0; i < size; i++) {
				if (data1[i] == key) { return false; }
			}
			
			int pos = size++;
			data1[pos] = key;
			data2[pos] = num;
			return true;
		}

		public void forEach(ObjectIntIter iter) {
			for (int i = 0; i < size; i++) {
				iter.each(data1[i], data2[i]);
			}
		}

		public boolean contains(Object key) {
			for (int i = 0; i < size; i++) {
				if (key == data1[i]) {
					return true;
				}
			}
			return false;
		}

		public int get(Object key) {
			for (int i = 0; i < size; i++) {
				if (key == data1[i]) {
					return data2[i];
				}
			}
			return NO_VALUE;
		}
    }

	Bucket[] table = new Bucket[4];

	int size = 0;

	public IdentityToIntHashMap() {
	}

	public void add(Object key, int num) {

		int orig_size = size;
		
		int bucketIndex = hash(key);
		Bucket buck = table[bucketIndex];
		if (buck == null) {
			buck = new Bucket(table.length);
			table[bucketIndex] = buck;
		} else {
			while (buck.isFull()) {
				split(bucketIndex);
				bucketIndex = hash(key);
				buck = table[bucketIndex];
			}
		}

		if (buck.add(key, num)) {
			size += 1;
		}
	}
	
	public int get(Object key) {
		int bucketIndex = hash(key);
		Bucket buck = table[bucketIndex];
		if (buck == null) {
			return NO_VALUE;
		} else {
			return buck.get(key);
		}
	}

	private void split(int split) {
		Bucket[] new_table;
		Bucket sp = table[split];

		if (sp.order == table.length) {
			new_table = new Bucket[table.length * 2];
			System.arraycopy(table, 0, new_table, 0, table.length);
			System.arraycopy(table, 0, new_table, table.length, table.length);
		} else {
			new_table = table;
		}

		for (int i = (split % sp.order); i < new_table.length; i += sp.order) {
			if (new_table[i] == sp) {
				new_table[i] = null;
			}
		}

		table = new_table;

		size -= sp.size;
		for (int i = 0; i < sp.size; i++) {
			add(sp.data1[i], sp.data2[i]);
		}
	}

	private int hash(Object key) {
		int val = System.identityHashCode(key);
		return Math.abs(val) % table.length;
	}

	public void forEach(ObjectIntIter iter) {
		for (int i = 0; i < table.length; i++) {
			Bucket b = table[i];
			if (b == null) continue;
			if (i < b.order) {
				b.forEach(iter);
			}
		}
	}

	public int size() {
		return size;
	}

	public boolean containsKey(Object key) {
		int bucketIndex = hash(key);
		Bucket buck = table[bucketIndex];
		if (buck == null) {
			return false;
		}
		return buck.contains(key);
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer("[");
		forEach(new ObjectIntIter() {
			boolean first = true;
			public void each(Object k, int i) {
				if (!first) { sb.append(", "); } else { first=false; }
				sb.append(classOf(k)).append('@').append(System.identityHashCode(k)).append('=').append(i);
			}
			private String classOf(Object k) {
				if (k==null) return "null";
				return k.getClass().getName();
			} 
		});
		sb.append("]");
		return sb.toString();
	}
}
