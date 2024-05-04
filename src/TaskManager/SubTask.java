package TaskManager;

public class SubTask extends Task {
    private int mainTaskId;


    public SubTask(String taskName, String taskDescription, TaskStatus status, int mainTaskId) {
        super(taskName, taskDescription, status);
        this.mainTaskId = mainTaskId;
    }

    public int getMainTaskId() {
        return mainTaskId;
    }

}
