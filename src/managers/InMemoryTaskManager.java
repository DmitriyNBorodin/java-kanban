package managers;

import exceptions.BusyTimeException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    static int taskCounter = 0;
    static Map<Integer, Task> ordinaryTasksMap = new HashMap<>();
    static Map<Integer, Epic> epicsMap = new HashMap<>();
    static Map<Integer, SubTask> subTasksMap = new HashMap<>();
    private final InMemoryHistoryManager taskHistory = new InMemoryHistoryManager();
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
        task.calculateEndTime();
        setPrioritizedTasks(task);
        ordinaryTasksMap.put(taskCounter, task);
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
        task.calculateEndTime();
        try {
            setPrioritizedTasks(task);
            subTasksMap.put(taskCounter, task);
            Epic currentMain = epicsMap.get(task.getMainTaskId());
            currentMain.setSubTask(task);
            taskCounter++;
        } catch (BusyTimeException e) {
            throw new BusyTimeException("Время уже занято");
        }
    }

    @Override
    public void refreshTask(Task task) {
        try {
            setPrioritizedTasks(task);
            ordinaryTasksMap.put(task.getTaskId(), task);
        } catch (BusyTimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void refreshEpic(Epic task) {
        epicsMap.put(task.getTaskId(), task);
    }

    @Override
    public void refreshSubTask(SubTask task) {
        try {
            setPrioritizedTasks(task);
            int mainTaskId = task.getMainTaskId();
            subTasksMap.put(task.getTaskId(), task);
            Epic currentMain = epicsMap.get(mainTaskId);
            currentMain.setSubTask(task);
        } catch (BusyTimeException e) {
            System.out.println(e.getMessage());
        }
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
    public boolean removeTaskById(int taskId) {
        boolean eliminated = false;
        if (ordinaryTasksMap.containsKey(taskId)) {
            prioritizedTasks.remove(ordinaryTasksMap.get(taskId));
            ordinaryTasksMap.remove(taskId);
            eliminated = true;
        }
        if (epicsMap.containsKey(taskId)) {
            for (SubTask subTask : epicsMap.get(taskId).getSubTasks()) {
                subTasksMap.remove(subTask.getTaskId());
                taskHistory.removeTask(subTask.getTaskId());
            }
            epicsMap.remove(taskId);
            eliminated = true;
        }
        if (subTasksMap.containsKey(taskId)) {
            prioritizedTasks.remove(subTasksMap.get(taskId));
            int currentMainId = subTasksMap.get(taskId).getMainTaskId();
            epicsMap.get(currentMainId).removeSubTaskById(taskId);
            epicsMap.get(currentMainId).calculateStatus();
            epicsMap.get(currentMainId).calculateDuration();
            subTasksMap.remove(taskId);
            eliminated = true;
        }
        taskHistory.removeTask(taskId);
        return eliminated;
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
            prioritizedTasks.remove(task);
            checkFreeTime(task);
            prioritizedTasks.add(task);
        } catch (BusyTimeException e) {
            throw e;
        }
    }

    public void checkFreeTime(Task newTask) {
        if (!prioritizedTasks.isEmpty() && newTask.getStartTime().isPresent()) {
            Optional<Task> checkStartTask = getPrioritizedTasks().stream().filter(task -> task.getStartTime().isPresent())
                    .filter(task -> task.getStartTime().get().isAfter(newTask.getStartTime().get())).findFirst();
            Optional<Task> checkEndTask = getPrioritizedTasks().stream().filter(task -> task.getEndTime().isPresent())
                    .filter(task -> task.getEndTime().get().isAfter(newTask.getStartTime().get())).findFirst();
            if ((checkStartTask.isPresent() &&
                    checkStartTask.get().getStartTime().get().isBefore(newTask.getEndTime().get())) ||
                    (checkEndTask.isPresent() && checkEndTask.get().getStartTime().get().isBefore(newTask.getEndTime().get()))) {
                throw new BusyTimeException("Время, отведенное под эту задачу, уже занято.");
            }
        }
    }
}



