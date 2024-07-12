package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicsHandler extends GeneralRequestsHandler implements HttpHandler {
    private final Gson gson;
    private final TaskManager manager;

    public EpicsHandler(Gson gson, TaskManager manager) {
        this.gson = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                String[] requestArray = exchange.getRequestURI().getPath().split("/");
                if (requestArray.length == 2) {
                    String response = gson.toJson(manager.getListOfEpics());
                    responseToClient(response, exchange);
                } else {
                    try {
                        int requiredEpicId = Integer.parseInt(requestArray[2]);
                        Optional<Epic> requiredEpic = Optional.ofNullable(manager.getEpicById(requiredEpicId));
                        if (requiredEpic.isPresent()) {
                            if (requestArray.length == 3) {
                                responseToClient(gson.toJson(requiredEpic.get()), exchange);
                            } else if (requestArray.length == 4 && requestArray[3].equals("subtasks")) {
                                responseToClient(gson.toJson(requiredEpic.get().getSubTasks()), exchange);
                            }
                        } else {
                            NotFoundException notFound = new NotFoundException("Некорректный запрос эпика");
                            notFoundResponse(notFound.getMessage(), exchange);
                        }
                    } catch (NumberFormatException | CloneNotSupportedException e) {
                        notFoundResponse(gson.toJson("Не удалось определить задачу"), exchange);
                    }
                }
                break;
            case "POST":
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic postingEpic = gson.fromJson(requestBody, Epic.class);
                manager.addNewEpic(postingEpic);
                responseOfSuccess(exchange);
                break;
            case "DELETE": {
                try {
                    String deletingEpicId = exchange.getRequestURI().getPath().split("/")[2];
                    if (manager.removeTaskById(Integer.parseInt(deletingEpicId))) {
                        responseToClient(gson.toJson("Эпик удален"), exchange);
                    } else {
                        NotFoundException notFound = new NotFoundException("Такого эпика нет");
                        notFoundResponse(notFound.getMessage(), exchange);
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    notFoundResponse(gson.toJson("Не удалось определить задачу"), exchange);
                }
                break;
            }
            default:
                notFoundResponse(gson.toJson("Неизвестный запрос"), exchange);
        }
    }
}
