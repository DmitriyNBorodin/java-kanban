package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getListOfTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<SubTask> getListOfSubTasks();

    Task getTaskById(int taskId) throws CloneNotSupportedException;

    Epic getEpicById(int taskID) throws CloneNotSupportedException;

    SubTask getSubTaskById(int taskID) throws CloneNotSupportedException;

    void addNewTask(Task task);

    void addNewEpic(Epic task);

    void addNewSubTask(SubTask task);

    void refreshTask(Task task);

    void refreshEpic(Epic task);

    void refreshSubTask(SubTask task);

    void removeTaskById(int taskId);

    void removeAllTasks();

    ArrayList<SubTask> getAllSubTasks(int EpicId);

    List<Task> getHistory();
}
