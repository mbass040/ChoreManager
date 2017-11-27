package seg2105.uottawa.com.taskmanager.source;


public class User {
    int ID;
    String name;

    public User(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
