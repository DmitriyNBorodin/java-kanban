package tasks;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String taskName, String taskDescription, TaskStatus status, int mainTaskId) {
        super(taskName, taskDescription, status);
        this.epicId = mainTaskId;
    }

    public int getMainTaskId() {
        return epicId;
    }

}
