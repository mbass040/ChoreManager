package seg2105.uottawa.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.content.DialogInterface;
import android.widget.TextView;
import seg2105.uottawa.com.taskmanager.source.Item;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class EquipmentActivity extends FragmentActivity {
    private ListView listView;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_activity);
        listView = (ListView) findViewById(R.id.list);
        EquipmentManager manager = EquipmentManager.getInstance();

        EquipmentArrayAdapter adapter = new EquipmentArrayAdapter(this, manager.getItemList());
        listView.setAdapter(adapter);


    }

    public void btnShowDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.nav_equipment, null);
        builder.setTitle(R.string.newEquipment);
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final TextView equipmentName = (TextView) dialogView.findViewById(R.id.txtEquipment);
                //int equipmentIndex = intent.getIntExtra(EquipmentManager.intentIndexTitle, 0);
                //final Item item = EquipmentManager.getInstance().getEquipmentAt(equipmentIndex);
                Item item = new Item();
                item.setItemName(equipmentName.getText().toString());
                //finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent datEquipmenta) {
        ListView listView = (ListView) findViewById(R.id.list);

        listView.refreshDrawableState();
    }


}
