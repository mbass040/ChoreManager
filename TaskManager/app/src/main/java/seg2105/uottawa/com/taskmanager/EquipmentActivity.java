package seg2105.uottawa.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class EquipmentActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_activity);
        listView = (ListView) findViewById(R.id.list);
        EquipmentManager manager = EquipmentManager.getInstance();

        EquipmentArrayAdapter adapter = new EquipmentArrayAdapter(this, manager.getEquipmentList());
        listView.setAdapter(adapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ListView listView = (ListView) findViewById(R.id.list);

        listView.refreshDrawableState();
    }

}
