package seg2105.uottawa.com.taskmanager;

import java.util.ArrayList;

import seg2105.uottawa.com.taskmanager.source.Item;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class EquipmentManager {
    private static EquipmentManager instance = null;
    private ArrayList<Item> itemList;

    protected EquipmentManager() {
        //This Exists to defeat instantiation

        String[] values = new String[]{
                "Soap", "Towel", "Windex", "Comet", "Rope", "", "empty", "empty", "empty", "Last Supper"
        };

        itemList = new ArrayList<>();

        for (int i = 0; i < values.length ; i++) {
            Item newItem = new Item(values[i],"Task name: ");
            itemList.add(newItem);
        }
    }

    public static EquipmentManager getInstance() {
        if (instance == null) {
            instance = new EquipmentManager();
        }
        return instance;
    }

    public ArrayList<Item> getItemList() {

        return itemList;
    }

    public Item getRecipeAt(int index) {

        return itemList.get(index);
    }
}
