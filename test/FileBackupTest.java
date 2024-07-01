import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackupTest {
    @Test
    void fileCreationTest() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager("CreationTestDataBase.txt");
        manager.loadFromFile();
        Assertions.assertTrue(Files.exists(Path.of("CreationTestDataBase.txt")), "Не удалось создать файл");
    }

    @Test
    void wrightingAndReadingTest() throws IOException, CloneNotSupportedException {
        FileBackedTaskManager writeTestManager = new FileBackedTaskManager("TestDataBase.txt");
        writeTestManager.loadFromFile();
        Task test1 = new Task("name1", "description1", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 1, 12, 00));
        Task test2 = new Task("name2", "description2", TaskStatus.NEW,
                Duration.ofMinutes(30));
        Task test3 = new Task("name3", "description3", TaskStatus.NEW,
                Duration.ofMinutes(40));
        writeTestManager.addNewTask(test1);
        writeTestManager.addNewTask(test2);
        writeTestManager.addNewTask(test3);
        FileBackedTaskManager readTestManager = new FileBackedTaskManager("TestDataBase.txt");
        readTestManager.loadFromFile();
        Assertions.assertEquals(readTestManager.getListOfTasks().size(), 3, "Не удалось загрузить задачи");
        Assertions.assertEquals(Duration.ofMinutes(30), readTestManager.getTaskById(0).getDuration(),
                "Не удалось восстановить продолжительность задачи");
        Assertions.assertEquals(LocalDateTime.of(2000, 1, 1, 12, 00),
                readTestManager.getTaskById(0).getStartTime().get(), "Не удалось восстановить время начала задачи");
        writeTestManager.removeAllTasks();
        readTestManager.removeAllTasks();
    }

    @AfterAll
    static void removeTestFiles() throws IOException {
        Files.deleteIfExists(Path.of("CreationTestDataBase.txt"));
        Files.deleteIfExists(Path.of("TestDataBase.txt"));
    }
}
