/*
 * Copyright (c) 2004 Trifork Technologies
 * All Rights Reserved
 * No part of this program may be copied, used or delivered to
 * anyone without the express written consent of Trifork Technologies 
 */
package com.trifork.hotruby.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import com.trifork.hotruby.util.collection.IdentityHashMap;
import com.trifork.hotruby.util.collection.ObjectToLongMap;
import com.trifork.hotruby.util.collection.PriorityQueue;
import com.trifork.hotruby.util.collection.WeakIdentityToLongMap;

/**
 * @author Kresten Krab Thorup (krab@trifork.com)
 */
final public class L4RootManager {

    private static final int ROOT_POOL_SIZE = 1000;

    PriorityQueue cachedRootsQueue;

    ArrayList rootPool;
    
    ObjectToLongMap.Entry[] entries = new ObjectToLongMap.Entry[ROOT_POOL_SIZE];

    static ThreadGroup main_thread_group;
	
	static {
		
		try {
			main_thread_group = (ThreadGroup) AccessController.doPrivileged(new GetMainThreadGroupAction());
		} catch (SecurityException ex) {
			// ignore 
		}
	}
	
    private static final class GetMainThreadGroupAction implements PrivilegedAction {
		public Object run() {
			ThreadGroup g = Thread.currentThread().getThreadGroup();
			while (g.getParent() != null) { g = g.getParent(); }
			return g;
		}
	}

	/**
     * @author Kresten Krab Thorup (krab@trifork.com)
     */
    static class Root implements Comparable {

        private Object object;

        private long allocTime;

        /**
         * @param key
         * @param value
         */
        public Root(Object object, long allocTime2) {
            this.object = object;
            this.allocTime = allocTime2;
        }

        public Object getObject() {
            return object;
        }

        public int compareTo(Object o) {
            long other = ((Root)o).allocTime;
            
            if (allocTime < other) return -1;
            else if (allocTime == other) return 0;
            else return 1;
        }
    }

    private WeakIdentityToLongMap unowned_roots = new WeakIdentityToLongMap();

    private ReferenceQueue thread_queue = new ReferenceQueue();

    private IdentityHashMap all_root_maps = new IdentityHashMap();
    private ObjectToLongMap[] per_thread_map;
    
    public L4RootManager() {
        all_root_maps.put(unowned_roots, Boolean.TRUE);
        updatePerThreadMap();
        
        if (Boolean.getBoolean("com.trifork.l4.preallocate_heapdumpctx")) {
            cachedRootsQueue = new PriorityQueue(100);
        }
        
        rootPool = new ArrayList(ROOT_POOL_SIZE);
        for (int i = 0; i < ROOT_POOL_SIZE; i++) {
            rootPool.add(new Root(null, 0));
        }
    }

    private class ThreadLocalRoots extends PhantomReference {
        private final WeakIdentityToLongMap threadRoots;

        public ThreadLocalRoots() {
            super(Thread.currentThread(), thread_queue);
            this.threadRoots = new WeakIdentityToLongMap();
            synchronized (all_root_maps) {
                all_root_maps.put(threadRoots, Boolean.TRUE);
                updatePerThreadMap();
            }
        }

        public void add(Object o) {

            // this synch will only congest while
            // reconstructing the rootset.
            synchronized (threadRoots) {
                threadRoots.put(o, System.currentTimeMillis());
            }
        }

        /**
         *  
         */
        void owningThreadDied() {
            synchronized (all_root_maps) {
                all_root_maps.remove(this.threadRoots);
                this.threadRoots.expunge();
                unowned_roots.putAll(this.threadRoots);
                updatePerThreadMap();
            }
        }
    }

    private ThreadLocal roots = new ThreadLocal() {
        protected Object initialValue() {
            return new ThreadLocalRoots();
        }
    };

    public void registerRoot(Object o) {
        
        ThreadLocalRoots threadRoots = (ThreadLocalRoots) roots.get();
        threadRoots.add(o);
        
        //System.out.println("REGISTER "+o.getClass()+"@"+System.identityHashCode(o));
        
        expunge();
    }

    private void expunge() {
        Reference ref;

        // this is a congestion point
        while ((ref = thread_queue.poll()) != null) {
            ((ThreadLocalRoots) ref).owningThreadDied();
        }
    }

    public Object[] getRoots() {
        final PriorityQueue rootsQueue;
        if (cachedRootsQueue != null) {
            rootsQueue = cachedRootsQueue;
        } else { 
            rootsQueue = new PriorityQueue();
        }
        //ObjectToLongMap[] per_thread_map;

        // clean out roots
        expunge();

//        synchronized (all_root_maps) {
//            updatePerThreadMap();
//        }

        for (int i = 0; i < per_thread_map.length; i++) {
            ObjectToLongMap object_map = per_thread_map[i];
			synchronized (object_map) {
            	
            	int size = object_map.size();
            	
				if (entries.length < size) {
					entries = new ObjectToLongMap.Entry[size];
            	}
            	
				
				object_map.forEach(new ObjectToLongMap.EntryIter() { 
					public void each(Object the_root, long allocTime) {
	                    if (the_root != null) {
	                        rootsQueue.enqueue(new_Root(the_root, allocTime));
	                    }
					}
				});
            	
/*                object_map.toEntriesArray(entries);
                
                for (int e = 0; e < entries.length; e++) {
                    if (entries[e] == null) {
                        break;
                    }
                    
                    Object the_root = entries[e].getKey();
                    long allocTime = entries[e].getValue();

                    if (the_root != null) {
                        rootsQueue.enqueue(new_Root(the_root, allocTime));
                    }
                }
*/                
//                ObjectToLongMap.EntryIterator it = per_thread_map[i].entryIterator();
//                while (it.hasNext()) {
//                    ObjectToLongMap.Entry ent = it.next();
//                    Object the_root = ent.getKey();
//                    long allocTime = ent.getValue();
//
//                    if (the_root != null) {
//                        rootsQueue.enqueue(new Root(the_root, allocTime));
//                    }
//                }
            }
        }

        for (int i = 0; i < entries.length; i++) {
        	entries[i] = null;
        }
        
       // if (main_thread_group != null) {
       // 	rootsQueue.enqueue(new_Root(main_thread_group, Integer.MAX_VALUE));
        //}

        Object[] result = new Object[rootsQueue.size()];
        for (int i = 0; i < result.length; i++) {
            Root root = (Root) rootsQueue.dequeue();
            result[i] = root.getObject();
            free_Root(root);
        }

        if (cachedRootsQueue != null) {
            cachedRootsQueue.clear();
        }
        
        return result;
    }


    private Object new_Root(Object object, long allocTime) {
        int free = rootPool.size();
        if (free > 0) {
            Root root = (Root) rootPool.remove(free - 1);
            root.allocTime = allocTime;
            root.object = object;
        
            return root;
        }

        return new Root(object, allocTime);
    }

    private void free_Root(Root root) {
        rootPool.add(root);
    }

    private void updatePerThreadMap() {
        per_thread_map = (ObjectToLongMap[]) all_root_maps.keySet().toArray(
                new ObjectToLongMap[all_root_maps.size()]);
    }
}