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
public interface IntegerToObjectMap {
    Object get(int key);
    Object remove(int key);
    Object put(int key, Object value);
    
    interface Entry {
        int getKey();
        Object getValue();
    }
    
    interface EntryIterator {
        boolean hasNext();
        Entry next();
    }

    EntryIterator entryIterator();
	IntIterator keyIterator();
	int size();
	boolean containsKey(int target);
}
