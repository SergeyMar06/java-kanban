package ru.common.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.interfaces.HistoryManager;
import ru.common.interfaces.TaskManager;
import ru.common.model.Task;

class InMemoryHistoryManagerTest {
    @Test
    public void shouldSavePreviousVersionWhenAddHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "description1", Status.NEW);
        Task task2 = new Task("task1", "description1", Status.DONE);

        taskManager.createTask(task1);
        historyManager.add(taskManager.getTaskById(task1.getId()));
        taskManager.updateTask(1, task2);
        historyManager.add(taskManager.getTaskById(task1.getId()));

        Assertions.assertEquals(Status.NEW, historyManager.getHistory().get(0).getStatus());
    }
}