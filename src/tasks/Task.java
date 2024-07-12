package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task implements Cloneable {
    private Integer id;
    private String taskName;
    private String taskDescription;
    protected TaskStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public Task(String taskName, String taskDescription, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        calculateEndTime();
    }

    public Task(String taskName, String taskDescription, TaskStatus status, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.duration = duration;
    }

    public Integer getTaskId() {
        return id;
    }

    public void setTaskId(int id) {
        this.id = id;
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

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }


    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task anotherTask = (Task) obj;
        return Objects.equals(id, anotherTask.id);
    }

    @Override
    public String toString() {
        return "Task," + id + "," + taskName + "," + taskDescription + "," + status + "," + duration.toMinutes() + "," + startTime;
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

    public void calculateEndTime() {
        if (startTime != null) {
            endTime = startTime.plus(duration);
        }
    }

}
