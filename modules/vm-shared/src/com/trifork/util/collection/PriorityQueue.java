//
//   Copyright (c) 2000, 2001 Trifork Technologies
//   All Rights Reserved
//   No part of this program may be copied, used or delivered to
//   anyone without the express written consent of Trifork Technologies
//

package com.trifork.util.collection;

import java.util.Comparator;

/**
 * 
 * @author Kresten Krab Thorup
 */
public class PriorityQueue {
    /** the default queue size is 4 */
    public static final int DEFAULT_QUEUE_SIZE = 4;

    Comparator cmp;

    int elem_count = 0;

    int elem_max;

    Object[] elem_data;

    /** Construct a new <code>PriorityQueue</code> (primary constructor) */
    public PriorityQueue(int size, Comparator cmp) {
        if (size < 4)
            elem_max = 4;
        else
            elem_max = size;

        elem_count = 0;
        elem_data = new Object[elem_max + 2];

        this.cmp = cmp;
    }

    public PriorityQueue(int size) {
        this(size, new ComparableComparator());
    }

    public PriorityQueue() {
        this(DEFAULT_QUEUE_SIZE, new ComparableComparator());
    }

    public PriorityQueue(Comparator cmp) {
        this(DEFAULT_QUEUE_SIZE, cmp);
    }

    public int size() {
        return elem_count;
    }

    public Object[] toArray(Object[] result) {
        if (result.length < size()) {
            Class elementClass = result.getClass().getComponentType();
            result = (Object[]) java.lang.reflect.Array.newInstance(elementClass, size());
        }

        int count = elem_count;
        for (int i = 0; i < count; i++) {
            result[i] = dequeue();
        }

        return result;
    }

    public void clear() {
        elem_count = 0;
    }

    public Object dequeue() {
        if (elem_count == 0)
            return null;

        Object result = elem_data[1];

        elem_data[1] = elem_data[elem_count];
        elem_count -= 1;
        sift_down(1);
        return result;
    }

    private void sift_down(int i) {
        final int top_idx = i;
        final int left_idx = i * 2;
        final int right_idx = left_idx + 1;

        if (elem_count < left_idx) {
            return;
        }

        // the left one is the last
        else if (elem_count == left_idx) {
            Object top = elem_data[top_idx];
            Object left = elem_data[left_idx];

            if (cmp.compare(left, top) < 0) {
                elem_data[top_idx] = left;
                elem_data[left_idx] = top;
            }

            return;
        }

        // there are more...
        else {
            Object top = elem_data[top_idx];
            Object left = elem_data[left_idx];
            Object right = elem_data[right_idx];

            if (cmp.compare(left, right) < 0) {
                // left is now the smaller

                if (cmp.compare(left, top) < 0) {
                    elem_data[top_idx] = left;
                    elem_data[left_idx] = top;

                    sift_down(left_idx);
                }
            }

            else if (cmp.compare(right, top) < 0) {
                // the right one is the smaller

                elem_data[top_idx] = right;
                elem_data[right_idx] = top;

                sift_down(right_idx);
            }
        }
    }

    public void enqueue(Object o) {

        // make room
        if (elem_max == elem_count) {
            int new_max = elem_max * 2;
            Object[] new_data = new Object[new_max + 2];
            System.arraycopy(elem_data, 0, new_data, 0, elem_max + 2);

            elem_max = new_max;
            elem_data = new_data;
        }

        elem_count += 1;
        elem_data[elem_count] = o;

        sift_up(elem_count);
    }

    private void sift_up(int idx) {
        if (idx == 1)
            return;

        Object me = elem_data[idx];
        Object top = elem_data[idx / 2];

        if (cmp.compare(me, top) < 0) {
            elem_data[idx] = top;
            elem_data[idx / 2] = me;

            sift_up(idx / 2);
        }
    }

    public void push(Object o) {
        enqueue(o);
    }

}

class ComparableComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        return ((Comparable) o1).compareTo(o2);
    }
}
