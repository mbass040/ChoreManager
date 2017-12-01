package seg2105.uottawa.com.taskmanager.source;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.ItemList;
import seg2105.uottawa.com.taskmanager.R;
import seg2105.uottawa.com.taskmanager.TaskManagerDatabaseHandler;

public class ShoppingList extends AppCompatActivity {

    //list of item
    private List<Item> groceryList;
    private List<Item> materialList;
    //adapter for item
    private ItemList groceryAdapter;
    private ItemList materialAdapter;
    //item type
    private enum itemType {Equipment, CartItem};

    //type of list
    private CartItem.ItemType type;

    //list view
    private ListView lvGrocery;
    private ListView lvMaterial;

    //database
    private TaskManagerDatabaseHandler db;


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

        //initiate both list and database
        groceryList = new ArrayList<Item>();
        materialList = new ArrayList<Item>();
        db = new TaskManagerDatabaseHandler(this);

        //get the ListView
        lvGrocery = (ListView) findViewById(R.id.lvGrocerie);
        lvMaterial = (ListView) findViewById(R.id.lvMaterial);

        //set the long click listener of grocery
        lvGrocery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                type = CartItem.ItemType.Grocery;
                modifyDialog(position);
                return true;
            }
        });

        //set the long click listener of material
        lvMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                type = CartItem.ItemType.Material;
                modifyDialog(position);
                return true;
            }
        });

        update();

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
                Toast.makeText(ShoppingList.this,"Item added",Toast.LENGTH_SHORT).show();
                update();
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

    private void modifyDialog(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.shoppingModify);
        final View dialogview = getLayoutInflater().inflate(R.layout.nav_modify_shopping,null);
        builder.setView(dialogview);
        final EditText text = (EditText) dialogview.findViewById(R.id.txtmodifyshopping);


        builder.setPositiveButton(R.string.modify, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modify(position,text.getText().toString());
                update();
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
            db.insertItem(TaskManagerDatabaseHandler.DBItemType.CartItem,item,CartItem.ItemType.Material);
        }else{
            db.insertItem(TaskManagerDatabaseHandler.DBItemType.CartItem,item,CartItem.ItemType.Grocery);
        }
    }

    private void update(){
        //get value from the database
        List<Item> list = db.getItems(false);
        CartItem cartItem;
        CartItem.ItemType type;
        materialList.clear();
        groceryList.clear();
        while(!list.isEmpty()){
            cartItem =(CartItem) list.remove(0);
            type = cartItem.getIsAMaterial();
            if(type == CartItem.ItemType.Grocery){
                groceryList.add(cartItem);
            }else{
                materialList.add(cartItem);
            }
        }

        //set the adapter
        groceryAdapter = new ItemList(ShoppingList.this,groceryList);
        materialAdapter = new ItemList(ShoppingList.this,materialList);
        lvGrocery.setAdapter(groceryAdapter);
        lvMaterial.setAdapter(materialAdapter);
    }
    private void modify(int position,String name){
        if(type == CartItem.ItemType.Grocery){
            db.updateItem(groceryList.get(position).getID(),name);
        }else{
            db.updateItem(materialList.get(position).getID(),name);
        }
    }
}
