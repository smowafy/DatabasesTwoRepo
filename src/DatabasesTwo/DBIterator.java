package DatabasesTwo;

import java.util.ListIterator;

import libs.BTreeLeafNode;

public class DBIterator implements ListIterator{
	public String start;
	public String end;
	public BTreeLeafNode<String, DBRecord> cur;
	public int idx;
	
	public DBIterator(String start, String end, BTreeLeafNode<String, DBRecord> startNode) {
		this.start = start;
		this.end = end;
		this.cur = startNode;
		idx = cur.search(start);
	}
	
	@Override
	public void add(Object e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNext() {
		return cur == null;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object next() {
		DBRecord result = null;
		BTreeLeafNode<String, DBRecord> tmp = cur;
		if (tmp != null) result = tmp.getValue(idx);
		if (idx == cur.getKeyCount()-1) {
			BTreeLeafNode<String, DBRecord> nxt = (BTreeLeafNode<String, DBRecord>) cur.getNextNodeforDB();
			if (nxt.getKey(0).compareTo(end) <= 0) {
				cur = nxt;
				idx = 0;
				
			} else {
				cur = null;
			}
		}
		else {
			if (cur.getKey(idx+1).compareTo(end) <= 0) {
				idx++;
			} else {
				cur = null;
			}
		}
		return result;
	}

	@Override
	public int nextIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object previous() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int previousIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(Object e) {
		// TODO Auto-generated method stub
		
	}

}
