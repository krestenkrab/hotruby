package com.trifork.util.collection;


public class IntHashSet2 implements IntSet {

	static final int BUCKET_SIZE = 32;

	static class Bucket {
		int[] data;

		final int order;

		int size;

		public Bucket(int order) {
			this.order = order;
			this.data = new int[BUCKET_SIZE];
		}

		public boolean isFull() {
			return size == BUCKET_SIZE;
		}

		public boolean add(int val) {
			for (int i = 0; i < size; i++) {
				if (data[i] == val) { return false; }
			}
			
			data[size++] = val;
			return true;
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

		public boolean contains(int val) {
			for (int i = 0; i < size; i++) {
				if (val == data[i]) {
					return true;
				}
			}
			return false;
		}
	}

	Bucket[] table = new Bucket[4];

	int size = 0;

	public IntHashSet2() {
	}

	public IntHashSet2(IntCollection roots) {

		roots.forEach(new IntIter() {
			public void each(int i) {
				IntHashSet2.this.add(i);	
			}});
	}

	public IntHashSet2(int[] instances) {
		for (int i = 0; i < instances.length; i++) {
			add(instances[i]);
		}
	}

	public void add(int val) {

		int bucketIndex = hash(val);
		Bucket buck = table[bucketIndex];
		if (buck == null) {
			buck = new Bucket(table.length);
			table[bucketIndex] = buck;
		} else {
			while (buck.isFull()) {
				split(bucketIndex);
				bucketIndex = hash(val);
				buck = table[bucketIndex];
				if(buck == null) {
					buck = new Bucket(table.length);
				}
			}
		}

		if (buck.add(val)) {
			size += 1;
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
			add(sp.data[i]);
		}
	}

	private int hash(int val) {
		return Math.abs(val) % table.length;
	}

	public int[] toArray() {
		final int[] result = new int[size];
		forEach(new IntIter() {
			int idx = 0;

			public void each(int i) {
				result[idx++] = i;
			}
		});
		return result;
	}

    public void forEach(IntIter iter) {
        for (int i = 0; i < table.length; i++) {
            Bucket b = table[i];
            if (b == null) continue;
            if (i < b.order) {
                b.forEach(iter);
            }
        }
    }

    public void forEach(IntExceptionIter iter) throws Exception {
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

	public boolean contains(int val) {
		int bucketIndex = hash(val);
		Bucket buck = table[bucketIndex];
		if (buck == null) {
			return false;
		}
		return buck.contains(val);
	}

	public void addAll(IntCollection other) {
		other.forEach(new IntIter() {
			public void each(int i) {
				IntHashSet2.this.add(i);
			}
		});
	}

	public IntIterator iterator() {
		return new IntIterator() {

			int buck = findFirst();

			int index = 0;

			private int findFirst() {
				int i;
				for (i = 0; i < table.length; i++) {
					if (table[i] != null && table[i].size > 0) {
						return i;
					}
				}
				return table.length;
			}

			private void findNext() {

				index += 1;

				if (index >= table[buck].size) {

					index = 0;

					do {
						buck += 1;
					} while (buck < table.length
							&& (table[buck] == null || table[buck].size == 0 || buck > table[buck].order));

					return;
				}
			}

			public int next() {
				int result = table[buck].data[index];
				findNext();
				return result;
			}

			public boolean hasNext() {
				return buck < table.length;
			}

			public int size() {
				return IntHashSet2.this.size;
			}

		};
	}
	
	public String toString() {
		final StringBuffer sb = new StringBuffer("[");
		forEach(new IntIter() {
			boolean first = true;
			public void each(int i) {
				if (!first) { sb.append(", "); } else { first=false; }
				sb.append(i);
			} 
		});
		sb.append("]");
		return sb.toString();
	}
}
