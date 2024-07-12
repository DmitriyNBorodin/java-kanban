package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PrioritizedHandler extends GeneralRequestsHandler implements HttpHandler {
    private final Gson gson;
    private final TaskManager manager;

    public PrioritizedHandler(Gson gson, TaskManager manager) {
        this.gson = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            Optional<List<Task>> prioritizedTasks = Optional.ofNullable(manager.getPrioritizedTasks());
            if (prioritizedTasks.isPresent()) {
                responseToClient(gson.toJson(prioritizedTasks.get()), exchange);
            } else {
                notFoundResponse(gson.toJson("Приоритетные задачи отсутствуют"), exchange);
            }
        } else {
            notFoundResponse(gson.toJson("Неизвестный запрос"), exchange);
        }
    }
}
