package ru.common.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.interfaces.HistoryManager;
import ru.common.interfaces.TaskManager;
import ru.common.model.Task;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void initializationTaskManager() {
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

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

        Assertions.assertEquals(Status.DONE, historyManager.getHistory().get(0).getStatus());
    }

    @Test
    public void shouldCorrectAddTaskInLinkedHashMap() {
        Task task1 = new Task("task1", "description1", Status.NEW);
        task1.setId(1);
        inMemoryHistoryManager.add(task1);

        Assertions.assertTrue(inMemoryHistoryManager.getNodeMap().containsKey(1));

        task1.setStatus(Status.DONE);

        inMemoryHistoryManager.add(task1);

        Assertions.assertEquals(Status.DONE, inMemoryHistoryManager.getNodeMap().get(1).task.getStatus());
    }

    @Test
    public void shouldCorrectDeleteToLinkedHashMap() {
        Task task1 = new Task("task1", "description1", Status.NEW);
        task1.setId(1);

        inMemoryHistoryManager.add(task1);

        Assertions.assertTrue(inMemoryHistoryManager.getNodeMap().containsKey(1));

        inMemoryHistoryManager.remove(task1.getId());

        Assertions.assertTrue(inMemoryHistoryManager.getNodeMap().isEmpty());
    }

    @Test
    public void shouldLinkLastCorrectWhenListEmptyAndNonEmpty() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("task1", "description1", Status.NEW);
        task1.setId(1);

        // Случай 1: добавление в пустой список
        InMemoryHistoryManager.Node node1 = historyManager.linkLast(task1);

        Assertions.assertEquals(node1, historyManager.getTail());
        Assertions.assertEquals(node1, historyManager.getHead());
        Assertions.assertNull(node1.prev);
        Assertions.assertNull(node1.next);

        // Случай 2: добавление в непустой список
        Task task2 = new Task("task2", "description2", Status.DONE);
        task2.setId(2);

        InMemoryHistoryManager.Node node2 = historyManager.linkLast(task2);

        Assertions.assertEquals(node2, historyManager.getTail());
        Assertions.assertEquals(node1, node2.prev);
        Assertions.assertEquals(node2, node1.next);
        Assertions.assertNull(node2.next);
    }
}