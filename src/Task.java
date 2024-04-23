import java.util.Objects;

public class Task {
    private int taskID;
    private String taskName;
    private String taskDescription;
    protected TaskStatus status;



    public Task(String taskName, String taskDescription, TaskStatus status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null|| getClass() != obj.getClass()) return false;
        Task anotherTask = (Task) obj;
        return Objects.equals(taskID, anotherTask.taskID);
    }

    @Override
    public String toString() {
        return "Task{" + this.getClass() + ", id=" + taskID + ", name=" + taskName + ", description_length=" +
                taskDescription.length() + ", status=" + status + "}";
    }
}
