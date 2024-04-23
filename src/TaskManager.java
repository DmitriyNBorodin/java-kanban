import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private int taskCounter = 0;
    Map<Integer, Task> ordinaryTasksMap = new HashMap<>();
    Map<Integer, EpicTask> epicTasksMap = new HashMap<>();
    Map<Integer, SubTask> subTasksMap = new HashMap<>();

    ArrayList<Task> getListOfTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for (int id: ordinaryTasksMap.keySet()) {
            listOfTasks.add(ordinaryTasksMap.get(id));
        }
        return listOfTasks;
    }

    ArrayList<EpicTask> getListOfEpicTasks() {
        ArrayList<EpicTask> listOfEpicTasks = new ArrayList<>();
        for (int id: epicTasksMap.keySet()) {
            listOfEpicTasks.add(epicTasksMap.get(id));
        }
        return listOfEpicTasks;
    }

    ArrayList<SubTask> getListOfSubTasks() {
        ArrayList<SubTask> listOfSubTasks = new ArrayList<>();
        for (int id: subTasksMap.keySet()) {
            listOfSubTasks.add(subTasksMap.get(id));
        }
        return listOfSubTasks;
    }


    void removeAll(TaskGroup group) {
        if (group == TaskGroup.ORDINARY || group == null) {
            for (int key : ordinaryTasksMap.keySet()) {
                ordinaryTasksMap.remove(key);
            }
        }
        if (group == TaskGroup.EPIC || group == null) {
            for (int key : epicTasksMap.keySet()) {
                epicTasksMap.remove(key);
            }
        }
        if (group == TaskGroup.SUB || group == null) {
            for (int key : subTasksMap.keySet()) {
                subTasksMap.remove(key);
            }
        }
        if (group == null) {
            taskCounter = 0;
        }
    }

    Task getTaskByID(int taskID) {
        return ordinaryTasksMap.get(taskID);
    }

    EpicTask getEpicTaskByID(int taskID) {
        return epicTasksMap.get(taskID);
    }

    SubTask getSubTaskByID(int taskID) {
        return subTasksMap.get(taskID);
    }

    public void addNewTask(Task task) {
        task.setTaskID(taskCounter);
        ordinaryTasksMap.put(taskCounter, task);
        taskCounter++;
    }

    public void addNewEpicTask(EpicTask task) {
        task.setTaskID(taskCounter);
        epicTasksMap.put(taskCounter, task);
        taskCounter++;
    }

    public void addNewSubTask(SubTask task) {
        task.setTaskID(taskCounter);
        subTasksMap.put(taskCounter, task);
        EpicTask currentMain = epicTasksMap.get(task.getMainTaskID());
        currentMain.setSubTask(taskCounter);
        currentMain.calculateStatus();
        taskCounter++;
    }

    public void refreshTask(Task task) {
        ordinaryTasksMap.put(task.getTaskID(), task);
    }

    public void refreshEpicTask(EpicTask task) {
        epicTasksMap.put(task.getTaskID(), task);
    }

    public void refreshSubTask(SubTask task) {
        int refreshingTaskID = task.getTaskID();
        int mainTaskID = task.getMainTaskID();
        subTasksMap.put(refreshingTaskID, task);
        EpicTask currentMain = epicTasksMap.get(mainTaskID);
        if (task.getStatus() == TaskStatus.DONE) {
            currentMain.completeSubTask(refreshingTaskID);
        }
        currentMain.calculateStatus();
    }


    void removeTaskByID(int taskID) {
        ordinaryTasksMap.remove(taskID);
        epicTasksMap.remove(taskID);
        subTasksMap.remove(taskID);
    }

    ArrayList<SubTask> getAllSubTasks (int epicTaskID) {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (int subTaskID: epicTasksMap.get(epicTaskID).subTasksID) {
            subTasksList.add(subTasksMap.get(subTaskID));
        }
        return subTasksList;
    }

}
