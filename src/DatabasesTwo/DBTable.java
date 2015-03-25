package DatabasesTwo;

import java.util.ArrayList;
import java.util.Hashtable;


public class DBTable {
	private  String tableName;
	private  int pageCount;
	private  ArrayList<Page> pageList;
	private  String keyColName;
	private  Hashtable<String, String>colNameType;
	private  Hashtable<String, String>colNameRef;

	public DBTable(String strTableName, Hashtable<String,String> htblColNameType, Hashtable<String,String>htblColNameRefs, String strKeyColName) {
		this.tableName = strTableName;
		this.keyColName = strKeyColName;
		this.htblColNameType = htblColNameType;
		this.colNameRef = htblColNameRefs;
		this.pageCount = 1;
		this.pageList.add(new Page(this.tableName));
	}
	
}
