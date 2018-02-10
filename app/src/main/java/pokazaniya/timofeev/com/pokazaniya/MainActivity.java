package pokazaniya.timofeev.com.pokazaniya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity {

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



        tableadapter = new TableAdapter(this, values);
        thisListView = (ListView)findViewById(R.id.mainListView1);
        thisListView.setAdapter(tableadapter);

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.setvalue, null);
        et = (EditText)view.findViewById(R.id.setvalue);

        db.getFullTable(values, tableadapter);
        registerForContextMenu(thisListView);
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
                String[] countList = getResources().getStringArray(R.array.count);
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

            default: return null;
        }

    }

    DialogInterface.OnClickListener addTpListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface p1, int position)
        {
            switch(position){
                case 0:
                    db.addTpOrCount(tp309, count0, values, tableadapter);
                    db.addTpOrCount(tp309, count1, values, tableadapter);
                    break;
                case 1:
                    db.addTpOrCount(tp310, count2, values, tableadapter);
                    db.addTpOrCount(tp310, count3, values, tableadapter);
                    db.addTpOrCount(tp310, count4, values, tableadapter);
                    db.addTpOrCount(tp310, count5, values, tableadapter);
                    break;
                case 2:
                    db.addTpOrCount(tp311, count6, values, tableadapter);
                    db.addTpOrCount(tp311, count7, values, tableadapter);
                    break;
                case 3:
                    db.addTpOrCount(tp312, count8, values, tableadapter);
                    db.addTpOrCount(tp312, count9, values, tableadapter);
                    break;
                case 4:
                    db.addTpOrCount(tp313, count10, values, tableadapter);
                    break;
                case 5:
                    db.addTpOrCount(tp314, count11, values, tableadapter);
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
                    db.addTpOrCount(tp309, count0, values, tableadapter);
                    break;
                case 1:
                    db.addTpOrCount(tp309, count1, values, tableadapter);
                    break;
                case 2:
                    db.addTpOrCount(tp310, count2, values, tableadapter);
                    break;
                case 3:
                    db.addTpOrCount(tp310, count3, values, tableadapter);
                    break;
                case 4:
                    db.addTpOrCount(tp310, count4, values, tableadapter);
                    break;
                case 5:
                    db.addTpOrCount(tp310, count5, values, tableadapter);
                    break;
                case 6:
                    db.addTpOrCount(tp311, count6, values, tableadapter);
                    break;
                case 7:
                    db.addTpOrCount(tp311, count7, values, tableadapter);
                    break;
                case 8:
                    db.addTpOrCount(tp312, count8, values, tableadapter);
                    break;
                case 9:
                    db.addTpOrCount(tp312, count9, values, tableadapter);
                    break;
                case 10:
                    db.addTpOrCount(tp313, count10, values, tableadapter);
                    break;
                case 11:
                    db.addTpOrCount(tp314, count11, values, tableadapter);
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
                    db.changeRecord(getItem, id,  value, values, tableadapter);
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

}
