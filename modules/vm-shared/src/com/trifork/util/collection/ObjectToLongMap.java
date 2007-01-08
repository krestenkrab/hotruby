/*
 * Copyright (c) 2004 Trifork Technologies
 * All Rights Reserved
 * No part of this program may be copied, used or delivered to
 * anyone without the express written consent of Trifork Technologies 
 */
package com.trifork.util.collection;

/**
 * @author Kresten Krab Thorup (krab@trifork.com)
 */
public interface ObjectToLongMap {
    public interface EntryIter {
    	void each(Object key, long value);
	}

	long get(Object key);
    long remove(Object key);
    long put(Object key, long value);
    int size();
    
    interface Entry {
        Object getKey();
        long getValue();
    }
    
    interface EntryIterator {
        boolean hasNext();
        Entry next();
    }

    EntryIterator entryIterator();
    void toEntriesArray(Entry[] entries);
    
	void forEach(EntryIter iter);
}
