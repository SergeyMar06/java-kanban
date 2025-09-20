package ru.common.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.interfaces.TaskManager;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    private T taskManager;

    protected abstract T createTaskManager() throws IOException;

    @BeforeEach
    public void initializationTaskManager() throws IOException {
        taskManager = createTaskManager();
    }

    @Test
    public void shouldAddTaskSubtaskAndEpicSuccessfully() {

        // для epic
        Epic epic = new Epic("epic1", "description1");
        int epicId = taskManager.createEpic(epic);

        Epic saveEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(saveEpic);
        Assertions.assertTrue(taskManager.getEpics().containsValue(saveEpic));

        // для subtask
        Subtask subtask = new Subtask("subtask1", "description1", Status.NEW, epicId, Duration.ofMinutes(60), null);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask saveSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertNotNull(saveSubtask);
        Assertions.assertTrue(taskManager.getSubtasks().containsValue(saveSubtask));

        // для task
        Task task = new Task("task1", "description1", Status.NEW, Duration.ofMinutes(60), null);
        int taskId = taskManager.createTask(task);

        Task saveTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(saveTask);
        Assertions.assertTrue(taskManager.getTasks().containsValue(saveTask));
    }

    @Test
    public void shouldReturnTaskSubtaskAndEpicById() {

        // для epic
        Epic epic = new Epic("epic1", "description1");
        int epicId = taskManager.createEpic(epic);

        Epic saveEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(saveEpic);

        // для subtask
        Subtask subtask = new Subtask("subtask1", "description1", Status.NEW, epicId, Duration.ofMinutes(60), null);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask saveSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertNotNull(saveSubtask);

        // для task
        Task task = new Task("task1", "description1", Status.NEW, Duration.ofMinutes(60), null);
        int taskId = taskManager.createTask(task);

        Task saveTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(saveTask);
    }

    @Test
    public void shouldRemainUnchangedWhenAddTaskSubtaskAndEpicInManager() {
        /* проверяем только неизменные поля (id задаётся в менеджере,
        поэтому его не проверяем)
         */

        // для epic
        Epic epic = new Epic("epic1", "description1");
        int epicId = taskManager.createEpic(epic);

        Epic saveEpic = taskManager.getEpicById(epicId);

        Assertions.assertEquals(epic.getTitle(), saveEpic.getTitle());
        Assertions.assertEquals(epic.getDescription(), saveEpic.getDescription());
        Assertions.assertEquals(epic.getStatus(), saveEpic.getStatus());
        Assertions.assertEquals(epic.getSubtaskIds(), saveEpic.getSubtaskIds());

        // для subtask
        Subtask subtask = new Subtask("subtask1", "description1", Status.NEW, epicId, Duration.ofMinutes(60), null);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask saveSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertEquals(subtask.getTitle(), saveSubtask.getTitle());
        Assertions.assertEquals(subtask.getDescription(), saveSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), saveSubtask.getStatus());
        Assertions.assertEquals(subtask.getEpicId(), saveSubtask.getEpicId());

        // для task
        Task task = new Task("task1", "description1", Status.NEW, Duration.ofMinutes(60), null);
        int taskId = taskManager.createTask(task);

        Task saveTask = taskManager.getTaskById(taskId);

        Assertions.assertEquals(task.getTitle(), saveTask.getTitle());
        Assertions.assertEquals(task.getDescription(), saveTask.getDescription());
        Assertions.assertEquals(task.getStatus(), saveTask.getStatus());
    }

    @Test
    public void shouldDeleteItCorrectTaskSubtaskAndEpic() {

        Epic epic = new Epic("epic1", "description1");
        taskManager.createEpic(epic);

        Task task = new Task("task1", "description2", Status.NEW, Duration.ofMinutes(60), null);
        taskManager.createTask(task);

        Subtask subtask = new Subtask("subtask1", "description3", Status.NEW, epic.getId(), Duration.ofMinutes(60), null);
        taskManager.createSubtask(subtask);

        Assertions.assertTrue(taskManager.getTasks().containsValue(task));
        Assertions.assertTrue(taskManager.getSubtasks().containsValue(subtask));
        Assertions.assertTrue(taskManager.getEpics().containsValue(epic));

        taskManager.removeTask(task.getId());
        taskManager.removeSubtask(subtask.getId());
        taskManager.removeEpic(epic.getId());

        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    public void shouldReturnCorrectListSubtaskByEpicId() {

        Epic epic = new Epic("epic1", "description1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("subtask1", "description2", Status.NEW, epic.getId(), Duration.ofMinutes(60), null);
        Subtask subtask2 = new Subtask("subtask2", "description3", Status.DONE, epic.getId(), Duration.ofMinutes(60), null);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(taskManager.getSubtaskById(subtask1.getId()));
        subtasks.add(taskManager.getSubtaskById(subtask2.getId()));

        Assertions.assertEquals(subtasks, taskManager.getSubtaskByEpicId(epic.getId()));
    }

    @Test
    public void shouldNotAllowOverlappingTasks() {
        Task task1 = new Task("Task1", "Description1", Status.NEW,
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 9, 20, 10, 0)); // 10:00 - 11:00
        taskManager.createTask(task1);

        Task task2 = new Task("Task2", "Description2", Status.NEW,
                Duration.ofMinutes(30),
                LocalDateTime.of(2025, 9, 20, 10, 30)); // 10:30 - 11:00

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2),
                "Интервалы выполнения задач не должны перекрываться");
    }

}
