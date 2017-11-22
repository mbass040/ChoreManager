package seg2105.uottawa.com.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import seg2105.uottawa.com.taskmanager.R;
import seg2105.uottawa.com.taskmanager.source.Task;

public class SpecificTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_task);

        //Intent intent = getIntent();
        Task task = new Task("Clean Pool");

        setTitle(task.getName());
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