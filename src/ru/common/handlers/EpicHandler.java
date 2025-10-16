package ru.common.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.manager.FileBackedTaskManager;;
import ru.common.model.Epic;
import ru.common.model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicHandler implements HttpHandler {
    private FileBackedTaskManager fileBackedTaskManager;

    public EpicHandler(FileBackedTaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathId = path.split("/");

        if (method.equalsIgnoreCase("GET")) {
            if (pathId.length == 2) {
                String response = fileBackedTaskManager.getEpics().toString();

                exchange.sendResponseHeaders(200, 0);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else if (pathId.length == 3) {
                Epic epic = fileBackedTaskManager.getEpicById(Integer.parseInt(pathId[2]));

                if (epic == null) {
                    exchange.sendResponseHeaders(404, 0);

                    String response = "Ошибка 404: Not found";

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    String response = epic.toString();

                    exchange.sendResponseHeaders(200, 0);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            } else if (pathId.length == 4) {
                List<Subtask> subtaskList = fileBackedTaskManager.getSubtaskByEpicId(Integer.parseInt(pathId[2]));

                if (subtaskList == null) {
                    exchange.sendResponseHeaders(404, 0);

                    String response = "Ошибка 404: Not found";

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    String response = subtaskList.toString();

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

            Epic newEpic = gson.fromJson(requestBody, Epic.class);


            fileBackedTaskManager.createEpic(newEpic);

            exchange.sendResponseHeaders(201, 0);

            String response = "Статус 200: эпик успешно создан";

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
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

            Epic newEpic = gson.fromJson(requestBody, Epic.class);

            fileBackedTaskManager.updateEpic(Integer.parseInt(pathId[2]), newEpic);

            String response = "Статус 201: эпик успешно обновлен";

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if (method.equalsIgnoreCase("DELETE")) {
            exchange.sendResponseHeaders(200, 0);

            fileBackedTaskManager.removeEpic(Integer.parseInt(pathId[2]));

            String response = "Статус 200: эпик успешно удален";

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
