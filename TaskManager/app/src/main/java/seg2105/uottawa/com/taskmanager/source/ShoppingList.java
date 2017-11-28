package seg2105.uottawa.com.taskmanager.source;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.R;

public class ShoppingList extends AppCompatActivity {

    private List<String> groceryList;
    private List<String> materialList;
    private ArrayAdapter<String> groceryAdapter;
    private ArrayAdapter<String> materialAdapter;
    private int selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddToCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groceryList = new ArrayList<String>();
        materialList = new ArrayList<String>();

        groceryList.add("pototo");
        groceryList.add("allo");
        groceryList.add("padfad");
        groceryList.add("pototo");
        groceryList.add("allo");
        groceryList.add("padfad");
        groceryList.add("pototo");
        groceryList.add("allo");
        groceryList.add("padfad");


        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");

        ListView lvGrocerie = (ListView) findViewById(R.id.lvGrocerie);
        groceryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groceryList);
        lvGrocerie.setAdapter(groceryAdapter);


        ListView lvMaterial = (ListView) findViewById(R.id.lvMaterial);
        materialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, materialList);
        lvMaterial.setAdapter(materialAdapter);
    }
    public void addDialog(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.shopdialog);

        CharSequence[] choice = {"Grocery","Material"};

        builder.setSingleChoiceItems(choice,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = which;
            }
        });

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add("allo");
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void modifyDialog(){

    }

    private void add(String item){
        if(selection == 0){
            materialList.add(item);
            materialAdapter.notifyDataSetChanged();

        }else{
            groceryList.add(item);
            groceryAdapter.notifyDataSetChanged();
        }
    }

}
