package DatabasesTwo;

import java.util.ArrayList;
import java.util.Hashtable;

import libs.BTree;


public class DBTable {
	public  String tableName;
	public  int pageCount;
	public  ArrayList<Page> pageList;
	public  String keyColName;
	public  ArrayList<String> colList;
	//TODO initialize colList in the constructor
	public  Hashtable<String, String>colNameType;
	public  Hashtable<String, String>colNameRef;
	public  Hashtable<String, BTree<String, DBRecord> > colNameBTree;

	public DBTable(String strTableName, Hashtable<String,String> htblColNameType, Hashtable<String,String>htblColNameRefs, String strKeyColName) {
		this.tableName = strTableName;
		this.keyColName = strKeyColName;
		this.colNameType = htblColNameType;
		this.colNameRef = htblColNameRefs;
		this.pageCount = 1;
		pageList = new ArrayList<Page>();
		colList = new ArrayList<String>();
		colNameBTree = new Hashtable<String, BTree<String,DBRecord>>();
		this.pageList.add(new Page(this.tableName));
	}
	
}
