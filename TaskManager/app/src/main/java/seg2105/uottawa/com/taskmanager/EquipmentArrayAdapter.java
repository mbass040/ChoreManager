package seg2105.uottawa.com.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class EquipmentArrayAdapter extends ArrayAdapter<Equipment> {
    private final Context context;
    private final ArrayList<Equipment> equipments;

    public EquipmentArrayAdapter(Context context, ArrayList<Equipment> values) {
        super(context, R.layout.equipment_item_layout, values);
        this.context = context;
        this.equipments = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Equipment curEquipments = equipments.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.equipment_item_layout, parent, false);
        TextView recipeName = (TextView) rowView.findViewById(R.id.itemName);
        TextView recipeDescription = (TextView) rowView.findViewById(R.id.itemDescription);

        recipeName.setText(curEquipments.getEquipmentName());
        recipeDescription.setText((curEquipments.getEquipmentDescription()));

        return rowView;
    }
}
