package DatabasesTwo;
import java.util.ArrayList;
public class DBApp {
	ArrayList<DBTable> tables = new ArrayList<DBTable>();
	public void init() {

	}
	public void createTable(String strTableName, Hashtable<String,String> htblColNameType, Hashtable<String,String>htblColNameRefs, String strKeyColName) throws DBAppException {
		DBTable table = new DBTable(strTableName, htblColNameType, htblColNameRefs, strKeyColName);
		tables.add(table);
	}
	public void createIndex(String strTableName, String strColName) throws DBAppException {
		
	}
	public void insertIntoTable(String strTableName, Hashtable<String,String> htblColNameValue) throws DBAppException {

	}
	public void deleteFromTable(String strTableName, Hashtable<String,String> htblColNameValue, String strOperator) throws DBEngineException {

	}
	public Iterator selectValueFromTable(String strTable, Hashtable<String,String> htblColNameValue, String strOperator) throws DBEngineException {

	}
	public Iterator selectRangeFromTable(String strTable, Hashtable<String,String> htblColNameRange, String strOperator) throws DBEngineException {

	}
	public void saveAll() throws DBEngineException {
		
	}
}
