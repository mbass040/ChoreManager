package seg2105.uottawa.com.taskmanager.source;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.R;

public class ShoppingList extends AppCompatActivity {

    List<String> grocerieList;
    List<String> materialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddToCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grocerieList = new ArrayList<String>();
        materialList = new ArrayList<String>();

        grocerieList.add("pototo");
        grocerieList.add("allo");
        grocerieList.add("padfad");
        grocerieList.add("pototo");
        grocerieList.add("allo");
        grocerieList.add("padfad");
        grocerieList.add("pototo");
        grocerieList.add("allo");
        grocerieList.add("padfad");


        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");
        materialList.add("things");

        ListView lvGrocerie = (ListView) findViewById(R.id.lvGrocerie);
        ArrayAdapter<String> grocerieAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grocerieList);
        lvGrocerie.setAdapter(grocerieAdapter);


        ListView lvMaterial = (ListView) findViewById(R.id.lvMaterial);
        ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, materialList);
        lvMaterial.setAdapter(materialAdapter);
    }

}
