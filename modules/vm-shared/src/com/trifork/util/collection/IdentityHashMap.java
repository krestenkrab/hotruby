//
//   Copyright (c) 2000, 2001 Trifork Technologies
//   All Rights Reserved
//   No part of this program may be copied, used or delivered to
//   anyone without the express written consent of Trifork Technologies
//

package com.trifork.util.collection;



/**
 * Class <code>IdentityHashMap</code> is a map that compares keys
 * using ==, and uses System.identityHashCode for computing hashes.
 *
 * @author <a href="mailto:krab@trifork.com">Kresten Krab Thorup</a>
 * @version 1.0
 * @since 1.0
 */
public final class IdentityHashMap extends DynamicHashMap 
{
    protected int keyHash (Object key)
    {
	if (key == null)
	    return 0;
	else
	    return System.identityHashCode (key);
    }

    protected boolean keyEquals (Object key1, Object key2)
    {
	return key1 == key2;
    }
    
}
