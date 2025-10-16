package ru.common.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.manager.FileBackedTaskManager;
import ru.common.manager.Managers;

import java.io.IOException;
import java.io.OutputStream;

public class HistoryHandler implements HttpHandler {
    private FileBackedTaskManager fileBackedTaskManager;

    public HistoryHandler(FileBackedTaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(200, 0);

            String response = fileBackedTaskManager.getHistoryManager().getHistory().toString();

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
