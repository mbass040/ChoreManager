package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.ShoppingList;
import seg2105.uottawa.com.taskmanager.source.Task;


import static seg2105.uottawa.com.taskmanager.R.id.lvTaskList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtName;
    private Button btnSwitchUser;
    private TaskManagerDatabaseHandler tmDB;
    private String newName = "";
    public AlertDialog alertDialog;
    List<String>  userList = new ArrayList<String>();
    List<String[]> taskList = new ArrayList<String[]>();
    private int currentUserID;
    private List<Integer> taskIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmDB = new TaskManagerDatabaseHandler(getApplicationContext());


//        taskList.add(new String []{"Shopping", "17 items in List"});
//        taskList.add(new String []{"Vaccum Living Room", "Deadline: Tonight - Unassigned"});
//        taskList.add(new String []{"Wash Car", "Note: Don't wash if it rains tonight"});
//        taskList.add(new String []{"Wash Dishes", "Repeat: Daily"});
//        taskList.add(new String []{"Call Veterinary", "Note: Urgent"});
        
        //TaskManagerDatabaseHandler db = new TaskManagerDatabaseHandler(this);

        // Temporarily manually adding tasks
//        tmDB.insertTask("Shopping", "17 items in List", "today", 4, 5, Task.TaskStatus.Unassigned, 1);
//        tmDB.insertItem(TaskManagerDatabaseHandler.DBItemType.Equipment, "Shovel", null);
//        tmDB.insertItem(TaskManagerDatabaseHandler.DBItemType.Equipment, "Soap", null);
//        tmDB.insertTaskEquipment(1,1);
//        tmDB.insertTaskEquipment(1,2);

        //creates a simple listView with an Item and subitem to be able to give a task a name and a description
        updateListView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        if(userList.size() == 0){
//            createName();
//        }
        //initializing the widgets
        btnSwitchUser = (Button)findViewById(R.id.btnSwitchUser);


    }

    private void updateListView () {
        ListView taskListView = (ListView) findViewById(lvTaskList);
        List<Task> tempList = tmDB.getTasks(false);

        // Clear from previous DB get
        taskList.clear();

        for (Task task : tempList) {
            taskList.add(new String []{task.getName(), task.getNotes()});
            taskIDs.add(task.getID());
        }

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

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewTaskDetails(taskIDs.get(i));
            }
        });
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
        }

        // Temporary for testing
        if (id == R.id.nav_open_task) {
            viewTaskDetails(1);
        }
        else if (id == R.id.nav_shop){
            Intent intent = new Intent(this, ShoppingList.class);
            startActivityForResult(intent,RESULT_OK);
        }else if(id == R.id.nav_backlog){
            Toast.makeText(getApplicationContext(), "Backlog not yet implemented", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void newTask(View view){
        // Use a builder to do initial dialog setup for us
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Use our custom layout for the dialog
        final View dialogView = getLayoutInflater().inflate(R.layout.new_task, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.newTask);
        // initiate a Switch
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        // check current state of a Switch (true or false).
        Boolean switchState = simpleSwitch.isChecked();

        // Add the Create/Cancel buttons to the dialog
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText txtName = (EditText) dialogView.findViewById(R.id.txtTitle);
                String name = txtName.getText().toString();
                EditText txtNotes = (EditText) dialogView.findViewById(R.id.txtNotes);
                String note = txtNotes.getText().toString();
                EditText txtDeadLine = (EditText) dialogView.findViewById(R.id.txtDeadline);
                String deadline = txtDeadLine.getText().toString();
                EditText txtDuration = (EditText) dialogView.findViewById(R.id.txtDuration);
                Integer duration = Integer.valueOf(txtDuration.getText().toString());
                EditText txtPoints = (EditText) dialogView.findViewById(R.id.txtPoints);
                Integer points = Integer.valueOf(txtPoints.getText().toString());
                tmDB.insertTask(name, note, deadline, duration, points, Task.TaskStatus.Unassigned, currentUserID);
                updateListView();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO
            }
        });

        // Create & show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // activated when a user clicks on the Switch User button in the navigation drawer
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
                String name = userList.get(position);
                int userID;
                userID = tmDB.getUserId(name);
                currentUserID = userID;
                txtName.setText(name);
                Toast.makeText(getApplicationContext(), "ID is " + currentUserID, Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });
        builder.setPositiveButton("New User", new DialogInterface.OnClickListener() {
            @Override //when user clicks on save after entering his name
            public void onClick(DialogInterface dialog, int which) {
                createName();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();


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
                currentUserID = tmDB.getUserId(newName);
                txtName.setText(newName);
                Toast.makeText(getApplicationContext(), "ID is " + currentUserID, Toast.LENGTH_LONG).show();

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

    public void viewTaskDetails(int id) {
        Intent intent = new Intent(this, SpecificTaskActivity.class);

        //Pass task's ID to the detail activity so that it can load the task's values
        intent.putExtra("taskID", id);

        startActivityForResult(intent, RESULT_OK);
    }
}
