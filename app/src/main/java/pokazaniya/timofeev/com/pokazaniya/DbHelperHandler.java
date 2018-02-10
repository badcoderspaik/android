package pokazaniya.timofeev.com.pokazaniya;

import java.util.ArrayList;

public interface DbHelperHandler {
    public void getFullTable(ArrayList<Item> values, TableAdapter tableadapter);
    public void addTpOrCount(String tp, String count, ArrayList<Item> values, TableAdapter tableadapter);
    public void removeRecord(int item, int id, ArrayList<Item> values, TableAdapter tableadapter);
    public void changeRecord(int item, int id, String value, ArrayList<Item> values, TableAdapter tableadapter);
    public void getCountByNumber(ArrayList<Item> values, TableAdapter tableadapter, int countNumber);
}
