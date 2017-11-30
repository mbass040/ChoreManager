package seg2105.uottawa.com.taskmanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import seg2105.uottawa.com.taskmanager.source.Item;

/**
 * Created by bedar on 2017-11-30.
 */

public class ItemList extends ArrayAdapter<Item> {

    private Activity context;
    private List<Item> items;

    public ItemList(Activity context,List<Item> items){
        super(context,R.layout.item_layout,items);
        this.context = context;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.item_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tvName);

        Item item = items.get(position);
        textViewName.setText(item.getItemName());
        return listViewItem;
    }

}
