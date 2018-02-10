package pokazaniya.timofeev.com.pokazaniya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper implements DbHelperHandler {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "pokazaniya";
    private static final String TABLE_NAME = "pok";
    private static final String ID_KEY = "id";
    private static final String TP_NUMBER = "tp_number";
    private static final String COUNT_NUMBER = "count_number";
    private static final String VALUE = "value";
    private static final String DATE = "date";
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
    private String query;
    Context context;

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        query = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, tp_number TEXT, count_number TEXT, value TEXT, date TEXT)";
        Toast.makeText(context, "База данных создана", Toast.LENGTH_SHORT).show();
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void getFullTable(ArrayList<Item> values, TableAdapter tableadapter) {
        SQLiteDatabase dbReader = getReadableDatabase();
        Cursor cursor = dbReader.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+ID_KEY+";", null);
        if(cursor.moveToFirst()){
            do{
                values.add(new Item(cursor.getInt(0),  cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }while(cursor.moveToNext());
            tableadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getCountByNumber(ArrayList<Item> values, TableAdapter tableadapter, int countNumber) {
        SQLiteDatabase dbReader = getReadableDatabase();
        Cursor cursor = dbReader.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COUNT_NUMBER+"="+countNumber+" ORDER BY "+ID_KEY+";", null);
        if(cursor.moveToFirst()){
            do{
                values.add(new Item(cursor.getInt(0),  cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }while (cursor.moveToNext());
            tableadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addTpOrCount(String tp, String count, ArrayList<Item> values, TableAdapter tableadapter) {
        SQLiteDatabase dbWriter = getWritableDatabase();
        SQLiteDatabase dbReader = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TP_NUMBER, tp);
        cv.put(COUNT_NUMBER, count);
        cv.put(VALUE, "");
        cv.put(DATE, format.format(new Date()));
        dbWriter.insert(TABLE_NAME, null, cv);

        Cursor cursor = dbReader.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY id DESC LIMIT 1;", null);
        if(cursor.moveToFirst()){
            values.add(new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            tableadapter.notifyDataSetChanged();
        }

        dbWriter.close();
        dbReader.close();
    }

    @Override
    public void removeRecord(int item, int id, ArrayList<Item> values, TableAdapter tableadapter) {
        SQLiteDatabase dbWriter = getWritableDatabase();
        dbWriter.delete(TABLE_NAME, ID_KEY+"="+id, null);
        values.remove(item);
        tableadapter.notifyDataSetChanged();
        dbWriter.close();
    }

    @Override
    public void changeRecord(int item, int id, String value, ArrayList<Item> values, TableAdapter tableadapter) {
        SQLiteDatabase dbWriter = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(VALUE, value);
        dbWriter.update(TABLE_NAME, cv, ID_KEY+"="+id, null);

        values.get(item).value = value;
        tableadapter.notifyDataSetChanged();
    }

}
