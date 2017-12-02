package seg2105.uottawa.com.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.Item;
import seg2105.uottawa.com.taskmanager.source.Task;
import seg2105.uottawa.com.taskmanager.source.User;

public class SpecificTaskActivity extends AppCompatActivity {

    private Boolean isEditMode;
    private TaskManagerDatabaseHandler db;
    private Task currentTask;
    private int currentTaskID;
    private int currentUserID;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_task);

        isEditMode = false;

        sharedPref = getSharedPreferences("main_activity", MODE_PRIVATE);

        db = new TaskManagerDatabaseHandler(this);

        Intent intent = getIntent();


        currentTaskID = intent.getIntExtra("taskID", -1);
        currentUserID = sharedPref.getInt(getString(R.string.sharedPrefUserID), -1);


        setToReadOnly(null);

        Button btnAddEquipment = (Button) findViewById(R.id.btnAddEquipment);

        btnAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                List<String[]> allEquipment = new ArrayList<>();

                List<Item> tempEquipList = db.addableTaskEquipment(currentTaskID);

                // Only extract the title and ID values needed for displaying
                for (Item equipment : tempEquipList) {
                    allEquipment.add(new String[] {String.valueOf(equipment.getID()), equipment.getItemName()});
                }

                // Set the title dynamically
                builder.setTitle((allEquipment.size() == 0 ? "No Equipment Exist" : "Add Equipment Item"));

                // Create adapter with returned equipment values
                KeyValueAdapter adapter = new KeyValueAdapter(view.getContext(), (ArrayList<String[]>) allEquipment);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView lvOptions = ((AlertDialog)dialogInterface).getListView();
                        ListAdapter adapter = lvOptions.getAdapter();
                        int equipID = (int) adapter.getItemId(i);

                        // Insert association between a task and an equipment item
                        db.insertTaskEquipment(currentTaskID, equipID);

                        // refresh the grid view with the new values
                        updateTaskEquipmentGridView(view.getContext());
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    public void editTask(View view) {
        isEditMode = !isEditMode; // Toggle the current tracked view mode

        LinearLayout lytHrsRead = (LinearLayout) findViewById(R.id.lytHrsRead),
                lytHrsWrite = (LinearLayout) findViewById(R.id.lytHrsWrite),

                lytCompDateRead = (LinearLayout) findViewById(R.id.lytDateRead),
                lytCompDateWrite = (LinearLayout) findViewById(R.id.lytDateWrite),
                lytPoints = (LinearLayout) findViewById(R.id.lytPoints);

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEditTask),
                             fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);

        TextView tvNotes = (TextView) findViewById(R.id.tvNotes);

        Button btnAddEquipment = (Button) findViewById(R.id.btnAddEquipment);

        EditText txtHoursToComplete = (EditText) lytHrsWrite.findViewById(R.id.txtHoursToComplete),
                txtCompDate = (EditText) lytCompDateWrite.findViewById(R.id.txtCompDate),
                txtPoints = (EditText) lytPoints.findViewById(R.id.txtPoints),
                txtNotes = (EditText) findViewById(R.id.txtNotes);

        // Change editable state of fields and FloatingActionButtons within the view
        if (isEditMode) {

            // Toggle values to hide/show fields used in Edit mode.
            fabEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
            fabCancel.setVisibility(View.VISIBLE);

            lytHrsRead.setVisibility(View.GONE);
            lytHrsWrite.setVisibility(View.VISIBLE);

            lytCompDateRead.setVisibility(View.GONE);
            lytCompDateWrite.setVisibility(View.VISIBLE);

            tvNotes.setVisibility(View.GONE);
            txtNotes.setVisibility(View.VISIBLE);

            // Set notes value to the EditText
            txtNotes.setText(currentTask.getNotes());

            lytPoints.setVisibility(View.VISIBLE);

            btnAddEquipment.setVisibility(View.VISIBLE);

            // Set values to the hoursToComplete, completionDate, and points EditTexts.
            txtHoursToComplete.setText(String.valueOf(currentTask.getHoursDuration()));
            txtCompDate.setText(String.valueOf(currentTask.getDeadline()));
            txtPoints.setText(String.valueOf(currentTask.getPointValue()));

            GridView gvEquipmentList = (GridView) findViewById(R.id.gvEquipmentList);
            updateTaskEquipmentGridView(this);

            gvEquipmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long equipID) {
                    int id = view.getId();

                    if (id == R.id.btnDeleteTaskEquip) {
                        db.deleteTaskEquipment(currentTaskID, (int)equipID);

                        // Refresh view after deleting an equipment from this task
                        updateTaskEquipmentGridView(view.getContext());
                    }
                }
            });

            fabEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
        }
        else {
            // Invoke database handler to update current task data
            db.updateTask(currentTaskID, txtNotes.getText().toString(), txtCompDate.getText().toString(), Integer.valueOf(txtHoursToComplete.getText().toString()), Integer.valueOf(txtPoints.getText().toString()));

            // Set & refresh the view to use most recent data & make read-only
            setToReadOnly(view);
        }
    }

    private void updateTaskEquipmentGridView (Context ctx) {
        GridView gvEquipmentList = (GridView) findViewById(R.id.gvEquipmentList);
        List<String[]> equipmentList = getEquipAsBasicMap();

        TaskEquipmentAdapter adapter = new TaskEquipmentAdapter(ctx, (ArrayList<String[]>) equipmentList);
        gvEquipmentList.setAdapter(adapter);
    }

    public void setToReadOnly(View view) {
        /* --- Toggles/sets the view to read-only mode --- */

        // Get the various layouts
        LinearLayout lytHrsRead = (LinearLayout) findViewById(R.id.lytHrsRead),
        lytHrsWrite = (LinearLayout) findViewById(R.id.lytHrsWrite),

        lytCompDateRead = (LinearLayout) findViewById(R.id.lytDateRead),
        lytCompDateWrite = (LinearLayout) findViewById(R.id.lytDateWrite),
        lytPoints = (LinearLayout) findViewById(R.id.lytPoints);

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEditTask),
                             fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);

        TextView tvNotes = (TextView) findViewById(R.id.tvNotes);
        EditText txtNotes = (EditText) findViewById(R.id.txtNotes);

        Button btnAddEquipment = (Button) findViewById(R.id.btnAddEquipment);

        // Display read-only regions
        fabEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
        fabCancel.setVisibility(View.GONE);

        lytHrsRead.setVisibility(View.VISIBLE);
        lytHrsWrite.setVisibility(View.GONE);

        lytCompDateRead.setVisibility(View.VISIBLE);
        lytCompDateWrite.setVisibility(View.GONE);

        tvNotes.setVisibility(View.VISIBLE);
        txtNotes.setVisibility(View.GONE);

        lytPoints.setVisibility(View.GONE);

        btnAddEquipment.setVisibility(View.GONE);

        fabEdit.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));


        /* --- Below sets the values of the labels based on current values of the specific task --- */

        // Fetch the record of the specific task using the taskID sent from the MainActivity
        currentTask = db.getSpecificTask(currentTaskID);

        // Set title of activity to the task's name + its point value
        setTitle(currentTask.getName() + " (" + currentTask.getPointValue() + " pt" + (currentTask.getPointValue() == 1 ? "" : "s") + ")");


        TextView lblDuration = (TextView) findViewById(R.id.lblTaskHowLong),
                lblTaskWhenToComplete = (TextView) findViewById(R.id.lblTaskWhenToComplete),
                lblTaskCreatorName = (TextView) findViewById(R.id.lblTaskCreatorName),
                lblAssigneeName = (TextView) findViewById(R.id.lblAssigneeName);

        // Display duration label
        lblDuration.setText(currentTask.getHoursDuration() + " hour" + (currentTask.getHoursDuration() == 1 ? "" : "s"));

        // Set the label of who this task is assigned to
        if (currentTask.getStatus() == Task.TaskStatus.Unassigned)
            lblAssigneeName.setText(R.string.unassigned);
        else
            lblAssigneeName.setText(db.getUser(currentTask.getAssignedTo()).getName());

        // Display deadline label
        lblTaskWhenToComplete.setText(currentTask.getDeadline());

        // TODO: Use Creator ID
        User creator = db.getUser(currentTask.getCreatedBy());
        lblTaskCreatorName.setText(creator.getName());

        // Display the task's notes
        tvNotes.setText(currentTask.getNotes());

        List<String> equipmentList = getEquipAsStringList();

        GridView gvTaskEquipment = (GridView) findViewById(R.id.gvEquipmentList);

        // Create adapter to load list items into the equipment GridView
        ArrayAdapter<String> equipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipmentList);

        // Show the list items in the grid
        gvTaskEquipment.setAdapter(equipAdapter);
    }

    private List<String[]> getEquipAsBasicMap () {
        List<Item> equipment = db.getTaskEquipment(currentTaskID);

        // Load the names into
        List<String[]> equipmentList = new ArrayList<>();

        for (Item item : equipment) {
            equipmentList.add(new String[] {String.valueOf(item.getID()), item.getItemName()});
        }

        return equipmentList;
    }

    private List<String> getEquipAsStringList () {
        // Get all equipment items associated to this task
        List<Item> equipment = db.getTaskEquipment(currentTaskID);

        // Load the names into
        List<String> equipmentList = new ArrayList<>();

        for (Item item : equipment) {
            equipmentList.add(Html.fromHtml("&#8226;") + " " + item.getItemName());
        }

        return equipmentList;
    }

    public void showMoreTaskOptions(View view) {
        // Build and show the dialog containing options for a specific task (finish, delete, unassign, etc.)
        List<String[]> optionsAsTuple = new ArrayList<>();

        // Populate various options for a task, limit the options based on the role of current user
        if (currentTask.getStatus() == Task.TaskStatus.Unassigned)
            optionsAsTuple.add(new String[] {"2","Assign Task"});

        // Different actions based on user's role
        if (currentUserID == currentTask.getAssignedTo()) {
            optionsAsTuple.add(new String[] {"3","Complete Task"});
            optionsAsTuple.add(new String[] {"4","Postpone Task"});
        }

        if (currentUserID == currentTask.getCreatedBy()) {
            optionsAsTuple.add(new String[] {"5","Delete Task"});
            optionsAsTuple.add(new String[]{"1", "Unassign Task"});
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.taskOptionTitle);

        KeyValueAdapter adapter = new KeyValueAdapter(view.getContext(), (ArrayList<String[]>) optionsAsTuple);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListView lvOptions = ((AlertDialog)dialogInterface).getListView();
                ListAdapter adapter = lvOptions.getAdapter();
                int optionID = (int) adapter.getItemId(i);

                switch (optionID) {
                    case 1: // Action to unassign task
                        db.updateTaskStatus(currentTaskID, Task.TaskStatus.Unassigned, -1);
                        setToReadOnly(null);
                        break;
                    case 2: // Action to assign task
                        List<String> users = db.getAllUsers();
                        AlertDialog.Builder userDialogBuilder = new AlertDialog.Builder(((AlertDialog) dialogInterface).getContext());
                        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(((AlertDialog) dialogInterface).getContext(),android.R.layout.simple_list_item_1, users);

                        userDialogBuilder.setTitle("Assign To");

                        // Assign data adapter to builder, then override the click listener
                        userDialogBuilder.setAdapter(userAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selectedUser = (String) ((AlertDialog)dialogInterface).getListView().getItemAtPosition(i);

                                // Fetch the userID by the user's name, then update the task to assign it to them
                                int userID = db.getUserId(selectedUser);
                                db.updateTaskStatus(currentTaskID, Task.TaskStatus.Assigned, userID);
                                setToReadOnly(null);
                            }
                        });

                        userDialogBuilder.create().show();
                        break;
                    case 3: // Action to set task to complete
                        db.updateTaskStatus(currentTaskID, Task.TaskStatus.Completed, -1);
                        break;
                    case 4: // Action to postpone task
                        break;
                    default: // Action to delete task
                        // Delete the task, then close the activity to return to the parent one
                        db.deleteTask(currentTaskID);
                        setResult(MainActivity.CHILD_DONE);
                        finish();
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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