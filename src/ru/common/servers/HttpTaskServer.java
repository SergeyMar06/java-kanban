package ru.common.servers;

import com.sun.net.httpserver.HttpServer;
import ru.common.handlers.*;
import ru.common.manager.FileBackedTaskManager;
import ru.common.manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final FileBackedTaskManager fileBackedTaskManager;

    public HttpTaskServer(FileBackedTaskManager fileBackedTaskManager) throws IOException {
        this.fileBackedTaskManager = fileBackedTaskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/tasks", new TaskHandler(fileBackedTaskManager));
        httpServer.createContext("/subtask", new SubtaskHandler(fileBackedTaskManager));
        httpServer.createContext("/epics", new EpicHandler(fileBackedTaskManager));
        httpServer.createContext("/history", new HistoryHandler(fileBackedTaskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(fileBackedTaskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на 8080 порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = Managers.loadFromFile("src/ru/common/files/tasks.txt");

        HttpTaskServer server = new HttpTaskServer(fileBackedTaskManager);
        server.start();
    }
}
