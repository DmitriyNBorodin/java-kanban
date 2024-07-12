package httpserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.ManagerUtils;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    Gson gson;
    TaskManager manager;
    HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TasksHandler(gson, manager));
        server.createContext("/subtasks", new SubtasksHandler(gson, manager));
        server.createContext("/epics", new EpicsHandler(gson, manager));
        server.createContext("/history", new HistoryHandler(gson, manager));
        server.createContext("/prioritized", new PrioritizedHandler(gson, manager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = ManagerUtils.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.activation();
        System.out.println("HTTP-сервер запущен");
    }

    public void activation() {
        server.start();
    }

    public void shutDown() {
        server.stop(0);
    }
}
