import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private int taskCounter = 0;
    Map<Integer, Task> ordinaryTasksMap = new HashMap<>();
    Map<Integer, Epic> epicsMap = new HashMap<>();
    Map<Integer, SubTask> subTasksMap = new HashMap<>();

    ArrayList<Task> getListOfTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for (int id : ordinaryTasksMap.keySet()) {
            listOfTasks.add(ordinaryTasksMap.get(id));
        }
        return listOfTasks;
    }

    ArrayList<Epic> getListOfEpics() {
        ArrayList<Epic> listOfEpics = new ArrayList<>();
        for (int id : epicsMap.keySet()) {
            listOfEpics.add(epicsMap.get(id));
        }
        return listOfEpics;
    }

    ArrayList<SubTask> getListOfSubTasks() {
        ArrayList<SubTask> listOfSubTasks = new ArrayList<>();
        for (int id : subTasksMap.keySet()) {
            listOfSubTasks.add(subTasksMap.get(id));
        }
        return listOfSubTasks;
    }

    Task getTaskById(int taskId) {
        return ordinaryTasksMap.get(taskId);
    }

    Epic getEpicById(int taskID) {
        return epicsMap.get(taskID);
    }

    SubTask getSubTaskById(int taskID) {
        return subTasksMap.get(taskID);
    }

    public void addNewTask(Task task) {
        task.setTaskId(taskCounter);
        ordinaryTasksMap.put(taskCounter, task);
        taskCounter++;
    }

    public void addNewEpic(Epic task) {
        task.setTaskId(taskCounter);
        epicsMap.put(taskCounter, task);
        taskCounter++;
    }

    public void addNewSubTask(SubTask task) {
        task.setTaskId(taskCounter);
        subTasksMap.put(taskCounter, task);
        Epic currentMain = epicsMap.get(task.getMainTaskId());
        currentMain.setSubTask(taskCounter);
        currentMain.calculateStatus();
        taskCounter++;
    }

    public void refreshTask(Task task) {
        ordinaryTasksMap.put(task.getTaskId(), task);
    }

    public void refreshEpic(Epic task) {
        epicsMap.put(task.getTaskId(), task);
    }

    public void refreshSubTask(SubTask task) {
        int refreshingTaskId = task.getTaskId();
        int mainTaskId = task.getMainTaskId();
        subTasksMap.put(refreshingTaskId, task);
        Epic currentMain = epicsMap.get(mainTaskId);
        if (task.getStatus() == TaskStatus.DONE) {
            currentMain.completeSubTask(refreshingTaskId);
        }
        currentMain.calculateStatus();
    }


    void removeTaskById(int taskId) {
        ordinaryTasksMap.remove(taskId);
        if (epicsMap.keySet().contains(taskId)) {
            for (int subTaskId : epicsMap.get(taskId).subTasksId) {
                subTasksMap.remove(subTaskId);
            }
        }
        epicsMap.remove(taskId);
        if (subTasksMap.keySet().contains(taskId)) {
            int currentMainId = subTasksMap.get(taskId).getMainTaskId();
            epicsMap.get(currentMainId).removeSubTask(taskId);
            epicsMap.get(currentMainId).calculateStatus();
        }
        subTasksMap.remove(taskId);
    }

    ArrayList<SubTask> getAllSubTasks(int EpicId) {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (int subTaskId : epicsMap.get(EpicId).subTasksId) {
            subTasksList.add(subTasksMap.get(subTaskId));
        }
        return subTasksList;
    }

}
