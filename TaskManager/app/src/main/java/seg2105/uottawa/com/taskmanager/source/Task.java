package seg2105.uottawa.com.taskmanager.source;

public class Task {
    public enum TaskStatus { Completed, Assigned, Postponed, Unassigned };

    private int ID, pointValue, assignedTo, createdBy, hoursDuration;
    private String name, notes, deadline;
    private TaskStatus status;

    public Task(String name) {
        this.name = name;
    }

    public Task(int ID, String name, String notes, String deadline, int hoursDuration, int pointValue, TaskStatus status, int createdBy, int assignedTo) {
        this.ID = ID;
        this.name = name;
        this.notes = notes;
        this.deadline = deadline;
        this.hoursDuration = hoursDuration;
        this.pointValue = pointValue;
        this.assignedTo = assignedTo; // ID of the respective user
        this.createdBy = createdBy;   // ID of the respective user
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public int getPointValue() {
        return pointValue;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public String getNotes() {
        return notes;
    }

    public String getDeadline() {
        return deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getHoursDuration() {
        return hoursDuration;
    }
}
