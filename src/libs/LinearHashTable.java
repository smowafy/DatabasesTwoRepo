/*
EduDB is made available under the OSI-approved MIT license.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package libs;

//import dataTypes.DataType;

import DatabasesTwo.DBRecord
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author  mohamed
 */
//public class LinearHashTable implements Map<DataType, DataType> {
public class LinearHashTable implements Map<TKey extends Comparable<TKey>, TValue> {

	/**
     * @uml.property  name="loadFactor"
     */
	private float loadFactor;
	/**
     * @uml.property  name="bucketSize"
     */
	private int bucketSize;
	/**
     * @uml.property  name="size"
     */
	private int size;
	/**
     * @uml.property  name="digits"
     */
	private int digits;
	/**
     * @uml.property  name="hashSeed"
     */
	private int hashSeed;
	/**
     * @uml.property  name="numberOfItems"
     */
	private int numberOfItems;
	/**
     * @uml.property  name="buckets"
     * @uml.associationEnd  multiplicity="(0 -1)" inverse="this$0:data_structures.linearHashTable.LinearHashTable$Bucket"
     */
	private ArrayList<Bucket> buckets;

	public LinearHashTable(float loadFactor, int bucketSize) {
		this.loadFactor = loadFactor;
		this.bucketSize = bucketSize;
        buckets = new ArrayList<>();
        init();
    }

	private void init() {
        size = 0;
		digits = 1;
		Bucket bucket = new Bucket(bucketSize);
		buckets.add(bucket);
		Random generator = new Random();
		hashSeed = generator.nextInt();
	}

	@Override
	public int size() {
		return numberOfItems;
	}

	@Override
	public boolean isEmpty() {
		return numberOfItems == 0;
	}

	@Override
	public boolean containsKey(TKey key) {
		return getEntry(key) != null;
	}

	private LHTEntry getEntry(TKey key) {
		//if (key instanceof DataType){
			//int b = getBucket((DataType)key);
			int b = getBucket(key);
			Bucket bucket = buckets.get(b);
			LHTEntry entry;
            entry = bucket.getEntry(key);
            return entry;
		//}
		return null;
	}

	@Override
	public boolean containsValue(TValue value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TKey get(TKey key) {
		LHTEntry entry = getEntry(key);
		return null == entry ? null : entry.getValue();
	}

	public int getBucket(TKey key){
		int hash = hash(key);
		int bits = hash & ((int)Math.pow(2, digits)-1);
		if(bits <= size){
			return bits;
		}else{
			bits = bits - (int)Math.pow(2, (digits-1));
			return bits;
		}
	}

	@Override
	public TKey put(TKey key, TValue value) {
		int b = getBucket(key);
		Bucket bucket = buckets.get(b);
		int hash = hash(key);
		bucket.put(key, value, hash);
		numberOfItems++;
		if(numberOfItems / ((size+1) * bucketSize) >= loadFactor){
			resize();
		}
		return null;
	}

	private void resize() {
		size++;
		Bucket b = new Bucket(bucketSize);
		buckets.add(b);
		if(size == (int)Math.pow(2, digits)){
			digits++;
		}
        int index = size - (int)Math.pow(2,digits-1);
        Bucket bucket = buckets.get(index);
        bucket.scan();
	}

    public void downSize(){

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
//final int hash(Object k) {
    final int hash(TKey k) {
		int h = hashSeed;
		h ^= k.hashCode();
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	@Override
	public TValue remove(TKey key) {
		int b = getBucket(key);
		Bucket bucket = buckets.get(b);
		LHTEntry entry = bucket.remove(key);
		numberOfItems--;
		return entry.value;
	}

	@Override
	//public void putAll(Map<? extends DataType, ? extends DataType> m) {
		// TODO Auto-generated method stub

	//}
	public void putAll(Map<TKey, TValue> m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<TKey> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TValue> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//public Set<DataType, DataType>> entrySet() {
	public Set<Entry<TKey, TValue>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * @author   mohamed
     */
	class Bucket {
		/**
         * @uml.property  name="entries"
         * @uml.associationEnd  multiplicity="(0 -1)"
         */
		LHTEntry[] entries;
		int lastItem;
		LinkedList<LHTEntry> overflow;

		public Bucket(int bucketSize) {
			entries = new LHTEntry[bucketSize];
			lastItem = 0;
		}
		
		public LHTEntry remove(TKey key) {
			LHTEntry r = null;
			for (int i = 0; i < lastItem; i++) {
				if(entries[i].getKey().equals(key)){
					r = entries[i];
					for (int j = i; j < lastItem-1; j++) {
						entries[j] = entries[j+1];
					}
					if(overflow != null){
						if(! overflow.isEmpty()){
							LHTEntry entry =overflow.removeFirst();
							entries[entries.length-1] = entry;
                            lastItem++;
						}
					}
					lastItem--;
					return r;
				}
			}
            if(overflow != null){
                Iterator<LHTEntry> itr = overflow.iterator();
                while(itr.hasNext()) {
                    LHTEntry element = itr.next();
                    if ((element.getKey()).equals(key)){
                        r = element;
                        itr.remove();
                        break;
                    }
                }
            }
			return r;
		}

		public LHTEntry getEntry(TKey key) {
			for (int i = 0; i < lastItem; i++) {
				//DataType dataKey = (DataType) key;
				TKey dataKey =  key;
				if(entries[i].getKey().equals(dataKey)){
					return entries[i];
				}
			}
			return null;
		}

		public void put(TKey key, TValue value, int hash) {
			if(lastItem == entries.length){
				overflow.add(new LHTEntry(key, value, hash));
			}else{
				entries[lastItem++] = new LHTEntry(key, value, hash);
				if(lastItem == entries.length){
					overflow = new LinkedList<>();
				}
			}
		}

        public void scan() {
            for (int i=0; i< lastItem; i++){
                int bits = entries[i].hash & ((int)Math.pow(2, digits)-1);
                if(bits > (int)Math.pow(2, digits-1)-1){
                    LHTEntry entry = entries[i];
                    remove(entries[i].key);
                    numberOfItems--;
                    LinearHashTable.this.put(entry.getKey(),entry.getValue());
                    i--;
                }
            }
            if (overflow != null){
                Iterator<LHTEntry> itr = overflow.iterator();
                while(itr.hasNext()) {
                    LHTEntry element;
                    element = itr.next();
                    int bits = element.hash & ((int)Math.pow(2, digits)-1);
                    if(bits > (int)Math.pow(2, digits-1)-1){
                        itr.remove();
                        numberOfItems--;
                        LinearHashTable.this.put(element.getKey(),element.getValue());
                    }
                }
            }
        }
    }
	
	/**
     * @author   mohamed
     */
	class LHTEntry implements Entry<TKey, TValue>{
		/**
         * @uml.property  name="key"
         * @uml.associationEnd  
         */
		private TKey key;
		/**
         * @uml.property  name="value"
         * @uml.associationEnd  
         */
		private TValue value;
        private int hash;

		public LHTEntry(TKey key, TValue value, int hash) {
			this.key = key;
			this.value = value;
            this.hash = hash;
		}

		/**
         * @return
         * @uml.property  name="value"
         */
		public TValue getValue(){
			return value;
		}

		/**
         * @return
         * @uml.property  name="key"
         */
		@Override
		public TKey getKey() {
			return key;
		}

		/**
         * @param value
         * @return
         * @uml.property  name="value"
         */
		@Override
		public TValue setValue(TValue value) {
			TValue old = this.value;
			this.value = value;
			return old;
		}
	}
	
}



