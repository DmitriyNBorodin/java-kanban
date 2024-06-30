import managers.ManagerUtils;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class HistoryTest {
    Task testTask = new Task("Test1", "anything", TaskStatus.NEW,
            Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 2, 12, 00));
    Epic testEpic = new Epic("Epic1", "anything2", TaskStatus.NEW);
    SubTask testSub1 = new SubTask("Sub1", "nothing", TaskStatus.NEW, 0,
            Duration.ofMinutes(30));
    SubTask testSub2 = new SubTask("Sub2", "nothing2", TaskStatus.NEW, 0,
            Duration.ofMinutes(30));
    SubTask testSub3 = new SubTask("Sub3", "nothing3", TaskStatus.NEW, 0,
            Duration.ofMinutes(30));
    static TaskManager historyManager;

    @BeforeAll
    static void taskManagerCreationTest() {
        historyManager = ManagerUtils.getDefault();
        Assertions.assertNotNull(historyManager, "Не удалось создать объект TaskManager");
    }


    @AfterEach
    public void prepareForTest() {
        historyManager.removeAllTasks();
    }

    @Test
    void historyManagerTest() throws CloneNotSupportedException {
        historyManager.addNewTask(testTask);
        historyManager.addNewEpic(testEpic);
        historyManager.getTaskById(0);
        historyManager.getEpicById(1);
        Assertions.assertNotNull(historyManager.getHistory(), "Не удалось получить историю просмотров");
        historyManager.getTaskById(0);
        historyManager.getTaskById(0);
        Assertions.assertEquals(2, historyManager.getHistory().size(),
                "Задача дублируется в истории");
        historyManager.removeTaskById(0);
        Assertions.assertEquals(1, historyManager.getHistory().size(), "Задача не удалилась из истории");
        historyManager.removeAllTasks();
        Assertions.assertEquals(0, historyManager.getHistory().size(), "Cписок задач не пустой");
    }

    @Test
    void subTaskDeletionTest() throws CloneNotSupportedException {
        historyManager.addNewEpic(testEpic);
        historyManager.addNewSubTask(testSub1);
        historyManager.addNewSubTask(testSub2);
        historyManager.addNewSubTask(testSub3);
        historyManager.getEpicById(0);
        historyManager.getSubTaskById(1);
        historyManager.getSubTaskById(2);
        historyManager.getSubTaskById(3);
        Assertions.assertEquals(4, historyManager.getHistory().size(), "Некорректный размер истории задач");
        historyManager.removeTaskById(0);
        Assertions.assertEquals(0, historyManager.getHistory().size(),
                "Подзадачи не удалились вместе с эпиком");
    }
}
