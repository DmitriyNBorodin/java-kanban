package TaskManager;

import java.util.Objects;

public class Task implements Cloneable{
    private int taskId;
    private String taskName;
    private String taskDescription;
    protected TaskStatus status;

    public Task(String taskName, String taskDescription, TaskStatus status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

    public String getTaskDescription() {
        return taskDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task anotherTask = (Task) obj;
        return Objects.equals(taskId, anotherTask.taskId);
    }

    @Override
    public String toString() {
        return "Utilities.Task{" + this.getClass() + ", id=" + taskId + ", name=" + taskName + ", description_length=" +
                taskDescription.length() + ", status=" + status + "}";
    }

    @Override
    public Task clone() throws CloneNotSupportedException{
        return (Task) super.clone();
    }
}
