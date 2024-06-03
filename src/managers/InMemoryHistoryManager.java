package managers;

import tasks.Task;
import tasks.TaskNode;

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
        List<Task> history = new ArrayList<>();
        if (headNode != null) for (TaskNode x = headNode; x != null; x = x.getNextNode()) {
            history.add(x.getTaskInHistory());
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
                tailNode.setNextNode(newNode);
                tailNode = newNode;
            }
            inMemoryHistory.put(clonedTask.getTaskId(), newNode);
        }
    }

    @Override
    public void cleanHistory() {
        inMemoryHistory.clear();
        headNode = tailNode = null;
    }

    @Override
    public void removeTask(int id) {
        removeNode(id);
        inMemoryHistory.remove(id);
    }

    private void removeNode(int id) {
        if (inMemoryHistory.containsKey(id)) {
            TaskNode vanishingNode = inMemoryHistory.get(id);
            if (vanishingNode.equals(headNode)) {
                headNode = headNode.getNextNode();
                if (headNode != null) {
                    headNode.setPreviousNode(null);
                }
            } else if (vanishingNode.equals(tailNode)) {
                tailNode = tailNode.getPreviousNode();
                if (tailNode != null) {
                    tailNode.setNextNode(null);
                }
            } else {
                vanishingNode.getPreviousNode().setNextNode(vanishingNode.getNextNode());
                vanishingNode.getNextNode().setPreviousNode(vanishingNode.getPreviousNode());
            }
        }
    }
}

