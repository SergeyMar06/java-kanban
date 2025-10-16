package ru.common.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.manager.FileBackedTaskManager;
import ru.common.manager.Managers;
import ru.common.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler implements HttpHandler {
    private FileBackedTaskManager fileBackedTaskManager;

    public TaskHandler(FileBackedTaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathId = path.split("/");

        if (method.equalsIgnoreCase("GET")) {
            if (pathId.length == 2) {
                exchange.sendResponseHeaders(200,  0);

                String response = fileBackedTaskManager.getTasks().toString();

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else if (pathId.length == 3) {
                Task task = fileBackedTaskManager.getTaskById(Integer.parseInt(pathId[2]));

                if (task == null) {
                    exchange.sendResponseHeaders(404, 0);
                    String response = "Ошибка 404: Not found";
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    String response = task.toString();
                    exchange.sendResponseHeaders(200, 0);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        } else if (method.equalsIgnoreCase("POST")) {
            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            is.close();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Duration.class, (JsonDeserializer<Duration>) (json, type, context) ->
                            json.isJsonNull() ? null : Duration.parse(json.getAsString()))
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                            json.isJsonNull() ? null : LocalDateTime.parse(json.getAsString()))
                    .create();

            Task newTask = gson.fromJson(requestBody, Task.class);

            try {
                fileBackedTaskManager.createTask(newTask);

                exchange.sendResponseHeaders(201, 0);

                String response = "Статус 200: задача успешно создана";

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (IllegalArgumentException e) {
                exchange.sendResponseHeaders(406, 0);
                String response = "Статус 406 задача пересекается с существующей";

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        } else if (method.equalsIgnoreCase("PUT")) {
            exchange.sendResponseHeaders(201, 0);

            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            is.close();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Duration.class, (JsonDeserializer<Duration>) (json, type, context) ->
                            json.isJsonNull() ? null : Duration.parse(json.getAsString()))
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                            json.isJsonNull() ? null : LocalDateTime.parse(json.getAsString()))
                    .create();

            Task newTask = gson.fromJson(requestBody, Task.class);

            fileBackedTaskManager.updateTask(Integer.parseInt(pathId[2]), newTask);

            String response = "Статус 200: задача успешно обновлена";

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if (method.equalsIgnoreCase("DELETE")) {
            exchange.sendResponseHeaders(200, 0);

            fileBackedTaskManager.removeTask(Integer.parseInt(pathId[2]));

            String response = "Статус 200: задача успешно удалена";

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
