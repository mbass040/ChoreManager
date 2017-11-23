package seg2105.uottawa.com.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static seg2105.uottawa.com.taskmanager.R.id.lvTaskList;



public class TaskList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ListView taskListView = (ListView) findViewById(lvTaskList);
        HashMap<String, String> taskName = new HashMap<>();

        taskName.put("Shopping", "17 items in List");
        taskName.put("Vaccum Living Room", "Deadline: Tonight - Unassigned");
        taskName.put("Wash Car", "Note: Don't wash if it rains tonight");
        taskName.put("Wash Dishes", "Repeat: Daily");
        taskName.put("Call Veterinary", "Note: Urgent");

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int []{R.id.text1, R.id.text2});

        Iterator iter = taskName.entrySet().iterator();
        while(iter.hasNext()){
            HashMap<String, String> taskMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) iter.next();
            taskMap.put("First line", pair.getKey().toString());
            taskMap.put("Second line", pair.getValue().toString());
            listItems.add(taskMap);
        }

        taskListView.setAdapter(adapter);

    }

}
