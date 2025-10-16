package ru.common.API;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import ru.common.enums.Status;
import ru.common.model.Task;
import ru.common.servers.HttpTaskServer;
import ru.common.manager.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TaskApiTest {
    private HttpTaskServer server;
    private FileBackedTaskManager manager;
    private Gson gson;
    private final HttpClient client = HttpClient.newHttpClient();
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // создаём временный файл, чтобы тесты не портили реальные данные
        tempFile = File.createTempFile("tasks", ".txt");
        manager = new FileBackedTaskManager(tempFile.getAbsolutePath());
        server = new HttpTaskServer(manager);
        server.start();

        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new JsonSerializer<Duration>() {
                    @Override
                    public JsonElement serialize(Duration src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString());
                    }
                })
                .registerTypeAdapter(Duration.class, new JsonDeserializer<Duration>() {
                    @Override
                    public Duration deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
                        return Duration.parse(json.getAsString());
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
                        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                })
                .create();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
        tempFile.deleteOnExit();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(
                "Test Task",
                "Description",
                Status.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.now()
        );

        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Ожидался статус 201 при добавлении задачи");


        Collection<Task> tasks = manager.getTasks().values();
        assertEquals(1, tasks.size(), "Задача не добавилась в менеджер");
        assertEquals("Test Task", tasks.iterator().next().getTitle(), "Название задачи не совпадает");
    }
}
