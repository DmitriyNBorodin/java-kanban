import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import httpserver.DurationAdapter;
import httpserver.HttpTaskServer;
import httpserver.LocalDateTimeAdapter;
import httpserver.TaskListTypeToken;
import managers.ManagerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTest {
    TaskManager manager = ManagerUtils.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    Task testTask = new Task("Test1", "anything", TaskStatus.NEW,
            Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 1, 12, 00));
    Task testTask1 = new Task("Test2", "anything else", TaskStatus.NEW,
            Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 11, 00));
    Epic testEpic = new Epic("Epic", "11111", TaskStatus.NEW);
    SubTask testSub1 = new SubTask("testSub1", "123", TaskStatus.NEW, 0,
            Duration.ofMinutes(40), LocalDateTime.of(2000, 12, 1, 10, 30));
    SubTask testSub2 = new SubTask("testSub1", "1234", TaskStatus.NEW, 0,
            Duration.ofMinutes(60), LocalDateTime.of(2001, 12, 3, 11, 00));
    SubTask testSub3 = new SubTask("testSub3", "12321", TaskStatus.NEW, 0, Duration.ofMinutes(90),
            LocalDateTime.of(2002, 12, 1, 10, 00));
    Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    String baseAdress = "http://localhost:8080";
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    HttpClient client = HttpClient.newHttpClient();

    public HttpTest() throws IOException {
    }

    @BeforeEach
    void HttpTestsPreparation() {
        server.activation();
    }

    @AfterEach
    void HttpTestsComplete() {
        manager.removeAllTasks();
        server.shutDown();
    }


    @Test
    void taskHttpTest() throws IOException, InterruptedException, CloneNotSupportedException {
        HttpResponse<String> responsePost = testPostResponse("/tasks", gson.toJson(testTask));
        Assertions.assertEquals(201, responsePost.statusCode(), "Неверный код ответа для POST задачи");
        Assertions.assertNotNull(manager.getListOfTasks(), "Не удалось создать задачу");
        Assertions.assertEquals(1, manager.getListOfTasks().size(), "Создано неверное количество задач");
        HttpResponse<String> responseGet = testGetResponse("/tasks/0");
        Assertions.assertEquals(200, responseGet.statusCode(), "Неверный код ответа для GET задачи");
        Task extractingTask = gson.fromJson(responseGet.body(), Task.class);
        testTask.setTaskId(0);
        Assertions.assertEquals(testTask, extractingTask, "Не удалось получить задачу");
        HttpResponse<String> responseNull = testGetResponse("/tasks/3");
        Assertions.assertEquals(404, responseNull.statusCode(),
                "Неверный код ответа на запрос несуществующей задачи");
        HttpResponse<String> responseDelete = testDeleteResponse("/tasks/0");
        Assertions.assertEquals(200, responseDelete.statusCode(), "Неверный код удаления задачи");
        Assertions.assertNull(manager.getTaskById(0), "Не удалось удалить задачу");
    }

    @Test
    void epicHttpTest() throws IOException, InterruptedException {
        HttpResponse<String> responsePost = testPostResponse("/epics", gson.toJson(testEpic));
        Assertions.assertEquals(201, responsePost.statusCode(), "Неверный код ответа для POST эпика");
        HttpResponse<String> responsePostSub = testPostResponse("/subtasks", gson.toJson(testSub1));
        Assertions.assertEquals(201, responsePostSub.statusCode(),
                "Неверный код ответа для POST подзадачи");
        responsePostSub = testPostResponse("/subtasks", gson.toJson(testSub2));
        responsePostSub = testPostResponse("/subtasks", gson.toJson(testSub3));
        HttpResponse<String> responseToGetEpic = testGetResponse("/epics/0");
        Assertions.assertEquals(TaskStatus.NEW, gson.fromJson(responseToGetEpic.body(), Epic.class).getStatus(),
                "Неверный статус нового эпика");
        testSub1.setTaskId(1);
        testSub1.setStatus(TaskStatus.DONE);
        responsePostSub = testPostResponse("/subtasks", gson.toJson(testSub1));
        responseToGetEpic = testGetResponse("/epics/0");
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, gson.fromJson(responseToGetEpic.body(), Epic.class).getStatus(),
                "Неверный статус обновленного эпика");
        HttpResponse responseToDeleteEpic = testDeleteResponse("/epics/0");
        Assertions.assertEquals(200, responseToDeleteEpic.statusCode(), "Неверный код удаления эпика");
        responseToGetEpic = testGetResponse("/epics/0");
        Assertions.assertEquals(404, responseToGetEpic.statusCode(),
                "Неверный код для GET отсутствующего эпика");
    }

    @Test
    void historyAndPrioritizedHttpTest() throws IOException, InterruptedException {
        HttpResponse<String> responsePost = testPostResponse("/tasks", gson.toJson(testTask));
        HttpResponse<String> responsePostSameTask = testPostResponse("/tasks", gson.toJson(testTask1));
        Assertions.assertEquals(406, responsePostSameTask.statusCode(),
                "Неверный код ответа при совпадении времени");
        HttpResponse<String> responseGet = testGetResponse("/tasks/0");
        HttpResponse<String> responseGetHistory = testGetResponse("/history");
        List<Task> history = gson.fromJson(responseGetHistory.body(), new TaskListTypeToken().getType());
        Assertions.assertEquals(1, history.size(), "Неверный размер истории задач");
        HttpResponse<String> responseGetPrioritized = testGetResponse("/prioritized");
        List<Task> prioritized = gson.fromJson(responseGetPrioritized.body(), new TaskListTypeToken().getType());
        Assertions.assertEquals(1, prioritized.size(), "Неверный размер списка приоритетов задач");
    }

    public HttpResponse<String> testPostResponse(String address, String taskToPost)
            throws IOException, InterruptedException {
        HttpRequest universalPostRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskToPost))
                .uri(URI.create(baseAdress + address))
                .build();
        return client.send(universalPostRequest, handler);
    }

    public HttpResponse<String> testGetResponse(String address) throws IOException, InterruptedException {
        HttpRequest universalRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseAdress + address))
                .build();
        return client.send(universalRequest, handler);
    }

    public HttpResponse<String> testDeleteResponse(String address) throws IOException, InterruptedException {
        HttpRequest universalRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(baseAdress + address))
                .build();
        return client.send(universalRequest, handler);
    }
}
