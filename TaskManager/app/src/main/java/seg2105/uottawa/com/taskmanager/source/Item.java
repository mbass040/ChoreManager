package seg2105.uottawa.com.taskmanager.source;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class Item {
    private String equipmentName = "Name Not Defined";
    private String equipmentDescription = "Description Not Defined";

    public Item(String name, String description) {
        this.equipmentName = name;
        this.equipmentDescription = description;
    }

    //Here would go some code to manage the information in this class
    public String getItemName() {
        return equipmentName;
    }

    public void setItemName(String newName) {
         equipmentName= newName;
    }

    public String getItemDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String newDescription) {
        equipmentDescription = newDescription;
    }
}
