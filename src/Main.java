import TaskManager.*;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Поехали!");

        Task something = new Task ("новая", "странная задача", TaskStatus.NEW);
        Task anything = new Task("ещё", "одна задача", TaskStatus.NEW);
        TaskManager manager = (InMemoryTaskManager) ManagerUtils.getDefault();
        manager.addNewTask(something);
        manager.addNewTask(anything);
        Epic epic1 =  new Epic("first", "epic task", TaskStatus.NEW);
        manager.addNewEpic(epic1);
        SubTask sub11 =  new SubTask("first-first", "anything here", TaskStatus.NEW, 2);
        SubTask sub12 = new SubTask("first-second", "nothing here", TaskStatus.NEW, 2);
        SubTask sub13 = new SubTask("first-third", "nothing at all here", TaskStatus.NEW, 2);
        manager.addNewSubTask(sub11);
        manager.addNewSubTask(sub12);
        sub11.setStatus(TaskStatus.DONE);
        manager.refreshSubTask(sub11);
        sub12.setStatus(TaskStatus.DONE);
        manager.refreshSubTask(sub12);
        manager.addNewSubTask(sub13);
        something.setStatus(TaskStatus.DONE);
        manager.getTaskById(0);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(0);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(0);
        manager.getTaskById(0);
        manager.getTaskById(1);

        manager.refreshTask(something);
        manager.removeTaskById(5);
        manager.removeTaskById(1);

        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpics());
        System.out.println(manager.getAllSubTasks(2));
        System.out.println(manager.getHistory());

    }
}
