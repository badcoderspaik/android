package pokazaniya.timofeev.com.pokazaniya;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item  {
    String tp_num;
    String count_num;
    String value;
    String date;
    int id;

    public Item(int id, String tp_num, String count_num, String value, String date){
        this.id = id;
        this.tp_num = tp_num;
        this.count_num = count_num;
        this.value = value;
        this.date = date;
    }

    public Item(String tp_num, String count_num, String value, String date){
        this.tp_num = tp_num;
        this.count_num = count_num;
        this.value = value;
        this.date = date;
    }

    public String getDate(){
        return new SimpleDateFormat("dd.MM.yy").format(new Date());
    }

}
