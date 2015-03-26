package DatabasesTwo;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable{

	public static final long serialVersionUID = 1L;
	public static int maxRecCount;
	public int recCount;
	public String tableName;
	public ArrayList<DBRecord> recordList;
	
	public Page(String tableName) {
		this.tableName = tableName;
		recCount = 0;
		recordList = new ArrayList<DBRecord>();
	}
		
}
