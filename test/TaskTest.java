import exceptions.BusyTimeException;
import managers.ManagerUtils;
import managers.TaskManager;
import tasks.*;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    Task testTask = new Task("Test1", "anything", TaskStatus.NEW,
            Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 1, 12, 00));
    Epic testEpic = new Epic("Epic", "11111", TaskStatus.NEW);
    SubTask testSub1 = new SubTask("testSub1", "123", TaskStatus.NEW, 0,
            Duration.ofMinutes(40), LocalDateTime.of(2000, 12, 1, 10, 30));
    SubTask testSub2 = new SubTask("testSub1", "1234", TaskStatus.NEW, 0,
            Duration.ofMinutes(60), LocalDateTime.of(2001, 12, 3, 11, 00));
    SubTask testSub3 = new SubTask("testSub3", "12321", TaskStatus.NEW, 0, Duration.ofMinutes(90),
            LocalDateTime.of(2002, 12, 1, 10, 00));
    static TaskManager manager;


    @BeforeAll
    static void taskManagerCreationTest() {
        manager = ManagerUtils.getDefault();
        Assertions.assertNotNull(manager, "Не удалось создать объект TaskManager");
    }

    @AfterEach
    void removeTasks() {
        manager.removeAllTasks();
    }

    @Test
    void tasksTest() throws CloneNotSupportedException {
        manager.addNewTask(testTask);
        Task controlTask = new Task("control1", "controlText", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 2, 12, 00));
        Assertions.assertTrue(manager.getTaskById(0).getEndTime().isPresent(),
                "Не удалось рассчитать время завершения задачи");
        controlTask.setTaskId(0);
        Assertions.assertNotNull(manager.getTaskById(0), "Не удалось извлечь задачу");
        Assertions.assertEquals(manager.getTaskById(0), controlTask,
                "Задачи с одинаковым ID считаются разными");
        Assertions.assertNotNull(manager.getListOfTasks(), "Не удается получить список задач");
        Assertions.assertEquals(1, manager.getListOfTasks().size(),
                "Некорректное количество задач в списке");
        Assertions.assertEquals(manager.getTaskById(0).getTaskName(), testTask.getTaskName(),
                "При добавлении задачи изменилось её название");
        Assertions.assertEquals(manager.getTaskById(0).getTaskDescription(), testTask.getTaskDescription(),
                "При добавлении задачи изменилось её описание");
        Assertions.assertEquals(manager.getTaskById(0).getStatus(), testTask.getStatus(),
                "При добавлении задачи изменился её статус");
        Task timeTestTask = new Task("Test1", "anything", TaskStatus.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2000, 1, 1, 12, 15));
        Assertions.assertThrows(BusyTimeException.class, () -> manager.checkFreeTime(timeTestTask),
                "Пересечение времени должно приводить к исключению");
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
        manager.addNewSubTask(testSub3);
        SubTask controlSub = new SubTask("Sub1", "nothing", TaskStatus.NEW, 0,
                Duration.ofMinutes(30), LocalDateTime.of(2010, 1, 2, 12, 00));
        controlSub.setTaskId(2);
        Assertions.assertEquals(manager.getSubTaskById(2), controlSub,
                "Подзадачи с одинаковым ID сичтаются разными");
        Assertions.assertEquals(TaskStatus.NEW, manager.getEpicById(0).getStatus(),
                "Неверно рассчитывается статус эпика");
        testSub1.setStatus(TaskStatus.DONE);
        manager.refreshSubTask(testSub1);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(0).getStatus(),
                "Неверно рассчитывается изменение статуса эпика");
        testSub2.setStatus(TaskStatus.DONE);
        testSub3.setStatus(TaskStatus.DONE);
        manager.refreshSubTask(testSub2);
        manager.refreshSubTask(testSub3);
        Assertions.assertEquals(TaskStatus.DONE, manager.getEpicById(0).getStatus(),
                "Неверно рассчитано завершение эпика");
        manager.removeTaskById(1);
        Assertions.assertNull(manager.getSubTaskById(1), "Не удалось удалить подзадачу");
        manager.removeTaskById(0);
        Assertions.assertNull(manager.getEpicById(0), "Не удалось удалить эпик");
    }
}

