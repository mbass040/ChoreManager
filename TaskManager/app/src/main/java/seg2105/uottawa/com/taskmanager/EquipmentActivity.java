package seg2105.uottawa.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ListView;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class EquipmentActivity extends FragmentActivity {
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

    public void btnShowDialog(View view){
        showEquipmentDialog();
    }

    public void showEquipmentDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEquipment addEquipment = new AddEquipment();
        addEquipment.setDialogTitle("Enter equipment name");
        addEquipment.show(fragmentManager, "Input Dialog");
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent datEquipmenta) {
        ListView listView = (ListView) findViewById(R.id.list);

        listView.refreshDrawableState();
    }


}
