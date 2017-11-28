package seg2105.uottawa.com.taskmanager.source;

public class Item {
    private int ID = -1;
    protected String itemName = "Name Not Defined";

    public Item() { }

    public Item(String name) {
        this.itemName = name;
    }

    public Item(int ID, String name) {
        this.ID = ID;
        this.itemName = name;
    }

    //Here would go some code to manage the information in this class
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String newName) {
         itemName = newName;
    }

    public int getID() {
        return ID;
    }
}
