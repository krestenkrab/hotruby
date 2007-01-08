package com.trifork.util.collection;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;


public class IdentityToIntHashMap2  {

	public static final int NO_VALUE = IdentityToIntegerMap.NO_VALUE;
	static final int BUCKET_SIZE = 128;
	static final int TABLE_SIZE = 1024;

	static class Bucket {
		Object[] keys;
		int[] values;

		final int order;

		int size;

		public Bucket(int order) {
			this.order = order;
			this.keys = new Object[BUCKET_SIZE];
			this.values = new int[BUCKET_SIZE];
			
			for(int i = 0; i <BUCKET_SIZE; i++) {
				values[i] = NO_VALUE;
			}
		}

		public boolean isFull() {
			return size == BUCKET_SIZE;
		}

		public boolean put(Object key, int value) {
			for (int i = 0; i < size; i++) {
				if (keys[i] == key) { return false; }
			}
			
			keys[size] = key;
			values[size++] = value;
			
			return true;
		}

		public boolean contains(Object key) {
			for (int i = 0; i < size; i++) {
				if (key == keys[i]) {
					return true;
				}
			}
			return false;
		}

		public int get(Object key) {
			for (int i = 0; i < size; i++) {
				if (key == keys[i]) {
					return values[i];
				}
			}
			return NO_VALUE;
		}

		public void forEachValue(IntIter iter) {
			for (int i = 0; i < size; i++) {
				iter.each(values[i]);
			}
		}
	}

	Bucket[] table = new Bucket[TABLE_SIZE];

	int size = 0;

	public IdentityToIntHashMap2() {
	}


	public void put(Object key, int val) {

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
				if(buck == null) {
					buck = new Bucket(table.length);
				}
			}
		}

		if (buck.put(key, val)) {
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
			put(sp.keys[i], sp.values[i]);
		}
	}

	private int hash(Object key) {
		return Math.abs(System.identityHashCode(key)) % table.length;
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


	public int get(Object key) {
		int bucketIndex = hash(key);
		Bucket buck = table[bucketIndex];
		if (buck == null) {
			return NO_VALUE;
		}
		return buck.get(key);
	}


	public void dumpstat(PrintStream pw) {
		
		Set seen= new HashSet();
		
		pw.println("HashMap table.length="+table.length+"; size="+size());
		int[] sizes = new int[BUCKET_SIZE+1];
		int used_table_entries = 0;
		
		for (int i = 0; i < table.length; i++) {
			
			Bucket b =table[i];
			if (b == null) {
				continue;
			}
			
			used_table_entries += 1;
			
			if (seen.contains(b)) { continue; }
			
			seen.add(b);
			
			sizes[b.size] += 1;
		}
		
		
		pw.println("bucket count="+seen.size()+"; used_table_entries="+used_table_entries
				+" ("+(100.0*used_table_entries/table.length)+"%)");
		
		/*
		for (int i = 0; i <sizes.length; i++) {
			if (sizes[i] == 0) { continue; }
			
			pw.print("size="+i+" ");
			for (int k = 0; k <sizes[i]; k++) {pw.print('=');}
			pw.println();
			
			// pw.println("size="+i+" :"+sizes[i]+" ("+ (100.0*sizes[i]/seen.size()) +"%)");
		}
		*/
		pw.println("done.");
	}




	public void forEachValue(IntIter iter) {
		Set seen= new HashSet();


		for (int i = 0; i < table.length; i++) {
			
			Bucket b =table[i];
			if (b == null) {
				continue;
			}
						
			if (seen.contains(b)) { continue; }
			seen.add(b);
			
			b.forEachValue(iter);
		}
		
	}
}
