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

/**
 * наследник класса SQLiteOpenHelper для работы с базой данных
 */
public class DbHelper extends SQLiteOpenHelper implements DbHelperHandler {
    /**
     * объект по форматированию даты
     */
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
    /**
     * строка запроса для БД, в которой создается основная таблица БД приложения
     */
    private String query;
    /**
     * контекст
     */
    Context context;

    /**
     * в конструктор передается контекст приложения, имя создаваемой базы и номер версии
     * @param context
     */
    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    /**
     * вызывается один раз при создании базы данных. Создание базы происходит при совершении какой-либо транзакции с ней -
     * чтенни значиния или вставке значения в нее
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        query = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+ID_KEY+" INTEGER PRIMARY KEY AUTOINCREMENT, tp_number TEXT, count_number TEXT, value TEXT, date TEXT)";
        //Toast.makeText(context, "База данных создана", Toast.LENGTH_SHORT).show();
        /**
         * выполнение запроса к базе
         */
        db.execSQL(query);
    }

    /**
     * вызывается при обновлении базы. Обновление происходит при смене номера версии базы. В данном случае при обновлении
     * происходит удаление текущей таблицы из базы и вызов метода onCreate
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * получает все записи из базы. Вызывается в MainActivity при запуске приложения для заполнения данными из базы
     * главного ListView акивности
     * @return
     */
    @Override
    public Cursor getAllRecords() {
        //объект базы данных для чтения данных
        SQLiteDatabase reader = getReadableDatabase();
        //курсор со считанными данными
        Cursor cursor = reader.rawQuery("SELECT * FROM "+TABLE_NAME+";", null);
        return cursor;
    }

    /**
     * получает все записи из базы, в которых значения столбцов count_number равны переданному параметру countNumber
     * @param countNumber
     * @return
     */
	@Override
	public Cursor getStatisticByCount(int countNumber)
	{
        //объект базы данных для чтения данных
		SQLiteDatabase dbReader = getReadableDatabase();
        //курсор со считанными данными
        Cursor cursor = dbReader.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COUNT_NUMBER+"="+countNumber+" ORDER BY "+ID_KEY+";", null);
		return cursor;
	}

    /**
     * вставляет в базу данные: в столбец tp_number - значение из параметра tp, в столбец count_number - значение из
     * параметра count, в value - пустое значение, в date - дату
     * @param tp
     * @param count
     */
    public  void addRecord(String tp, String count){
        //объект базы данных для записи данных в базу
        SQLiteDatabase writer = getWritableDatabase();
        //объект, необходимый для упакавки значений, которые будут вставлены в базу данных
        ContentValues cv = new ContentValues();
        //упаковка значений в объект
        cv.put(TP_NUMBER, tp);
        cv.put(COUNT_NUMBER, count);
        cv.put(VALUE, "");
        cv.put(DATE, format.format(new Date()));
        //вставка значений в базу
        writer.insert(TABLE_NAME, null, cv);
    }
    //удаляет из базы данных запись с id, равной переданному параметру id
    @Override
    public void removeRecord(long id){
        //объект базы данных для записи данных в нее
        SQLiteDatabase dbWriter = getWritableDatabase();
        //удалить запись из базы
        dbWriter.delete(TABLE_NAME, ID_KEY + "=" + id, null);
        //закрыть соединение с базой
        dbWriter.close();
    }

    /**
     * вставить в запись с id, равным параметру id, в столбец value значение параметра value
     * @param id
     * @param value
     */
    @Override
    public void changeRecord(long id, String value) {
        //объект базы данных для записи в нее
        SQLiteDatabase dbWriter = getWritableDatabase();
        //объект для упаковки данных
        ContentValues contentValues = new ContentValues();
        //упакавка значения в объект
        contentValues.put(VALUE, value);
        //вставка значения
        dbWriter.update(TABLE_NAME, contentValues, ID_KEY+"="+id, null);
    }

}
