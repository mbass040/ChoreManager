package seg2105.uottawa.com.taskmanager;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class Equipment {
    private String equipmentName = "Name Not Defined";

    public Equipment(String name, String description) {
        this.equipmentName = name;
    }

    //Here would go some code to manage the information in this class
    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String newName) {
         equipmentName= newName;
    }

}
