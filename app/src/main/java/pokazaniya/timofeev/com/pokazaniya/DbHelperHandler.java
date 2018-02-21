package pokazaniya.timofeev.com.pokazaniya;

import android.database.Cursor;

import java.util.ArrayList;

public interface DbHelperHandler {

    public void removeRecord(long id);
    public void changeRecord(long id, String value);
    public void getStatisticByCount(int countNumber, ArrayList<Item> values, TableAdapter tableadapter);
    public Cursor getAllRecords();

}
