package seg2105.uottawa.com.taskmanager;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import java.util.LinkedList;
import java.util.List;
import android.widget.TextView;


public class TaskList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        final List<String[]> taskList = new LinkedList<String[]>();
        taskList.add(new String []{"Shopping", "17 items in List"});
        taskList.add(new String []{"Vaccum Living Room", "Deadline: Tonight - Unassigned"});
        taskList.add(new String []{"Wash Car", "Note: Don't wash if it rains tonight"});
        taskList.add(new String []{"Wash Dishes", "Repeat: Daily"});
        taskList.add(new String []{"Call Veterinary", "Note: Urgent"});

        ArrayAdapter<String[]> adapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, taskList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);

                String entry[] = taskList.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(entry[0]);
                text2.setText(entry[1]);

                return view;
            }

        };


        setListAdapter(adapter);
    }


}
