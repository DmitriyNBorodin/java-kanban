public class SubTask extends Task {
    private int mainTaskID;
    private TaskGroup group = TaskGroup.SUB;

    SubTask (String taskName, String taskDescription, TaskStatus status, int mainTaskID) {
        super(taskName, taskDescription, status);
        this.mainTaskID = mainTaskID;
    }

    public int getMainTaskID() {
        return mainTaskID;
    }

}
