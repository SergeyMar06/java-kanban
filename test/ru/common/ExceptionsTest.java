package ru.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.common.errors.ManagerSaveException;
import ru.common.manager.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;

public class ExceptionsTest {
    @Test
    public void shouldThrowIOExceptionForInvalidFile() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager("/invalid/path/tasks.csv");
            manager.save();
        }, "Сохранение в недопустимый путь должно выбрасывать IOException");
    }

    @Test
    public void shouldSaveWithoutException() {
        Assertions.assertDoesNotThrow(() -> {
            File tempFile = File.createTempFile("tasks", ".csv");
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile.getAbsolutePath());
            manager.save();
        }, "Сохранение во временный файл не должно приводить к исключению");
    }

}
