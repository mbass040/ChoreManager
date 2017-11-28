package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.Item;
import seg2105.uottawa.com.taskmanager.source.Task;

public class SpecificTaskActivity extends AppCompatActivity {

    private Boolean isEditMode;
    private TaskManagerDatabaseHandler db;
    private Task currentTask;
    private int currentTaskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_task);

        isEditMode = false;

        db = new TaskManagerDatabaseHandler(this);

        Intent intent = getIntent();

        currentTaskID = intent.getIntExtra("taskID", -1);

        setToReadOnly(null);
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

            fabEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
        }
        else {
            // Invoke database handler to update current task data
            db.updateTask(currentTaskID, txtNotes.getText().toString(), txtCompDate.getText().toString(), Integer.valueOf(txtHoursToComplete.getText().toString()), Integer.valueOf(txtPoints.getText().toString()));

            // Set & refresh the view to use most recent data & make read-only
            setToReadOnly(view);
        }
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
                lblTaskCreatorName = (TextView) findViewById(R.id.lblTaskCreatorName);

        // Display duration label
        lblDuration.setText(currentTask.getHoursDuration() + " hour" + (currentTask.getHoursDuration() == 1 ? "" : "s"));

        // Display deadline label
        lblTaskWhenToComplete.setText(currentTask.getDeadline());

        // TODO: Use Creator ID
        //User creator = db.getUser(task.getCreatedBy());
        //lblTaskCreatorName.setText(creator.getName());
        lblTaskCreatorName.setText("Christopher Wallace");

        // Display the task's notes
        tvNotes.setText(currentTask.getNotes());

        // Get all equipment items associated to this task
        List<Item> equipment = db.getTaskEquipment(currentTask.getID());

        // Load the names into
        List<String> equipmentList = new ArrayList<>();

        for (Item item : equipment) {
            equipmentList.add(Html.fromHtml("&#8226;") + " " + item.getItemName());
        }

        GridView gvTaskEquipment = (GridView) findViewById(R.id.gvEquipmentList);

        // Create adapter to load list items into the equipment GridView
        ArrayAdapter<String> equipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipmentList);

        // Show the list items in the grid
        gvTaskEquipment.setAdapter(equipAdapter);
    }

    public void showMoreTaskOptions(View view) {

        // Build and show the dialog containing options for a specific task (finish, delete, unassign, etc.)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.taskOptionTitle);
        builder.setItems(R.array.taskOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}