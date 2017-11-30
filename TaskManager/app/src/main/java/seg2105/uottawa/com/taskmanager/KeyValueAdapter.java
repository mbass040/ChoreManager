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


public class KeyValueAdapter extends BaseAdapter {

    private Context mContext;
    private List<String[]> mElems;

    // Gets the context so it can be used later
    public KeyValueAdapter(Context c, ArrayList<String[]> list) {
        mContext = c;
        mElems = list;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(mElems.get(position)[0]);
    }

    @Override
    public int getCount() {
        return mElems.size();
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View listView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            listView = inflater.inflate(R.layout.key_value_listitem, null);
            final TextView lblID = (TextView) listView.findViewById(R.id.lblID);
            TextView lblValue = (TextView) listView.findViewById(R.id.lblValue);

            lblID.setText(String.valueOf(mElems.get(position)[0]));
            lblValue.setText(String.valueOf(mElems.get(position)[1]));
        }
        else {
            listView = convertView;
        }

        return listView;
    }

}
