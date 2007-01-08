package com.trifork.util.collection;

import java.io.PrintStream;



/**
 * An "open addressing" identity-to-int HashMap that keeps its representation in
 * a single int[] holding pairs of (System.identityHashCode(key), value)
 * 
 * @author Jeppe Sommer (jso@trifork.com)
 */
public class IdentityToIntHashMap3  {

	public static final int NO_VALUE = IdentityToIntegerMap.NO_VALUE;
	static final int TABLE_SIZE = 1024;
    static final double MAX_LOAD_FACTOR = 0.8;
    static final double GROWTH_RATE= 0.4;

    Object[] keys;
    int[] values;
    int size;
    int capacity;
    
    public IdentityToIntHashMap3(int initialCapacity) {
        this.capacity = initialCapacity;
        
        keys = new Object[initialCapacity];
        values = new int[initialCapacity];
    }


    public void put(Object key, int val) {
        int index;

        if (size > MAX_LOAD_FACTOR * capacity) {
            grow();
        }
        
        for (index = hash(key); keys[index] != null; index = probe(key, index));
        
        keys[index] = key;
        values[index] = val;
        
        size++;
    }

    private void grow() {
        int newCapacity = (int) (capacity + capacity * GROWTH_RATE);
        if (newCapacity <= capacity) {
            newCapacity += 1;
        }
        
        System.out.println("Growing, old capacity: " + capacity + " new capacity: " + newCapacity);
        
        try {   
            // Create a new map and "steal" its representation
            final IdentityToIntHashMap3 newMap = new IdentityToIntHashMap3(newCapacity);

            try {
                forEachValue(new ObjectAndIntExceptionIter() {

                    public void each(Object o, int i) {
                        newMap.put(o, i);
                    }

                });

                keys = newMap.keys;
                values = newMap.values;
                capacity = newMap.capacity;
                size = newMap.size;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (OutOfMemoryError er) {
            System.err.println("Allocation failure trying to grow IdentityToIntHashMap");
        }

    }


    private int probe(Object key, int index) {
        // Linear probing
        int result = index + 1;
        if (result == capacity) {
            result = 0;
        }
        
        return result;
    }


    private int hash(Object key) {
        return Math.abs(System.identityHashCode(key)) % capacity;
    }

//    private int hash(int identityHashCode) {
//        return Math.abs(identityHashCode) % tableLength;
//    }

    public int size() {
        return size;
    }

    public boolean containsKey(Object key) {
        return get(key) != NO_VALUE;
    }


    public int get(Object key) {
        int index;

        for (index = hash(key); keys[index] != key && keys[index] != null; index = probe(key, index));

        if (keys[index] != null) {
            return values[index];
        }
        
        return NO_VALUE;
    }

//	public void dumpstat(PrintStream pw) {
//		
//		Set seen= new HashSet();
//		
//		pw.println("HashMap table.length="+table.length+"; size="+size());
//		int[] sizes = new int[BUCKET_SIZE+1];
//		int used_table_entries = 0;
//		
//		for (int i = 0; i < table.length; i++) {
//			
//			Bucket b =table[i];
//			if (b == null) {
//				continue;
//			}
//			
//			used_table_entries += 1;
//			
//			if (seen.contains(b)) { continue; }
//			
//			seen.add(b);
//			
//			sizes[b.size] += 1;
//		}
//		
//		
//		pw.println("bucket count="+seen.size()+"; used_table_entries="+used_table_entries
//				+" ("+(100.0*used_table_entries/table.length)+"%)");
//		
//		/*
//		for (int i = 0; i <sizes.length; i++) {
//			if (sizes[i] == 0) { continue; }
//			
//			pw.print("size="+i+" ");
//			for (int k = 0; k <sizes[i]; k++) {pw.print('=');}
//			pw.println();
//			
//			// pw.println("size="+i+" :"+sizes[i]+" ("+ (100.0*sizes[i]/seen.size()) +"%)");
//		}
//		*/
//		pw.println("done.");
//	}
//



    public void forEachValue(IntIter iter){
        for (int i = 0; i < capacity; i++) {
            Object key = keys[i];
            if (key != null) {
                iter.each(values[i]);
            }
        }
    }

    public void forEachValue(IntExceptionIter iter) throws Exception {
        for (int i = 0; i < capacity; i++) {
            Object key = keys[i];
            if (key != null) {
                iter.each(values[i]);
            }
        }
    }

    public void forEachValue(ObjectAndIntExceptionIter iter) throws Exception {
        for (int i = 0; i < capacity; i++) {
            Object key = keys[i];
            if (key != null) {
                iter.each(keys[i], values[i]);
            }
        }
    }


	static public void main(String[] args) throws Exception {
        IdentityToIntHashMap3 map = new IdentityToIntHashMap3(2);

        map.put("hans", 1);
        map.put("peter", 2);
        map.put("frans", 3);
        map.put("simon", 4);
        map.put("uffe", 5);
        map.put("jesper", 6);
        map.put("jeppe", 7);
        map.put("christian", 8);
        map.put("torben", 9);
        map.put("john", 10);
        map.put("dennis", 11);
        map.put("brian", 12);
        map.put("thøger", 13);

        map.forEachValue(new IntIter() {

            public void each(int i) {
                System.out.print(" " + i);
            }
            
        });
        
        System.out.println();
        
        System.out.println("hans: " + map.get("hans"));
        System.out.println("peter: " + map.get("peter"));
        System.out.println("frans: " + map.get("frans"));
        System.out.println("simon: " + map.get("simon"));
        System.out.println("uffe: " + map.get("uffe"));
        System.out.println("jesper: " + map.get("jesper"));
        System.out.println("jeppe: " + map.get("jeppe"));
        System.out.println("christian: " + map.get("christian"));
        System.out.println("torben: " + map.get("torben"));
        System.out.println("john: " + map.get("john"));
        System.out.println("dennis: " + map.get("dennis"));
        System.out.println("brian: " + map.get("brian"));
        System.out.println("thøger: " + map.get("thøger"));
    }


    public void dumpstat(PrintStream err) {
        // TODO Auto-generated method stub
        
    }


    public void clear() {
        System.out.println("Clearing IdentityToIntHashMap3, size was " + size);

        size = 0;
        for (int i = 0; i < capacity; i++) {
            keys[i] = null;
        }
    }



}
