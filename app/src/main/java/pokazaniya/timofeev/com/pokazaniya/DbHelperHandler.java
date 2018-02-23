package pokazaniya.timofeev.com.pokazaniya;

import android.database.Cursor;

/**
 * В данный интерфейс вынесены методы по работе с базой данных и константы с именами базы данных, таблиц базы
 * и столбцов
 */
public interface DbHelperHandler {

    public static final int DB_VERSION = 2;//версия базы данных
    public static final String DB_NAME = "pokazaniya";//имя базы данных
    public static final String TABLE_NAME = "pok";//имя основной таблицы
    public static final String TABLE_TRASH = "trash";//имя таблицы, в которой будут храниться удаленные записи
    public static final String ID_KEY = "_id";//id записи
    public static final String TP_NUMBER = "tp_number";//номер ТП
    public static final String COUNT_NUMBER = "count_number";//номер счетчика
    public static final String VALUE = "value";//значение показаний
    public static final String DATE = "date";//дата
    public void removeRecord(long id);//удаление записи по id
    public void changeRecord(long id, String value);//вставка значения value или изменение записи c id, равным id
    public Cursor getStatisticByCount(int countNumber);//получение статистики по счетчику с номером countNumber
	public Cursor getAllRecords();//получение всех записей из базы

}
