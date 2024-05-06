package Managers;

import Tasks.Task;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> inMemoryHistory = new ArrayList<>();

    @Override
    public List<Task> getDefaultHistory() {
        return inMemoryHistory;
    }

    @Override
    public void add(Task task) throws CloneNotSupportedException {
        if (task != null) {
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
