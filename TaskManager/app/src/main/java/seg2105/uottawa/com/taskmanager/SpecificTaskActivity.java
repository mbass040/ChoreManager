package seg2105.uottawa.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

        Intent intent = getIntent();
        Task task = new Task("Clean Pool");

        setTitle(task.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEditTask);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void editTask(View view) {

    }

}