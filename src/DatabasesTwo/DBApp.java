package DatabasesTwo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.HashSet;

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

			//end of writing to metadata.CSV file
			//beginning of creating the index and adding it to the table
			BTree<String, DBRecord> idxBTree = new BTree();
			DBTable targetTable;
			//TODO add linear hashtable

			for(DBTable tmpTable : tables) {
				if (tmpTable.tableName.equals(strTableName)) {
					targetTable = tmpTable;
					tmpTable.colNameBTree.put(strColName, idxBtree);
					//TODO add linear hashtable
					break;
				}
			}

			//start inserting values from the table pages into the B+ Tree
			for(Page tmpPage : targetTable.pageList) {
				for(int i = 0; i < tmpPage.recCount; i++) {
					DBRecord tmpRecord = tmpPage.recordList.get(i);
					idxBtree.insert(tmpRecord.recValue.get(strColName), tmpRecord);
				}
			}
			//End of inserting into B+ Tree

			//TODO add same values to linear hashtable


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws DBAppException {
		
		//Begin inserting new record into the table
		DBTable targetTable;
		for(targetTable : tables) {
			if (targetTable.tableName.equals(strTableName)) break;
		}
		DBRecord toBeAddedRecord = new DBRecord(htblColNameValue);
		Page lastPage = targetTable.pageList.get(targetTable.pageList.size() - 1);
		if (lastPage.recCount < lastPage.maxRecCount) {
			lastPage.recCount++;
		} else {
			Page newPage = new Page(strTableName);
			newPage.recCount++;
			newPage.recordList.add(toBeAddedRecord);
			targetTable.pageCount++;
			targetTable.pageList.add(newPage);
		}
		//End inserting new record into the table
		//Begin inserting new entry into the column indices
		Set<String> keys = htblColNameValue.keySet();
		BTree tmpIdx;
		for(String key : keys) {
			tmpIdx = targetTable.colNameBTree.get(key);
			if (tmpIdx != null) {
				tmpIdx.insert(htblColNameValue.get(key), toBeAddedRecord);
			}
		}
		//End inserting new entry into the column indices
		//TODO inserting new entry into Linear Hashtable
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
		//fetching table reference
		DBTable targetTable;		
		for(targetTable : tables) {
			if (targetTable.tableName.equals(strTable)) break;
		}

		//Begin collecting each query result separately before using the operator
		ArrayList<HashSet<DBRecord> > queryList = new ArrayList<HashSet>();
		Set<String> keys = htblColNameRange.keySet();
		StringTokenizer strTok;
		for(String colName:keys) {
			String queryRange = htblColNameRange.get(colName);
			strTok = new StringTokenizer(queryRange, COMMA);
			String rangeStart = strTok.nextToken();
			String rangeEnd = strTok.nextToken();
			BTree tmpIdx = targetTable.colNameBTree.get(colName);
			DBRecord queryResultRecord;
			if (tmpIdx != null) {
				queryResultRecord = tmpIdx.search(rangeStart);
			} else {
				queryResultRecord = linearSearch(targetTable, colName, rangeStart);
			}
			HashSet<DBRecord> tmpRecordHashSet = new HashSet<DBRecord>();
			//! --
				tmpRecordHashSet.insert(queryResultRecord);
			// --!
				queryList.add(tmpRecordHashSet);
		}
		return evaluateQueryWithOperator(queryList, strOperator);


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

	public DBRecord linearSearch(DBTable inputTable, String colName, String colValue) {
		for(Page page : inputTable.pageList) {
			for(DBRecord record : page.recordList) {
				if (record.recValue.get(colName).equals(colValue)) return record;
			}
		}
	}

	public Iterator evaluateQueryWithOperator(ArrayList<HashSet<DBRecord> > arr, String operator) {
		return null;
		//TODO write method body
	}



}
