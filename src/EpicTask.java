import java.util.ArrayList;

public class EpicTask extends Task {
    ArrayList<Integer> subTasksID = new ArrayList<>();
    ArrayList<Integer> incompletedSubTasksID = new ArrayList<>();

    public EpicTask(String taskName, String taskDescription, TaskStatus status) {

        super(taskName, taskDescription, status);
    }

    public void setSubTask(int newSubTaskID) {
        subTasksID.add(newSubTaskID);
        incompletedSubTasksID.add(newSubTaskID);
    }
    public void completeSubTask(int subTaskID) {
        if (incompletedSubTasksID.contains(subTaskID)) {
            incompletedSubTasksID.remove((Integer)subTaskID);
        }
    }
    public void removeSubTask(int subTaskID) {
        subTasksID.remove((Integer)subTaskID);
        incompletedSubTasksID.remove((Integer)subTaskID);
    }
    public void calculateStatus() {
        if (subTasksID.size()==0||subTasksID.size()==incompletedSubTasksID.size()) {
            status = TaskStatus.NEW;
        } else if (incompletedSubTasksID.size()==0) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

}
