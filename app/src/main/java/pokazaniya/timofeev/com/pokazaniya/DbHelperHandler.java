package pokazaniya.timofeev.com.pokazaniya;

import android.database.Cursor;

import java.util.ArrayList;

public interface DbHelperHandler {

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "pokazaniya";
    public static final String TABLE_NAME = "pok";
    public static final String ID_KEY = "_id";
    public static final String TP_NUMBER = "tp_number";
    public static final String COUNT_NUMBER = "count_number";
    public static final String VALUE = "value";
    public static final String DATE = "date";
    public void removeRecord(long id);
    public void changeRecord(long id, String value);
    public Cursor getStatisticByCount(int countNumber);
	public Cursor getAllRecords();

}
