package TaskManager;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Integer> subTasksId = new ArrayList<>();
    List<Integer> incompletedsubTasksId = new ArrayList<>();

    public Epic(String taskName, String taskDescription, TaskStatus status) {

        super(taskName, taskDescription, status);
    }

    public void setSubTask(int newSubTaskId) {
        subTasksId.add(newSubTaskId);
        incompletedsubTasksId.add(newSubTaskId);
    }

    public void completeSubTask(int subTaskId) {
        incompletedsubTasksId.remove((Integer) subTaskId);
    }

    public void removeSubTask(int subTaskId) {
        subTasksId.remove((Integer) subTaskId);
        incompletedsubTasksId.remove((Integer) subTaskId);
    }

    public void calculateStatus() {
        if (subTasksId.isEmpty() || subTasksId.size() == incompletedsubTasksId.size()) {
            status = TaskStatus.NEW;
        } else if (incompletedsubTasksId.isEmpty()) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

}
