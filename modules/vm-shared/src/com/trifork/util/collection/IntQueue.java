package com.trifork.util.collection;


public class IntQueue implements IntCollection {

	int[] data = new int[256];
	int nextAdd, nextGet;
	int data_length = 256;
	
	public IntQueue() {
		super();
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer("{");
		
		for (int i = nextGet; i != nextAdd; i = (i+1)%data_length) {
			if (i != nextGet) { sb.append(", "); }
			sb.append(data[i]);
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
	public void addFirst(int val)
	{
		if (isFull()) grow();
		nextGet = ((nextGet-1) + data_length) % data_length;
		data[nextGet] = val;
	}
	
	public void add(int val) 
	{
		addLast(val);
	}
	
	public void addLast(int dest) {
		if (isFull()) grow();
		data[nextAdd] = dest;
		nextAdd = (nextAdd+1) % data_length;
	}

	private void grow() {
		int old_size = size();
		int first = data[nextGet];
		int[] new_data = new int[data_length * 3 / 2];
		if (nextGet <= nextAdd) {
			System.arraycopy(data, nextGet, new_data, 0, nextAdd-nextGet);
			nextAdd = nextAdd-nextGet;
			nextGet = 0;
		} else {
			System.arraycopy(data, nextGet, new_data, 0, data_length-nextGet);
			System.arraycopy(data, 0, new_data, data_length-nextGet, nextAdd);
			nextAdd = data_length+nextAdd-nextGet;
			nextGet = 0;
		}
		data = new_data;
		data_length = data.length;
		if (size() != old_size) {
			throw new IllegalStateException("internal error");
		}
		if (first != data[nextGet]) {
			throw new IllegalStateException("internal error");
		}
	}

	private boolean isFull() {
		return ((nextAdd+1) % data_length) == nextGet;
	}

	public int dequeue() {
		if (isEmpty()) { return 0; }
		int result = data[nextGet];
		nextGet = (nextGet+1) % data_length;
		return result;
	}

	private boolean isEmpty() {
		return nextGet==nextAdd;
	}

	public int[] toArray() {
		int[] result = new int[size()];
		
		if (nextGet <= nextAdd) {
			System.arraycopy(data, nextGet, result, 0, nextAdd-nextGet);
		} else {
			System.arraycopy(data, nextGet, result, 0, data_length-nextGet);
			System.arraycopy(data, 0, result, data_length-nextGet, nextAdd);
		}

		return result;
	}

    public void forEach(IntIter iter) {
        for (int i = nextGet; i != nextAdd; i = (i+1)%data_length)
        {
            iter.each(data[i]);
        }
    }

    public void forEach(IntExceptionIter iter) throws Exception {
        for (int i = nextGet; i != nextAdd; i = (i+1)%data_length)
        {
            iter.each(data[i]);
        }
    }

	public int size() {
		if (nextGet <= nextAdd) {
			return nextAdd-nextGet;
		} else {
			return nextAdd-nextGet + data_length;
		}
	}

	public int removeFirst() {
		return dequeue();
	}

	public IntIterator iterator() {
		return new IntIterator() {

			int pos = nextGet;
			
			public int next() {
				int result = data[pos];
				pos = (pos + 1) % data_length;
				return result;
			}

			public boolean hasNext() {
				return pos == nextAdd;
			}

			public int size() {
				return IntQueue.this.size();
			}
			
		};
	}

	public boolean contains(int value) {
		for (int i = nextGet; i != nextAdd; i = (i+1)%data_length)
		{
			if(data[i] == value) { return true; }
		}
		return false;
	}
}
