package HistoryManager;

import TaskManager.Task;

import java.util.ArrayList;

public interface HistoryManager {
    ArrayList<Task> getDefaultHistory();

    void add(Task task) throws CloneNotSupportedException;

    void cleanHistory();
}
