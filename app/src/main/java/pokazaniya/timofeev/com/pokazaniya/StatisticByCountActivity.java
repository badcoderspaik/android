package pokazaniya.timofeev.com.pokazaniya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class StatisticByCountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<Item> values = new ArrayList<Item>();
        TableAdapter tableadapter = new TableAdapter(this, values);
        ListView thisListView = (ListView)findViewById(R.id.mainListView1);
        thisListView.setAdapter(tableadapter);
        DbHelper db = new DbHelper(this);

        int countNumber = getIntent().getIntExtra("countNumber", 0);
        db.getStatisticByCount(countNumber, values, tableadapter);
    }
}
