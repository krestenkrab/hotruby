//
//   Copyright (c) 2000, 2001 Trifork Technologies
//   All Rights Reserved
//   No part of this program may be copied, used or delivered to
//   anyone without the express written consent of Trifork Technologies
//

package com.trifork.util.collection;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class <code>DynamicHashMap</code> is a generic dynamic hash
 * map.  
 *
 * @author <a href="mailto:krab@trifork.com">Kresten Krab Thorup</a>
 * @version 2.0
 * @since 1.0
 */
public class DynamicHashMap extends GenericMap implements Map
{
    class Entry extends GenericMap.Entry
    {
	Entry next;
        int hash;
	Object key;
	Object value;

	Entry (int hash, Object key, Object value, Entry next)
	{
            this.hash = hash;
	    this.key = key;
	    this.value = value;
	    this.next = next;
	}

	public Object getKey () { return key; }
	
	public Object getValue() { return value; }

	public Object setValue (Object value)
	{
	    Object result = this.value;
	    this.value = value;
	    return result;
	}

        boolean sameKey (int hash, Object key)
        {
            return this.hash==hash && keyEquals (this.key, key);
        }
    }

    /** the hash index */ 
    private Entry[] table;
    
    /** the current range for table. */
    private int range;

    private float ratio;

    /** translate hash code bucket to index */
    private int index (int hash)
    {
        return (hash & 0x7ffffff) % range;
    }

    /** the default and only constructor */
    public DynamicHashMap ()
    {
	clear ();
    }

    public void clear ()
    {
	range = 3;
	size = 0;
        ratio = 0.75F;
	table = new Entry[range];
    }

    /** return the element with the given key */
    public Object get (Object key)
    {
	int hash = keyHash (key);
        return get (hash, key);
    }

    public Object get (int hash, Object key)
    {
	int idx  = index(hash);

	for (Entry ent = table[idx]; ent != null; ent=ent.next )
	    {
		if (ent.sameKey (hash, key))
		    return ent.value;
	    }

	return null;
    }

    /** return the element with the given key */
    public boolean containsKey (Object key)
    {
	int hash = keyHash (key);
        return containsKey (hash, key);
    }

    public boolean containsKey (int hash, Object key)
    {
	int idx  = index(hash);

	for (Entry ent = table[idx]; ent != null; ent=ent.next )
	    {
		if (ent.sameKey (hash, key))
		    return true;
	    }

	return false;
    }

    public Object put (Object key, Object value)
    {
	int hash = keyHash (key);
        return put (hash, key, value);
    }

    public Object put (int hash, Object key, Object value)
    {
	int idx  = index(hash);

	for (Entry ent = table[idx]; ent != null; ent=ent.next )
	    {
		if (ent.sameKey (hash, key))
		    {
			return ent.setValue (value);
		    }
	    }
	
	if (1.0F*size/range > ratio)
	    {
                grow ();
		idx  = index(hash);
	    }

	table[idx] = new Entry (hash, key, value, table[idx]);

	size += 1;

	return null;
    }

    public Object remove (Object key)
    {
	int hash = keyHash (key);
        return remove (hash, key);
    }

    public Object remove (int hash, Object key)
    {
	int idx  = index(hash);

        Entry entry = table[idx];
        if (entry != null) {

            if (entry.sameKey (hash, key)) {
                table[idx] = entry.next;
                size -= 1;
                return entry.getValue ();

            } else {
                Entry ahead = entry.next;

                while (ahead != null) {
                    if (ahead.sameKey (hash, key))
                        {
                            entry.next = ahead.next;
                            size -= 1;
                            return ahead.getValue ();
                        }
                    
                    entry = ahead;
                    ahead = ahead.next;
                }
            }
        }
        
	// it was not found at all!
	return null;
    }

    private void grow ()
    { 
        int old_range = range;
        Entry[] old_table = table;

        range = old_range*2+1;
        table = new Entry[range];

        for (int i = 0; i < old_range; i++) {
            Entry entry = old_table[i];

            while (entry != null) {
                Entry ahead = entry.next;
                int idx = index(entry.hash);
                entry.next = table[idx];
                table[idx] = entry;
                entry = ahead;
            }
        }
    }


    final class EntryIterator implements Iterator
    {
        int   idx;
	Entry entry;

	EntryIterator ()
	{
            idx = 0;
            entry = table[0];
            locateNext ();
	}

	private void locateNext ()
	{
	    // we reached the end of a list
	    while (entry == null)
		{
		    // goto next bucket
		    idx += 1;
		    if (idx == range)
			{
			    // we reached the end
			    return;
			}

		    // entry is the first element of this bucket 
		    entry = table[idx];
		}
	}
	
	public boolean hasNext ()
	{
	    return (entry != null);
	}

	public Object next ()
	{
	    Object result = entry;

	    if (result == null)
		{
		    throw new NoSuchElementException ();
		}
	    else
		{
		    entry = entry.next;
		    locateNext ();
		    return result;
		}
	}

	public void remove ()
	{
	    Entry remove = entry;

	    entry = entry.next;
	    locateNext ();

	    DynamicHashMap.this.remove (remove.key);
	}
    }

    protected Iterator entryIterator () { return new EntryIterator (); }

}
