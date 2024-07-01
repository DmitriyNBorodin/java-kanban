package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String taskName, String taskDescription, TaskStatus status, int mainTaskId,
                   Duration duration, LocalDateTime startTime) {
        super(taskName, taskDescription, status, duration, startTime);
        this.epicId = mainTaskId;
    }

    public SubTask(String taskName, String taskDescription, TaskStatus status, int mainTaskId,
                   Duration duration) {
        super(taskName, taskDescription, status, duration);
        this.epicId = mainTaskId;
    }

    public int getMainTaskId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Sub," + this.getTaskId() + "," + this.getTaskName() + "," + this.getTaskDescription() +
                "," + this.getStatus() + "," + epicId + "," + getDuration() + "," + getStartTime().map(time -> time.toString()).orElse("null");
    }

}
