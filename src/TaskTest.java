import TaskManager.*;
import org.junit.jupiter.api.*;

class TaskManagerTest {
    Task testTask = new Task("Test1", "anything", TaskStatus.NEW);
    Epic testEpic = new Epic("Epic1", "anything2", TaskStatus.NEW);
    SubTask testSub1 = new SubTask("Sub1", "nothing", TaskStatus.NEW, 0);
    SubTask testSub2 = new SubTask("Sub2", "nothing2", TaskStatus.NEW, 0);
    static TaskManager manager;

    @BeforeAll
    static void taskManagerCreationTest() {
        manager = (InMemoryTaskManager) ManagerUtils.getDefault();
        Assertions.assertNotNull(manager, "Не удалось создать объект TaskManager");
    }


    @AfterEach
    public void prepareForTest() {
        manager.removeAllTasks();
    }

    @Test
    void tasksTest() throws CloneNotSupportedException {
        manager.addNewTask(testTask);
        Task controlTask = new Task("control1", "controlText", TaskStatus.NEW);
        controlTask.setTaskId(0);
        Assertions.assertNotNull(manager.getTaskById(0), "Не удалось извлечь задачу");
        Assertions.assertEquals(manager.getTaskById(0), controlTask,
                "Задачи с одинаковым ID считаются разными");
        Assertions.assertNotNull(manager.getListOfTasks(), "Не удается получить список задач");
        Assertions.assertEquals(manager.getListOfTasks().size(), 1,
                "Некорректное количество задач в списке");
        Assertions.assertEquals(manager.getTaskById(0).getTaskName(), testTask.getTaskName(),
                "При добавлении задачи изменилось её название");
        Assertions.assertEquals(manager.getTaskById(0).getTaskDescription(), testTask.getTaskDescription(),
                "При добавлении задачи изменилось её описание");
        Assertions.assertEquals(manager.getTaskById(0).getStatus(), testTask.getStatus(),
                "При добавлении задачи изменился её статус");
        manager.removeTaskById(0);
        Assertions.assertNull(manager.getTaskById(0), "Не удалось удалить задачу");
    }

    @Test
    void epicsTest() throws CloneNotSupportedException {
        manager.addNewEpic(testEpic);
        Assertions.assertNotNull(manager.getEpicById(0), "Не удалось извлечь эпик");
        Assertions.assertNotNull(manager.getListOfEpics(), "Не удалось получить список эпиков");
        Epic controlEpic = new Epic("controlEpic", "controlEpicText", TaskStatus.NEW);
        controlEpic.setTaskId(0);
        Assertions.assertEquals(manager.getEpicById(0), controlEpic,
                "Эпики с одинаковым ID считаются разными");
        manager.addNewSubTask(testSub1);
        manager.addNewSubTask(testSub2);
        SubTask controlSub = new SubTask("Sub1", "nothing", TaskStatus.NEW, 0);
        controlSub.setTaskId(1);
        Assertions.assertEquals(manager.getSubTaskById(1), controlSub,
                "Подзадачи с одинаковым ID сичтаются разными");
        testSub1.setStatus(TaskStatus.DONE);
        manager.refreshSubTask(testSub1);
        Assertions.assertEquals(manager.getEpicById(0).getStatus(), TaskStatus.IN_PROGRESS,
                "Неверно рассчитывается изменение статуса эпика");
        manager.removeTaskById(1);
        Assertions.assertNull(manager.getSubTaskById(1), "Не удалось удалить подзадачу");
        manager.removeTaskById(0);
        Assertions.assertNull(manager.getEpicById(0), "Не удалось удалить эпик");
    }

    @Test
    void historyManagerTest() throws CloneNotSupportedException {
        manager.addNewTask(testTask);
        manager.getTaskById(0);
        Assertions.assertNotNull(manager.getHistory(), "Не удалось получить историю просмотров");
        manager.removeTaskById(0);
        Assertions.assertEquals(manager.getHistory().get(0).getTaskName(), testTask.getTaskName(),
                "Задача не сохранилась в истории");
    }
}