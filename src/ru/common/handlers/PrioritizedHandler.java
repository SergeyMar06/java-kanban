package ru.common.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.manager.FileBackedTaskManager;

import java.io.IOException;
import java.io.OutputStream;

public class PrioritizedHandler implements HttpHandler {
    private FileBackedTaskManager fileBackedTaskManager;

    public PrioritizedHandler(FileBackedTaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(200, 0);

            String response = fileBackedTaskManager.getPrioritizedTasks().toString();

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
