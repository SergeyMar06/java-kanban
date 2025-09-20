package ru.common.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.interfaces.HistoryManager;
import ru.common.model.Task;
import ru.common.enums.Status;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void initializationHistoryManager() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task("Task1", "Description1", Status.NEW, Duration.ofMinutes(30), null);
        task1.setId(1);
        task2 = new Task("Task2", "Description2", Status.NEW, Duration.ofMinutes(30), null);
        task2.setId(2);
        task3 = new Task("Task3", "Description3", Status.NEW, Duration.ofMinutes(30), null);
        task3.setId(3);
    }

    @Test
    void shouldReturnEmptyHistoryInitially() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой изначально");
    }

    @Test
    void shouldAddTasksToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void shouldNotDuplicateTasksInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));

    }

    @Test
    void shouldRemoveTaskFromStartOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task1));
        assertEquals(task2, history.get(0));
    }

    @Test
    void shouldRemoveTaskFromMiddleOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task2));
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    void shouldRemoveTaskFromEndOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task3));
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }
}
