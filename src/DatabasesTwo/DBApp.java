package DatabasesTwo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

public class DBApp {
	final String COMMA = ",";
	ArrayList<DBTable> tables = new ArrayList<DBTable>();

	public void init() {

	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException {
		DBTable table = new DBTable(strTableName, htblColNameType,
				htblColNameRefs, strKeyColName);
		tables.add(table);
		writeIntoCSV(strTableName, htblColNameType, htblColNameRefs,
				strKeyColName);
		createIndex(strTableName, strKeyColName);
	}

	public void createIndex(String strTableName, String strColName)
			throws DBAppException {
		// edit metadata file
		BufferedReader reader = null;
		FileWriter temp = null;
		String oldFileName = "metadata.csv";
		String tempFileName = "temp.csv";
		try {
			reader = new BufferedReader(new FileReader(oldFileName));
			temp = new FileWriter(tempFileName);
			String currentLine = reader.readLine();
			StringTokenizer tokenizer = new StringTokenizer(currentLine, COMMA);
			String tableName = tokenizer.nextToken();
			String colName = tokenizer.nextToken();
			while (!tableName.equals(strTableName)
					|| !colName.equals(strColName)) {
				temp.append(currentLine);
				currentLine = reader.readLine();
				tokenizer = new StringTokenizer(currentLine, COMMA);
				tableName = tokenizer.nextToken();
				colName = tokenizer.nextToken();
			}
			String colType = tokenizer.nextToken();
			String isKey = tokenizer.nextToken();
			String indexed = tokenizer.nextToken();
			if (indexed.equals("True")) {
				// There exists an index for this column
				(new File(tempFileName)).delete();
				return;
			}
			String ref = tokenizer.nextToken();
			temp.append(tableName + COMMA + colName + COMMA + colType + COMMA
					+ isKey + COMMA + "True" + COMMA + ref + '\n');
			while ((currentLine = reader.readLine()) != null
					&& !currentLine.equals(""))
				temp.append(currentLine);
			File oldFile = new File(oldFileName);
			(new File(oldFileName)).delete();
			File newFile = new File(tempFileName);
			newFile.renameTo(oldFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws DBAppException {

	}

	public void deleteFromTable(String strTableName,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {

	}

	public Iterator selectValueFromTable(String strTable,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {
		return null;

	}

	public Iterator selectRangeFromTable(String strTable,
			Hashtable<String, String> htblColNameRange, String strOperator)
			throws DBEngineException {
		return null;

	}

	public void saveAll() throws DBEngineException {

	}

	private void writeIntoCSV(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName) {

		FileWriter writer = null;
		ArrayList<String> colNames = (ArrayList<String>) htblColNameType.keys();

		try {
			writer = new FileWriter("metadata.csv");
			for (String colName : colNames) {
				writer.append(strTableName + COMMA);
				writer.append(colName + COMMA);
				writer.append(htblColNameType.get(colName) + COMMA);
				if (colName.equals(strKeyColName))
					writer.append("True" + COMMA + "True" + COMMA);
				else
					writer.append("False" + COMMA + "False" + COMMA);
				writer.append(htblColNameRefs.get(colName) + '\n');
			}

		} catch (Exception e) {
			System.out
					.println("Error in writing CSV file for: " + strTableName);
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
