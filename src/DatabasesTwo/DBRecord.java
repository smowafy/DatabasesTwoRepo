package DatabasesTwo;

import java.io.Serializable;
import java.util.Hashtable;

public class DBRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	public Hashtable<String, String> recValue;
	public DBRecord(Hashtable<String, String> recValue) {
		super();
		this.recValue = recValue;
	}
	@Override
	public String toString() {
		return "DBRecord [recValue=" + recValue + "]";
	}
	
	
}
