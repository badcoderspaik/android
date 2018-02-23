package pokazaniya.timofeev.com.pokazaniya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.content.*;

/**
 * Активность, отображающая статистику по выбранному счетчику
 */
public class StatisticByCountActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    //адаптер для работы с данными из базы
    SimpleCursorAdapter simpleCursorAdapter;
    //ListView для построения основного интерфейса активности
    ListView thisListView;
    //класс по работе с базой данных
    DbHelper db;
    //переменная содержит номер счетчика, по которому будет отображена статистика
    static int countNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        thisListView = (ListView) findViewById(R.id.mainListView1);
        db = new DbHelper(this);
        /**
         * массив строк, содержащих имена столбцов в базе данных. Используется в SimpleCursorAdapter
         */
        String[] from = {DbHelper.TP_NUMBER, DbHelper.COUNT_NUMBER, DbHelper.VALUE, DbHelper.DATE};
        /**
         * массив элементов из R.layout.table, в которые будут вставляться значения из базы данных.
         * Используется в SimpleCursorAdapter
         */
        int[] to = {R.id.tableTextView1, R.id.tableTextView2, R.id.tableTextView3, R.id.tableTextView4};
        /**
         * адаптер, связанный с базой данных
         */
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.table, null, from, to, 0);
        /**
         * установка адаптера на ListView
         */
        thisListView.setAdapter(simpleCursorAdapter);
        /**
         * LoaderManager, связанный с базой и адаптером, предназначен для чтения данных из базы
         */
        getSupportLoaderManager().initLoader(0, null, this);
        /**
         * инициализация переменной значением, переданным из MainActivity в методе Intent.putExtra
         */
        countNumber = getIntent().getIntExtra("countNumber", 0);

    }
    /**
     *Именно в этом методе создается инициализированный в строке 56 загрузчик
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new DbCursorLoader(this, db);
    }
    /**
     * вызывается, когда созданный загрузчик завершил загрузку
     * @param loader
     * @param cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /**
     * наследник класса CursorLoader. В нем переопределен метод loadInBackground(), в котором происходит получение
     * курсора, с помощью которого читаются данные из базы
     */
    static class DbCursorLoader extends CursorLoader {
        DbHelper db;

        public DbCursorLoader(Context context, DbHelper db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getStatisticByCount(countNumber);
            return cursor;
        }
    }

}
