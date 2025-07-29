package ru.common.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class EpicTest {
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


}