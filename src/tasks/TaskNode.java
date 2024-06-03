package tasks;

public class TaskNode {

    private final Task taskInHistory;
    private TaskNode previousNode;
    private TaskNode nextNode;

    public TaskNode(Task taskInHistory, TaskNode previousNode, TaskNode nextNode) {
        this.taskInHistory = taskInHistory;
        this.previousNode = previousNode;
        this.nextNode = nextNode;
    }

    public Task getTaskInHistory() {
        return taskInHistory;
    }

    public TaskNode getPreviousNode() {
        return previousNode;
    }

    public TaskNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(TaskNode nextNode) {
        this.nextNode = nextNode;
    }

    public void setPreviousNode(TaskNode previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        TaskNode comparingNode = (TaskNode) obj;
        return this.taskInHistory.equals(comparingNode.taskInHistory);
    }
}
