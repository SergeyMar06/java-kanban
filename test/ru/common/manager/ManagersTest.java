package ru.common.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.interfaces.HistoryManager;
import ru.common.interfaces.TaskManager;
import ru.common.model.Task;


class ManagersTest {
    @Test
    public void shouldReturnInitializedTaskManager() {
        TaskManager taskManager = Managers.getDefault();

        Assertions.assertNotNull(taskManager);

        Assertions.assertEquals(1, taskManager.getId());
    }

    @Test
    public void shouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Assertions.assertNotNull(historyManager);

        Task task = new Task("task1", "description1", Status.NEW);
        task.setId(1);

        historyManager.getHistory().add(task);

        Assertions.assertTrue(historyManager.getHistory().contains(task));
    }
}