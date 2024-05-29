package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Integer> subTasksId = new ArrayList<>();
    List<Integer> incompletedSubTasksId = new ArrayList<>(); //С использованием этого поля рассчитывается статус эпика

    public Epic(String taskName, String taskDescription, TaskStatus status) {

        super(taskName, taskDescription, status);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTask(int newSubTaskId) {
        subTasksId.add(newSubTaskId);
        incompletedSubTasksId.add(newSubTaskId);
    }

    public void completeSubTask(int subTaskId) {
        incompletedSubTasksId.remove((Integer) subTaskId);
    }

    public void removeSubTask(int subTaskId) {
        subTasksId.remove((Integer) subTaskId);
        incompletedSubTasksId.remove((Integer) subTaskId);
    }

    public void calculateStatus() {
        if (subTasksId.size() == incompletedSubTasksId.size()) {
            status = TaskStatus.NEW;
        } else if (incompletedSubTasksId.isEmpty()) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

}
