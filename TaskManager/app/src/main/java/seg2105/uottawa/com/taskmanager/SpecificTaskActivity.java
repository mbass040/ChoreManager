package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import seg2105.uottawa.com.taskmanager.source.Task;

public class SpecificTaskActivity extends AppCompatActivity {

    private Boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_task);

        isEditMode = false;

        //Intent intent = getIntent();
        Task task = new Task("Clean Pool");

        setTitle(task.getName());

        // Populate the task equipment list
        List<String> equipmentList = new ArrayList<>();
        equipmentList.add(Html.fromHtml("&#8226;") + " Pool Vacuum");
        equipmentList.add(Html.fromHtml("&#8226;") + " Chlorine");
        equipmentList.add(Html.fromHtml("&#8226;") + " Sponge");
        equipmentList.add(Html.fromHtml("&#8226;") + " Soap");

        GridView gvTaskEquipment = (GridView) findViewById(R.id.gvEquipmentList);

        // Create adapter to load list items into the equipment GridView
        ArrayAdapter<String> equipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipmentList);

        // Show the list items in the grid
        gvTaskEquipment.setAdapter(equipAdapter);
    }

    public void editTask(View view) {
        isEditMode = !isEditMode; // Toggle the current tracked view mode

        LinearLayout lytHrsRead = (LinearLayout) findViewById(R.id.lytHrsRead);
        LinearLayout lytHrsWrite = (LinearLayout) findViewById(R.id.lytHrsWrite),

                lytCompDateRead = (LinearLayout) findViewById(R.id.lytDateRead),
                lytCompDateWrite = (LinearLayout) findViewById(R.id.lytDateWrite);

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEditTask),
                             fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);
        TextView tvNotes = (TextView) findViewById(R.id.tvNotes);
        EditText txtNotes = (EditText) findViewById(R.id.txtNotes);

        Button btnAddEquipment = (Button) findViewById(R.id.btnAddEquipment);

        // Change editable state of fields and FloatingActionButtons within the view
        if (isEditMode) {
            fabEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
            fabCancel.setVisibility(View.VISIBLE);

            lytHrsRead.setVisibility(View.GONE);
            lytHrsWrite.setVisibility(View.VISIBLE);

            lytCompDateRead.setVisibility(View.GONE);
            lytCompDateWrite.setVisibility(View.VISIBLE);

            tvNotes.setVisibility(View.GONE);
            txtNotes.setVisibility(View.VISIBLE);

            btnAddEquipment.setVisibility(View.VISIBLE);

            fabEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
        }
        else {
            // TODO: save changes to DB
            editTaskReadOnly(view);
        }
    }

    public void editTaskReadOnly(View view) {
        LinearLayout lytHrsRead = (LinearLayout) findViewById(R.id.lytHrsRead),
        lytHrsWrite = (LinearLayout) findViewById(R.id.lytHrsWrite),

        lytCompDateRead = (LinearLayout) findViewById(R.id.lytDateRead),
        lytCompDateWrite = (LinearLayout) findViewById(R.id.lytDateWrite);

        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEditTask),
                             fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);

        TextView tvNotes = (TextView) findViewById(R.id.tvNotes);
        EditText txtNotes = (EditText) findViewById(R.id.txtNotes);

        Button btnAddEquipment = (Button) findViewById(R.id.btnAddEquipment);

        fabEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
        fabCancel.setVisibility(View.GONE);

        lytHrsRead.setVisibility(View.VISIBLE);
        lytHrsWrite.setVisibility(View.GONE);

        lytCompDateRead.setVisibility(View.VISIBLE);
        lytCompDateWrite.setVisibility(View.GONE);

        tvNotes.setVisibility(View.VISIBLE);
        txtNotes.setVisibility(View.GONE);

        btnAddEquipment.setVisibility(View.GONE);

        fabEdit.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
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