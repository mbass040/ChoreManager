package seg2105.uottawa.com.taskmanager.source;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.R;

public class ShoppingList extends AppCompatActivity {

    //list of item
    private List<String> groceryList;
    private List<String> materialList;
    //adapter for item
    private ArrayAdapter<String> groceryAdapter;
    private ArrayAdapter<String> materialAdapter;
    //item type
    private enum itemType {Equipment, CartItem};

    //type of list
    private int type;


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

        //initiate both list
        groceryList = new ArrayList<String>();
        materialList = new ArrayList<String>();

        //get the ListView
        ListView lvGrocery = (ListView) findViewById(R.id.lvGrocerie);
        ListView lvMaterial = (ListView) findViewById(R.id.lvMaterial);

        //create adapter
        groceryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groceryList);
        materialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, materialList);

        //set the adapter
        lvGrocery.setAdapter(groceryAdapter);
        lvMaterial.setAdapter(materialAdapter);

        //set the long click listener of grocery
        lvGrocery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                type = 0;
                modifyDialog(position);
                return true;
            }
        });

        //set the long click listener of material
        lvMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                type = 1;
                modifyDialog(position);
                return true;
            }
        });

    }
    public void addDialog(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.shoppingAdd);
        final View dialogview = getLayoutInflater().inflate(R.layout.nav_add_to_shoppin,null);
        builder.setView(dialogview);
        final Spinner spinner = (Spinner) dialogview.findViewById(R.id.spinnerShopOption);
        final EditText text = (EditText) dialogview.findViewById(R.id.txtShoppingOption);


        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add(text.getText().toString(),spinner.getSelectedItemPosition());
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

    private void modifyDialog(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.shoppingModify);
        final View dialogview = getLayoutInflater().inflate(R.layout.nav_modify_shopping,null);
        builder.setView(dialogview);
        final EditText text = (EditText) dialogview.findViewById(R.id.txtmodifyshopping);


        builder.setPositiveButton(R.string.modify, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type ==0){
                    groceryList.set(position,text.getText().toString());
                    groceryAdapter.notifyDataSetChanged();
                }else{
                    materialList.set(position,text.getText().toString());
                    materialAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
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

    private void add(String item,int type){
        if(type == 1){
            materialList.add(item);
            materialAdapter.notifyDataSetChanged();

        }else{
            groceryList.add(item);
            groceryAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this,"Item added",Toast.LENGTH_SHORT).show();
    }
}
