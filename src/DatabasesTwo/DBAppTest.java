package DatabasesTwo;

import java.util.Hashtable;
import java.util.Iterator;

import libs.DBAppException;
import libs.DBEngineException;

public class DBAppTest {
	public static void main(String[] args) throws DBAppException, DBEngineException {
		DBApp app = new DBApp();
		Hashtable<String, String> nameType, nameRefs;
		nameType = new Hashtable<String, String>();
		nameRefs = new Hashtable<String, String>();
		nameType.put("name", "string");
		nameType.put("address", "string");
		nameRefs.put("name", "null");
		nameRefs.put("address", "null");
		app.createTable("Person", nameType, nameRefs, "name");
		Hashtable<String, String> insertion = new Hashtable<String, String>();
		insertion.put("name", "sherif");
		insertion.put("address", "rehab city");
		app.insertIntoTable("Person", insertion);
		insertion = new Hashtable<String, String>();
		insertion.put("name", "wanis");
		insertion.put("address", "rehab city");
		app.insertIntoTable("Person", insertion);
		insertion = new Hashtable<String, String>();
		insertion.put("name", "aya");
		insertion.put("address", "tagamoe");
		app.insertIntoTable("Person", insertion);
		app.createIndex("Person", "address");
		Hashtable<String, String> nameRange = new Hashtable<String, String>();
		nameRange.put("name", "a,t");
		nameRange.put("address", "q,s");
		Iterator it = app.selectRangeFromTable("Person", nameRange, "AND");
		if (!it.hasNext()) System.err.println("Nothing");
		while (it.hasNext()) {
			System.out.println((DBRecord)it.next());
		}
	}
}
