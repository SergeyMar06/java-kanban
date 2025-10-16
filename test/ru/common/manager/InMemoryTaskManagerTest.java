package ru.common.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.model.Epic;
import ru.common.model.Subtask;

import java.io.IOException;
import java.time.Duration;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private InMemoryTaskManager taskManager;

    @Override
    protected InMemoryTaskManager createTaskManager() throws IOException {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void initialization() throws IOException {
        taskManager = createTaskManager();
    }

    @Test
    void shouldReturnNewWhenAllSubtasksAreNew() {
        Epic epic = new Epic("Epic1", "Description");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", Status.NEW, epic.getId(), Duration.ofMinutes(30), null);
        Subtask sub2 = new Subtask("Sub2", "Desc", Status.NEW, epic.getId(), Duration.ofMinutes(20), null);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.checkTheSubtasksInEpic(epic.getId());

        Assertions.assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnDoneWhenAllSubtasksAreDone() {
        Epic epic = new Epic("Epic2", "Description");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", Status.DONE, epic.getId(), Duration.ofMinutes(30), null);
        Subtask sub2 = new Subtask("Sub2", "Desc", Status.DONE, epic.getId(), Duration.ofMinutes(20), null);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.checkTheSubtasksInEpic(epic.getId());

        Assertions.assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnInProgressWhenSubtasksAreNewAndDone() {
        Epic epic = new Epic("Epic3", "Description");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", Status.NEW, epic.getId(), Duration.ofMinutes(30), null);
        Subtask sub2 = new Subtask("Sub2", "Desc", Status.DONE, epic.getId(), Duration.ofMinutes(20), null);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.checkTheSubtasksInEpic(epic.getId());

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnInProgressWhenSubtasksAreInProgress() {
        Epic epic = new Epic("Epic4", "Description");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(30), null);
        Subtask sub2 = new Subtask("Sub2", "Desc", Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(20), null);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.checkTheSubtasksInEpic(epic.getId());

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }
}