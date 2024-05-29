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

class HistoryTest {
    Task testTask = new Task("Test1", "anything", TaskStatus.NEW);
    Epic testEpic = new Epic("Epic1", "anything2", TaskStatus.NEW);
    SubTask testSub1 = new SubTask("Sub1", "nothing", TaskStatus.NEW, 0);
    SubTask testSub2 = new SubTask("Sub2", "nothing2", TaskStatus.NEW, 0);
    SubTask testSub3 = new SubTask("Sub3", "nothing3", TaskStatus.NEW, 0);
    static TaskManager manager;

    @BeforeAll
    static void taskManagerCreationTest() {
        manager = ManagerUtils.getDefault();
        Assertions.assertNotNull(manager, "Не удалось создать объект TaskManager");
    }


    @AfterEach
    public void prepareForTest() {
        manager.removeAllTasks();
    }

    @Test
    void historyManagerTest() throws CloneNotSupportedException {
        manager.addNewTask(testTask);
        manager.addNewEpic(testEpic);
        manager.getTaskById(0);
        manager.getEpicById(1);
        Assertions.assertNotNull(manager.getHistory(), "Не удалось получить историю просмотров");
        manager.getTaskById(0);
        manager.getTaskById(0);
        Assertions.assertEquals(2, manager.getHistory().size(),
                "Задача дублируется в истории");
        manager.removeTaskById(0);
        Assertions.assertEquals(1, manager.getHistory().size(), "Задача не удалилась из истории");
    }

    @Test
    void subTaskDeletionTest() throws CloneNotSupportedException {
        manager.addNewEpic(testEpic);
        manager.addNewSubTask(testSub1);
        manager.addNewSubTask(testSub2);
        manager.addNewSubTask(testSub3);
        manager.getEpicById(0);
        manager.getSubTaskById(1);
        manager.getSubTaskById(2);
        manager.getSubTaskById(3);
        Assertions.assertEquals(4, manager.getHistory().size(), "Некорректный размер истории задач");
        manager.removeTaskById(0);
        Assertions.assertEquals(0, manager.getHistory().size(),
                "Подзадачи не удалились вместе с эпиком");
    }
}
