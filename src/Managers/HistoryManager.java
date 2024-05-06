package Managers;

import Tasks.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getDefaultHistory();

    void add(Task task) throws CloneNotSupportedException;

    void cleanHistory();
}
