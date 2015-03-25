package DatabasesTwo;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable{

	private static final long serialVersionUID = 1L;
	private static int maxRecCount;
	private String tableName;
	private ArrayList<DBRecord> recordList;
	
	public Page(String tableName) {
		this.tableName = tableName;
	}
		
}
