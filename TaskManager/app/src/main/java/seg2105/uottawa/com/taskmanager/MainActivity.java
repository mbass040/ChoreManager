package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.Task;


import static seg2105.uottawa.com.taskmanager.R.id.lvTaskList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int CHILD_DONE = 1;
    private TextView txtName;
    private TaskManagerDatabaseHandler tmDB;
    private String newName = "";
    public AlertDialog alertDialog;
    private List<String>  userList = new ArrayList<String>();
    private List<Integer> taskIDs = new ArrayList<>();
    private List<String[]> taskList = new LinkedList<String[]>();
    private int currentUserID, totalPoints;// global variable that keeps track of the userID and total points
    private SharedPreferences sharedPref;
    private boolean justMe = false; //global variable that checks if the user wants to just see his own tasks or everybody's tasks


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmDB = new TaskManagerDatabaseHandler(getApplicationContext());

        Switch justMeSwitch = (Switch) findViewById(R.id.switch1);
        justMeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                justMe = isChecked; //true if toggled, false otherwise
                updateListView(justMe);
            }
        });

        //creates a simple listView with an Item and subitem to be able to give a task a name and a description
        updateListView(justMe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawerIntro = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerIntro.openDrawer(GravityCompat.START);
        drawerIntro.closeDrawer(GravityCompat.START);

        // user id will be stored in the shared preferences to be easier to maintain it
        sharedPref = getSharedPreferences("main_activity", MODE_PRIVATE);



    }

    private void updateListView (boolean justMe) {
        ListView taskListView = (ListView) findViewById(lvTaskList);
        List<Task> tempList = tmDB.getTasks(justMe);

        // Clear from previous DB get
        taskList.clear();
        taskIDs.clear();
        if (justMe) {
            // Just select the tasks assigned to the current user
            for (Task task : tempList) {
                if (task.getID() == currentUserID) {
                    taskList.add(new String[]{task.getName(), task.getNotes()});
                    taskIDs.add(task.getID());
                }
            }
        }
        else{
            for (Task task : tempList) {
                taskList.add(new String []{task.getName(), task.getNotes()});
                taskIDs.add(task.getID());
            }
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

        if (id == R.id.nav_shop){
            Intent intent = new Intent(this, ShoppingList.class);
            startActivityForResult(intent,RESULT_OK);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void newTask(View view){

        if(tmDB.getAllUsers().size() == 0) {
            createName(true);
            return;
        }

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
                    Integer duration, points;
                    EditText txtName = (EditText) dialogView.findViewById(R.id.txtTitle);
                    String name = txtName.getText().toString();
                    EditText txtNotes = (EditText) dialogView.findViewById(R.id.txtNotes);
                    String note = txtNotes.getText().toString();
                    EditText txtDeadLine = (EditText) dialogView.findViewById(R.id.txtDeadline);
                    String deadline = txtDeadLine.getText().toString();
                    EditText txtDuration = (EditText) dialogView.findViewById(R.id.txtDuration);
                    if (txtDuration.getText().toString() == "") {
                        duration = 0;
                    } else {
                        duration = Integer.valueOf(txtDuration.getText().toString());
                    }
                    EditText txtPoints = (EditText) dialogView.findViewById(R.id.txtPoints);
                    if (txtPoints.getText().toString() == "") {
                        points = 0;
                    } else {
                        points = Integer.valueOf(txtPoints.getText().toString());
                    }
                    tmDB.insertTask(name, note, deadline, duration, points, Task.TaskStatus.Unassigned, currentUserID);
                    updateListView(justMe);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            // Create & show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    // activated when a user clicks on the Switch User button in the navigation drawer
    public void btnChangeUser(View view){
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
                currentUserID = tmDB.getUserId(name);
                setCurrentUser(currentUserID);
                Toast.makeText(getApplicationContext(), "ID is " + currentUserID + " and points is " + totalPoints, Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });
        builder.setPositiveButton("New User", new DialogInterface.OnClickListener() {
            @Override //when user clicks on save after entering his name
            public void onClick(DialogInterface dialog, int which) {
                createName(false);
            }
        });

        alertDialog = builder.create();
        alertDialog.show();


    }

    public void createName(final boolean isNewTask){
        //create a alert dialog box that will prompt the new user to enter his name
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("What is your name?");
        final EditText etName = new EditText(this);
        alert.setView(etName);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override //when user clicks on save after entering his name
            public void onClick(DialogInterface dialog, int which) {
                newName = etName.getText().toString();
                currentUserID = tmDB.insertUser(newName);
                setCurrentUser(currentUserID);

                Toast.makeText(getApplicationContext(), "ID is " + currentUserID, Toast.LENGTH_LONG).show();
                dialog.dismiss();

                if(isNewTask){
                    newTask(null);
                }
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

    public void setCurrentUser(int userID){
        txtName = (TextView) findViewById(R.id.txtUser);

        //Shared preference code retrieved from
        //https://developer.android.com/training/data-storage/shared-preferences.html
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.sharedPrefUserID), userID);
        editor.apply();
        editor.commit();
        totalPoints = tmDB.getTotalPointForUser(userID);
        txtName.setText(tmDB.getUser(userID).getName() + " (" + totalPoints + " pt" + (totalPoints == 1 ? "" : "s") + ")");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int savedID = sharedPref.getInt(getString(R.string.sharedPrefUserID), -1);
        // Refresh listview when child activity has finished (i.e. Specific Task), task could have been deleted
        if (resultCode == CHILD_DONE){
            updateListView(justMe);
            setCurrentUser(savedID);
        }

    }

    public void viewTaskDetails(int id) {
        Intent intent = new Intent(this, SpecificTaskActivity.class);

        //Pass task's ID to the detail activity so that it can load the task's values
        intent.putExtra("taskID", id);

        startActivityForResult(intent, CHILD_DONE);
    }
}
