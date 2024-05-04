package HistoryManager;

import HistoryManager.HistoryManager;
import TaskManager.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> inMemoryHistory =  new ArrayList<>();
    @Override
    public ArrayList<Task> getDefaultHistory() {
        return inMemoryHistory;
    }

    @Override
    public void add(Task task) throws CloneNotSupportedException {
        if(task != null) {
            if (inMemoryHistory.size() >= 10) {
                inMemoryHistory.removeFirst();
            }
            Task clonedTask = task.clone();
            inMemoryHistory.add(clonedTask);
        }
    }

    @Override
    public void cleanHistory() {
        inMemoryHistory = new ArrayList<>();
    }
}
