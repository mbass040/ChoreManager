package seg2105.uottawa.com.taskmanager.source;

/**
 * Created by Max on 2017-11-20.
 */

public class Task {
    public enum TaskStatus { Completed, Assigned, Postponed, Unassigned };

    private String name;
    private User assignedTo, createdBy;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
