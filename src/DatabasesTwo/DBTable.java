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
	public Hashtable<String, LinearHashtable<String, DBRecord>> colNameHash;

	public DBTable(String strTableName, Hashtable<String,String> htblColNameType, Hashtable<String,String>htblColNameRefs, String strKeyColName) {
		this.tableName = strTableName;
		this.keyColName = strKeyColName;
		this.colNameType = htblColNameType;
		this.colNameRef = htblColNameRefs;
		this.pageCount = 1;
		this.pageList.add(new Page(this.tableName));
	}
	
}
