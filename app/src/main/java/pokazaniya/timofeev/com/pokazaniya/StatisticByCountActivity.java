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

public class StatisticByCountActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

	SimpleCursorAdapter simpleCursorAdapter;
	ListView thisListView;
	DbHelper db;
	static int countNumber;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        ArrayList<Item> values = new ArrayList<Item>();
//        TableAdapter tableadapter = new TableAdapter(this, values);
//        ListView thisListView = (ListView)findViewById(R.id.mainListView1);
//        thisListView.setAdapter(tableadapter);
		thisListView = (ListView)findViewById(R.id.mainListView1);
		db = new DbHelper(this);
		String[] from = {DbHelper.TP_NUMBER, DbHelper.COUNT_NUMBER, DbHelper.VALUE, DbHelper.DATE};
        int[] to = {R.id.tableTextView1, R.id.tableTextView2, R.id.tableTextView3, R.id.tableTextView4};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.table, null, from, to, 0);
        thisListView.setAdapter(simpleCursorAdapter);
        getSupportLoaderManager().initLoader(0, null, this);

        countNumber = getIntent().getIntExtra("countNumber", 0);
        //db.getStatisticByCount(countNumber, values, tableadapter);
		
    }
	
	@Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new DbCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

	private void update(){
		getSupportLoaderManager().getLoader(0).forceLoad();
	}
	
	static class DbCursorLoader extends CursorLoader{
        DbHelper db;

        public DbCursorLoader(Context context, DbHelper db){
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
