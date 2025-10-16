package ru.common.API;

import org.junit.jupiter.api.*;
import ru.common.manager.FileBackedTaskManager;
import ru.common.servers.HttpTaskServer;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryApiTest {
    private HttpTaskServer server;
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("history", ".txt");
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
    public void testHistoryInitiallyEmpty() {
        assertTrue(manager.getHistoryManager().getHistory().isEmpty(), "История должна быть пустой при старте");
    }
}
