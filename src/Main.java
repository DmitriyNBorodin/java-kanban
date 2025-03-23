import httpserver.HttpTaskServer;
import managers.ManagerUtils;
import managers.TaskManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskManager manager = ManagerUtils.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.activation();
        System.out.println("HTTP-сервер запущен");
    }
}
