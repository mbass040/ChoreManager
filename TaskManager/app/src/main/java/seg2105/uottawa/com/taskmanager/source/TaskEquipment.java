package seg2105.uottawa.com.taskmanager.source;

public class TaskEquipment {
    private Item equipment;
    private Task task;

    public TaskEquipment(Item equipment, Task task) {
        this.equipment = equipment;
        this.task = task;
    }

    public Item getEquipment() {
        return equipment;
    }

    public Task getTask() {
        return task;
    }
}
