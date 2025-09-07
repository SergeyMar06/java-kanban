package ru.common.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setup() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile.getAbsolutePath());
    }

    @Test
    void shouldSaveAndLoadEmptyFile() {
        manager.save();

        FileBackedTaskManager loaded = Managers.loadFromFile(tempFile.getAbsolutePath());

        assertTrue(loaded.getTasks().isEmpty());
        assertTrue(loaded.getEpics().isEmpty());
        assertTrue(loaded.getSubtasks().isEmpty());
    }

    @Test
    void shouldSaveAndLoadSingleTask() {
        Task task = new Task("task1", "desc1", Status.NEW);
        int taskId = manager.createTask(task);

        FileBackedTaskManager loaded = Managers.loadFromFile(tempFile.getAbsolutePath());

        assertEquals(1, loaded.getTasks().size());
        assertEquals("task1", loaded.getTaskById(taskId).getTitle());
        assertEquals(Status.NEW, loaded.getTaskById(taskId).getStatus());
    }

    @Test
    void shouldSaveAndLoadEpicWithSubtask() {
        Epic epic = new Epic("epic1", "desc epic");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("sub1", "desc sub", Status.IN_PROGRESS, epicId);
        int subId = manager.createSubtask(subtask);

        FileBackedTaskManager loaded = Managers.loadFromFile(tempFile.getAbsolutePath());

        assertEquals(1, loaded.getEpics().size());
        assertEquals(1, loaded.getSubtasks().size());

        Epic loadedEpic = loaded.getEpicById(epicId);
        Subtask loadedSub = loaded.getSubtaskById(subId);

        assertEquals("epic1", loadedEpic.getTitle());
        assertEquals("sub1", loadedSub.getTitle());
        assertTrue(loadedEpic.getSubtaskIds().contains(subId));
    }

    @Test
    void shouldSaveAndLoadSeveralTasks() {
        manager.createTask(new Task("task1", "desc1", Status.NEW));
        manager.createTask(new Task("task2", "desc2", Status.DONE));

        FileBackedTaskManager loaded = Managers.loadFromFile(tempFile.getAbsolutePath());

        assertEquals(2, loaded.getTasks().size());
        assertEquals("task2", loaded.getTaskById(2).getTitle());
        assertEquals(Status.DONE, loaded.getTaskById(2).getStatus());
    }
}
