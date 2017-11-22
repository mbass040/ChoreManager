package seg2105.uottawa.com.taskmanager;

import java.util.ArrayList;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class EquipmentManager {
    private static EquipmentManager instance = null;
    private ArrayList<Equipment> equipmentList;

    protected EquipmentManager() {
        //This Exists to defeat instantiation

        String[] values = new String[]{
                "Soap", "Towel", "Windex", "Comet", "Rope", "empty", "empty", "empty", "empty", "Last Supper"
        };

        equipmentList = new ArrayList<>();

        for (int i = 0; i < values.length ; i++) {
            Equipment newEquipment = new Equipment(values[i],"Task name: ");
            equipmentList.add(newEquipment);
        }
    }

    public static EquipmentManager getInstance() {
        if (instance == null) {
            instance = new EquipmentManager();
        }
        return instance;
    }

    public ArrayList<Equipment> getEquipmentList() {

        return equipmentList;
    }

    public Equipment getRecipeAt(int index) {

        return equipmentList.get(index);
    }
}
