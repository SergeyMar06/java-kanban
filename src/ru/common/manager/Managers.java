package ru.common.manager;

import ru.common.errors.ManagerSaveException;
import ru.common.errors.TaskTypeException;
import ru.common.interfaces.HistoryManager;
import ru.common.interfaces.TaskManager;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager loadFromFile(String url) {
        FileBackedTaskManager manager = new FileBackedTaskManager(url);

        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            String line;
            int maxId = 0;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                Task task = manager.fromString(line);

                if (task instanceof Subtask) {
                    manager.getSubtasks().put(task.getId(), (Subtask) task);
                    // добавляем связь с эпиком
                    Epic epic = manager.getEpics().get(((Subtask) task).getEpicId());
                    if (epic != null) {
                        epic.getSubtaskIds().add(task.getId());
                    }
                } else if (task instanceof Epic) {
                    manager.getEpics().put(task.getId(), (Epic) task);
                } else {
                    manager.getTasks().put(task.getId(), task);
                }

                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }

            manager.setId(maxId + 1);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла", e);
        } catch (TaskTypeException e) {
            throw new RuntimeException(e);
        }

        return manager;
    }
}
