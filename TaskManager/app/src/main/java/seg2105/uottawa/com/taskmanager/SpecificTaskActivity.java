package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import seg2105.uottawa.com.taskmanager.source.Task;

public class SpecificTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_task);

        //Intent intent = getIntent();
        Task task = new Task("Clean Pool");

        setTitle(task.getName());

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

    }

    public void showMoreTaskOptions(View view) {

        // Build and show the dialog containing options for a specific task (finish, delete, unassign, etc.)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.taskOptionTitle);
        builder.setItems(R.array.taskOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}