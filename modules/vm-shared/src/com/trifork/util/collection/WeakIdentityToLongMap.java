//
//   Copyright (c) 2000, 2001 Trifork Technologies
//   All Rights Reserved
//   No part of this program may be copied, used or delivered to
//   anyone without the express written consent of Trifork Technologies
//

package com.trifork.util.collection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;


/**
 * @author Kresten Krab Thorup (krab@trifork.com)
 */

public class WeakIdentityToLongMap implements ObjectToLongMap {
    
    public static final int NO_VALUE = Integer.MIN_VALUE;
    
    protected int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    protected static int keyHash(Object key) {
        if (key == null)
            return 0;
        else
            return System.identityHashCode(key);
    }

    protected static boolean keyEquals(Object key1, Object key2) {
        return key1 == key2;
    }

    static int valueHash(long value) {
        return (int) value;
    }

    static boolean valueEquals(long value1, long value2) {
        return value1 == value2;
    }

    static class Entry extends WeakReference implements ObjectToLongMap.Entry {
        public int hashCode() {
            return keyHash(getKey()) ^ valueHash(getValue());
        }

        public boolean equals(Object other) {
            if (other instanceof Entry) {
                Entry ent = (Entry) other;
                return keyEquals(getKey(), ent.getKey())
                        && valueEquals(getValue(), ent.getValue());
            } else {
                return false;
            }
        }

        Entry next;

        long value;

        private int hash;

        Entry(int hash, Object key, long value, Entry next, ReferenceQueue queue) {
            super(key, queue);
            this.value = value;
            this.next = next;
            this.hash = hash;
        }

        public Object getKey() {
            return super.get();
        }

        public long getValue() {
            return value;
        }

        public long setValue(long value2) {
            long result = this.value;
            this.value = value2;
            return result;
        }

        boolean sameKey(int hash, Object key) {
            return keyEquals(getKey(), key);
        }

        /**
         * @return
         */
        public int getHash() {
            return hash;
        }
    }

    /** the hash index */
    private Entry[] table;

    /** the current range for table. */
    private int range;

    private float ratio;

    private ReferenceQueue queue = new ReferenceQueue();

    /** translate hash code bucket to index */
    private int index(int hash) {
        return (hash & 0x7ffffff) % range;
    }

    /** the default and only constructor */
    public WeakIdentityToLongMap() {
        clear();
    }

    public void clear() {
        range = 17;
        size = 0;
        ratio = 0.75F;
        table = new Entry[range];
    }

    /** return the element with the given key */
    public long get(Object key) {
        if (key == null) { throw new NullPointerException(); }
        int hash = keyHash(key);
        return get(hash, key);
    }

    private long get(int hash, Object key) {
        int idx = index(hash);

        for (Entry ent = table[idx]; ent != null; ent = ent.next) {
            if (ent.sameKey(hash, key))
                return ent.value;
        }

        return NO_VALUE;
    }

    /** return the element with the given key */
    public boolean containsKey(Object key) {
        int hash = keyHash(key);
        return containsKey(hash, key);
    }

    private boolean containsKey(int hash, Object key) {
        int idx = index(hash);

        for (Entry ent = table[idx]; ent != null; ent = ent.next) {
            if (ent.sameKey(hash, key))
                return true;
        }

        return false;
    }

