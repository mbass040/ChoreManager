package seg2105.uottawa.com.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.HashMap;

import static seg2105.uottawa.com.taskmanager.R.id.lvTaskList;

/**
 * Created by gaby on 2017-11-23.
 */

public class TaskList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ListView taskListView = (ListView) findViewById(lvTaskList);
        HashMap<String, String> taskName = new HashMap<>();
         
    }

}
