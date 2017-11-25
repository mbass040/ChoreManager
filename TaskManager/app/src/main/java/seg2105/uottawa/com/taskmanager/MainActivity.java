package seg2105.uottawa.com.taskmanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.TabLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import seg2105.uottawa.com.taskmanager.source.ShoppingList;
import seg2105.uottawa.com.taskmanager.source.Task;

import static seg2105.uottawa.com.taskmanager.R.id.lvTaskList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtName;
    private Button btnSwitchUser;
    private TaskManagerDatabaseHandler tmDB;
    private String newName = "";
    List<String>  userList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmDB = new TaskManagerDatabaseHandler(getApplicationContext());
        ListView taskListView = (ListView) findViewById(lvTaskList);

        final List<String[]> taskList = new LinkedList<String[]>();
        taskList.add(new String []{"Shopping", "17 items in List"});
        taskList.add(new String []{"Vaccum Living Room", "Deadline: Tonight - Unassigned"});
        taskList.add(new String []{"Wash Car", "Note: Don't wash if it rains tonight"});
        taskList.add(new String []{"Wash Dishes", "Repeat: Daily"});
        taskList.add(new String []{"Call Veterinary", "Note: Urgent"});


        //creates a simple listView with an Item and subitem to be able to give a task a name and a description
        ArrayAdapter<String[]> adapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, taskList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);

                String entry[] = taskList.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(entry[0]);
                text2.setText(entry[1]);

                return view;
            }

        };

        taskListView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initializing the widgets
        btnSwitchUser = (Button)findViewById(R.id.btnSwitchUser);


    }

    @Override
    public void onBackPressed() { //when the user clicks the back button on his Android
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_broom) {
            Intent intent = new Intent(getApplicationContext(),EquipmentActivity.class);
            startActivityForResult(intent,0);

//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
        }

        // Temporary for testing
        if (id == R.id.nav_open_task) {
            Intent intent = new Intent(this, SpecificTaskActivity.class);

            //Pass task's ID to the detail activity so that it can load the task's values
            //intent.putExtra("taskID", -1);

            startActivityForResult(intent, RESULT_OK);
        }
        else if (id == R.id.nav_shop){
            Intent intent = new Intent(this, ShoppingList.class);
            startActivityForResult(intent,RESULT_OK);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // activated when a user clicks on teh Switch User button in the navigation drawer
    public void btnChangeUser(View view){
        txtName = (TextView) findViewById(R.id.txtUser);
        final ListView lvUser = new ListView(this);
        userList = tmDB.getAllUsers();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectUser);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, userList);
        lvUser.setAdapter(arrayAdapter);
        builder.setView(lvUser);

        //when user selects item
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtName.setText(userList.get(position));
            }
        });
        builder.setPositiveButton("New User", new DialogInterface.OnClickListener() {
            @Override //when user clicks on save after entering his name
            public void onClick(DialogInterface dialog, int which) {
                createName();
                txtName.setText(newName);
            }
        });

        builder.show();


    }

    public void createName(){
        //create a alert dialog box that will prompt the new user to enter his name
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("What is your name?");
        final EditText etName = new EditText(this);
        alert.setView(etName);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override //when user clicks on save after entering his name
            public void onClick(DialogInterface dialog, int which) {
                newName = etName.getText().toString();
                tmDB.insertUser(newName);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override //when user clicks on Cancel
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    public void viewTaskDetails(View view) {
        Intent intent = new Intent(this, SpecificTaskActivity.class);

        //Pass task's ID to the detail activity so that it can load the task's values
        //intent.putExtra("taskID", -1);

        startActivityForResult(intent, RESULT_OK);
    }
}
