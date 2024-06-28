package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Integer> subTasksId = new ArrayList<>();
    List<Integer> incompleteSubTasksId = new ArrayList<>();

    public Epic(String taskName, String taskDescription, TaskStatus status) {

        super(taskName, taskDescription, status);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTask(int newSubTaskId) {
        subTasksId.add(newSubTaskId);
        incompleteSubTasksId.add(newSubTaskId);
    }

    public void completeSubTask(int subTaskId) {
        incompleteSubTasksId.remove((Integer) subTaskId);
    }

    public void removeSubTask(int subTaskId) {
        subTasksId.remove((Integer) subTaskId);
        incompleteSubTasksId.remove((Integer) subTaskId);
    }

    public void calculateStatus() {
        if (subTasksId.size() == incompleteSubTasksId.size()) {
            status = TaskStatus.NEW;
        } else if (incompleteSubTasksId.isEmpty()) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic," + this.getTaskId() + "," + this.getTaskName() + "," + this.getTaskDescription() +
                "," + this.getStatus() + "," + subTasksId.size();
    }

}
