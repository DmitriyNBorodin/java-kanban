package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HistoryHandler extends GeneralRequestsHandler implements HttpHandler {
    private final Gson gson;
    private final TaskManager manager;

    public HistoryHandler(Gson gson, TaskManager manager) {
        this.gson = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            Optional<List<Task>> tasksHistory = Optional.ofNullable(manager.getHistory());
            if (tasksHistory.isPresent()) {
                responseToClient(gson.toJson(tasksHistory.get()), exchange);
            } else {
                notFoundResponse(gson.toJson("История отсутствует"), exchange);
            }
        } else {
            notFoundResponse(gson.toJson("Неизвестный запрос"), exchange);
        }
    }
}
