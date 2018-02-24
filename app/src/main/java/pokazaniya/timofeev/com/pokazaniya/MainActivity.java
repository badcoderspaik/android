package pokazaniya.timofeev.com.pokazaniya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.app.DialogFragment;
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

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import dialogs.AddTpDialog;

/**
* главный класс приложения, точка входа
* */
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, UiUpdater {
    /**
     *главный ListView приложения
     */
    ListView thisListView;
    /**
     * объект для построения диалоговых окон
     */
    AlertDialog.Builder builder;
    /**
     * класс базы данных
     */
    DbHelper db = new DbHelper(this);
    /**
     * создает layout из ресурса R.layout.setvalue. Этот layout содержит текстовое поле ввода, которое служит для
     * редактирования значения показаний
     */
    LayoutInflater inflater;
    /**
     * layout из R.layout.setvalue, который содержит текстовое поле для редактирования значения показаний
     */
    View setValueView;
    /**
     * текстовое поле из R.layout.setvalue, служащее для редактирования значений показаний
     */
    EditText et;
    /**
    * этой переменной будет присвоено значение AdapterView.AdapterContextMenuInfo.position. Свойство position этого класса
    * содержит номер пункта ListView, на котором было вызвано контекстное меню. Переменная используется для вывода
    * сообщения в Toast при удалении пункта записи из базы данных и пункта ListView соответственно.
    * */
    int getItem;
    /**
    * этой переменной будет писвоено значение AdapterView.AdapterContextMenuInfo.id. Свойство id этого класса содержит
    * _id записи из базы данных - primary key значение. Переменная используется при удалении записи из базы данных
    * */
    long id;
    /**
     * переменная используется при вызове контекстного меню на пункте списка и равна getItem + 1
     * нужна для корректного отображения номера удаленной записи в сообщении Toast, т.к. getItem начинается с 0,
     * а нужно, чтоб счет начинался с 1
     */
    int temp;
    /**
     * номера ТП и счетчиков из R.strings
     */
    private String tp309, tp310, tp311, tp312, tp313, tp314;
    private String count0, count1, count2, count3, count4,
            count5, count6, count7, count8, count9, count10, count11;
    /**
     * Камера
     */
    Camera camera;
    /**
     * параметры камеры
     */
    Parameters parameters;
    /**
     * флаг, указывающий - включена камера или нет
     */
    boolean ledIsChecked = false;
    /**
     * массив строк номеров счетчиков из R.strings
     */
    String[] countList;
    Cursor cursor;
    /**
     * Адаптер, к которому привязаны данные из базы
     */
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

        thisListView = (ListView) findViewById(R.id.mainListView1);

        inflater = LayoutInflater.from(this);
        setValueView = inflater.inflate(R.layout.setvalue, null);
        et = (EditText) setValueView.findViewById(R.id.setvalue);
        /**
         * регистрация контекстного меню на ListView
         */
        registerForContextMenu(thisListView);
        /**
         * массив строк, содержащих имена столбцов в базе данных. Используется в SimpleCursorAdapter
         */
        String[] from = {DbHelper.TP_NUMBER, DbHelper.COUNT_NUMBER, DbHelper.VALUE, DbHelper.DATE};
        /**
         * массив элементов из R.layout.table, в которые будут вставляться значения из базы данных.
         * Используется в SimpleCursorAdpater
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
         * LoaderManager, связанный с базой и адаптером, предназначенные для чтения данных из базы
         */
        getSupportLoaderManager().initLoader(0, null, this);
    }

    /**
     * метод оболочка для вывода Toast сообщений
     * @param message String выводимое сообщение
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * инициализация камеры
     */
    private void getCamera() {
        if (camera == null) {
            camera = Camera.open();
            parameters = camera.getParameters();
        }
    }

    /**
     * включение led
     */
    private void ledOn() {
        if (!ledIsChecked) {//если камера выключена
            if (camera == null || parameters == null) return;//если камера не иницализирована - прервать выполнение метода
            parameters = camera.getParameters();//иницализация параметров камеры
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);//включить led
            camera.setParameters(parameters);//установить камере данные параметры
            camera.startPreview();//стартануть камеру
            ledIsChecked = true;// установить флаг в значение true
        }
    }

    /**
     * выключение led
     */
    private void ledOff() {
        if (ledIsChecked) {//если камера включена
            if (camera == null || parameters == null) return;//если камера не инициализирована - превать метод
            parameters = camera.getParameters();//инициализация параметров камеры
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);//выключить led
            camera.setParameters(parameters);//устанавить камере данные параметры
            camera.stopPreview();//остановить камеру
            ledIsChecked = false;//установить флаг в значение false
        }
    }

    /**
     * включение или отключение камеры. Метод вызывается из R.menu.settings
     * @param item
     */
    public void turnLed(MenuItem item) {
        if (!ledIsChecked) {//если фонарик выключен
            ledOn();//включить фонарик
            item.setTitle("LED ON");//изменить надпись пункта меню настроек
            item.setIcon(R.drawable.lighton);//сменить иконку пункта меню настроек
        } else {//иначе
            ledOff();//выключить фонарик
            item.setTitle("LED OFF");//изменить надпись пункта меню настроек
            item.setIcon(R.drawable.lightoff);//сменить иконку пункта меню настроек
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ledOff();//выключить фонарик, если он включен
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ledIsChecked) ledOn();// включить фонарик, если флаг установлен в true
        invalidateOptionsMenu();//обновить пункт меню списка настроек
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();//инициализировать камеру
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();//освободить камеру
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {
        showDialog(4);//вывести диалоговое окно при нажатии кнопки назад
    }

    /**
     * создание меню настроек
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * обновление меню настроек. Вызывается перед показом меню настроек
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(2);//пункт меню управления фонариком
        String title = item.getTitle().toString();// название пункта
        if (!ledIsChecked) {//если false
            item.setTitle("LED OFF");//сменить название пункта
            item.setIcon(R.drawable.lightoff);//изменить иконку пункта
        } else {//иначе
            item.setTitle("LED ON");//сменить название пункта на другое
            item.setIcon(R.drawable.lighton);//сменить иконку на другую
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * создание контекстного меню
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context, menu);
    }

    /**
     * обработка нажатий на пункты контекстного меню
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /**
         * в объекте данного класса хранится вся информация об элементе AdapterView, к которому
         * привязано данное контекстное меню
         */
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        /**
         * позиция (int) элемента в AdapterView. Счет начинаетс с нуля
         */
        getItem = info.position;
        /**
         * id элемента, который равен id записи в базе данных
         */
        id = info.id;
        /**
         * временная переменная для корректного вывода позиции элемента, т.к. счет элементов начинается с нуля
         */
        temp = getItem + 1;

        switch (item.getItemId()) {
            case R.id.item_remove://если выбран пункт "Удалить" - вызвать соотвествующий диалог
                showDialog(5);
                return true;

            case R.id.item_change://если выбран пункт "Изменить" - вызвать соответствующий диалог
                showDialog(3);
        }
        return super.onContextItemSelected(item);
    }

    /**
     * метод построения диалоговых окон. Какой диалог будет отображен, зависит от того, какой пункт
     * контекстного меню или меню настроек будет выбран или же будет нажата системная кнопка "Назад"
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 2://если в метод showDialog() передан параметр 2
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selectCount);
                builder.setSingleChoiceItems(countList, -1, addCountListener);
                return builder.create();
            case 3://если в метод showDialog() передан параметр 3
                builder = new AlertDialog.Builder(this);
                builder.setView(setValueView);//заполнить диалоговое окно лайотом из R.layout.setvalue, который содержит текстовое поле для редактирования значения показаний
                builder.setTitle("Ввести показания");
                builder.setIcon(R.drawable.edit);//установить иконку окна
                builder.setPositiveButton(R.string.insert, setValueListener);//установить кнопки для диалогового
                builder.setNegativeButton(R.string.cancel, setValueListener);//окна и слушатели на них
                return builder.create();
            case 4://если в метод showDialog() передан параметр 4
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.exitFromApp);//установить в качестве содержимого окна сообщение
                builder.setIcon(R.drawable.exit);
                builder.setPositiveButton(R.string.yes, exitListener);
                builder.setNegativeButton(R.string.no, exitListener);
                return builder.create();
            case 5://если в метод showDialog() передан параметр 5
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert);
                builder.setMessage(R.string.removeRecord);
                builder.setIcon(R.drawable.alert);
                builder.setPositiveButton(R.string.remove, removeRecordListener);
                builder.setNegativeButton(R.string.no, removeRecordListener);
                return builder.create();
            case 6://если в метод showDialog() передан параметр 6
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.statisticByCount);
                builder.setItems(countList, statisticByCountListener);//установить в качестве содержимого окна обычный список
                return builder.create();

            default:
                return null;
        }

    }

    /**
     * объект интерфейса будет установлен в качестве слушателя действия "добавить ТП" из меню настрек
     */
    DialogInterface.OnClickListener addTpListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface p1, int position) {
            switch (position) {
                case 0://если нажат пункт 0 в списке
                    db.addRecord(tp309, count0);
                    db.addRecord(tp309, count1);
                    update();
                    break;
                case 1://если нажат пункт 1 в списке
                    db.addRecord(tp310, count2);
                    db.addRecord(tp310, count3);
                    db.addRecord(tp310, count4);
                    db.addRecord(tp310, count5);
                    update();
                    break;
                case 2://если нажат пункт 2 в списке
                    db.addRecord(tp311, count6);
                    db.addRecord(tp311, count7);
                    update();
                    break;
                case 3://если нажат пункт 3 в списке
                    db.addRecord(tp312, count8);
                    db.addRecord(tp312, count9);
                    update();
                    break;
                case 4://если нажат пункт 4 в списке
                    db.addRecord(tp313, count10);
                    update();
                    break;
                case 5://если нажат пункт 5 в списке
                    db.addRecord(tp314, count11);
                    update();
                    break;
            }

            showMessage("ТП добавлена");//отобразить сообщение

        }

    };
    /**
     * объект интерфейса будет установлен в качестве слушателя действия "добавить счетчик" из меню настрек
     */
    DialogInterface.OnClickListener addCountListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface p1, int position) {
            switch (position) {
                case 0:
                    db.addRecord(tp309, count0);
                    update();
                    break;
                case 1:
                    db.addRecord(tp309, count1);
                    update();
                    break;
                case 2:
                    db.addRecord(tp310, count2);
                    update();
                    break;
                case 3:
                    db.addRecord(tp310, count3);
                    update();
                    break;
                case 4:
                    db.addRecord(tp310, count4);
                    update();
                    break;
                case 5:
                    db.addRecord(tp310, count5);
                    update();
                    break;
                case 6:
                    db.addRecord(tp311, count6);
                    update();
                    break;
                case 7:
                    db.addRecord(tp311, count7);
                    update();
                    break;
                case 8:
                    db.addRecord(tp312, count8);
                    update();
                    break;
                case 9:
                    db.addRecord(tp312, count9);
                    update();
                    break;
                case 10:
                    db.addRecord(tp313, count10);
                    update();
                    break;
                case 11:
                    db.addRecord(tp314, count11);
                    update();
                    break;
            }
            showMessage("Счетчик добавлен");
        }
    };
    /**
     * объект этого интерфейса устанавливается в качестве слушателя пункта меню настроек "Выход" или
     * нажатия системной кнопки "Назад"
     */
    DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE://если нажата кнопка "Выйти"
                    finish();// завершить работу приложения
                    break;
                case Dialog.BUTTON_NEGATIVE:// если нажата кнопка "Отмена"
                    break;// просто скрыть диалоговое окно и остаться в приложении
            }
        }
    };
    /**
     * объект данного интерфейса устанавливается в качестве слушателя события выбора пункта контекстного
     * меню "Изменить"
     */
    DialogInterface.OnClickListener setValueListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE://если нажата кнопка "Изменить"
                    String value = et.getText().toString();//получить данные, введенные в текстовое поле
                    db.changeRecord(id, value);//изменить данные в базе данных
                    update();//обновить
                    break;
                case Dialog.BUTTON_NEGATIVE://если нажата кнопка "Оменить" - ничего не делать
                    break;
            }
        }


    };
    /**
     * объект данного интерфейса устанавливается в качестве слушателя события выбора контекстного меню "Удалить"
     */
    DialogInterface.OnClickListener removeRecordListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface p1, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE://если нажата кнопка "Удалить"
                    db.removeRecord(id);//удалить запись с указанным id из базы
                    update();//обновить интерфейс
                    showMessage("Запись " + temp + " удалена");//вывести сообщение
                    break;
                case Dialog.BUTTON_NEGATIVE:// если нажата кнопка "Отмена"
                    break;//ничего не делать и просто скрыть диалоговое окно
            }
        }

    };
    /**
     * объект данного интерфейса устанавливается в качестве слушателя выбора пункта меню настроек
     * "Статистика по счетчику"
     */
    DialogInterface.OnClickListener statisticByCountListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int position) {
            /**
             * intent для перехода на StatisticByCountActivity
             * эта активность отображает ListView со статистикой по выбранному номеру счетчика
             */
            Intent statisticByCountIntent = new Intent(MainActivity.this, StatisticByCountActivity.class);
            switch (position) {
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

    /**
     * метод инкапсулирует отправку сообщения с помощью параметра intent
     * в сообщении передается номер счетчика в параметре countNumber
     * @param intent
     * @param countNumber
     */
    private void sendMessage(Intent intent, int countNumber) {
        //отправить сообщение с именем "countNumber" и значением параметра countNumber
        intent.putExtra("countNumber", countNumber);
        //перейти на активность intent
        startActivity(intent);
    }
    //метод вызыватся при нажатии на пункт меню настроек "Добавить ТП"
    public void showSelectTpDialog(MenuItem item) {
        AddTpDialog tpDialog = new AddTpDialog();
        tpDialog.show(getSupportFragmentManager(),"addTpDialog");
    }
    //метод вызыватся при нажатии на пункт меню настроек "Добавить счетчик"
    public void showSelectCountDialog(MenuItem item) {
        showDialog(2);
    }
    //метод вызыватся при нажатии на пункт контекстного меню "Изменить"
    public void showSetValueDialog(MenuItem item) {
        showDialog(3);
    }
    //метод вызыватся при нажатии на пункт меню настроек "выход" или на системную кнопку "Назад"
    public void showExitDialog(MenuItem item) {
        showDialog(4);
    }
    //метод вызывается при нажатии на пункт меню настроек "Статистика по номеру счетчика"
    public void showStatisticByCountDialog(MenuItem item) {
        showDialog(6);
    }
    //в следующих трех методах происхдит работа с асинхронным манипулированием данных из базы

    /**
     *Именно в этом методе создается инициализированный в строке 158 загрузчик
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
     * метод-оболочка над методом загрузчика forceLoad(), который при вызове заново читает данные из связанного с ним источника
     */
    public void update() {
        getSupportLoaderManager().getLoader(0).forceLoad();
        showMessage("Тп добавлена");
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
            Cursor cursor = db.getAllRecords();
            return cursor;
        }
    }

}
