package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;

import pokazaniya.timofeev.com.pokazaniya.DbHelper;
import pokazaniya.timofeev.com.pokazaniya.R;
import pokazaniya.timofeev.com.pokazaniya.UiUpdater;

/**
 * Класс диалогового окна, которое появляется при нажатии пункта меню настроек "Добавить ТП". Окно содержит список номеров ТП
 */
public class AddTpDialog extends DialogFragment {

    DbHelper db;//объект базы данных приложения
    /**
     * объект интерфейса, через который происходит взаимодействие диалогового окна с главной Activity.
     * При нажатии на пункт списка, из которого состоит диалоговое окно, в объекте addListener в методе onClick
     * происходит вставка записей в базу данных и для того, чтобы ListView активности был синхронизирован с базой, вызывается
     * метод update интерфейса UiUpdater, реализованного в MainActivity. В методе update как раз и вызывается метод
     * getSupportLoaderManager().getLoader(0).forceLoad(), синхронизирующий интерфейс активности с базой данных
     */
    private UiUpdater uiUpdater;
    /**
     * номера ТП и счетчиков из R.strings
     */
    private String tp309, tp310, tp311, tp312, tp313, tp314;
    private String count0, count1, count2, count3, count4,
            count5, count6, count7, count8, count9, count10, count11;

    /**
     * основной метод, в котором создается диалоговое окно. Для создания непосредственно диалогового окна внутри медода
     * применяется класс AlertDialog.Builder
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        db = new DbHelper(getActivity());
        //инициализация
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
        /**
         * объект непосредственно диалогового окна
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        /**
         * массив строк для списка внутри диалогового окна
         */
        String[] tpList = getResources().getStringArray(R.array.tp);//массив строк с номерами ТП из ресурса R.strings
        /**
         * установка заголовка окна
         */
        builder.setTitle(R.string.selectTp);
        /**
         * заполнить диалоговое окно списком с одиночным выбором типа radiobutton:
         * tpList - массив строк с номерами ТП из ресурса R.strings
         * -1 - не устанавливать ни один из пунктов списка как активный
         * addTpListener - слушатель нажатия на пункт списка
         */
        builder.setSingleChoiceItems(tpList, -1, addTpListener);
        return builder.create();
    }

    /**
     * Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь мы можем получить контекст фрагмента,
     * в качестве которого выступает класс MainActivity. Так как MainActivity реализует интерфейс UiUpdater,
     * то мы можем преобразовать контекст к данному интерфейсу.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        uiUpdater = (UiUpdater) context;
    }

    /**
     * объект интерфейса будет установлен в качестве слушателя нажатия пункта "добавить ТП" из меню настроек
     */
    DialogInterface.OnClickListener addTpListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface p1, int position) {
            switch (position) {
                case 0://если нажат пункт 0 в списке
                    db.addRecord(tp309, count0);
                    db.addRecord(tp309, count1);
                    uiUpdater.update();//синхронизировать интерфейс с базой данных
                    break;
                case 1://если нажат пункт 1 в списке
                    db.addRecord(tp310, count2);
                    db.addRecord(tp310, count3);
                    db.addRecord(tp310, count4);
                    db.addRecord(tp310, count5);
                    uiUpdater.update();//синхронизировать интерфейс с базой данных
                    break;
                case 2://если нажат пункт 2 в списке
                    db.addRecord(tp311, count6);
                    db.addRecord(tp311, count7);
                    uiUpdater.update();//синхронизировать интерфейс с базой данных
                    break;
                case 3://если нажат пункт 3 в списке
                    db.addRecord(tp312, count8);
                    db.addRecord(tp312, count9);
                    uiUpdater.update();//синхронизировать интерфейс с базой данных
                    break;
                case 4://если нажат пункт 4 в списке
                    db.addRecord(tp313, count10);
                    uiUpdater.update();//синхронизировать интерфейс с базой данных
                    break;
                case 5://если нажат пункт 5 в списке
                    db.addRecord(tp314, count11);
                    uiUpdater.update();//синхронизировать интерфейс с базой данных
                    break;
            }

        }

    };
}
