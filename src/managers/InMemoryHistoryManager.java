package managers;

import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private TaskNode headNode;
    private Map<Integer, TaskNode> inMemoryHistory = new HashMap<>();
    private TaskNode tailNode;

    @Override
    public List<Task> getDefaultHistory() {
        ArrayList<Task> history = new ArrayList<>();
        if (headNode != null) {
            TaskNode currentNode = headNode;
            while (currentNode != null) {
                history.add(currentNode.taskInHistory);
                currentNode = currentNode.nextNode;
            }
        }
        return history;
    }

    @Override
    public void add(Task task) throws CloneNotSupportedException {
        if (task != null) {
            removeNode(task.getTaskId());
            Task clonedTask = task.clone();
            TaskNode newNode;
            if (inMemoryHistory.isEmpty()) {
                newNode = new TaskNode(clonedTask, null, null);
                headNode = tailNode = newNode;
            } else {
                newNode = new TaskNode(clonedTask, tailNode, null);
                tailNode.nextNode = newNode;
                tailNode = newNode;
            }
            inMemoryHistory.put(clonedTask.getTaskId(), newNode);
        }
    }

    @Override
    public void cleanHistory() {
        inMemoryHistory = new HashMap<>();
        headNode = tailNode = null;
    }

    @Override
    public void removeTask(int id) {
        removeNode(id);
        inMemoryHistory.remove(id);
    }

    void removeNode(int id) {
        if (inMemoryHistory.containsKey(id)) {
            TaskNode vanishingNode = inMemoryHistory.get(id);
            if (vanishingNode.equals(headNode)) {
                headNode = headNode.nextNode;
                if (headNode != null) {
                    headNode.previousNode = null;
                }
            } else if (vanishingNode.equals(tailNode)) {
                tailNode = tailNode.previousNode;
                if (tailNode != null) {
                    tailNode.nextNode = null;
                }
            } else {
                vanishingNode.previousNode.nextNode = vanishingNode.nextNode;
                vanishingNode.nextNode.previousNode = vanishingNode.previousNode;
            }
        }
    }
}

