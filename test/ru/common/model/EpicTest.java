package ru.common.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.common.enums.Status;
import ru.common.interfaces.TaskManager;
import ru.common.manager.Managers;

class EpicTest {
    private TaskManager taskManager;

    @BeforeEach
    public void initializationTaskManager() {
        this.taskManager = Managers.getDefault();
    }

    @Test
    public void shouldReturnTrueWhenEpicsIdsAreEquals() {
        Epic epic1 = new Epic("epic1", "description1");
        Epic epic2 = new Epic("epic2", "description2");
        Epic epic3 = new Epic("epic3", "description3");
        epic1.setId(1);
        epic2.setId(1);
        epic3.setId(2);

        Assertions.assertEquals(epic1, epic2);
        Assertions.assertNotEquals(epic2, epic3);
    }

    @Test
    public void shouldCorrectDeleteSubtaskInEpicWhenDeleteSubtask() {
        Epic epic1 = new Epic("epic1", "description");

        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "description2", Status.DONE, epic1.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Assertions.assertFalse(epic1.getSubtaskIds().isEmpty());

        taskManager.removeSubtask(subtask1.getId());
        taskManager.removeSubtask(subtask2.getId());

        Assertions.assertTrue(epic1.getSubtaskIds().isEmpty());
    }

    @Test
    public void shouldResetEpicIdWhenSubtaskDeleted() {
        Epic epic1 = new Epic("epic1", "description");

        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "description2", Status.DONE, epic1.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Assertions.assertFalse(epic1.getSubtaskIds().isEmpty());

        taskManager.removeSubtask(subtask1.getId());

        Assertions.assertFalse(epic1.getSubtaskIds().contains(subtask1.getId()));
    }
}