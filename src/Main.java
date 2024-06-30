import managers.InMemoryTaskManager;
import managers.ManagerUtils;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = ManagerUtils.getDefault();
        Task test1 = new Task("name1", "anything", TaskStatus.NEW, Duration.ofMinutes(30));
        Task test2 = new Task("name2", "nothing", TaskStatus.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 6,20, 10, 30));
        Task test3 = new Task("name3", "description", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2024, 6, 20, 10, 20));
        manager.addNewTask(test1);
        manager.addNewTask(test2);
        manager.addNewTask(test3);
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getPrioritizedTasks());
        Epic testEpic = new Epic("Epic", "11111", TaskStatus.NEW);
        SubTask testSub1 = new SubTask("testSub1", "123", TaskStatus.NEW, 3,
                Duration.ofMinutes(40), LocalDateTime.of(2000, 12,1, 10, 30));
        SubTask testSub2 = new SubTask("testSub1", "1234", TaskStatus.DONE, 3,
                Duration.ofMinutes(60), LocalDateTime.of(2000, 12, 3, 10, 00));
        SubTask testSub3 = new SubTask("testSub3", "12321", TaskStatus.NEW, 3, Duration.ofMinutes(60),
                LocalDateTime.of(2010, 12, 1, 10, 00));
        manager.addNewEpic(testEpic);
        manager.addNewSubTask(testSub1);
        manager.addNewSubTask(testSub2);
        manager.addNewSubTask(testSub3);
        System.out.println(manager.getListOfEpics());
        System.out.println(manager.getListOfSubTasks());
        System.out.println(manager.getPrioritizedTasks());
    }
}