    public long put(Object key, long value) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = keyHash(key);
        return put(hash, key, value);
    }

    private long put(int hash, Object key, long value) {
        int idx = index(hash);

        for (Entry ent = table[idx]; ent != null; ent = ent.next) {
            if (ent.sameKey(hash, key)) {
                return ent.setValue(value);
            }
        }
        
        expunge();

        if (1.0F * size / range > ratio) {
            grow();
            idx = index(hash);
        }

        table[idx] = new Entry(hash, key, value, table[idx], queue);

        size += 1;

        return NO_VALUE;
    }

    /**
     * 
     */
    public void expunge() {
        Entry ent;
        
        while ((ent = (Entry) queue.poll()) != null) {
            removeEntry(ent);
        }
    }

    public long remove(Object key) {
        if (key == null) throw new NullPointerException();
        int hash = keyHash(key);
        return remove(hash, key);
    }

    private long remove(int hash, Object key) {
        int idx = index(hash);

        Entry entry = table[idx];
        if (entry != null) {

            if (entry.sameKey(hash, key)) {
                table[idx] = entry.next;
                size -= 1;
                return entry.getValue();

            } else {
                Entry ahead = entry.next;

                while (ahead != null) {
                    if (ahead.sameKey(hash, key)) {
                        entry.next = ahead.next;
                        size -= 1;
                        return ahead.getValue();
                    }

                    entry = ahead;
                    ahead = ahead.next;
                }
            }
        }

        // it was not found at all!
        return NO_VALUE;
    }

    public long removeEntry(Entry ent) {
        int hash = ent.getHash();
        int idx = index(hash);

        Entry entry = table[idx];
        if (entry != null) {

            if (entry == ent) {
                table[idx] = entry.next;
                size -= 1;
                return entry.getValue();

            } else {
                Entry ahead = entry.next;

                while (ahead != null) {
                    if (ahead == ent) {
                        entry.next = ahead.next;
                        size -= 1;
                        return ahead.getValue();
                    }

                    entry = ahead;
                    ahead = ahead.next;
                }
            }
        }

        // it was not found at all!
        return NO_VALUE;
    }

    private void grow() {
        int old_range = range;
        Entry[] old_table = table;

        range = old_range * 2 + 1;
        table = new Entry[range];

        for (int i = 0; i < old_range; i++) {
            Entry entry = old_table[i];

            while (entry != null) {
                Entry ahead = entry.next;
                int idx = index(keyHash(entry.getKey()));
                entry.next = table[idx];
                table[idx] = entry;
                entry = ahead;
            }
        }
    }

    final class EntryIterator implements ObjectToLongMap.EntryIterator {
        int idx;

        Entry entry;

        EntryIterator() {
            idx = 0;
            entry = table[0];
            locateNext();
        }

        private void locateNext() {
            // we reached the end of a list
            while (entry == null) {
                // goto next bucket
                idx += 1;
                if (idx == range) {
                    // we reached the end
                    return;
                }

                // entry is the first element of this bucket
                entry = table[idx];
            }
        }

        public boolean hasNext() {
            return (entry != null);
        }

        public ObjectToLongMap.Entry next() {
            Entry result = entry;

            if (result == null) {
                throw new NoSuchElementException();
            } else {
                entry = entry.next;
                locateNext();
                return result;
            }
        }

        public void remove() {
            Entry remove = entry;

            entry = entry.next;
            locateNext();

            WeakIdentityToLongMap.this.removeEntry(remove);
        }
    }

    public ObjectToLongMap.EntryIterator entryIterator() {
        return new EntryIterator();
    }

    /**
     * @param map
     */
    public void putAll(ObjectToLongMap map) {
        ObjectToLongMap.EntryIterator it = map.entryIterator();
        while (it.hasNext()) {
            ObjectToLongMap.Entry ent = it.next();
            Object o = ent.getKey();
            if (o != null) {
                put(o, ent.getValue());
            }
        }
    }

    public void forEach(EntryIter iter) {

        for (int tableIdx = 0; tableIdx < table.length; tableIdx++) {
            Entry entry = table[tableIdx];

            while (entry != null) {
            	iter.each(entry.getKey(), entry.getValue());
                entry = entry.next;  
            } 
        }
    	
    }
    
    public void toEntriesArray(ObjectToLongMap.Entry[] entries) {
        int entriesIdx = 0;

        for (int tableIdx = 0; tableIdx < table.length; tableIdx++) {
            Entry entry = table[tableIdx];

            while (entry != null) {
                entries[entriesIdx++] = entry;
                entry = entry.next;  
            } 
        }
    }


}