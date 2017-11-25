package seg2105.uottawa.com.taskmanager.source;

/**
 * Created by milena_dionnne on 2017-11-22.
 */

public class Item {
    private String itemName = "Name Not Defined";

    public Item() {
        itemName = "";
    }

    public Item(String name) {

        this.itemName = name;
    }

    //Here would go some code to manage the information in this class
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String newName) {
         itemName = newName;
    }

}
