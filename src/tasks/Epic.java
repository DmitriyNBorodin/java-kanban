package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;

public class Epic extends Task {
    Map<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String taskName, String taskDescription, TaskStatus status) {
        super(taskName, taskDescription, status, Duration.ZERO);
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void setSubTask(SubTask newSubTask) {
        subTasks.put(newSubTask.getTaskId(), newSubTask);
        calculateStatus();
        calculateDuration();
    }

    public void removeSubTaskById(int subTaskId) {
        subTasks.remove(subTaskId);
        calculateStatus();
        calculateDuration();
    }

    public void calculateStatus() {
        int incompleteTasks = subTasks.values().stream()
                .filter(task -> task.getStatus().equals(TaskStatus.NEW)).collect(Collectors.toList()).size();
        if (incompleteTasks == subTasks.keySet().size()) {
            status = TaskStatus.NEW;
        } else if (incompleteTasks == 0) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    public void calculateDuration() {
        setDuration(subTasks.values().stream().map(task -> task.getDuration()).reduce(Duration.ZERO, Duration::plus));
        subTasks.values().stream().filter(task -> task.getStartTime().isPresent())
                .min(Comparator.comparing(task -> task.getStartTime().get()))
                .ifPresent(task -> setStartTime(task.getStartTime().get()));
        subTasks.values().stream().filter(task -> task.getEndTime().isPresent())
                .max(Comparator.comparing(task -> task.getEndTime().get()))
                .ifPresent(task -> setEndTime(task.getEndTime().get()));
    }

    @Override
    public String toString() {
        return "Epic," + this.getTaskId() + "," + this.getTaskName() + "," + this.getTaskDescription() +
                "," + this.getStatus() + "," + subTasks.size() + "," + this.getDuration().toMinutes() + "," + this.getStartTime();
    }
}
