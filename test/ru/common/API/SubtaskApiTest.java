package ru.common.API;

import org.junit.jupiter.api.*;
import ru.common.manager.FileBackedTaskManager;
import ru.common.servers.HttpTaskServer;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubtaskApiTest {
    private HttpTaskServer server;
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("subtasks", ".txt");
        manager = new FileBackedTaskManager(tempFile.getAbsolutePath());
        server = new HttpTaskServer(manager);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
        tempFile.deleteOnExit();
    }

    @Test
    public void testEmptySubtasksInitially() {
        assertTrue(manager.getSubtasks().isEmpty(), "Подзадачи должны быть пустыми при старте");
    }
}
