package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.BusyTimeException;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler extends GeneralRequestsHandler implements HttpHandler {
    private final Gson gson;
    private final TaskManager manager;

    public TasksHandler(Gson gson, TaskManager manager) {
        this.gson = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                try {
                    String taskId = exchange.getRequestURI().getPath().split("/")[2];
                    Optional<Task> requiredTask = Optional.ofNullable(manager.getTaskById(Integer.parseInt(taskId)));
                    if (requiredTask.isPresent()) {
                        String response = gson.toJson(requiredTask.get());
                        responseToClient(response, exchange);
                    } else {
                        NotFoundException taskNotFound = new NotFoundException("Задача с таким ID отсутствует");
                        notFoundResponse(gson.toJson(taskNotFound.getMessage()), exchange);
                    }
                } catch (IndexOutOfBoundsException e) {
                    String response = gson.toJson(manager.getListOfTasks());
                    responseToClient(response, exchange);
                } catch (CloneNotSupportedException | NumberFormatException e) {
                    notFoundResponse(gson.toJson("Не удалось определить задачу"), exchange);
                }
                break;
            case "POST":
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task postingTask = gson.fromJson(requestBody, Task.class);
                Optional<Integer> postingTaskId = Optional.ofNullable(postingTask.getTaskId());
                try {
                    if (postingTaskId.isEmpty()) {
                        manager.addNewTask(postingTask);
                    } else {
                        manager.refreshTask(postingTask);
                    }
                    responseOfSuccess(exchange);
                } catch (BusyTimeException e) {
                    interactionResponse(gson.toJson(e.getMessage()), exchange);
                }
                break;
            case "DELETE":
                try {
                    String deletingTaskId = exchange.getRequestURI().getPath().split("/")[2];
                    boolean taskDeleteProcess = manager.removeTaskById(Integer.parseInt(deletingTaskId));
                    if (taskDeleteProcess) {
                        responseToClient(gson.toJson("Задача удалена"), exchange);
                    } else {
                        NotFoundException notFound = new NotFoundException("Такой задачи нет");
                        notFoundResponse(gson.toJson(notFound.getMessage()), exchange);
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    notFoundResponse(gson.toJson("Не удалось определить задачу"), exchange);
                }
                break;
            default:
                notFoundResponse(gson.toJson("Неизвестный запрос"), exchange);
        }
    }
}
