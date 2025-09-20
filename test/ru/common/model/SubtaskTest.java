package ru.common.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;

import java.time.Duration;

class SubtaskTest {
    @Test
    public void shouldReturnTrueWhenSubtaskIdsAreEquals() {
        Subtask subtask1 = new Subtask("subtask1", "description1", Status.NEW, 1, Duration.ofMinutes(60), null);
        Subtask subtask2 = new Subtask("subtask2", "description2", Status.IN_PROGRESS, 2, Duration.ofMinutes(60), null);
        Subtask subtask3 = new Subtask("subtask3", "description3", Status.DONE, 3, Duration.ofMinutes(60), null);
        subtask1.setId(1);
        subtask2.setId(1);
        subtask3.setId(2);

        Assertions.assertEquals(subtask1, subtask2);
        Assertions.assertNotEquals(subtask2, subtask3);
    }
}