package ru.common.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;

class TaskTest {
    @Test
    public void shouldReturnTrueWhenTasksIdsAreEquals() {
        Task task1 = new Task("task1", "description1", Status.NEW);
        Task task2 = new Task("task2", "description2", Status.DONE);
        Task task3 = new Task("task3", "description3", Status.IN_PROGRESS);
        task1.setId(1);
        task2.setId(1);
        task3.setId(2);

        Assertions.assertEquals(task1, task2);
        Assertions.assertNotEquals(task2, task3);
    }
}