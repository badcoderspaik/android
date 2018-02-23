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

    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
    private String query;
    Context context;

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        query = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+ID_KEY+" INTEGER PRIMARY KEY AUTOINCREMENT, tp_number TEXT, count_number TEXT, value TEXT, date TEXT)";
        //Toast.makeText(context, "База данных создана", Toast.LENGTH_SHORT).show();
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public Cursor getAllRecords() {
        SQLiteDatabase reader = getReadableDatabase();
        Cursor cursor = reader.rawQuery("SELECT * FROM "+TABLE_NAME+";", null);
        return cursor;
    }

	@Override
	public Cursor getStatisticByCount(int countNumber)
	{
		SQLiteDatabase dbReader = getReadableDatabase();
        Cursor cursor = dbReader.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COUNT_NUMBER+"="+countNumber+" ORDER BY "+ID_KEY+";", null);
		return cursor;
	}

    public  void addRecord(String tp, String count){
        SQLiteDatabase writer = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TP_NUMBER, tp);
        cv.put(COUNT_NUMBER, count);
        cv.put(VALUE, "");
        cv.put(DATE, format.format(new Date()));
        writer.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void removeRecord(long id){
        SQLiteDatabase dbWriter = getWritableDatabase();
        dbWriter.delete(TABLE_NAME, ID_KEY + "=" + id, null);
        dbWriter.close();
    }

    @Override
    public void changeRecord(long id, String value) {
        SQLiteDatabase dbWriter = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VALUE, value);
        dbWriter.update(TABLE_NAME, contentValues, ID_KEY+"="+id, null);
    }

}
