package managers;

import exceptions.BusyTimeException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    static int taskCounter = 0;
    static Map<Integer, Task> ordinaryTasksMap = new HashMap<>();
    static Map<Integer, Epic> epicsMap = new HashMap<>();
    static Map<Integer, SubTask> subTasksMap = new HashMap<>();
    private InMemoryHistoryManager taskHistory = new InMemoryHistoryManager();
    static Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().get()));

    @Override
    public ArrayList<Task> getListOfTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for (int id : ordinaryTasksMap.keySet()) {
            listOfTasks.add(ordinaryTasksMap.get(id));
        }
        return listOfTasks;
    }

    @Override
    public ArrayList<Epic> getListOfEpics() {
        ArrayList<Epic> listOfEpics = new ArrayList<>();
        for (int id : epicsMap.keySet()) {
            listOfEpics.add(epicsMap.get(id));
        }
        return listOfEpics;
    }

    @Override
    public ArrayList<SubTask> getListOfSubTasks() {
        ArrayList<SubTask> listOfSubTasks = new ArrayList<>();
        for (int id : subTasksMap.keySet()) {
            listOfSubTasks.add(subTasksMap.get(id));
        }
        return listOfSubTasks;
    }

    @Override
    public Task getTaskById(int taskId) throws CloneNotSupportedException {
        taskHistory.add(ordinaryTasksMap.get(taskId));
        return ordinaryTasksMap.get(taskId);
    }

    @Override
    public Epic getEpicById(int taskId) throws CloneNotSupportedException {
        taskHistory.add(epicsMap.get(taskId));
        return epicsMap.get(taskId);

    }

    @Override
    public SubTask getSubTaskById(int taskId) throws CloneNotSupportedException {
        taskHistory.add(subTasksMap.get(taskId));
        return subTasksMap.get(taskId);
    }

    @Override
    public void addNewTask(Task task) {
        task.setTaskId(taskCounter);
        ordinaryTasksMap.put(taskCounter, task);
        setPrioritizedTasks(task);
        taskCounter++;
    }

    @Override
    public void addNewEpic(Epic task) {
        task.setTaskId(taskCounter);
        epicsMap.put(taskCounter, task);
        taskCounter++;
    }

    @Override
    public void addNewSubTask(SubTask task) {
        task.setTaskId(taskCounter);
        setPrioritizedTasks(task);
        subTasksMap.put(taskCounter, task);
        Epic currentMain = epicsMap.get(task.getMainTaskId());
        currentMain.setSubTask(task);
        taskCounter++;
    }

    @Override
    public void refreshTask(Task task) {
        ordinaryTasksMap.put(task.getTaskId(), task);
    }

    @Override
    public void refreshEpic(Epic task) {
        epicsMap.put(task.getTaskId(), task);
    }

    @Override
    public void refreshSubTask(SubTask task) {
        int refreshingTaskId = task.getTaskId();
        int mainTaskId = task.getMainTaskId();
        subTasksMap.put(refreshingTaskId, task);
        Epic currentMain = epicsMap.get(mainTaskId);
        if (task.getStatus() == TaskStatus.DONE) {
            currentMain.calculateStatus();
        }
        currentMain.calculateStatus();
    }

    @Override
    public void removeAllTasks() {
        for (int i = 0; i < taskCounter; i++) {
            removeTaskById(i);
        }
        taskCounter = 0;
        taskHistory.cleanHistory();
        prioritizedTasks.clear();
    }


    @Override
    public void removeTaskById(int taskId) {
        ordinaryTasksMap.remove(taskId);
        if (epicsMap.containsKey(taskId)) {
            for (SubTask subTask : epicsMap.get(taskId).getSubTasks()) {
                subTasksMap.remove(subTask.getTaskId());
                taskHistory.removeTask(subTask.getTaskId());
            }
        }
        epicsMap.remove(taskId);
        if (subTasksMap.containsKey(taskId)) {
            int currentMainId = subTasksMap.get(taskId).getMainTaskId();
            epicsMap.get(currentMainId).removeSubTaskById(taskId);
            epicsMap.get(currentMainId).calculateStatus();
        }
        subTasksMap.remove(taskId);
        taskHistory.removeTask(taskId);
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks(int epicId) {
        return epicsMap.get(epicId).getSubTasks();
    }

    public List<Task> getHistory() {
        return taskHistory.getDefaultHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void setPrioritizedTasks(Task task) {
        if (task.getStartTime().isPresent()) try {
            checkFreeTime(task);
            prioritizedTasks.add(task);
        } catch (BusyTimeException e) {
        }
    }

    public void checkFreeTime(Task newTask) {
        if (!prioritizedTasks.isEmpty() && newTask.getStartTime().isPresent()) {
            Optional<Task> checkStartTask = getPrioritizedTasks().stream().filter(task -> task.getStartTime().get().isAfter(newTask.getStartTime()
                    .get())).findFirst();
            Optional<Task> checkEndTask = getPrioritizedTasks().stream().filter(task -> task.getEndTime().get().isAfter(newTask.getStartTime().get()))
                    .findFirst();
            if ((checkStartTask.isPresent() &&
                    checkStartTask.get().getStartTime().get().isBefore(newTask.getEndTime().get())) ||
                    (checkEndTask.isPresent() && checkEndTask.get().getStartTime().get().isBefore(newTask.getEndTime().get()))) {
                throw new BusyTimeException("Время, отведенное под эту задачу, уже занято.");
            }
        }
    }
}



