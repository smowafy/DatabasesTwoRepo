package DatabasesTwo;

import java.io.Serializable;
import java.util.Hashtable;

public class DBRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private Hashtable<DBColumn, String> recValue;
}
