package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.BusyTimeException;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtasksHandler extends GeneralRequestsHandler implements HttpHandler {
    private final Gson gson;
    private final TaskManager manager;

    public SubtasksHandler(Gson gson, TaskManager manager) {
        this.gson = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                try {
                    String subtaskId = exchange.getRequestURI().getPath().split("/")[2];
                    Optional<Task> requiredSubtask = Optional.ofNullable(manager.getSubTaskById(Integer.parseInt(subtaskId)));
                    if (requiredSubtask.isPresent()) {
                        String response = gson.toJson(requiredSubtask.get());
                        responseToClient(response, exchange);
                    } else {
                        NotFoundException taskNotFound = new NotFoundException("Подзадача с таким ID отсутствует");
                        notFoundResponse(taskNotFound.getMessage(), exchange);
                    }
                } catch (IndexOutOfBoundsException e) {
                    String response = gson.toJson(manager.getListOfSubTasks());
                    responseToClient(response, exchange);
                } catch (CloneNotSupportedException | NumberFormatException e) {
                    notFoundResponse(gson.toJson("Не удалось определить подзадачу"), exchange);
                }
                break;
            case "POST":
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                SubTask postingSubTask = gson.fromJson(requestBody, SubTask.class);
                Optional<Integer> postingSubTaskId = Optional.ofNullable(postingSubTask.getTaskId());
                try {
                    if (postingSubTaskId.isEmpty()) {
                        manager.addNewSubTask(postingSubTask);
                    } else {
                        manager.refreshSubTask(postingSubTask);
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
                        responseToClient(gson.toJson("Подзадача удалена"), exchange);
                    } else {
                        NotFoundException notFound = new NotFoundException("Такой подзадачи нет");
                        notFoundResponse(gson.toJson(notFound.getMessage()), exchange);
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    notFoundResponse(gson.toJson("Не удалось определить подзадачу"), exchange);
                }
                break;
            default:
                notFoundResponse(gson.toJson("Неизвестный запрос"), exchange);
        }
    }
}
