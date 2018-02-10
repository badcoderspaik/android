package pokazaniya.timofeev.com.pokazaniya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TableAdapter extends ArrayAdapter<Item> {

    private final Context context;
    ArrayList<Item> values = new ArrayList<Item>();


    public TableAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.table, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.table, parent, false);
        TextView tv1 = (TextView)rowView.findViewById(R.id.tableTextView1);
        TextView tv2 = (TextView)rowView.findViewById(R.id.tableTextView2);
        TextView tv3 = (TextView)rowView.findViewById(R.id.tableTextView3);
        TextView tv4 = (TextView)rowView.findViewById(R.id.tableTextView4);
        tv1.setText(values.get(position).tp_num);
        tv2.setText(values.get(position).count_num);
        tv3.setText(values.get(position).value);
        tv4.setText(values.get(position).date);
        return rowView;
    }

}
