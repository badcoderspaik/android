package pokazaniya.timofeev.com.pokazaniya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import java.util.ArrayList;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    ArrayList<Item> values = new ArrayList<Item>();
    TableAdapter tableadapter;
    ListView thisListView;
    AlertDialog.Builder builder;

    DbHelper db = new DbHelper(this);
    LayoutInflater inflater;
    View view;
    EditText et;
    int getItem;
    int id;
    int temp;
    private String tp309, tp310, tp311, tp312, tp313, tp314;
    private String count0, count1, count2, count3, count4,
            count5, count6, count7, count8, count9, count10, count11;
    Camera camera;
    Parameters parameters;
    boolean ledIsChecked = false;
    Menu menu;
    String[] countList;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tp309 = getResources().getStringArray(R.array.tp)[0];
        tp310 = getResources().getStringArray(R.array.tp)[1];
        tp311 = getResources().getStringArray(R.array.tp)[2];
        tp312 = getResources().getStringArray(R.array.tp)[3];
        tp313 = getResources().getStringArray(R.array.tp)[4];
        tp314 = getResources().getStringArray(R.array.tp)[5];

        count0 = getResources().getStringArray(R.array.count)[0];
        count1 = getResources().getStringArray(R.array.count)[1];
        count2 = getResources().getStringArray(R.array.count)[2];
        count3 = getResources().getStringArray(R.array.count)[3];
        count4 = getResources().getStringArray(R.array.count)[4];
        count5 = getResources().getStringArray(R.array.count)[5];
        count6 = getResources().getStringArray(R.array.count)[6];
        count7 = getResources().getStringArray(R.array.count)[7];
        count8 = getResources().getStringArray(R.array.count)[8];
        count9 = getResources().getStringArray(R.array.count)[9];
        count10 = getResources().getStringArray(R.array.count)[10];
        count11 = getResources().getStringArray(R.array.count)[11];

        countList = getResources().getStringArray(R.array.count);

        tableadapter = new TableAdapter(this, values);
        thisListView = (ListView)findViewById(R.id.mainListView1);
        thisListView.setAdapter(tableadapter);

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.setvalue, null);
        et = (EditText)view.findViewById(R.id.setvalue);

        //db.getFullTable(values, tableadapter);
        registerForContextMenu(thisListView);

        /*cursor = db.getAllRecords();
        startManagingCursor(cursor);*/
        String[] from = {DbHelper.TP_NUMBER, DbHelper.COUNT_NUMBER, DbHelper.VALUE, DbHelper.DATE};
        int[] to = {R.id.tableTextView1, R.id.tableTextView2, R.id.tableTextView3, R.id.tableTextView4};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.table, null, from, to, 0);
        thisListView.setAdapter(simpleCursorAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
        /*SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.table, cursor, from, to);
        */
    }

    private  void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getCamera(){
        if(camera == null){
            camera = Camera.open();
            parameters = camera.getParameters();
        }
    }

    private void ledOn(){
        if(!ledIsChecked){
            if(camera == null || parameters == null) return;
            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            ledIsChecked = true;
        }
    }

    private void ledOff(){
        if(ledIsChecked){
            if(camera == null || parameters == null) return;
            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            ledIsChecked = false;
        }
    }

    public  void turnLed(MenuItem item){
        if(!ledIsChecked){
            ledOn();
            item.setTitle("LED ON");
            item.setIcon(R.drawable.lighton);
        } else {
            ledOff();
            item.setTitle("LED OFF");
            item.setIcon(R.drawable.lightoff);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ledOff();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ledIsChecked) ledOn();
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {
        showDialog(4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(2);
        String title = item.getTitle().toString();
        if(!ledIsChecked){
            item.setTitle("LED OFF");
            item.setIcon(R.drawable.lightoff);
        }
        else{
            item.setTitle("LED ON");
            item.setIcon(R.drawable.lighton);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        getItem = info.position;
        id = values.get(getItem).id;
        temp = getItem + 1;

        switch(item.getItemId()){
            case R.id.item_remove:
                showDialog(5);
                return true;

            case R.id.item_change:
                showDialog(3);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id){
            case 1:
                String[] tpList = getResources().getStringArray(R.array.tp);
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selectTp);
                builder.setSingleChoiceItems(tpList, -1, addTpListener);
                return builder.create();
            case 2:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selectCount);
                builder.setSingleChoiceItems(countList, -1, addCountListener);
                return builder.create();
            case 3:
                builder = new AlertDialog.Builder(this);
                builder.setView(view);
                builder.setTitle("Ввести показания");
                builder.setIcon(R.drawable.edit);
                builder.setPositiveButton(R.string.insert, setValueListener);
                builder.setNegativeButton(R.string.cancel, setValueListener);
                return builder.create();
            case 4:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.exitFromApp);
                builder.setIcon(R.drawable.exit);
                builder.setPositiveButton(R.string.yes, exitListener);
                builder.setNegativeButton(R.string.no, exitListener);
                return builder.create();
            case 5:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert);
                builder.setMessage(R.string.removeRecord);
                builder.setIcon(R.drawable.alert);
                builder.setPositiveButton(R.string.remove, removeRecordListener);
                builder.setNegativeButton(R.string.no, removeRecordListener);
                return builder.create();
            case 6:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.statisticByCount);
                builder.setItems(countList, statisticByCountListener);
                return builder.create();

            default: return null;
        }

    }

    DialogInterface.OnClickListener addTpListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface p1, int position)
        {
            switch(position){
                case 0:
                    db.addRecord(tp309, count0);
                    db.addRecord(tp309, count1);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 1:
                    db.addRecord(tp310, count2);
                    db.addRecord(tp310, count3);
                    db.addRecord(tp310, count4);
                    db.addRecord(tp310, count5);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 2:
                    db.addRecord(tp311, count6);
                    db.addRecord(tp311, count7);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 3:
                    db.addRecord(tp312, count8);
                    db.addRecord(tp312, count9);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 4:
                    db.addRecord(tp313, count10);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 5:
                    db.addRecord(tp314, count11);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
            }

            showMessage("ТП добавлена");

        }

    };
    DialogInterface.OnClickListener addCountListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface p1, int position)
        {
            switch(position){
                case 0:
                    db.addRecord(tp309, count0);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 1:
                    db.addRecord(tp309, count1);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 2:
                    db.addRecord(tp310, count2);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 3:
                    db.addRecord(tp310, count3);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 4:
                    db.addRecord(tp310, count4);
                    cursor.requery();
                    break;
                case 5:
                    db.addRecord(tp310, count5);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 6:
                    db.addRecord(tp311, count6);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 7:
                    db.addRecord(tp311, count7);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 8:
                    db.addRecord(tp312, count8);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 9:
                    db.addRecord(tp312, count9);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 10:
                    db.addRecord(tp313, count10);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
                case 11:
                    db.addRecord(tp314, count11);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    break;
            }
            showMessage("Счетчик добавлен");
        }
    };

    DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch(which){
                case Dialog.BUTTON_POSITIVE:
                    finish();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener setValueListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch(which){
                case Dialog.BUTTON_POSITIVE:
                    String value = et.getText().toString();
                    db.changeRecord(getItem, id, value, values, tableadapter);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }


    };

    DialogInterface.OnClickListener removeRecordListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface p1, int which)
        {
            switch(which){
                case Dialog.BUTTON_POSITIVE:
                    db.removeRecord(getItem, id, values, tableadapter);
                    showMessage("Запись "+temp+" удалена");
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }

    };

    DialogInterface.OnClickListener statisticByCountListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialogInterface, int position) {
            Intent statisticByCountIntent = new Intent(MainActivity.this, StatisticByCountActivity.class);
            switch (position){
                case 0:
                    sendMessage(statisticByCountIntent, 100964);
                    break;
                case 1:
                    sendMessage(statisticByCountIntent, 160000);
                    break;
                case 2:
                    sendMessage(statisticByCountIntent, 215110);
                    break;
                case 3:
                    sendMessage(statisticByCountIntent, 995258);
                    break;
                case 4:
                    sendMessage(statisticByCountIntent, 19250);
                    break;
                case 5:
                    sendMessage(statisticByCountIntent, 114489);
                    break;
                case 6:
                    sendMessage(statisticByCountIntent, 215933);
                    break;
                case 7:
                    sendMessage(statisticByCountIntent, 516465);
                    break;
                case 8:
                    sendMessage(statisticByCountIntent, 820943);
                    break;
                case 9:
                    sendMessage(statisticByCountIntent, 835057);
                    break;
                case 10:
                    sendMessage(statisticByCountIntent, 20297549);
                    break;
                case 11:
                    sendMessage(statisticByCountIntent, 20309187);
                    break;
            }
        }
    };

    private void sendMessage(Intent intent, int countNumber){
        intent.putExtra("countNumber", countNumber);
        startActivity(intent);
    }

    public void showSelectTpDialog(MenuItem item){
        showDialog(1);
    }


    public void showSelectCountDialog(MenuItem item){
        showDialog(2);
    }

    public void showSetValueDialog(MenuItem item){
        showDialog(3);
    }

    public void showExitDialog(MenuItem item){
        showDialog(4);
    }

    public void showStatisticByCountDialog(MenuItem item){showDialog(6);}

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

    static class DbCursorLoader extends CursorLoader{
        DbHelper db;

        public DbCursorLoader(Context context, DbHelper db){
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllRecords();
            return cursor;
        }
    }

}