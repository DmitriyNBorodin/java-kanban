package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Task;
import tasks.SubTask;
import tasks.TaskStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static String dataBase;

    public FileBackedTaskManager(String dataBase) {
        this.dataBase = dataBase;
    }

    public void save() throws ManagerSaveException {
        try (Writer taskWriter = new BufferedWriter(new FileWriter(dataBase))) {
            for (int i = 0; i < taskCounter; i++) {
                if (ordinaryTasksMap.containsKey(i)) {
                    taskWriter.write(ordinaryTasksMap.get(i).toString());
                } else if (epicsMap.containsKey(i)) {
                    taskWriter.write(epicsMap.get(i).toString());
                } else if (subTasksMap.containsKey(i)) {
                    taskWriter.write(subTasksMap.get(i).toString());
                }
                taskWriter.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Не удалось сохранить задачу");
        }
    }

    public static void loadFromFile() throws IOException {
        try (BufferedReader uploadTasks = new BufferedReader(new FileReader(dataBase))) {
            while (uploadTasks.ready()) {
                String[] currentTask = uploadTasks.readLine().split(",");
                switch (currentTask[0]) {
                    case "Task":
                        Task uploadingTask = new Task(currentTask[2], currentTask[3], TaskStatus.valueOf(currentTask[4]),
                                Duration.parse(currentTask[5]));
                        try {
                            uploadingTask.setStartTime(LocalDateTime.parse(currentTask[6]));
                            uploadingTask.calculateEndTime();
                        } catch (Exception e) {
                            uploadingTask.setStartTime(null);
                        }
                        int uploadingTaskId = Integer.parseInt(currentTask[1]);
                        uploadingTask.setTaskId(uploadingTaskId);
                        ordinaryTasksMap.put(uploadingTaskId, uploadingTask);
                        taskCounter = ++uploadingTaskId;
                        break;
                    case "Epic":
                        Epic uploadingEpic = new Epic(currentTask[2], currentTask[3], TaskStatus.valueOf(currentTask[4]));
                        int uploadingEpicId = Integer.parseInt(currentTask[1]);
                        uploadingEpic.setTaskId(uploadingEpicId);
                        epicsMap.put(uploadingEpicId, uploadingEpic);
                        taskCounter = ++uploadingEpicId;
                        break;
                    case "Sub":
                        SubTask uploadingSub = new SubTask(currentTask[2], currentTask[3],
                                TaskStatus.valueOf(currentTask[4]), Integer.parseInt(currentTask[5]),
                                Duration.parse(currentTask[6]));
                        try {
                            uploadingSub.setStartTime(LocalDateTime.parse(currentTask[7]));
                            uploadingSub.calculateEndTime();
                        } catch (Exception e) {
                            uploadingSub.setStartTime(null);
                        }
                        int uploadingSubId = Integer.parseInt(currentTask[1]);
                        uploadingSub.setTaskId(uploadingSubId);
                        subTasksMap.put(uploadingSubId, uploadingSub);
                        Epic currentMain = epicsMap.get(uploadingSub.getMainTaskId());
                        currentMain.setSubTask(uploadingSub);
                        taskCounter = ++uploadingSubId;
                }
            }
        } catch (FileNotFoundException ex) {
            Files.createFile(Path.of(dataBase));
        } catch (IOException ex) {
            throw new ManagerSaveException("Не удалось загрузить задачи");
        }
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
        save();
    }

    @Override
    public void refreshTask(Task task) {
        super.refreshTask(task);
        save();
    }

    @Override
    public void refreshEpic(Epic epic) {
        super.refreshEpic(epic);
        save();
    }

    @Override
    public void refreshSubTask(SubTask subTask) {
        super.refreshSubTask(subTask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }
}
