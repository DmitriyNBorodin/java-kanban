package managers;
import tasks.Task;

public class TaskNode {
    Task taskInHistory;
    TaskNode previousNode;
    TaskNode nextNode;
    public TaskNode(Task taskInHistory, TaskNode previousNode, TaskNode nextNode) {
        this.taskInHistory = taskInHistory;
        this.previousNode = previousNode;
        this.nextNode = nextNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        TaskNode comparingNode = (TaskNode) obj;
        return this.taskInHistory.equals(comparingNode.taskInHistory);
    }
}
