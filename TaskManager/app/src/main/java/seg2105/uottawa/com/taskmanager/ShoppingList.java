package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.CartItem;
import seg2105.uottawa.com.taskmanager.source.Item;

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

        //initiate the lists and database
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
                modifyDialog(position,groceryList.get(position).getItemName());
                return true;
            }
        });

        //set the long click listener of material
        lvMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                type = CartItem.ItemType.Material;
                modifyDialog(position,materialList.get(position).getItemName());
                return true;
            }
        });

        //set the click listener of grocery
        lvGrocery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = CartItem.ItemType.Grocery;
                deleteDialog(position);
            }
        });

        //set the click listener of material
        lvMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = CartItem.ItemType.Material;
                deleteDialog(position);
            }
        });

        //show the listView
        update();

    }

    private void addDialog(View view){

        //create a builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set the title
        builder.setTitle(R.string.shoppingAdd);

        //set the view of the dialog
        final View dialogview = getLayoutInflater().inflate(R.layout.nav_add_to_shoppin,null);
        builder.setView(dialogview);

        //get id in the view
        final Spinner spinner = (Spinner) dialogview.findViewById(R.id.spinnerShopOption);
        final EditText text = (EditText) dialogview.findViewById(R.id.txtShoppingOption);

        //set the add button
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add(text.getText().toString(),spinner.getSelectedItemPosition());
                Toast.makeText(ShoppingList.this,"Item added",Toast.LENGTH_SHORT).show();
                update();
                dialog.dismiss();
            }
        });

        //set the cancel button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void modifyDialog(final int position, String name){

        //create builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set title
        builder.setTitle(R.string.shoppingModify);

        //set view
        final View dialogview = getLayoutInflater().inflate(R.layout.nav_modify_shopping,null);
        builder.setView(dialogview);

        //get id int the view
        final EditText text = (EditText) dialogview.findViewById(R.id.txtmodifyshopping);
        text.setText(name);

        //set the modify button
        builder.setPositiveButton(R.string.modify, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modify(position,text.getText().toString());
                update();
                dialog.dismiss();
            }
        });

        //set the cancel button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteDialog(final int position){

        //create builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set title
        builder.setTitle(R.string.deleteItem);

        //set delete button
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(position);
                update();
                dialog.dismiss();
            }
        });

        //set cancel button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void add(String item,int type){

        if(type == 1){ // if it's a material it add to the list of material
            db.insertItem(TaskManagerDatabaseHandler.DBItemType.CartItem,item,CartItem.ItemType.Material);
        }else{ // if it's a grocery it add it to the list of grocery
            db.insertItem(TaskManagerDatabaseHandler.DBItemType.CartItem,item,CartItem.ItemType.Grocery);
        }
    }

    private void update(){
        //get value from the database
        List<Item> list = db.getItems(false);

        CartItem cartItem;
        CartItem.ItemType type;

        //clear the list of grocery and material
        materialList.clear();
        groceryList.clear();

        //put the value in the right list
        while(!list.isEmpty()){//stop when the list is empty
            cartItem =(CartItem) list.remove(0);
            type = cartItem.getIsAMaterial();
            if(type == CartItem.ItemType.Grocery){//if grocery add to grocery
                groceryList.add(cartItem);
            }else{  //if material add to material
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

        if(type == CartItem.ItemType.Grocery){//if grocery modify the item in grocery
            db.updateItem(groceryList.get(position).getID(),name);
        }else{ //if material modify the item in material
            db.updateItem(materialList.get(position).getID(),name);
        }
    }

    private void delete(int position){
        if(type == CartItem.ItemType.Grocery){ // if grocery delete the item in grocery
            db.deleteItem(groceryList.get(position).getID());
        }else{ // material delete the item in material
            db.deleteItem(materialList.get(position).getID());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(MainActivity.CHILD_DONE);
                finish();
                break;
        }
        return true;
    }
}
