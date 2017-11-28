package seg2105.uottawa.com.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/* Inspired by example: https://www.learn2crack.com/2014/01/android-custom-gridview.html */

public class TaskEquipmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<String[]> mTasks;

    // Gets the context so it can be used later
    public TaskEquipmentAdapter(Context c, ArrayList<String[]> list) {
        mContext = c;
        mTasks = list;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.task_equipment_delete, null);
            TextView lblEquipment = (TextView) grid.findViewById(R.id.lblTaskEquipment);
            final TextView lblTaskEquipID = (TextView) grid.findViewById(R.id.lblTaskEquipID);

            lblTaskEquipID.setText(String.valueOf(mTasks.get(position)[0]));
            lblEquipment.setText(String.valueOf(mTasks.get(position)[1]));

            ImageButton btnDeleteTaskEquip = (ImageButton) grid.findViewById(R.id.btnDeleteTaskEquip);

            // Click handler to handle deletion of a task's equipment
            btnDeleteTaskEquip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Send back to the Activity to handle removal of task equipment
                    ((GridView) parent).performItemClick(view, position, Integer.valueOf(lblTaskEquipID.getText().toString()));
                }
            });
        }
        else {
            grid = convertView;
        }

        return grid;
    }
}
